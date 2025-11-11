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
@Table(name = "premios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Premio.findAll", query = "SELECT p FROM Premio p")
    , @NamedQuery(name = "Premio.findById", query = "SELECT p FROM Premio p WHERE p.id = :id")
    , @NamedQuery(name = "Premio.findByTipoLibro", query = "SELECT p FROM Premio p WHERE p.tipo = 'L' ORDER BY p.nomPremio")
    , @NamedQuery(name = "Premio.findByTipoAutor", query = "SELECT p FROM Premio p WHERE p.tipo = 'A' ORDER BY p.nomPremio")
    , @NamedQuery(name = "Premio.findByPaisTipoLibro", query = "SELECT p FROM Premio p WHERE p.tipo = 'L' AND p.paisId = :elPais")
    , @NamedQuery(name = "Premio.findByTipo", query = "SELECT p FROM Premio p WHERE p.tipo = :tipo")})
public class Premio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "nom_premio")
    private String nomPremio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    private Character tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "premioId")
    private List<LibroPremio> libroPremioList;
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pais paisId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "premioId")
    private List<AutorPremio> autorPremioList;

    public Premio() {
    }

    public Premio(Integer id) {
        this.id = id;
    }

    public Premio(Integer id, String nomPremio, Character tipo) {
        this.id = id;
        this.nomPremio = nomPremio;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomPremio() {
        return nomPremio;
    }

    public void setNomPremio(String nomPremio) {
        this.nomPremio = nomPremio;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<LibroPremio> getLibroPremioList() {
        return libroPremioList;
    }

    public void setLibroPremioList(List<LibroPremio> libroPremioList) {
        this.libroPremioList = libroPremioList;
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
        if (!(object instanceof Premio)) {
            return false;
        }
        Premio other = (Premio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Premio[ id=" + id + " ]";
    }
    
}
