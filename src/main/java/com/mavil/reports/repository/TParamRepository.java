package com.mavil.reports.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TParamRepository {

    @PersistenceContext
    private EntityManager entityManager;


    private String auxGetParamValue(String schema, String paramAbr, String section) {
        String whereSection = " and tprm_seccion = 0";
        if (section != null && !section.trim().isEmpty()) {
            whereSection = String.format(" and tprm_seccion = %s", section);
        }

        String sql = String.format("select tprm_val from %s.tparams where tprm_abrev='%s' %s", schema, paramAbr, whereSection);
        String result = "";

        List<String> res = this.entityManager.createNativeQuery(sql).getResultList();
        if (res != null && res.size() > 0) {
            result = res.get(0);
        }

        return result;

    }

    public String getParamValue(String esquema, String prmabrev) {
        return this.auxGetParamValue(esquema, prmabrev, null);
    }

    public String getParamValue(String esquema, String prmabrev, String section) {
        String result = this.auxGetParamValue(esquema, prmabrev, section);
        if (result == null || result.trim().isEmpty()) {
            result = this.auxGetParamValue(esquema, prmabrev, null);
        }
        return result;
    }

    public Integer getTraCodigo(String esquema, Integer trncodigo) {
        try {
            Integer tracodigo = 0;
            String queryStr = String.format("select tra_codigo, trn_codigo from %s.tasiento where trn_codigo = %s ",
                    String.valueOf(esquema),
                    String.valueOf(trncodigo));
            Query query = entityManager.createNativeQuery(queryStr);
            Object[] res = (Object[]) query.getSingleResult();
            if (res != null) {
                tracodigo = Integer.valueOf(String.valueOf(res[0]));
            }
            return tracodigo;
        } catch (RuntimeException re) {
            log.trace("prueba");
            //log.error("Error al obtener datos de la factura", re);
            throw re;
        }
    }

    public Map<String, Object> getTransaccData(String schema, Integer trncod) {
        try {
            String sql = String.format("select tra_codigo, sec_codigo from %s.tasiento where trn_codigo = %d",
                    schema, trncod);
            Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();
            Map resultMap = new HashMap();
            if (result != null) {
                resultMap.put("tra_codigo", result[0]);
                resultMap.put("sec_codigo", result[1]);
            }
            return resultMap;

        } catch (RuntimeException ex) {
            log.error("Error al recuperar datos de transaccion", ex);
            throw ex;
        }
    }

    /*public TReporteEntity getDatosReporte(Integer repId, String esquema) {
        String queryStr = String.format("select rep_id, rep_nombre, rep_jasper, rep_detalle, rep_params, rep_cat from %s.treporte where rep_id = %s ",
                String.valueOf(esquema),
                String.valueOf(repId));
        Query query = entityManager.createNativeQuery(queryStr);
        Object[] res = (Object[]) query.getSingleResult();
        TReporteEntity reporteEntity = null;

        if (res != null) {
            Integer _repId = Integer.valueOf(String.valueOf(res[0]));
            String _repNombre = String.valueOf(res[1]);
            String _repJasper = String.valueOf(res[2]);
            String _repDetalles = String.valueOf(res[3]);
            String _repParams = String.valueOf(res[4]);
            Integer _repCat = Integer.valueOf(String.valueOf(res[5]));

            reporteEntity = new TReporteEntity(_repId, _repNombre, _repJasper, _repDetalles, _repParams, _repCat);
        }
        return reporteEntity;
    }


    public Boolean isNotaVenta(Map<String, Object> transaccDataMap) {
        Integer tra_codigo = Integer.valueOf(String.valueOf(transaccDataMap.get("tra_codigo")));
        return tra_codigo.intValue() == 2;
    }*/

}
