package com.mavil.reports.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties
public class ResumenBalanceVo {
    private String activos;
    private String pasivos;
    private String patrimonio;
    private String resultado;
    private String resumen;
}
