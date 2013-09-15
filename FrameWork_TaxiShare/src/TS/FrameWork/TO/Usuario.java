/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.TO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Bruno
 */
@Entity
@XmlRootElement
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String login;
    private String senha;
    private String resposta;
    @OneToOne
    @JoinColumn(name = "idPessoa")
    private Pessoa novaPessoa;
    @OneToOne
    @JoinColumn(name="idPergunta")
    private Pergunta pergunta;
    @ManyToMany(mappedBy = "usuarios")
    private List<Rota> rotas;
    

    public Usuario()
    {
    
    }
    
    public Usuario(int id) {
        this.id = id;
    }

    public Usuario(String login, String senha, String resposta, Pessoa novaPessoa, Pergunta pergunta, List<Rota> rotas) {
        this.login = login;
        this.senha = senha;
        this.resposta = resposta;
        this.novaPessoa = novaPessoa;
        this.pergunta = pergunta;
        this.rotas = rotas;
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

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Pessoa getNovaPessoa() {
        return novaPessoa;
    }

    public void setNovaPessoa(Pessoa novaPessoa) {
        this.novaPessoa = novaPessoa;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }  

    /**
     * @return the rotas
     */
    
    public List<Rota> getRotas() {
        return rotas;
    }

    /**
     * @param rotas the rotas to set
     */
    public void setRotas(List<Rota> rotas) {
        this.rotas = rotas;
    }
 
    
    @Override
    public String toString(){
        String saida = "Login: " + getLogin() + "\n";
        saida += "Senha: " + getSenha() + "\n";
        saida += "Resposta: " + getSenha() + "\n";        
        return saida;
    }
   
}
