package com.br.entidades;

public class LoginApp {
	
	private Long id;
	private String login;
	private String senha;
	private PerguntaApp pergunta;
	private String resposta;
	private NovaPessoaApp pessoa;
	
	public LoginApp(Long id, String login, String senha, PerguntaApp pergunta,
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public NovaPessoaApp getPessoa() {
		return pessoa;
	}
	public void setPessoa(NovaPessoaApp pessoa) {
		this.pessoa = pessoa;
	}
}
