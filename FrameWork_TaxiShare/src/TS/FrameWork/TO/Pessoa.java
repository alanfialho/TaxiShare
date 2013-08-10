package TS.FrameWork.TO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Pessoa implements Serializable {

    //Marcação que define como ID auto gerado no BD
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //Marcação que define como tipo data no BD
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascimento;
    private String nome;
    private String nick;
    private String ddd;
    private String celular;
    private String sexo;
    private String email;

    public Pessoa() {
    }

    public Pessoa(String nome, Date dataNascimento, String celular) {
        super();

        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.celular = celular;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
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
    public String toString() {

        String saida = "Nome: " + getNome() + "\n";
        saida += "Nick: " + getNick() + "\n";
        saida += "Email: " + getEmail() + "\n";
        saida += "DDD: " + getDdd() + "\n";
        saida += "DATA: " + getDataNascimento() + "\n";
        saida += "Sexo: " + getSexo() + "\n";
        saida += "Celular: " + getCelular() + "\n";
        saida += "ID: " + getId() + "\n";

        return saida;

    }
}