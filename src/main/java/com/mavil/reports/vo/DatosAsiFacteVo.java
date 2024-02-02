package com.mavil.reports.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatosAsiFacteVo {

    private String claveacceso;
    private String estado;
    private String numAutorizacion;
    private String fechaAutorizacion;
    private String ambiente;
    private String trncodigo;

}
