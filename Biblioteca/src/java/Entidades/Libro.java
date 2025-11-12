/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author anton
 */
@Entity
@Table(name = "libros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l")
    , @NamedQuery(name = "Libro.findByAutor", query = "SELECT l FROM Libro l JOIN l.autorLibroList al WHERE al.autorId = :elAutor ORDER BY l.ano")
    , @NamedQuery(name = "Libro.findById", query = "SELECT l FROM Libro l WHERE l.id = :id")
    , @NamedQuery(name = "Libro.findAllOrdenado", query = "SELECT l FROM Libro l ORDER BY l.nomLibro")
    , @NamedQuery(name = "Libro.findByPremio", query = "SELECT l FROM Libro l JOIN l.libroPremioList lp WHERE lp.premioId = :elPremio ORDER BY l.ano")
    , @NamedQuery(name = "Libro.findByNomLibro", query = "SELECT l FROM Libro l WHERE l.nomLibro = :nomLibro")
    , @NamedQuery(name = "Libro.findByAno", query = "SELECT l FROM Libro l WHERE l.ano = :ano")
    , @NamedQuery(name = "Libro.findByPortada", query = "SELECT l FROM Libro l WHERE l.portada = :portada")
    , @NamedQuery(name = "Libro.findByNomArchivo", query = "SELECT l FROM Libro l WHERE l.nomArchivo = :nomArchivo")
    , @NamedQuery(name = "Libro.findByPelicula", query = "SELECT l FROM Libro l WHERE l.pelicula = :pelicula")
    , @NamedQuery(name = "Libro.findByIndiceSerie", query = "SELECT l FROM Libro l WHERE l.indiceSerie = :indiceSerie")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "nom_libro")
    private String nomLibro;
    @Column(name = "ano")
    private Integer ano;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "portada")
    private String portada;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_archivo")
    private String nomArchivo;
    @Size(max = 30)
    @Column(name = "pelicula")
    private String pelicula;
    @Lob
    @Size(max = 16777215)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "indice_serie")
    private int indiceSerie;
    @JoinColumn(name = "serie_id", referencedColumnName = "id")
    @ManyToOne
    private Serie serieId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroId")
    private List<LibroPremio> libroPremioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroId")
    private List<AutorLibro> autorLibroList;

    public Libro() {
    }

    public Libro(Integer id) {
        this.id = id;
    }

    public Libro(Integer id, String portada, String nomArchivo, int indiceSerie) {
        this.id = id;
        this.portada = portada;
        this.nomArchivo = nomArchivo;
        this.indiceSerie = indiceSerie;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomLibro() {
        return nomLibro;
    }

    public void setNomLibro(String nomLibro) {
        this.nomLibro = nomLibro;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getNomArchivo() {
        return nomArchivo;
    }

    public void setNomArchivo(String nomArchivo) {
        this.nomArchivo = nomArchivo;
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getIndiceSerie() {
        return indiceSerie;
    }

    public void setIndiceSerie(int indiceSerie) {
        this.indiceSerie = indiceSerie;
    }

    public Serie getSerieId() {
        return serieId;
    }

    public void setSerieId(Serie serieId) {
        this.serieId = serieId;
    }

    @XmlTransient
    public List<LibroPremio> getLibroPremioList() {
        return libroPremioList;
    }

    public void setLibroPremioList(List<LibroPremio> libroPremioList) {
        this.libroPremioList = libroPremioList;
    }

    @XmlTransient
    public List<AutorLibro> getAutorLibroList() {
        return autorLibroList;
    }

    public void setAutorLibroList(List<AutorLibro> autorLibroList) {
        this.autorLibroList = autorLibroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Libro[ id=" + id + " ]";
    }
    
}
