package com.br.entidades;

import java.util.List;

public class LoginApp {
	
	private int id;
	private String login;
	private String senha;
	private PerguntaApp pergunta;
	private String resposta;
	private PessoaApp pessoa;
	private List<RotaApp> rotas;
	private List<RotaApp> rotasAdm;
	
	public LoginApp(int id, String login, String senha, PerguntaApp pergunta,
			String resposta) {
		super();
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.pergunta = pergunta;
		this.resposta = resposta;
	}
	public LoginApp() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public PerguntaApp getPergunta() {
		return pergunta;
	}
	public void setPergunta(PerguntaApp pergunta) {
		this.pergunta = pergunta;
	}
	public String getResposta() {
		return resposta;
	}
	public void setResposta(String resposta) {
		this.resposta = resposta;
	}
	public PessoaApp getPessoa() {
		return pessoa;
	}
	public void setPessoa(PessoaApp pessoa) {
		this.pessoa = pessoa;
	}
	public List<RotaApp> getRotas() {
		return rotas;
	}
	public void setRotas(List<RotaApp> rotas) {
		this.rotas = rotas;
	}
	public List<RotaApp> getRotasAdm() {
		return rotasAdm;
	}
	public void setRotasAdm(List<RotaApp> rotasAdm) {
		this.rotasAdm = rotasAdm;
	}
}
