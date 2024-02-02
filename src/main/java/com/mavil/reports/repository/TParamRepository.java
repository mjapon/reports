package com.mavil.reports.repository;

import com.mavil.reports.vo.TransaccDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

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
            String queryStr = String.format("select tra_codigo, trn_codigo from %s.tasiento where trn_codigo = %d ",
                    esquema,
                    trncodigo);
            Query query = entityManager.createNativeQuery(queryStr);
            Object[] res = (Object[]) query.getSingleResult();
            if (res != null) {
                tracodigo = Integer.valueOf(String.valueOf(res[0]));
            }
            return tracodigo;
        } catch (RuntimeException re) {
            log.trace("Error al obtener datos de la factura", re);
            throw re;
        }
    }

    public TransaccDataVo getTransaccData(String schema, Integer trncod) {
        try {
            String sql = String.format("select tra_codigo, sec_codigo from %s.tasiento where trn_codigo = %d",
                    schema, trncod);
            Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();
            if (result != null) {
                return TransaccDataVo.builder()
                        .traCodigo(String.valueOf(result[0]))
                        .secCodigo(String.valueOf(result[1]))
                        .build();
            }
            return null;
        } catch (RuntimeException ex) {
            log.error("Error al recuperar datos de transaccion", ex);
            throw ex;
        }
    }

    public Boolean isNotaVenta(Integer traCodigo) {
        return traCodigo.intValue() == 2;
    }

}
