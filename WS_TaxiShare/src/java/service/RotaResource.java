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
import TS.FrameWork.TO.Rota;
import TS.FrameWork.DAO.RotaJpaController;
import com.google.gson.Gson;
import entities.ResponseEntity;
import java.text.SimpleDateFormat;


/**
 *
 * @author alan
 */
@Stateless
@Path("/rota")
public class RotaResource {
    
    @POST
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    public String create(Rota rota) {
       
        ResponseEntity saida;
        RotaJpaController rotaDAO;
        
        try 
        {
             
            rotaDAO = new RotaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
            rotaDAO.create(rota);
            
            saida = new ResponseEntity("Sucesso", 0, "Rota criada com sucesso!", null);    
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
    public String findById(@PathParam("id") int id) {

        ResponseEntity saida;
        RotaJpaController rotaDAO;
        
        try
        {
            rotaDAO = new RotaJpaController(Persistence.createEntityManagerFactory("HibernateJPAPU"));
            Rota rota = rotaDAO.findRota(id);
            
            if(rota != null)
            {
                saida = new ResponseEntity("Sucesso", 0, "Rota Encontrada", rota);
            }
            else
            {
                saida = new ResponseEntity("Sucesso", 2, "Rota não encontrada", null);
            }
            
        }
        catch(Exception ex)
        {
            System.out.println("ERRRO --> " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde", null);
        }

        return new Gson().toJson(saida);

    }
}
