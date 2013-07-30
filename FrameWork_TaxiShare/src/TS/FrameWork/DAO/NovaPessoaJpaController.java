/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.DAO;

import TS.FrameWork.DAO.exceptions.NonexistentEntityException;
import TS.FrameWork.TO.NovaPessoa;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Bruno
 */
public class NovaPessoaJpaController implements Serializable {

    public NovaPessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NovaPessoa novaPessoa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(novaPessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NovaPessoa novaPessoa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            novaPessoa = em.merge(novaPessoa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = novaPessoa.getId();
                if (findNovaPessoa(id) == null) {
                    throw new NonexistentEntityException("The novaPessoa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            NovaPessoa novaPessoa;
            try {
                novaPessoa = em.getReference(NovaPessoa.class, id);
                novaPessoa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The novaPessoa with id " + id + " no longer exists.", enfe);
            }
            em.remove(novaPessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<NovaPessoa> findNovaPessoaEntities() {
        return findNovaPessoaEntities(true, -1, -1);
    }

    public List<NovaPessoa> findNovaPessoaEntities(int maxResults, int firstResult) {
        return findNovaPessoaEntities(false, maxResults, firstResult);
    }

    private List<NovaPessoa> findNovaPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from NovaPessoa as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public NovaPessoa findNovaPessoa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NovaPessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getNovaPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from NovaPessoa as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
