/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import TS.FrameWork.DAO.LoginJpaController;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import TS.FrameWork.TO.Pessoa;
import TS.FrameWork.DAO.PessoaJpaController;
import TS.FrameWork.TO.Login;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import entities.LoginEntity;
import entities.PessoaEntity;
import entities.ResponseEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.QueryParam;

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
    public void create(PessoaEntity entity) {
        /*
         * MÉTODO EM DESUSO
         * MÉTODO EM DESUSO
         * MÉTODO EM DESUSO
         * MÉTODO EM DESUSO         
         */

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            java.sql.Date data = new java.sql.Date(format.parse(entity.getDataNascimento()).getTime());

            //Cria uma pessoa e um login
            Pessoa pessoa = new Pessoa();
            Login login = new Login();

            //Cria um novo controle de pessoa
            PessoaJpaController pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
            LoginJpaController loginDAO = new LoginJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));

            //Inicializa os valores da pessoa de acordo com a entity
            pessoa.setNome(entity.getNome());
            pessoa.setNick(entity.getNick());
            pessoa.setCelular(entity.getCelular());
            pessoa.setDdd(entity.getDdd());
            pessoa.setSexo(entity.getSexo());
            pessoa.setEmail(entity.getEmail());
            pessoa.setDataNascimento(data);

            //Inicia os valores do login de acordo com a entity
            login.setLogin(entity.getLogin().getLogin());
            login.setSenha(entity.getLogin().getSenha());
            login.setPergunta(entity.getLogin().getPergunta());
            login.setResposta(entity.getLogin().getResposta());

            loginDAO.create(login);

            //Insere a pessoa preenchida no banco
            pessoaDAO.create(pessoa);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GET
    @Path("/findById/{id}")
    @Produces("application/json")
    public String findById(@PathParam("id") Long id) {


        PessoaEntity entity = new PessoaEntity();
        PessoaJpaController pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
        Pessoa pessoa = pessoaDAO.findNovaPessoa(id);


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        String data = format.format(pessoa.getDataNascimento());
        entity.setId(pessoa.getId());
        entity.setNome(pessoa.getNome());
        entity.setSexo(pessoa.getSexo());
        entity.setEmail(pessoa.getEmail());
        entity.setDdd(pessoa.getDdd());
        entity.setCelular(pessoa.getCelular());
        entity.setDataNascimento(data);

        return new Gson().toJson(entity);

    }

    @GET
    @Path("/findAll")
    @Produces("application/json")
    public String findAll() {
        System.out.println("nada");

        PessoaEntity entity = new PessoaEntity();
        PessoaJpaController pessoaDAO = new PessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));

        entity.setLstPessoas(pessoaDAO.findNovaPessoaEntities());
        return new Gson().toJson(entity.getLstPessoas());

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
            ResponseEntity saida = new ResponseEntity("Sucesso", 0, "Cadastro Alterado", entity);

            //Retorna um json completo com os dados do usuarios
            return new Gson().toJson(saida);
            
        } catch (Exception ex) {
            System.out.println("ERRRO --> " + ex.getStackTrace());
            ResponseEntity saida = new ResponseEntity("Erro", 2, "Exception de cu é rola!", null);
            return new Gson().toJson(saida);
        }

    }
}
