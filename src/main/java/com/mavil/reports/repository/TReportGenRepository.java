package com.mavil.reports.repository;

import com.mavil.reports.vo.TReportVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
@Slf4j
public class TReportGenRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public TReportVo getDatosReporte(Integer repId, String esquema) {
        String queryStr = String.format("select rep_id, rep_nombre, rep_jasper, rep_detalle, rep_params, rep_cat from %s.treporte where rep_id = %d ",
                esquema, repId);
        Query query = entityManager.createNativeQuery(queryStr);
        Object[] res = (Object[]) query.getSingleResult();
        TReportVo reporteVo = null;

        if (res != null) {
            Integer _repId = Integer.valueOf(String.valueOf(res[0]));
            String _repNombre = String.valueOf(res[1]);
            String _repJasper = String.valueOf(res[2]);
            String _repDetalles = String.valueOf(res[3]);
            String _repParams = String.valueOf(res[4]);
            Integer _repCat = Integer.valueOf(String.valueOf(res[5]));

            reporteVo = TReportVo.builder()
                    .repId(_repId)
                    .repNombre(_repNombre)
                    .repJasper(_repJasper)
                    .repDetalle(_repDetalles)
                    .repParams(_repParams)
                    .repCat(_repCat)
                    .build();
        }
        return reporteVo;
    }
}
