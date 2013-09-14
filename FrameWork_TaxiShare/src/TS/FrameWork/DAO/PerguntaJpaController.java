/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.DAO;

import TS.FrameWork.DAO.exceptions.NonexistentEntityException;
import TS.FrameWork.TO.Pergunta;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Bruno
 */
public class PerguntaJpaController implements Serializable {

    public PerguntaJpaController(EntityManager emf) {
        this.emf = emf;
    }
    private EntityManager emf = null;

    public EntityManager getEntityManager() {
        return emf;
    }

    public void create(Pergunta pergunta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.persist(pergunta);
        } catch(Exception ex) {
            throw ex;
        }
    }

    public void edit(Pergunta pergunta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            pergunta = em.merge(pergunta);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = pergunta.getId();
                if (findPergunta(id) == null) {
                    throw new NonexistentEntityException("The pergunta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Pergunta pergunta;
            try {
                pergunta = em.getReference(Pergunta.class, id);
                pergunta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pergunta with id " + id + " no longer exists.", enfe);
            }
            em.remove(pergunta);
        } catch(Exception ex) {
            throw ex;
        }
    }

    public List<Pergunta> findPerguntaEntities() {
        return findPerguntaEntities(true, -1, -1);
    }

    public List<Pergunta> findPerguntaEntities(int maxResults, int firstResult) {
        return findPerguntaEntities(false, maxResults, firstResult);
    }

    private List<Pergunta> findPerguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Pergunta as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch(Exception ex) {
            throw ex;
        }
    }

    public Pergunta findPergunta(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pergunta.class, id);
        } catch(Exception ex) {
            throw ex;
        }
    }

    public int getPerguntaCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Pergunta as o");
            return ((Long) q.getSingleResult()).intValue();
        } catch(Exception ex) {
            throw ex;
        }
    }
    
}
