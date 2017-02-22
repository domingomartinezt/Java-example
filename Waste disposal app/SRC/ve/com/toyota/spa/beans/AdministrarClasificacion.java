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
import ve.com.toyota.spa.entities.SpaClasificacion;
import ve.com.toyota.spa.entities.SpaCategoria;

/**
 * Clase controladora de la vista "clasificaciones.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "administrarClasificacion")
@ViewScoped
public class AdministrarClasificacion {

	private final static Logger log = Logger.getLogger(AdministrarCategoria.class);

	/**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient AdministracionBean administracionEJB;

	private SpaClasificacion spaClasificacion = new SpaClasificacion();
	
	private SpaClasificacion clasificacionSeleccionada = null;

	private List<SpaClasificacion> spaClasificaciones;
	
	private List<SpaCategoria> spaCategorias;

	/**
	 * Método de pre-carga de información.
	 */
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log	
		try {
			setSpaClasificaciones(administracionEJB.getSpaClasificacionFindAll());
			setSpaCategorias(administracionEJB.getSpaCategoriaFindActiveAll());
		} catch (Exception e) {
			log.error("Buscando todos las clasificaciones y categorias. ",e);
		}
	}

	/**
	 * Método que guarda una nueva clasificacion
	 */	
	public void guardarClasificacion() {
		try {
			spaClasificacion.setActivo("A");
			spaClasificacion.setCreationDate(Calendar.getInstance().getTime());
			spaClasificacion.setLastUpdateDate(Calendar.getInstance().getTime());
			spaClasificacion.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaClasificacion.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			administracionEJB.persistSpaClasificacion(spaClasificacion);
			spaClasificacion = new SpaClasificacion();
			spaClasificaciones = administracionEJB.getSpaClasificacionFindAll();
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Guardado.", "Nueva clasificación creada.");			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando clasificación.", "La clasificación debe ser unica, verifique los datos.");
			log.error("Creando clasificación. ",e);
		}		
	}
	
	/**
	 * Método que elimina una clasificación seleccionada
	 */		
	public void eliminarClasificacion() {
		try {
			if(clasificacionSeleccionada!=null){ 
				administracionEJB.removeSpaClasificacion(clasificacionSeleccionada);
				spaClasificaciones = administracionEJB.getSpaClasificacionFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "La clasificación seleccionada ha sido borrada.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione una clasificación para ser eliminada.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Eliminando clasificación.", "La clasificación no se pudo eliminar, verifique que no tenga un desecho asociado.");
			log.error("Eliminando clasificación. ",e);
		}

	}	
	
	/**
	 * Método que actualiza una clasificacion seleccionada
	 */		
	public void actualizarClasificacion() {
		try {
			if(clasificacionSeleccionada!=null){ 
				clasificacionSeleccionada.setLastUpdateDate(Calendar.getInstance().getTime());
				clasificacionSeleccionada.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				administracionEJB.mergeSpaClasificacion(clasificacionSeleccionada);
				spaClasificaciones = administracionEJB.getSpaClasificacionFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "La clasificación seleccionada ha sido actualizada.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione una clasificación para ser actualizada.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Actualizando clasificación.", "La clasificación seleccionada no se pudo actualizar, verifique que los datos no esten repetidos.");
			log.error("Actualizando clasificación. ",e);
		}

	}	
	
	/**
	 * Método que cancela la actualizacion de una clasificacion seleccionada
	 */		
	public void cancelarActualizacionClasificacion() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada actualización.", "Se ha cancelado la actulización de datos para el tipo la clasificación seleccionada.");
	}


	public SpaClasificacion getSpaClasificacion() {
		return spaClasificacion;
	}


	public void setSpaClasificacion(SpaClasificacion spaClasificacion) {
		this.spaClasificacion = spaClasificacion;
	}


	public SpaClasificacion getClasificacionSeleccionada() {
		return clasificacionSeleccionada;
	}


	public void setClasificacionSeleccionada(SpaClasificacion clasificacionSeleccionada) {
		this.clasificacionSeleccionada = clasificacionSeleccionada;
	}


	public List<SpaClasificacion> getSpaClasificaciones() {
		return spaClasificaciones;
	}


	public void setSpaClasificaciones(List<SpaClasificacion> spaClasificaciones) {
		this.spaClasificaciones = spaClasificaciones;
	}


	public List<SpaCategoria> getSpaCategorias() {
		return spaCategorias;
	}


	public void setSpaCategorias(List<SpaCategoria> spaCategorias) {
		this.spaCategorias = spaCategorias;
	}

	
}
