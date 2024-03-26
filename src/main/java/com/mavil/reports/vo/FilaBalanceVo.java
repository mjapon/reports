package com.mavil.reports.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties
public class FilaBalanceVo {
    private String codigo;

    private String nombre;

    private String total;

}
