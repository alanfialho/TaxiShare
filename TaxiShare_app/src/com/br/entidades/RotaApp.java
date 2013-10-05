package com.br.entidades;

import java.util.List;

public class RotaApp {

	private int id;
	private String dataRota;
	private Boolean flagAberta;
	private int passExistentes;
	private List<EnderecoApp> enderecos;
	private List<LoginApp> usuarios;
	private LoginApp administrador;
	
	public RotaApp(){}

	public RotaApp(String dataRota, Boolean flagAberta, Short passExistentes,
			List<EnderecoApp> enderecos, List<LoginApp> usuarios) {
		
		this.dataRota = dataRota;
		this.flagAberta = flagAberta;
		this.passExistentes = passExistentes;
		this.enderecos = enderecos;
		this.usuarios = usuarios;
	}





	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDataRota() {
		return dataRota;
	}
	public void setDataRota(String dataRota) {
		this.dataRota = dataRota;
	}
	public Boolean getFlagAberta() {
		return flagAberta;
	}
	public void setFlagAberta(Boolean flagAberta) {
		this.flagAberta = flagAberta;
	}
	public int getPassExistentes() {
		return passExistentes;
	}
	public void setPassExistentes(int passExistentes) {
		this.passExistentes = passExistentes;
	}
	public List<EnderecoApp> getEnderecos() {
		return enderecos;
	}
	public void setEnderecos(List<EnderecoApp> enderecos) {
		this.enderecos = enderecos;
	}
	public List<LoginApp> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<LoginApp> usuarios) {
		this.usuarios = usuarios;
	}

	public LoginApp getAdministrador() {
		return administrador;
	}

	public void setAdministrador(LoginApp administrador) {
		this.administrador = administrador;
	}
	

}
