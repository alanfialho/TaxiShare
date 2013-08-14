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

/**
 *
 * @author alan
 */
@Stateless
@Path("/pessoa")
public class PessoaResource {

    @POST
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    public String create(PessoaEntity entity) {
       
        ResponseEntity saida;
        PessoaJpaController pessoaDAO;
        
        try 
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            java.sql.Date data = new java.sql.Date(format.parse(entity.getDataNascimento()).getTime());
            
            Pessoa pessoa = new Pessoa();
            pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
           
            //Inicializa os valores da pessoa de acordo com a entity
            pessoa.setNome(entity.getNome());
            pessoa.setNick(entity.getNick());
            pessoa.setCelular(entity.getCelular());
            pessoa.setDdd(entity.getDdd());
            pessoa.setSexo(entity.getSexo());
            pessoa.setEmail(entity.getEmail());
            pessoa.setDataNascimento(data);
            //Insere a pessoa preenchida no banco
            pessoaDAO.create(pessoa);
            
            saida = new ResponseEntity("Sucesso", 0, "Cadastro realizado com sucesso!", null);    
        } 
        catch (Exception ex) 
        {
            System.out.println("ERRRO --> " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);
        }
        
        return new Gson().toJson(saida);
    }

    @GET
    @Path("/findById/{id}")
    @Produces("application/json")
    public String findById(@PathParam("id") Long id) {

        ResponseEntity saida;
        PessoaJpaController pessoaDAO;
        
        try
        {
            pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
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
            pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
            entity.setLstPessoas(pessoaDAO.findPessoaEntities());
            
            if(entity.getLstPessoas().size() > 0)
                saida = new ResponseEntity("Sucesso", 0, "Lista de Pessoas", entity);
            else
                saida = new ResponseEntity("Sucesso", 2, "Pessoas não encontradas", null);
        }
        catch(Exception ex)
        {
            System.out.println("ERRRO --> " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);
        }

        //Retorna um json completo com os dados das pessoas
        return new Gson().toJson(saida);

    }

    @POST
    @Path("/edit")
    @Produces("application/json")
    @Consumes("application/json")
    public String edit(PessoaEntity entity) {
        PessoaJpaController pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
        Pessoa pessoa = new Pessoa();
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            java.sql.Date data = new java.sql.Date(format.parse(entity.getDataNascimento()).getTime());
            pessoa.setId(entity.getId());
            pessoa.setNome(entity.getNome());
            pessoa.setSexo(entity.getSexo());
            pessoa.setEmail(entity.getEmail());
            pessoa.setDdd(entity.getDdd());
            pessoa.setCelular(entity.getCelular());
            pessoa.setNick(entity.getNick());
            pessoa.setDataNascimento(data);

            pessoaDAO.edit(pessoa);

            //Objeto de retorno
            ResponseEntity saida = new ResponseEntity("Sucesso", 0, "Cadastro alterado com sucesso!", entity);

            //Retorna um json completo com os dados do usuarios
            return new Gson().toJson(saida);
            
        } catch (Exception ex) {
            System.out.println("ERRRO --> " + ex.getStackTrace());
            ResponseEntity saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);
            return new Gson().toJson(saida);
        }

    }
}
