package com.br.entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class NovaPessoaApp implements Parcelable {


	private Long id;
	private String dataNascimento;
	private String nome;
	private String nick;
	private String ddd;
	private String celular;
	private String sexo;
	private String email;
	
	public NovaPessoaApp(Long id, String dataNascimento, String nome, String nick,
			String ddd, String celular, String sexo, String email) {
		super();
		this.id = id;
		this.dataNascimento = dataNascimento;
		this.nome = nome;
		this.nick = nick;
		this.ddd = ddd;
		this.celular = celular;
		this.sexo = sexo;
		this.email = email;
	}
	public NovaPessoaApp() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}   
	
	

}