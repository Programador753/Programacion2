/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author anton
 */
@Entity
@Table(name = "autores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autor.findAll", query = "SELECT a FROM Autor a")
    , @NamedQuery(name = "Autor.findById", query = "SELECT a FROM Autor a WHERE a.id = :id")
    , @NamedQuery(name = "Autor.findAllOrdenado", query = "SELECT a FROM Autor a ORDER BY a.nomAutor")
    , @NamedQuery(name = "Autor.findBySerie", query = "SELECT DISTINCT a FROM Autor a JOIN a.autorLibroList al JOIN al.libroId l WHERE l.serieId = :serie")  
    , @NamedQuery(name = "Autor.findByNomAutor", query = "SELECT a FROM Autor a WHERE a.nomAutor = :nomAutor")
    , @NamedQuery(name = "Autor.findByApellido1", query = "SELECT a FROM Autor a WHERE a.apellido1 = :apellido1")
    , @NamedQuery(name = "Autor.findByApellido2", query = "SELECT a FROM Autor a WHERE a.apellido2 = :apellido2")
    , @NamedQuery(name = "Autor.findByFNac", query = "SELECT a FROM Autor a WHERE a.fNac = :fNac")
    , @NamedQuery(name = "Autor.findByFDef", query = "SELECT a FROM Autor a WHERE a.fDef = :fDef")
    , @NamedQuery(name = "Autor.findByFoto", query = "SELECT a FROM Autor a WHERE a.foto = :foto")
    , @NamedQuery(name = "Autor.findByLocalidad", query = "SELECT a FROM Autor a WHERE a.localidad = :localidad")})
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 20)
    @Column(name = "nom_autor")
    private String nomAutor;
    @Size(max = 25)
    @Column(name = "apellido1")
    private String apellido1;
    @Size(max = 25)
    @Column(name = "apellido2")
    private String apellido2;
    @Column(name = "f_nac")
    @Temporal(TemporalType.DATE)
    private Date fNac;
    @Column(name = "f_def")
    @Temporal(TemporalType.DATE)
    private Date fDef;
    @Size(max = 30)
    @Column(name = "foto")
    private String foto;
    @Size(max = 50)
    @Column(name = "localidad")
    private String localidad;
    @Lob
    @Size(max = 16777215)
    @Column(name = "biografia")
    private String biografia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorId")
    private List<AutorLibro> autorLibroList;
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne
    private Pais paisId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorId")
    private List<AutorPremio> autorPremioList;

    public Autor() {
    }

    public Autor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomAutor() {
        return nomAutor;
    }

    public void setNomAutor(String nomAutor) {
        this.nomAutor = nomAutor;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Date getFNac() {
        return fNac;
    }

    public void setFNac(Date fNac) {
        this.fNac = fNac;
    }

    public Date getFDef() {
        return fDef;
    }

    public void setFDef(Date fDef) {
        this.fDef = fDef;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    @XmlTransient
    public List<AutorLibro> getAutorLibroList() {
        return autorLibroList;
    }

    public void setAutorLibroList(List<AutorLibro> autorLibroList) {
        this.autorLibroList = autorLibroList;
    }

    public Pais getPaisId() {
        return paisId;
    }

    public void setPaisId(Pais paisId) {
        this.paisId = paisId;
    }

    @XmlTransient
    public List<AutorPremio> getAutorPremioList() {
        return autorPremioList;
    }

    public void setAutorPremioList(List<AutorPremio> autorPremioList) {
        this.autorPremioList = autorPremioList;
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
        if (!(object instanceof Autor)) {
            return false;
        }
        Autor other = (Autor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Autor[ id=" + id + " ]";
    }
    
}
