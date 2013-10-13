/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.TO;

/**
 *
 * @author alan
 */
public class Perimetro {
    
    private double cima;
    private double baixo;
    private double esquerda;
    private double direita;

    public Perimetro(){};
    
    public Perimetro(double cima, double baixo, double esquerda, double direita) {
        this.cima = cima;
        this.baixo = baixo;
        this.esquerda = esquerda;
        this.direita = direita;
    }

    public double getCima() {
        return cima;
    }

    public void setCima(double cima) {
        this.cima = cima;
    }

    public double getBaixo() {
        return baixo;
    }

    public void setBaixo(double baixo) {
        this.baixo = baixo;
    }

    public double getDireita() {
        return direita;
    }

    public void setDireita(double direita) {
        this.direita = direita;
    }
    
    public double getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(double esquerda) {
        this.esquerda = esquerda;
    }
    
}
