package com.mavil.reports.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonIgnoreProperties
public class GenerateReportRequestVo {
    private String title;
    private List<ColumnReportVo> columns;
    private List<Map<String, String>> data;
    private Map<String, String> totals;
}
