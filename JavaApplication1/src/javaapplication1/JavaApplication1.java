/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author Bruno
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JavaApplication1 j = new JavaApplication1();
//        Pessoa p = j.
        
        Pessoa a = new Pessoa("Bruno");
        Pessoa b = a;
        Pessoa c = b;
        
        c.nome = "alan";
        
    }
    
    private class Pessoa{
       private String nome;
       
       public Pessoa(String nome ){
           this.nome = nome;
       }
       
       public void update (Pessoa p){
           p.nome = "bruno";
           System.out.println("Bruno");
           
       }
    }
}
