/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositorios;

import Entidades.Autor;
import Entidades.AutorPremio;
import Entidades.Premio;
import Entidades.Serie;
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
public class AutorFacade extends AbstractFacade<Autor> {

    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AutorFacade() {
        super(Autor.class);
    }
    
    public List<Autor> autoresOrdenados(){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Autor.findAllOrdenado");
        return q.getResultList();
    }
    public List<AutorPremio> premioAutorOrdenado(Premio premio){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("AutorPremio.findByPremio").setParameter("elPremio", premio);
        return q.getResultList();
    }
    public List<Autor> AutoresSerie(Serie serie){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Autor.findBySerie").setParameter("serie", serie);
        return q.getResultList();
    }
}
