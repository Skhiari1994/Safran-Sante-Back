package com.tn.arabsoft.administrations.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JsonResponse {

     private Long id;

     private String label;

     private String icon;

     private String link;

     private List<JsonResponse> subItems;

     private Long parentId;

     private boolean isCollapsed;

     private String collapseid;

}
