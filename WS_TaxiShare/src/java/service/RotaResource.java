/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import TS.FrameWork.TO.Rota;
import TS.FrameWork.DAO.RotaJpaController;
import TS.FrameWork.DAO.UsuarioJpaController;
import TS.FrameWork.TO.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import entities.ResponseEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author alan
 */
@Stateless
@Path("/rota")
public class RotaResource {
    @PersistenceContext(unitName = "PU")
    private EntityManager em;  

    @POST
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    public String create(Rota rota) {
        ResponseEntity saida;
        Usuario usuario = null;
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        UsuarioJpaController usuarioDAO = new UsuarioJpaController(getEntityManager());
        
        try
        {
            usuarioDAO.findUsuario(rota.getUsuarios().get(0).getId());
            
            rotaDAO.create(rota);
            saida = new ResponseEntity("Sucesso", 0, "Rota criada com sucesso!", null);
        }
        catch(Exception ex){
            System.out.println("ERRRO --> " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);
        }
        
        return new Gson().toJson(saida);
    }
    
    @GET
    @Path("find/{id}")
    @Produces({"application/json"})
    public String find(@PathParam("id") Integer id) {
        ResponseEntity saida;
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        Rota rota = null;
        
        try
        {
            rota = rotaDAO.findRota(id);
            if(rota != null)
                saida = new ResponseEntity("Sucesso", 0, "Rota encontrada!", rota);
            else
                saida = new ResponseEntity("Sucesso", 2, "Rota não encontrada!", null);
        }
        catch(Exception ex) 
        {
           System.out.println("ERRRO --> " + ex.getMessage());
           saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);  
        }
        
        return new Gson().toJson(saida);
    }
 
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
