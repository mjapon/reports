package com.mavil.reports.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties
public class GenBalanceRequestVo {
    private String titulo;
    private String periodo;
    private List<FilaBalanceVo> items;
    private List<RBRowVo> resumenItems;

}
