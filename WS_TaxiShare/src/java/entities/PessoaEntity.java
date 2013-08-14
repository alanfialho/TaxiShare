/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//comentando aqui na moral
package entities;

import TS.FrameWork.TO.Login;
import TS.FrameWork.TO.Pessoa;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author alan
 */
@XmlRootElement
public class PessoaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private Long id;
    @XmlElement
    private String nome;
    @XmlElement
    private String nick;
    @XmlElement
    private String ddd;
    @XmlElement
    private String celular;
    @XmlElement
    private String dataNascimento;
    @XmlElement
    private String email;
    @XmlElement
    private String sexo;
    @XmlElement
    private Login login;
    @XmlElement
    private List<Pessoa> lstPessoas;

    public PessoaEntity(Long id, String nome, String ddd, String celular, String dataNascimento, String email, String sexo, List<Pessoa> lstPessoas) {
        this.id = id;
        this.nome = nome;
        this.ddd = ddd;
        this.celular = celular;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.sexo = sexo;
        this.lstPessoas = lstPessoas;
    }

    public PessoaEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
 
    public List<Pessoa> getLstPessoas() {
        return lstPessoas;
    }

    public void setLstPessoas(List<Pessoa> lstPessoas) {
        this.lstPessoas = lstPessoas;
    }  

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
    
    
   
}
