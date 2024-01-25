package com.mavil.reports.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TReportVo {
    private int repId;
    private String repNombre;
    private String repJasper;
    private String repDetalle;
    private String repParams;
    private int repCat;
}
