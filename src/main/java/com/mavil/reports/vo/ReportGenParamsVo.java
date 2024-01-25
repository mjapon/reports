package com.mavil.reports.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportGenParamsVo {
    private Integer codemp;
    private Integer codrep;
    private String pdesde;
    private String phasta;
    private String psecid;
    private String pusid;
    private String prefid;
    private String pfmt;
    private String labelParams;
}
