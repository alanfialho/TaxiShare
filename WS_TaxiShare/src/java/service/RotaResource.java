/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import TS.FrameWork.TO.Rota;
import TS.FrameWork.DAO.RotaJpaController;
import TS.FrameWork.DAO.UsuarioJpaController;
import TS.FrameWork.TO.Endereco;
import TS.FrameWork.TO.Usuario;
import com.google.gson.Gson;
import entities.ResponseEntity;
import entities.RotaEntity;
import entities.UsuarioEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
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
    public String create(RotaEntity entity) {
        
        ResponseEntity saida;
        Rota rota = new Rota();
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        UsuarioJpaController usuarioDAO = new UsuarioJpaController(getEntityManager());
        
        
        try
        {
            //find para carregar as rotas do usuario
            Usuario usuario = usuarioDAO.findUsuario(entity.getAdministrador().getId());
            if(validaHora(usuario.getRotasAdm()))
            {
                //De para entity/rota
                rota.setEnderecos(entity.getEnderecos());
                rota.setFlagAberta(entity.getFlagAberta());
                rota.setPassExistentes(entity.getPassExistentes());
                rota.setAdministrador(usuario);

                //trata a data
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
                java.sql.Date data = new java.sql.Date(format.parse(entity.getDataRota()).getTime());
                rota.setDataRota(data);

                rotaDAO.create(rota);
                saida = new ResponseEntity("Sucesso", 0, "Rota criada com sucesso!", null);
            }
            else
            {
                saida = new ResponseEntity("Erro", 2, "Já existe uma rota aberta no intervalo de 1 hora!", null);
            }
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
    
    @GET
    @Path("/findAll")
    @Produces({"application/json"})
    public String findAll() {
        ResponseEntity saida;
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        List<Rota> rotas = null;
        
        try
        {
            rotas = rotaDAO.findRotaEntities();
            if(rotas != null && rotas.size() > 0)
                saida = new ResponseEntity("Sucesso", 0, "Rotas encontradas!", rotas);
            else
                saida = new ResponseEntity("Sucesso", 2, "Rotas não encontradas!", null);
        }
        catch(Exception ex) 
        {
           System.out.println("ERRRO --> " + ex.getMessage());
           saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);  
        }
        
        return new Gson().toJson(saida);
    }
    
    
    @PUT
    @Path("/joinIn/{idRota}/{idUsuario}")
    @Produces("application/json")
    @Consumes("application/json")
    public String joinIn(@PathParam("idRota")int idRota, @PathParam("idUsuario")int idUsuario) {
        
        ResponseEntity saida = null;
        Rota rota = null;
        Usuario usuario = null;
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        UsuarioJpaController usuarioDAO = new UsuarioJpaController(getEntityManager());
        
        try
        {

            if(idRota <= 0){
                saida = new ResponseEntity("Erro", 2, "Rota inválida!", null);
                throw new Exception("Erro");
            }        
            if(idUsuario <= 0){
                saida = new ResponseEntity("Erro", 3, "Usuário inválido!", null);
                throw new Exception("Erro");
            }
            //validações referente a rota
            rota = rotaDAO.findRota(idRota);
            if(rota == null){
                saida = new ResponseEntity("Erro", 4, "Rota não encontrada!", null);
                throw new Exception("Erro");
            }
            else if(rota.getUsuarios().size() == 4){
                saida = new ResponseEntity("Erro", 5, "Rota já esta lotada!", null);
                throw new Exception("Erro");
            }
            else
            {
                //verifica se o usuário já se encontra na rota
                for(Usuario u : rota.getUsuarios())
                {
                    if(u.getId() == idUsuario){
                        saida = new ResponseEntity("Erro", 6, "O usuário já é participante desta rota", null);
                        throw new Exception("Erro");
                    }
                }
            }

            usuario = usuarioDAO.findUsuario(idUsuario);
            if(usuario == null){
                saida = new ResponseEntity("Erro", 7, "Usuario não encontrado!", null);
                throw new Exception("Erro");
            }
            
            if(saida == null){
                rota.getUsuarios().add(usuario);
                rotaDAO.edit(rota);
                saida = new ResponseEntity("Sucesso", 0, "Participação concluida com sucesso!", null);
            }        
        }
        catch(Exception ex){
            if (!ex.getMessage().equals("Erro"))
            {
                System.out.println("ERRRO --> " + ex.getMessage());
                saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);
            }
        }
        
        return new Gson().toJson(saida);
    }
 
    protected EntityManager getEntityManager() {
        return em;
    }
    protected Boolean validaHora(List<Rota> rotasUsuario)
    {
        Date horaAtual = new Date();        
        
        //verifica se o usuario tem rota aberta e pega o endereço de origem para comparação
        for(Rota r : rotasUsuario)
        {
           
           if(r.getFlagAberta() == true)
           {
               long diff = r.getDataRota().getTime() - horaAtual.getTime() ;//em milesegundos
               int timeInSeconds = (int)diff/1000;
               //verifica se passou uma hora
               if(timeInSeconds > 3600)
               {
                   return true;
               }
               else
               {
                   return false;
               }

           }

        }
        
        return true;
    }
    
}
