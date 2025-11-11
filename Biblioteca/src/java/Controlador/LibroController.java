package Controlador;

import Entidades.Libro;
import Controlador.util.JsfUtil;
import Controlador.util.PaginationHelper;
import Entidades.Autor;
import Entidades.AutorLibro;
import Entidades.LibroPremio;
import Entidades.Premio;
import Repositorios.LibroFacade;

import java.io.Serializable;
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

@Named("libroController")
@SessionScoped
public class LibroController implements Serializable {

    private Libro current;
    private DataModel items = null;
    @EJB
    private Repositorios.LibroFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Autor autor;
    private List<Libro> lista; // Lista de libros
    private DataModel<Libro> dataModelLibros;

    public Libro getCurrent() {
        return current;
    }

    public void setCurrent(Libro current) {
        this.current = current;
    }

    public List<Libro> getLista() {
        return lista;
    }

    public void setLista(List<Libro> lista) {
        this.lista = lista;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public LibroController() {
    }

    public Libro getSelected() {
        if (current == null) {
            current = new Libro();
            selectedItemIndex = -1;
        }
        return current;
    }

    private LibroFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(getItemsAvailableSelectOne().length) {

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
        current = (Libro) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Libro();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            for (AutorLibro al : current.getAutorLibroList()) {
                al.setLibroId(current);
            }
            for (LibroPremio lp : current.getLibroPremioList()) {
                lp.setLibroId(current);
            }
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LibroCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Libro) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public DataModel<Libro> getDataModelLibros() {
        if (dataModelLibros == null && lista != null) {
            dataModelLibros = new ListDataModel<>(lista);
        }
        return dataModelLibros;
    }

    public String prepareEdit2() {
        Libro seleccionado = getDataModelLibros().getRowData();
        current = seleccionado;
        selectedItemIndex = lista.indexOf(seleccionado); // O usa paginación si aplica
        return "Edit";
    }

    public String prepareEdit3() {
        selectedItemIndex = lista.indexOf(current); // Usa paginación si aplica
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LibroUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Libro) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LibroDeleted"));
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
        return getSelectItems(ejbFacade.libroOrdenado(), true);
    }

    public Libro getLibro(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Libro.class, value = "libroConverter")
    public static class LibroControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LibroController controller = (LibroController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "libroController");
            return controller.getLibro(getKey(value));
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
            if (object instanceof Libro) {
                Libro o = (Libro) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Libro.class.getName());
            }
        }

    }

    public static SelectItem[] getSelectItems(List<Libro> entities, boolean selectOne) {
        SelectItem[] items = new SelectItem[entities.size()];
        int i = 0;
        for (Libro libro : entities) {
            items[i++] = new SelectItem(libro, libro.getNomLibro());
        }
        return items;
    }

    public List<Libro> cargarLibrosAutor(Autor autor) {
        return ejbFacade.libroAutorOrdenado(autor);
    }

    public boolean tienePeli(Libro libro) {
        return (libro.getPelicula() != "");
    }

    public void loadLibrosAutor() {
        this.setLista(ejbFacade.libroAutorOrdenado(autor));
        this.dataModelLibros = new ListDataModel<>(lista); // Actualiza el DataModel
    }
}
