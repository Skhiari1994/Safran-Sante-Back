package com.arabsoft.Credits.Entities.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReponseCalculDetailCredit {
   private String wprInteret;
    private String  wprtMntRem ;
    private String  wremMen;
    private String  wdernRemMen;
    private String wprtRendu;
    private BigDecimal wnbrRetenue ;
    private String  message ;
}
