/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.TO;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bruno
 */
@Entity
@XmlRootElement
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String login;
    private String senha;
    private String resposta;
    @OneToOne
    @JoinColumn(name = "idPessoa")
    private NovaPessoa novaPessoa;
    @OneToOne
    @JoinColumn(name="idPergunta")
    private Pergunta pergunta;

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

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public NovaPessoa getNovaPessoa() {
        return novaPessoa;
    }

    public void setNovaPessoa(NovaPessoa novaPessoa) {
        this.novaPessoa = novaPessoa;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }  
    
    
    @Override
    public String toString(){
        String saida = "Login: " + getLogin() + "\n";
        saida += "Senha: " + getSenha() + "\n";
        saida += "Resposta: " + getSenha() + "\n";        
        return saida;
    }
   
}
