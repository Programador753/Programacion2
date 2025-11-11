/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositorios;

import Entidades.Pais;
import Entidades.Premio;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author anton
 */
@Stateless
public class PremioFacade extends AbstractFacade<Premio> {

    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PremioFacade() {
        super(Premio.class);
    }
    
    public List<Premio> premiosLibro(){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Premio.findByTipoLibro");
        return q.getResultList();
    }
    public List<Premio> premiosAutor(){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Premio.findByTipoAutor");
        return q.getResultList();
    }
    public List<Premio> premiosPaisLibro(Pais pais){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Premio.findByPaisTipoLibro").setParameter("elPais", pais);
        return q.getResultList();
    }
}
