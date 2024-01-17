package com.mavil.reports.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "tparams", schema = "${schema}", catalog = "imprentadb")
public class TparamsEntity {
    private Integer tprmId;
    private String tprmAbrev;
    private String tprmNombre;
    private String tprmVal;
    private Timestamp tprmFechacrea;

    private Integer tprmSeccion;

    @Id
    @Column(name = "tprm_id")
    public Integer getTprmId() {
        return tprmId;
    }

    public void setTprmId(Integer tprmId) {
        this.tprmId = tprmId;
    }

    @Basic
    @Column(name = "tprm_abrev")
    public String getTprmAbrev() {
        return tprmAbrev;
    }

    public void setTprmAbrev(String tprmAbrev) {
        this.tprmAbrev = tprmAbrev;
    }

    @Basic
    @Column(name = "tprm_nombre")
    public String getTprmNombre() {
        return tprmNombre;
    }

    public void setTprmNombre(String tprmNombre) {
        this.tprmNombre = tprmNombre;
    }

    @Basic
    @Column(name = "tprm_val")
    public String getTprmVal() {
        return tprmVal;
    }

    public void setTprmVal(String tprmVal) {
        this.tprmVal = tprmVal;
    }

    @Basic
    @Column(name = "tprm_fechacrea")
    public Timestamp getTprmFechacrea() {
        return tprmFechacrea;
    }

    public void setTprmFechacrea(Timestamp tprmFechacrea) {
        this.tprmFechacrea = tprmFechacrea;
    }

    @Basic
    @Column(name = "tprm_seccion")
    public Integer getTprmSeccion() {
        return tprmSeccion;
    }

    public void setTprmSeccion(Integer tprmSeccion) {
        this.tprmSeccion = tprmSeccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TparamsEntity that = (TparamsEntity) o;

        if (tprmId != that.tprmId) return false;
        if (tprmAbrev != null ? !tprmAbrev.equals(that.tprmAbrev) : that.tprmAbrev != null) return false;
        if (tprmNombre != null ? !tprmNombre.equals(that.tprmNombre) : that.tprmNombre != null) return false;
        if (tprmVal != null ? !tprmVal.equals(that.tprmVal) : that.tprmVal != null) return false;
        if (tprmFechacrea != null ? !tprmFechacrea.equals(that.tprmFechacrea) : that.tprmFechacrea != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tprmId;
        result = 31 * result + (tprmAbrev != null ? tprmAbrev.hashCode() : 0);
        result = 31 * result + (tprmNombre != null ? tprmNombre.hashCode() : 0);
        result = 31 * result + (tprmVal != null ? tprmVal.hashCode() : 0);
        result = 31 * result + (tprmFechacrea != null ? tprmFechacrea.hashCode() : 0);
        return result;
    }
}
