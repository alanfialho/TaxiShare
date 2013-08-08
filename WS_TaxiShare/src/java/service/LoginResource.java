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
import TS.FrameWork.TO.NovaPessoa;
import TS.FrameWork.DAO.NovaPessoaJpaController;
import TS.FrameWork.DAO.PerguntaJpaController;
import TS.FrameWork.TO.Login;
import TS.FrameWork.TO.Pergunta;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import entities.LoginEntity;
import entities.NovaPessoaEntity;
import entities.PerguntaEntity;
import entities.ResponseEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import javax.ws.rs.QueryParam;

/**
 *
 * @author alan
 */
@Stateless
@Path("/login")
public class LoginResource {

    @GET
    @Path("/login")
    @Produces("application/json")
    public String login(@QueryParam("login") String loginInfo, @QueryParam("password") String password) {
        System.out.println("Login  --> " + loginInfo);
        System.out.println("Senha --> " + password);

        //Controlador do banco de login
        LoginJpaController loginDao = new LoginJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));

        //Entidades de login, pessoa e pergunta
        LoginEntity loginEntity = new LoginEntity();
        NovaPessoaEntity pessoaEntity = new NovaPessoaEntity();
        PerguntaEntity perguntaEntity = new PerguntaEntity();

        try {
            Login login = (Login) loginDao.findLogin(loginInfo);
            if (login != null) {
                //Checa se a senha bate com o login
                if (login.getSenha().equals(password)) {

                    //Pegando a pessoa do login
                    NovaPessoa pessoa = login.getNovaPessoa();

                    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy");
                    String date = formater.format(pessoa.getDataNascimento());
                    //Pega os dados da pessoa do FW e seta na entidade pessoa WS
                    pessoaEntity.setNome(pessoa.getNome());
                    pessoaEntity.setNick(pessoa.getNick());
                    pessoaEntity.setDataNascimento(date);
                    pessoaEntity.setCelular(pessoa.getCelular());
                    pessoaEntity.setSexo(pessoa.getSexo());
                    pessoaEntity.setDdd(pessoa.getDdd());
                    pessoaEntity.setEmail(pessoa.getEmail());

                    //Seta os dados da pergunta do FW e seta na entidade pergunta WS
                    perguntaEntity.setId(login.getPergunta().getId());
                    perguntaEntity.setPergunta(login.getPergunta().getPergunta());


                    //Pega os dados do login do FW e seta na entidade login do WS
                    loginEntity.setId(login.getId());
                    loginEntity.setLogin(login.getLogin());
                    loginEntity.setPessoa(pessoaEntity);

                    //Objeto de retorno
                    ResponseEntity saida = new ResponseEntity("Sucesso", 0, "Login efetuado!", loginEntity);

                    //Retorna um json completo com os dados do usuarios
                    return new Gson().toJson(saida);
                } else {
                    System.out.println("Não Logou");
                    ResponseEntity saida = new ResponseEntity("Erro", 1, "Senha inválida!", null);
                    return new Gson().toJson(saida);
                }

            } else {
                System.out.println("Não Logou");
                ResponseEntity saida = new ResponseEntity("Erro", 1, "Login inválido!", null);

                return new Gson().toJson(saida);
            }
        } catch (Exception ex) {
            System.out.println("ERRRO --> " + ex.getStackTrace());
            ResponseEntity saida = new ResponseEntity("Erro", 2, "Exception de cu é rola!", null);
            return new Gson().toJson(saida);
        }
    }

    @GET
    @Path("/checkLogin")
    @Produces("application/json")
    public String login(@QueryParam("login") String loginInfo) {
        System.out.println("Login BRUNAO --> " + loginInfo);

        //Controlador do banco de login
        LoginJpaController loginDao = new LoginJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));

        try {
            Login login = (Login) loginDao.findLogin(loginInfo);
            if (login != null) {
                //Retorna um json informando que o login ja existe
                ResponseEntity saida = new ResponseEntity("Erro", 1, "Login já existe!", null);

                return new Gson().toJson(saida);
            } else {
                System.out.println("Login Disponivel");
                //Retorna um json informando que o login esta disponivel
                ResponseEntity saida = new ResponseEntity("Sucesso", 0, "Login Disponivel!", null);
                return new Gson().toJson(saida);
            }

        } catch (Exception ex) {
            System.out.println("ERRRO --> " + ex.getStackTrace());
            ResponseEntity saida = new ResponseEntity("Erro", 2, "Exception de cu é rola!", null);
            return new Gson().toJson(saida);
        }
    }

    @POST
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    public String create(LoginEntity entity) {

        try {

            //Cria uma pessoa e um login
            NovaPessoa pessoa = new NovaPessoa();
            Login login = new Login();
            Pergunta pergunta = new Pergunta();

            //Cria um novo controle de pessoa
            NovaPessoaJpaController pessoaDAO = new NovaPessoaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
            LoginJpaController loginDAO = new LoginJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
            PerguntaJpaController perguntaDAO = new PerguntaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));

            NovaPessoaEntity pessoaEntity = entity.getPessoa();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            java.sql.Date data = new java.sql.Date(format.parse(pessoaEntity.getDataNascimento()).getTime());

            //Inicializa os valores da pessoa de acordo com a entity
            pessoa.setNome(pessoaEntity.getNome());
            pessoa.setNick(pessoaEntity.getNick());
            pessoa.setCelular(pessoaEntity.getCelular());
            pessoa.setDdd(pessoaEntity.getDdd());
            pessoa.setSexo(pessoaEntity.getSexo());
            pessoa.setEmail(pessoaEntity.getEmail());
            pessoa.setDataNascimento(data);

            System.out.println("Pessoa --> " + pessoa.toString());

            //Recupera a pertgunta
            PerguntaEntity perguntaEntity = entity.getPergunta();
            pergunta = perguntaDAO.findPergunta(perguntaEntity.getId() + 1);
            System.out.println("Pergutna --> " + pergunta.toString());

            //Inicia os valores do login de acordo com a entity
            login.setLogin(entity.getLogin());
            login.setSenha(entity.getSenha());
            login.setPergunta(pergunta);
            login.setNovaPessoa(pessoa);
            login.setResposta(entity.getResposta());

            System.out.println("Login --> " + login.toString());

            System.out.println("Criando pessoas");
            pessoaDAO.create(pessoa);
            System.out.println("Criando Login");
            loginDAO.create(login);

            ResponseEntity saida = new ResponseEntity("Erro", 0, "Cadastro Efetuado!", null);
            return new Gson().toJson(saida);

        } catch (Exception ex) {
            System.out.println("Erro no login resource --> " + ex.getMessage());
            ResponseEntity saida = new ResponseEntity("Erro", 2, "Exception de cu é rola!", null);
            return new Gson().toJson(saida);
        }
    }
}
