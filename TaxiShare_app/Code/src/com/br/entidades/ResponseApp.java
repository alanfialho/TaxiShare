package com.br.entidades;

import java.io.Serializable;


public class ResponseApp implements Serializable {

    private String erro;
    private int errorCode;
    private String descricao;
    private Object data;

    public ResponseApp() {
    }

    public ResponseApp(String erro, int errorCode, String descricao, Object data) {
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
