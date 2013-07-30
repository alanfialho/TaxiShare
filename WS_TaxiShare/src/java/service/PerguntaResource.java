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
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import entities.LoginEntity;
import entities.NovaPessoaEntity;
import entities.PerguntaEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.QueryParam;

/**
 *
 * @author alan
 */
@Stateless
@Path("/pergunta")
public class PerguntaResource {

    @GET
    @Path("/findAll")
    @Produces("application/json")
    public String findAll() {
        System.out.println("aaaa");
        PerguntaEntity entity = new PerguntaEntity();
        PerguntaJpaController perguntasDAO = new PerguntaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));

        entity.setPerguntas(perguntasDAO.findPerguntaEntities());
        return new Gson().toJson(entity.getPerguntas());

    }
}
