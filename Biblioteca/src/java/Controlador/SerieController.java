package Controlador;

import Entidades.Serie;
import Controlador.util.JsfUtil;
import Controlador.util.PaginationHelper;
import Entidades.Autor;
import Entidades.Libro;
import Repositorios.SerieFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("serieController")
@SessionScoped
public class SerieController implements Serializable {

    private Serie current;
    private DataModel items = null;
    @EJB
    private Repositorios.SerieFacade ejbFacade;
    @EJB
    private Repositorios.LibroFacade libroFacade;
    @EJB
    private Repositorios.AutorFacade autorFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Autor autorSeleccionado;
    private List<Libro> librosSeleccionados;
    private List<Libro> librosDelAutor;

    public List<Libro> getLibrosDelAutor() {
        return librosDelAutor;
    }

    public void setLibrosDelAutor(List<Libro> librosDelAutor) {
        this.librosDelAutor = librosDelAutor;
    }

    public List<Libro> getLibrosSeleccionados() {
        return librosSeleccionados;
    }

    public void setLibrosSeleccionados(List<Libro> librosSeleccionados) {
        this.librosSeleccionados = librosSeleccionados;
    }

    public Autor getAutorSeleccionado() {
        return autorSeleccionado;
    }

    public void setAutorSeleccionado(Autor autorSeleccionado) {
        this.autorSeleccionado = autorSeleccionado;
    }

    public SerieController() {
    }

    public Serie getSelected() {
        if (current == null) {
            current = new Serie();
            selectedItemIndex = -1;
        }
        return current;
    }

    private SerieFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Serie) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Serie();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            ejbFacade.crearSerieConLibros(current, librosSeleccionados);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SerieCreated"));
            current = new Serie();
            librosSeleccionados = null;
            autorSeleccionado = null;
            return "/Vista/serie/List?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Serie) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        try {
            List<Autor> autoresParaSerie = autorFacade.AutoresSerie(current);
            if (autoresParaSerie != null && !autoresParaSerie.isEmpty()) {
                this.autorSeleccionado = autoresParaSerie.get(0);
                cargarLibrosAutor();
            } else {
                this.autorSeleccionado = null;
                this.librosDelAutor = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error al pre-cargar el autor: " + e.getMessage());
            JsfUtil.addErrorMessage(e, "Error al cargar datos del autor");
            this.autorSeleccionado = null;
            this.librosDelAutor = new ArrayList<>();
        }

        // 7. Navega a la página de Edición
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SerieUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Serie) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SerieDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return getSelectItems(ejbFacade.findAll(), true);
    }

    public static SelectItem[] getSelectItems(List<Serie> entities, boolean selectOne) {
        SelectItem[] items = new SelectItem[entities.size()];
        int i = 0;
        for (Serie serie : entities) {
            items[i++] = new SelectItem(serie, serie.getDenominacion());
        }
        return items;
    }

    public Serie getSerie(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Serie.class)
    public static class SerieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SerieController controller = (SerieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "serieController");
            return controller.getSerie(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Serie) {
                Serie o = (Serie) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Serie.class.getName());
            }
        }
    }

    public void cargarLibrosAutor() {
        if (autorSeleccionado != null) {
            this.librosDelAutor = libroFacade.libroAutorOrdenado(autorSeleccionado);
        } else {
            this.librosDelAutor = new ArrayList<>();
        }
    }

}
