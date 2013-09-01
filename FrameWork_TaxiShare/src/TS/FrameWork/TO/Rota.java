/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.TO;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    private int id;
    @Column(name = "data_rota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRota;
    @Column(name = "flag_aberta")
    private Boolean flagAberta;
    @Column(name = "pass_existentes")
    private Short passExistentes;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "rota_endereco", joinColumns = {
        @JoinColumn(name = "id_rota", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_endereco", referencedColumnName = "id")})
    private List<Endereco> enderecos;
    @ManyToMany(fetch = FetchType.LAZY,  cascade=CascadeType.ALL)
    @JoinTable(name = "rota_usuario", joinColumns = {
                    @JoinColumn(name = "id_rota", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
                    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)})
    private List<Usuario> usuarios;

    public Rota() {
    }

    public Rota(int id) {
        this.id = id;
    }

    public Rota(Date dataRota, Boolean flagAberta, Short passExistentes, List<Endereco> enderecoList) {
        this.dataRota = dataRota;
        this.flagAberta = flagAberta;
        this.passExistentes = passExistentes;
        this.enderecos = enderecoList;
    }
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataRota() {
        return dataRota;
    }

    public void setDataRota(Date dataRota) {
        this.dataRota = dataRota;
    }

    public Boolean getFlagAberta() {
        return flagAberta;
    }

    public void setFlagAberta(Boolean flagAberta) {
        this.flagAberta = flagAberta;
    }

    
    public Short getPassExistentes() {
        return passExistentes;
    }

    public void setPassExistentes(Short passExistentes) {
        this.passExistentes = passExistentes;
    }

    @XmlTransient
    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    /**
     * @return the usuarios
     */
    @XmlTransient
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    

  @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
   public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pergunta)) {
            return false;
        }
        Rota other = (Rota) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TS.FrameWork.TO.Rota[ id=" + id + " ]";
    }
    
}
