/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.TO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alan
 */
@Entity
@Table(name = "rota")
@XmlRootElement
public class Rota implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "latitute")
    private String latitute;
    @Column(name = "pass_existentes")
    private short passExistentes;
    @Column(name = "pass_max")
    private short passMax;
    @Column(name = "flag_aberta")
    private Boolean flagAberta;
    @Column(name = "flag_cancelada")
    private Boolean flagCancelada;
    @Column(name = "data_rota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRota;
    @JoinColumn(name = "id_end_destino", referencedColumnName = "id")
    @ManyToOne
    private Endereco endDestino;
    @JoinColumn(name = "id_end_origem", referencedColumnName = "id")
    @ManyToOne
    private Endereco endOrigem;

    public Rota() {
    }

    public Rota(String longitude, String latitute, short passExistentes, short passMax, Boolean flagAberta, Boolean flagCancelada, Date dataRota, Endereco endDestino, Endereco endOrigem) {
        this.longitude = longitude;
        this.latitute = latitute;
        this.passExistentes = passExistentes;
        this.passMax = passMax;
        this.flagAberta = flagAberta;
        this.flagCancelada = flagCancelada;
        this.dataRota = dataRota;
        this.endDestino = endDestino;
        this.endOrigem = endOrigem;
    }
    
    

    public Rota(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public short getPassExistentes() {
        return passExistentes;
    }

    public void setPassExistentes(short passExistentes) {
        this.passExistentes = passExistentes;
    }

    public short getPassMax() {
        return passMax;
    }

    public void setPassMax(short passMax) {
        this.passMax = passMax;
    }

    public Boolean getFlagAberta() {
        return flagAberta;
    }

    public void setFlagAberta(Boolean flagAberta) {
        this.flagAberta = flagAberta;
    }

    public Boolean getFlagCancelada() {
        return flagCancelada;
    }

    public void setFlagCancelada(Boolean flagCancelada) {
        this.flagCancelada = flagCancelada;
    }

    public Date getDataRota() {
        return dataRota;
    }

    public void setDataRota(Date dataRota) {
        this.dataRota = dataRota;
    }
    
    /**
     * @return the endDestino
     */
    public Endereco getEndDestino() {
        return endDestino;
    }

    /**
     * @param endDestino the endDestino to set
     */
    public void setEndDestino(Endereco endDestino) {
        this.endDestino = endDestino;
    }

    /**
     * @return the endOrigem
     */
    public Endereco getEndOrigem() {
        return endOrigem;
    }

    /**
     * @param endOrigem the endOrigem to set
     */
    public void setEndOrigem(Endereco endOrigem) {
        this.endOrigem = endOrigem;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rota)) {
            return false;
        }
        Rota other = (Rota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TS.FrameWork.TO.Rota[ id=" + id + " ]";
    }
}
