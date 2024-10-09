package com.mavil.reports.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties
public class ColumnReportVo {
    private String label;
    private String field;
}
