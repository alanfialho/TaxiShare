/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import javax.ejb.Stateless;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import TS.FrameWork.DAO.PerguntaJpaController;
import com.google.gson.Gson;
import entities.PerguntaEntity;

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
        PerguntaEntity entity = new PerguntaEntity();
        PerguntaJpaController perguntasDAO = new PerguntaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));

        entity.setPerguntas(perguntasDAO.findPerguntaEntities());
        return new Gson().toJson(entity.getPerguntas());

    }
}
