/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import TS.FrameWork.TO.Endereco;
import TS.FrameWork.TO.Usuario;
import java.util.List;

/**
 *
 * @author alan
 */
public class RotaEntity {
    
    	private int id;
	private String dataRota;
	private Boolean flagAberta;
	private Short passExistentes;
	private List<Endereco> enderecos;
	private List<Usuario> usuarios;

    public RotaEntity() {
    }

    public RotaEntity(int id) {
        this.id = id;
    }

    public RotaEntity(String dataRota, Boolean flagAberta, Short passExistentes, List<Endereco> enderecos, List<Usuario> usuarios) {
        this.dataRota = dataRota;
        this.flagAberta = flagAberta;
        this.passExistentes = passExistentes;
        this.enderecos = enderecos;
        this.usuarios = usuarios;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the dataRota
     */
    public String getDataRota() {
        return dataRota;
    }

    /**
     * @param dataRota the dataRota to set
     */
    public void setDataRota(String dataRota) {
        this.dataRota = dataRota;
    }

    /**
     * @return the flagAberta
     */
    public Boolean getFlagAberta() {
        return flagAberta;
    }

    /**
     * @param flagAberta the flagAberta to set
     */
    public void setFlagAberta(Boolean flagAberta) {
        this.flagAberta = flagAberta;
    }

    /**
     * @return the passExistentes
     */
    public Short getPassExistentes() {
        return passExistentes;
    }

    /**
     * @param passExistentes the passExistentes to set
     */
    public void setPassExistentes(Short passExistentes) {
        this.passExistentes = passExistentes;
    }

    /**
     * @return the enderecos
     */
    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    /**
     * @param enderecos the enderecos to set
     */
    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    /**
     * @return the usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
        
        
    
}
