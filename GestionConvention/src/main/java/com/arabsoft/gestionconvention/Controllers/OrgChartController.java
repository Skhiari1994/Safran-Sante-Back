package com.arabsoft.gestionconvention.Controllers;

import com.arabsoft.gestionconvention.Projections.LibSocProjection;
import com.arabsoft.gestionconvention.Repositories.ConventionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrgChartController {

    private static final Logger logger = LoggerFactory.getLogger(OrgChartController.class);

    @Autowired
    ConventionRepository conventionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static class Node {
        public String id;
        public String name;
        public String parentId;
        public int level;

        public Node(String id, String name, String parentId, int level) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
            this.level = level;
        }
    }

    @GetMapping("/orgchart/{codSoc}")
    public ResponseEntity<?> getOrgChart(@PathVariable String codSoc) {
        logger.info("Received request to fetch org chart data");

        List<Node> nodes = new ArrayList<>();
        try {
            String sql = "SELECT cod_soc, cod_serv, " +
                    "DECODE(lib_serv, 'الإدارة', (SELECT lib_soc FROM societe WHERE cod_soc = s.cod_soc), lib_serv) AS lib_serv, " +
                    "ser_cod_serv " +
                    "FROM service s " +
                    "WHERE cod_soc = ?";

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, codSoc);

            Map<String, String> nodeParentMap = new HashMap<>();
            Map<String, Integer> levelMap = new HashMap<>();

            // First pass: Collect nodes and parent relationships
            for (Map<String, Object> row : rows) {
                String id = (String) row.get("cod_serv");
                String name = (String) row.get("lib_serv");
                String parentId = row.get("ser_cod_serv") != null ? (String) row.get("ser_cod_serv") : null;

                nodeParentMap.put(id, parentId);
            }

            // Second pass: Calculate levels
            for (Map<String, Object> row : rows) {
                String id = (String) row.get("cod_serv");
                int level = calculateLevel(id, nodeParentMap, levelMap);
                String name = (String) row.get("lib_serv");
                String parentId = nodeParentMap.get(id);

                nodes.add(new Node(id, name, parentId, level));
            }

            // Validate hierarchy
            for (Node node : nodes) {
                if (node.parentId != null && nodes.stream().noneMatch(n -> n.id.equals(node.parentId))) {
                    logger.error("Invalid hierarchy: Parent ID {} not found", node.parentId);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("خطأ في الهيكلية: معرف الأب غير موجود في البيانات");
                }
            }

            logger.info("Successfully fetched org chart with {} nodes", nodes.size());
            return ResponseEntity.ok(nodes);
        } catch (Exception e) {
            logger.error("Error fetching org chart data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("خطأ في جلب بيانات الهيكل التنظيمي: " + e.getMessage());
        }
    }

    private int calculateLevel(String id, Map<String, String> nodeParentMap, Map<String, Integer> levelMap) {
        if (levelMap.containsKey(id)) {
            return levelMap.get(id);
        }

        String parentId = nodeParentMap.get(id);
        if (parentId == null) {
            levelMap.put(id, 1); // Root node
            return 1;
        }

        int parentLevel = calculateLevel(parentId, nodeParentMap, levelMap);
        int level = parentLevel + 1;
        levelMap.put(id, level);
        return level;
    }

    @GetMapping("/getLibSoc")
    List<LibSocProjection> getLibSoc() {
        return conventionRepository.getLibSocProjection();
    }

    @GetMapping("/service/{codServ}")
    public ResponseEntity<?> getServiceDetails(@PathVariable String codServ) {
        try {
            String sql = "SELECT cod_soc,lib_serv  FROM service WHERE cod_serv = ?";
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, codServ);


            return ResponseEntity.ok(row);
        } catch (Exception e) {
            logger.error("Error fetching service details for cod_serv {}: {}", codServ, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("err");
        }
    }
}