package com.mavil.reports.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatosFacteVo {

    private String pathDocsSigned;
    private String pathRoot;
    private String pathDocs;
    private String esquemaDb;
    private String pathLogo;

}
