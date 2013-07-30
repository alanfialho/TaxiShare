package com.br.entidades;

public class PerguntaApp {
	
	private Long id;
    private String pergunta;
	public PerguntaApp(Long id, String pergunta) {
		super();
		this.id = id;
		this.pergunta = pergunta;
	}
	public PerguntaApp() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPergunta() {
		return pergunta;
	}
	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}
    
    

}
