package com.mavil.reports.repository;

import com.mavil.reports.vo.DatosAsiFacteVo;
import com.mavil.reports.vo.DatosFacteVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Component
@Slf4j
public class TDatosFacteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retorna los datos de una factura
     *
     * @param esquema
     * @param claveAcceso
     * @return
     */
    public Optional<DatosAsiFacteVo> getDatosAsiFacte(String esquema, String claveAcceso) {

        try {
            String sql = String.format("select trn_codigo," +
                    "    tfe_claveacceso," +
                    "    tfe_numautoriza," +
                    "    coalesce(est.est_nombre,'NODISPONIBLE') as estado," +
                    "    tfe_fecautoriza," +
                    "    case tfe_ambiente" +
                    "        when 1 then 'PRUEBAS'" +
                    "        WHEN 2 then 'PRODUCCION'" +
                    "            else " +
                    "                'DESCONOCIDO' end as ambiente," +
                    "    tfe_mensajes," +
                    "    tfe_estadosri from %s.tasifacte asi " +
                    "    left join public.testadoscompe est  on est.est_id = asi.tfe_estado" +
                    "                  where tfe_claveacceso = '%s'", esquema, claveAcceso);

            Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();

            if (result != null) {
                return Optional.of(
                        DatosAsiFacteVo.builder()
                                .trncodigo(String.valueOf(result[0]))
                                .claveacceso(String.valueOf(result[1]))
                                .estado(String.valueOf(result[3]))
                                .numAutorizacion(String.valueOf(result[2]))
                                .fechaAutorizacion(String.valueOf(result[4]))
                                .ambiente(String.valueOf(result[5]))
                                .build()
                );
            }

            return Optional.empty();

        } catch (RuntimeException ex) {
            log.error("Error al recuperar datos de tasifacte", ex);
            throw ex;
        }

    }

    /**
     * Retorna la configuracion de facturacion electronica para una empresa
     *
     * @param rucEmpresa
     * @return
     */
    public Optional<DatosFacteVo> getDatosFacte(String rucEmpresa) {

        try {
            String sql = String.format("select df.df_pathdocs, df.df_pathdocs_signed, " +
                    " df.df_pathroot, em.emp_esquemadb, df.df_pathlogo from comprobantes.tdatosfacte df\n" +
                    " join comprobantes.tempresa em on em.emp_codigo = df.emp_codigo\n" +
                    " where em.emp_ruc = '%s'", rucEmpresa);

            Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();

            if (result != null) {
                return Optional.of(DatosFacteVo.builder()
                        .pathDocs(String.valueOf(result[0]))
                        .pathDocsSigned(String.valueOf(result[1]))
                        .pathRoot(String.valueOf(result[2]))
                        .esquemaDb(String.valueOf(result[3]))
                        .pathLogo(String.valueOf(result[4]))
                        .build());
            }

            return Optional.empty();

        } catch (RuntimeException ex) {
            log.error("Error al recuperar datos de facturacion electronica", ex);
            throw ex;
        }
    }
}
