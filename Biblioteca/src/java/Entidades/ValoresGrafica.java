package Entidades;

public class ValoresGrafica {
    private String nomPais;
    private Long numAutores;
    
    public ValoresGrafica(){
        
    }
    public ValoresGrafica(String nomPais, Long numAutores){
        this.nomPais = nomPais;
        this.numAutores = numAutores;
    }

    public String getNomPais() {
        return nomPais;
    }

    public void setNomPais(String nomPais) {
        this.nomPais = nomPais;
    }

    public Long getNumAutores() {
        return numAutores;
    }

    public void setNumAutores(Long numAutores) {
        this.numAutores = numAutores;
    }
    
}
