package com.mavil.reports.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComproAguaRequestVo {

    private Integer empCodigo;
    private Integer trnCodigo;

    private String pexceso;
    private String pvconsumo;
    private String pvexceso;
    private String pvsubt;
    private String pvdesc;
    private String pvmulta;
    private String pvtotal;
    private String pfechamaxpago;
}
