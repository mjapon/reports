package com.mavil.reports.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportGenParamsVo {
    private Integer codrep;
    private String pdesde;
    private String phasta;
    private String secid;
    private String usid;
    private String refid;
    private String fmt;
    private String labelparams;
}
