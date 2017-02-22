package ve.com.toyota.spa.beans;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import ve.com.toyota.spa.ejb.AdministracionBean;
import ve.com.toyota.spa.entities.SpaCategoria;
import ve.com.toyota.spa.entities.SpaTipoTratamiento;

/**
 * Clase controladora de la vista "categorias.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "administrarCategoria")
@ViewScoped
public class AdministrarCategoria {

	private final static Logger log = Logger.getLogger(AdministrarCategoria.class);

	/**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient AdministracionBean administracionEJB;

	private SpaCategoria spaCategoria = new SpaCategoria();
	
	private SpaCategoria categoriaSeleccionada = null;

	private List<SpaCategoria> spaCategorias;
	
	private List<SpaTipoTratamiento> spaTiposTratamientos;

	/**
	 * Método de pre-carga de información.
	 */
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log	
		try {
			setSpaCategorias(administracionEJB.getSpaCategoriaFindAll());
			setSpaTiposTratamientos(administracionEJB.getSpaTipoTratamientoFindActiveAll());
		} catch (Exception e) {
			log.error("Buscando todos las categorias y tipos de tratamiento. ",e);
		}
	}

	/**
	 * Método que guarda una nueva categoria
	 */
	public void guardarCategoria() {
		try {
			spaCategoria.setActivo("A");
			spaCategoria.setCreationDate(Calendar.getInstance().getTime());
			spaCategoria.setLastUpdateDate(Calendar.getInstance().getTime());
			spaCategoria.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaCategoria.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			administracionEJB.persistSpaCategoria(spaCategoria);
			spaCategoria = new SpaCategoria();
			spaCategorias = administracionEJB.getSpaCategoriaFindAll();
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Guardado.", "Nueva categoria creada.");			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando categoria.", "La categoria debe ser unica, verifique los datos.");
			log.error("Creando categoria. ",e);
		}		
	}
	
	/**
	 * Método que elimina una categoria seleccionada
	 */	
	public void eliminarCategoria() {
		try {
			if(categoriaSeleccionada!=null){ 
				administracionEJB.removeSpaCategoria(categoriaSeleccionada);
				spaCategorias = administracionEJB.getSpaCategoriaFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "La categoria seleccionada ha sido borrada.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione una categoria para ser eliminada.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Eliminando categoria.", "La categoria no se pudo eliminar, verifique que no tenga una clasificación asociada.");
			log.error("Eliminando categoria. ",e);
		}

	}	
	
	/**
	 * Método que actualiza una categoria seleccionada
	 */		
	public void actualizarCategoria() {
		try {
			if(categoriaSeleccionada!=null){ 
				categoriaSeleccionada.setLastUpdateDate(Calendar.getInstance().getTime());
				categoriaSeleccionada.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				administracionEJB.mergeSpaCategoria(categoriaSeleccionada);
				spaCategorias = administracionEJB.getSpaCategoriaFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "La categoria seleccionada ha sido actualizada.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione una categoria para ser actualizada.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Actualizando categoria.", "La categoria seleccionada no se pudo actualizar, verifique que los datos no esten repetidos.");
			log.error("Actualizando categoria. ",e);
		}

	}	
	
	/**
	 * Método que cancela la actualizacion de una categoria seleccionada
	 */		
	public void cancelarActualizacionCategoria() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada actualización.", "Se ha cancelado la actulización de datos para el tipo la categoria seleccionada.");
	}


	public SpaCategoria getSpaCategoria() {
		return spaCategoria;
	}


	public void setSpaCategoria(SpaCategoria spaCategoria) {
		this.spaCategoria = spaCategoria;
	}


	public SpaCategoria getCategoriaSeleccionada() {
		return categoriaSeleccionada;
	}


	public void setCategoriaSeleccionada(SpaCategoria categoriaSeleccionada) {
		this.categoriaSeleccionada = categoriaSeleccionada;
	}


	public List<SpaCategoria> getSpaCategorias() {
		return spaCategorias;
	}


	public void setSpaCategorias(List<SpaCategoria> spaCategorias) {
		this.spaCategorias = spaCategorias;
	}


	public List<SpaTipoTratamiento> getSpaTiposTratamientos() {
		return spaTiposTratamientos;
	}


	public void setSpaTiposTratamientos(List<SpaTipoTratamiento> spaTiposTratamientos) {
		this.spaTiposTratamientos = spaTiposTratamientos;
	}


	

}