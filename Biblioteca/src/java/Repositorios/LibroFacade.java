/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositorios;

import Entidades.Autor;
import Entidades.Libro;
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
public class LibroFacade extends AbstractFacade<Libro> {

    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LibroFacade() {
        super(Libro.class);
    }
        public List<Libro> libroOrdenado(){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Libro.findAllOrdenado");
        return q.getResultList();
    }
        public List<Libro> libroAutorOrdenado(Autor autor){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Libro.findByAutor").setParameter("elAutor", autor);
        return q.getResultList();
    }
        public List<Libro> libroPremioOrdenado(Premio premio){
        em = this.getEntityManager();
        Query q;
        q = em.createNamedQuery("Libro.findByPremio").setParameter("elPremio", premio);
        return q.getResultList();
    }
        
        
}
