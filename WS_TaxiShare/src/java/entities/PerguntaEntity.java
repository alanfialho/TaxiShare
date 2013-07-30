/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import TS.FrameWork.TO.NovaPessoa;
import TS.FrameWork.TO.Pergunta;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author bruno
 */
@XmlRootElement
public class PerguntaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private Long id;
    @XmlElement
    private String pergunta;
    @XmlElement
    private List<Pergunta> perguntas;

    public PerguntaEntity() {
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

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }
    
    @Override
    public String toString(){
        String saida = "ID: " + getId() + " Pergunta: " + getPergunta();
        return saida;
    }
}