/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bruno
 */
@XmlRootElement
public class ResponseEntity implements Serializable {

    @XmlElement
    private String erro;
    @XmlElement
    private int errorCode;
    @XmlElement
    private String descricao;
    @XmlElement
    private Object data;

    public ResponseEntity() {
    }

    public ResponseEntity(String erro, int errorCode, String descricao, Object data) {
        this.erro = erro;
        this.errorCode = errorCode;
        this.descricao = descricao;
        this.data = data;
    }   

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }  
    
}
