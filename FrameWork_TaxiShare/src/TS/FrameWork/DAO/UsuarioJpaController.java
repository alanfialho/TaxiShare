/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.DAO;

import TS.FrameWork.DAO.exceptions.NonexistentEntityException;
import TS.FrameWork.TO.Rota;
import TS.FrameWork.TO.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author alan
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManager emf) {
        this.emf = emf;
    }
    private EntityManager emf = null;

    public EntityManager getEntityManager() {
        return emf;
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.persist(usuario);
        } 
        catch(Exception ex) {
            throw ex;
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.merge(usuario);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        Usuario usuario;
        try {
            em = getEntityManager();
            usuario = em.getReference(Usuario.class, id);
            em.remove(usuario);
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Usuario as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch(Exception ex) {
            throw ex;
        }
    }

    public Usuario findUsuario(int id) {
        EntityManager em = getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return usuario;
        } catch(Exception ex){
            throw ex;
        }
    }
    
    public Usuario findLogin(String login) {
        EntityManager em = getEntityManager();
        System.out.println("ENtrou no find login");
        try {
            String query = "select u from Usuario u where u.login like :login";
            Query q = em.createQuery(query).setParameter("login", login);
            List results = q.getResultList();
            Usuario foundEntity = null;
            if (!results.isEmpty()) {
                // ignora multiplos resultados
                foundEntity = (Usuario) results.get(0);
            }

            return foundEntity;
        }catch(Exception ex){
            throw ex;
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Usuario as o");
            return ((Long) q.getSingleResult()).intValue();
        } catch(Exception ex) {
            throw ex;
        }
    }
    
}
