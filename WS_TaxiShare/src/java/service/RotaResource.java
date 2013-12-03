package service;

import TS.FrameWork.TO.Rota;
import TS.FrameWork.DAO.RotaJpaController;
import TS.FrameWork.DAO.UsuarioJpaController;
import TS.FrameWork.TO.Endereco;
import TS.FrameWork.TO.Perimetro;
import TS.FrameWork.TO.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.ResponseEntity;
import entities.RotaEntity;
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
import org.hibernate.Hibernate;
import util.TsExclusionStrategy;
import util.Utils;

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
            if(validaHora(usuario.getRotasAdm(), entity.getDataRota()))
            {
                //De para entity/rota
                rota.setEnderecos(entity.getEnderecos());
                rota.setFlagAberta(entity.getFlagAberta());
                //soma 1 no passageiro existente
                short aux = (short)(entity.getPassExistentes() + 1);
                rota.setPassExistentes(aux);
                rota.setAdministrador(usuario);

                //trata a data
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
            System.out.println("ERRRO createRote(): " + ex.getMessage());
            saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);
        }
        
        return new Gson().toJson(saida);
    }
    
    @GET
    @Path("findById/{id}")
    @Produces({"application/json"})
    public String find(@PathParam("id") Integer id) {
        
        
        ResponseEntity saida;
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        Rota rota = null;
        
        try
        {
            rota = rotaDAO.findRota(id);
            if(rota != null)
            {
                rota.setUsuarios(Utils.solveRecursionRotas(rota.getUsuarios()));
                rota.setAdministrador(Utils.solveRecursionRotas(rota.getAdministrador()));
                saida = new ResponseEntity("Sucesso", 0, "Rota encontrada!", rota);
            }
            else{
                saida = new ResponseEntity("Sucesso", 2, "Rota não encontrada!", null);
            }
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
    public String findAll(){
        ResponseEntity saida;
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        List<Rota> rotas = null;
        try
        {
            rotas = rotaDAO.findRotaEntities();
            if(rotas != null && rotas.size() > 0){
                saida = new ResponseEntity("Sucesso", 0, "Rotas encontradas!", rotas);
            }
            else
                saida = new ResponseEntity("Sucesso", 2, "Rotas não encontradas!", null);
        }
        catch(Exception ex) 
        {
           System.out.println("ERRRO --> " + ex.getMessage());
           saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);  
        }
        //elimina os usuarios na serialização do objeto
        //ideal é usar o detach da JPA no objeto mas não foi possivel devido aos problemas com jar
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new TsExclusionStrategy(Usuario.class))
                .serializeNulls()
                .create();
        
        return gson.toJson(saida);
    }
    
    @POST
    @Path("/findByPerimeter")
    @Produces({"application/json"})
    public String findByPerimeter(List<Perimetro> perimetros){
        ResponseEntity saida;
        RotaJpaController rotaDAO = new RotaJpaController(getEntityManager());
        List<Rota> rotas = null;
        try
        {
            //perimetros igual a dois por causa da origem e destino
            if(perimetros != null && perimetros.size() == 2){
                rotas = rotaDAO.findByPerimeter(perimetros.get(0), perimetros.get(1));
                if(rotas != null && rotas.size() > 0){
                    saida = new ResponseEntity("Sucesso", 0, "Rotas encontradas!", rotas);
                }
                else
                    saida = new ResponseEntity("Sucesso", 2, "Rotas não encontradas!", null);
            }
            else
            {
                saida = new ResponseEntity("Erro", 3, "Perimetros inválidos!", null);
            }
        }
        catch(Exception ex) 
        {
           System.out.println("ERRRO --> " + ex.getMessage());
           saida = new ResponseEntity("Erro", 1, "Não foi possivel realizar operação, tente mais tarde!", null);  
        }
        //elimina os usuarios na serialização do objeto
        //ideal é usar o detach da JPA no objeto mas não foi possivel devido aos problemas com jar
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new TsExclusionStrategy(Usuario.class))
                .serializeNulls()
                .create();
        
        return gson.toJson(saida);
    }
    
    @PUT
    @Path("/joinIn/{idRota}/{idUsuario}")
    @Produces("application/json")
    @Consumes("application/json")
    public String joinIn(@PathParam("idRota")int idRota, @PathParam("idUsuario")int idUsuario, Endereco endereco) {
        
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
            else if(rota.getPassExistentes() == 4){
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
            
            if(endereco.getId() != 0)
            {
                saida = new ResponseEntity("Erro", 8, "Endereco inválido!", null);
                throw new Exception("Erro");
            }
            if(saida == null){
                rota.getUsuarios().add(usuario);
                rota.getEnderecos().add(endereco);
                //soma 1 no passageiro existente
                short aux = (short)(rota.getPassExistentes() + 1);
                rota.setPassExistentes(aux);
                //atualiza a rota
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
    
    @PUT
    @Path("/exitRote/{idRota}/{idUsuario}")
    @Produces("application/json")
    @Consumes("application/json")
    public String exitRote(@PathParam("idRota")int idRota, @PathParam("idUsuario")int idUsuario, Endereco endereco)    {
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
            else
            {
                //verifica se o usuário já se encontra na rota
                Boolean ok = false;
                for(Usuario u : rota.getUsuarios())
                {
                    if(u.getId() == idUsuario)
                         ok = true;
                }
                if(!ok)
                {
                        saida = new ResponseEntity("Erro", 6, "O usuário não é participante desta rota", null);
                        throw new Exception("Erro");
                }
                    
            }

            usuario = usuarioDAO.findUsuario(idUsuario);
            if(usuario == null){
                saida = new ResponseEntity("Erro", 7, "Usuario não encontrado!", null);
                throw new Exception("Erro");
            }
            
            if(endereco.getId() == 0)
            {
                saida = new ResponseEntity("Erro", 8, "Endereco inválido!", null);
                throw new Exception("Erro");
            }
            if(saida == null){
                rota.getUsuarios().remove(usuario);
                rota.getEnderecos().remove(endereco);
                //diminui 1 no passageiro existente
                short aux = (short)(rota.getPassExistentes() - 1);
                rota.setPassExistentes(aux);
                //atualiza a rota
                rotaDAO.edit(rota); 
                saida = new ResponseEntity("Sucesso", 0, "Saiu da rota com sucesso!", null);
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
    
    protected Boolean validaHora(List<Rota> rotasAdm, String dt)    {
        //se não for maior que zero significa que é a primeira rota
        if (rotasAdm.size() > 0)
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for(Rota r : rotasAdm)
            {
                try
                {
                    //Horario que está tentando cadastrar a rota
                    Date dataAgendamento = format.parse(dt);
                    //Horario das rotas na base
                    String aux = format.format(r.getDataRota());
                    Date dataRota = format.parse(aux);
                    
                    //faz a diferença entre horarios 
                    long diff = dataAgendamento.getTime() - dataRota.getTime();
                    if(diff == 0)
                        return false;
                    
                    long seconds = diff/1000;
                    //quando o horario do agendamento é posterior do horario de uma rota cadastrada
                    if(seconds > 0 && seconds < 3600)
                        return false;
                    //quando o horario do agendamento é antes do horário de uma rota cadastrada
                    if(seconds < 0)
                    {
                        
                        if(seconds > -3600)
                            return false;
                        //agora compara com a data atual para fechar intervalo de uma hora
                        long diffNow = dataAgendamento.getTime() - new Date().getTime(); 
                        long nowInSeconds = diffNow/1000;
                        if(nowInSeconds < 3600)
                            return false;
                        
                    }
                    
                }
                catch(Exception ex)
                {
                    System.out.println("ERRRO validaHora(): " + ex.getMessage());
                }
            }
        }
     
        
        return true;
    }
    
}
