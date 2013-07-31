/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.DAO;

import TS.FrameWork.DAO.exceptions.NonexistentEntityException;
import TS.FrameWork.TO.Login;
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
public class LoginJpaController implements Serializable {

    public LoginJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Login login) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(login);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Login login) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            login = em.merge(login);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = login.getId();
                if (findLogin(id) == null) {
                    throw new NonexistentEntityException("The login with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Login login;
            try {
                login = em.getReference(Login.class, id);
                login.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The login with id " + id + " no longer exists.", enfe);
            }
            em.remove(login);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Login> findLoginEntities() {
        return findLoginEntities(true, -1, -1);
    }

    public List<Login> findLoginEntities(int maxResults, int firstResult) {
        return findLoginEntities(false, maxResults, firstResult);
    }

    private List<Login> findLoginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Login as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Login findLogin(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Login.class, id);
        } finally {
            em.close();
        }
    }

    public Login findLogin(String login) {
        EntityManager em = getEntityManager();
        System.out.println("ENtrou no find login");
        try {
            String query = "select l from Login l where l.login like :login";
            Query q = em.createQuery(query).setParameter("login", login);
            List results = q.getResultList();
            Login foundEntity = null;
            if (!results.isEmpty()) {
                // ignora multiplos resultados
                foundEntity = (Login) results.get(0);
            }

            return foundEntity;
        } finally {
            em.close();
        }
    }

    public int getLoginCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Login as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
