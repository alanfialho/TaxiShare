package com.br.entidades;

public class PerguntaApp {
	
	private int id;
    private String pergunta;
	public PerguntaApp(int id, String pergunta) {
		super();
		this.id = id;
		this.pergunta = pergunta;
	}
	public PerguntaApp() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPergunta() {
		return pergunta;
	}
	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}
    
    

}
