package com.mavil.reports.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotaCredInfoVo {
    private String numeroFactura;
    private String fechaFactura;
}
