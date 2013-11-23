/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import javax.ejb.Stateless;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import TS.FrameWork.TO.Pessoa;
import TS.FrameWork.DAO.PessoaJpaController;
import com.google.gson.Gson;
import entities.PessoaEntity;
import entities.ResponseEntity;
import java.text.SimpleDateFormat;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alan
 */
@Stateless
@Path("/pessoa")
public class PessoaResource {
    
    @PersistenceContext(unitName = "PU")
    private EntityManager em;  
    
    @POST
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    public String create(Pessoa pessoa) {
       
        ResponseEntity saida;
        PessoaJpaController pessoaDAO;
                    System.out.println("entrou no create pessoa resource");

        
        try 
        {
            //trata a data 
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            String data = format.format(pessoa.getDataNascimento());
            pessoa.setDataNascimento(format.parse(data));
            
            pessoaDAO = new PessoaJpaController(getEntityManager());
            pessoaDAO.create(pessoa);
            
            saida = new ResponseEntity("Sucesso", 0, "Cadastro realizado com sucesso!", null); 
            System.out.println("create pessoa resource sucesso");
            
        } 
        catch (Exception ex) 
        {
            System.out.println("ERRRO --> " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, ex.getMessage(), null);
            System.out.println("create pessoa resource exception -> " + ex + " -- Message -> " + ex.getMessage());
        }
        
        return new Gson().toJson(saida);
    }

    @GET
    @Path("/findById/{id}")
    @Produces("application/json")
    public String findById(@PathParam("id") int id) {

        ResponseEntity saida;
        PessoaJpaController pessoaDAO;
        
        try
        {
            pessoaDAO = new PessoaJpaController(getEntityManager());
            Pessoa pessoa = pessoaDAO.findPessoa(id);
            
            if(pessoa != null)
            {
                saida = new ResponseEntity("Sucesso", 0, "Pessoa Encontrada", pessoa);
            }
            else
            {
                saida = new ResponseEntity("Sucesso", 2, "Pessoa não encontrada", null);
            }
            
        }
        catch(Exception ex)
        {
            System.out.println("ERRRO --> " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde", null);
        }
        return new Gson().toJson(saida);

    }

    @GET
    @Path("/findAll")
    @Produces("application/json")
    public String findAll() {
        
        ResponseEntity saida;
        PessoaEntity entity;
        PessoaJpaController pessoaDAO;
        try
        {
            entity = new PessoaEntity();
            pessoaDAO = new PessoaJpaController(getEntityManager());
            entity.setLstPessoas(pessoaDAO.findPessoaEntities());
            
            if(entity.getLstPessoas().size() > 0)
                saida = new ResponseEntity("Sucesso", 0, "Lista de Pessoas", entity);
            else
                saida = new ResponseEntity("Sucesso", 2, "Pessoas não encontradas", null);
        }
        catch(Exception ex)
        {
            System.out.println("ERRRO --> " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, ex.getMessage(), null);
        }

        //Retorna um json completo com os dados das pessoas
        return new Gson().toJson(saida);

    }

    @POST
    @Path("/edit")
    @Produces("application/json")
    @Consumes("application/json")
    public String edit(Pessoa pessoa) {
        PessoaJpaController pessoaDAO = new PessoaJpaController(getEntityManager());
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            java.sql.Date data = new java.sql.Date(pessoa.getDataNascimento().getTime());
            String sqlDate = format.format(data);
            pessoa.setDataNascimento(format.parse(sqlDate));

            pessoaDAO.edit(pessoa);

            //Objeto de retorno
            ResponseEntity saida = new ResponseEntity("Sucesso", 0, "Cadastro alterado com sucesso!", null);

            //Retorna um json completo com os dados do usuarios
            return new Gson().toJson(saida);
            
        } catch (Exception ex) {
            System.out.println("ERRRO --> " + ex.getStackTrace());
            ResponseEntity saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);
            return new Gson().toJson(saida);
        }

    }
    protected EntityManager getEntityManager() {
        return em;
    }
}
