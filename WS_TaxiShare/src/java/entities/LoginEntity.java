/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//comentando aqui na moral
package entities;

import TS.FrameWork.TO.Pessoa;
import TS.FrameWork.TO.Pergunta;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author bruno
 */
@XmlRootElement
public class LoginEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private Long id;
    @XmlElement
    private String login;
    @XmlElement
    private String senha;
    @XmlElement
    private PerguntaEntity pergunta;
    @XmlElement
    private String resposta;
    @XmlElement
    private PessoaEntity pessoa;
    

    public LoginEntity(Long id, String login, String senha, PerguntaEntity pergunta, String resposta) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public LoginEntity() {
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

    public PerguntaEntity getPergunta() {
        return pergunta;
    }

    public void setPergunta(PerguntaEntity pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public PessoaEntity getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaEntity pessoa) {
        this.pessoa = pessoa;
    }
    
    
}