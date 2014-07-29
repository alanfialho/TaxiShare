package com.br.entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class PessoaApp implements Parcelable {


	private int id;
	private String dataNascimento;
	private String nome;
	private String ddd;
	private String celular;
	private String sexo;
	private String email;
	
	public PessoaApp(int id, String dataNascimento, String nome, String nick,
			String ddd, String celular, String sexo, String email) {
		super();
		this.id = id;
		this.dataNascimento = dataNascimento;
		this.nome = nome;
		this.ddd = ddd;
		this.celular = celular;
		this.sexo = sexo;
		this.email = email;
	}
	public PessoaApp() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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