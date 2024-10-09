package com.mavil.reports.repository;

import com.mavil.reports.util.FechasUtil;
import com.mavil.reports.vo.NotaCredInfoVo;
import com.mavil.reports.vo.TransaccDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
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

    public NotaCredInfoVo getDatosNotaCred(String esquema, Integer trncod) {
        try {
            String sql = String.format("select fac.trn_compro, fac.trn_fecreg from %s.tasiento fac " +
                    "join %s.ttransaccrel rel on rel.trn_codorigen  = fac.trn_codigo and rel.trel_tracoddestino  = 4 " +
                    "and rel.trel_activo  = true where rel.trn_coddestino  = %d", esquema, esquema, trncod);

            Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();
            if (result != null) {
                String numeroFactura = String.valueOf(result[0]);
                Date fecha = (Date) result[1];
                return NotaCredInfoVo.builder()
                        .numeroFactura(numeroFactura)
                        .fechaFactura(FechasUtil.formatDate(fecha))
                        .build();
            }
            return null;

        } catch (RuntimeException ex) {
            log.error("Error al recuperar datos de factura relacionada con nota de credito", ex);
            throw ex;
        }
    }


    public Boolean isNotaVenta(Integer traCodigo) {
        return traCodigo.intValue() == 2;
    }

}
