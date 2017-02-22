package ve.com.toyota.spa.beans;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;  

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import ve.com.toyota.spa.ejb.AdministracionBean;
import ve.com.toyota.spa.entities.SpaTipoDesecho;
import ve.com.toyota.spa.entities.SpaTipoTratamiento;
import static ve.com.toyota.spa.util.Messages.createMessageFrame;

/**
 * Clase controladora de la vista "tiposTratamientos.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "administrarTipoTratamiento")
@ViewScoped
public class AdministrarTipoTratamiento{

	private final static Logger log = Logger.getLogger(AdministrarTipoTratamiento.class);

	/**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient AdministracionBean administracionEJB;

	private SpaTipoTratamiento spaTipoTratamiento = new SpaTipoTratamiento();
	
	private SpaTipoTratamiento tipoTratamientoSeleccionado = null;

	private List<SpaTipoTratamiento> spaTiposTratamiento;
	
	private List<SpaTipoDesecho> spaTiposDesechos;

	/**
	 * Método de pre-carga de información.
	 */
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log	
		try {
			setSpaTiposTratamiento(administracionEJB.getSpaTipoTratamientoFindAll());
			setSpaTiposDesechos(administracionEJB.getSpaTipoDesechoFindActiveAll());
		} catch (Exception e) {
			log.error("Buscando todos los desechos. ",e);
		}
	}

	/**
	 * Método que guarda un nuevo tipo de tratamiento
	 */	
	public void guardarTipoTratamiento() {
		try {
			spaTipoTratamiento.setActivo("A");
			spaTipoTratamiento.setCreationDate(Calendar.getInstance().getTime());
			spaTipoTratamiento.setLastUpdateDate(Calendar.getInstance().getTime());
			spaTipoTratamiento.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaTipoTratamiento.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			administracionEJB.persistSpaTipoTratamiento(spaTipoTratamiento);
			spaTipoTratamiento = new SpaTipoTratamiento();
			spaTiposTratamiento = administracionEJB.getSpaTipoTratamientoFindAll();
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Guardado.", "Nuevo tipo tratamiento creado.");			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando tipo de tratamiento.", "El tipo de tratamiento debe ser unico, verifique los datos.");
			log.error("Creando tipo de tratamiento. ",e);
		}		
	}
	
	/**
	 * Método que elimina un tipo de tratamiento seleccionado
	 */			
	public void eliminarTipoTratamiento() {
		try {
			if(tipoTratamientoSeleccionado!=null){ 
				administracionEJB.removeSpaTipoTratamiento(tipoTratamientoSeleccionado);
				tipoTratamientoSeleccionado=null;
				spaTiposTratamiento = administracionEJB.getSpaTipoTratamientoFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "El tipo de tratamiento seleccionado ha sido borrado.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un tipo de tratamiento para ser eliminado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Eliminando tipo de tratamiento.", "El tipo de tratamiento seleccionado no se pudo eliminar, verifique que no tenga categorias asociadas.");
			log.error("Eliminando tipo de tratamiento. ",e);
		}

	}	
	
	/**
	 * Método que actualiza un tipo de tratamiento seleccionado
	 */		
	public void actualizarTipoTratamiento() {
		try {
			if(tipoTratamientoSeleccionado!=null){ 
				tipoTratamientoSeleccionado.setLastUpdateDate(Calendar.getInstance().getTime());
				tipoTratamientoSeleccionado.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				administracionEJB.mergeSpaTipoTratamiento(tipoTratamientoSeleccionado);
				tipoTratamientoSeleccionado=null;
				spaTiposTratamiento = administracionEJB.getSpaTipoTratamientoFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "El tipo de tratamiento seleccionado ha sido actualizado.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un tipo de tratamiento para ser actualizado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Actualizando tipo de desecho.", "El tipo de tratamiento seleccionado no se pudo actualizar, verifique que los datos no esten repetidos.");
			log.error("Actualizando tipo de desecho. ",e);
		}

	}	
	
	/**
	 * Método que cancela la actualizacion de un tipo de tratamiento seleccionado
	 */		
	public void cancelarActualizacionTipoTratamiento() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada actualización.", "Se ha cancelado la actulización de datos para el tipo de tratamiento seleccionado.");
	}


	public SpaTipoTratamiento getSpaTipoTratamiento() {
		return spaTipoTratamiento;
	}


	public void setSpaTipoTratamiento(SpaTipoTratamiento spaTipoTratamiento) {
		this.spaTipoTratamiento = spaTipoTratamiento;
	}


	public SpaTipoTratamiento getTipoTratamientoSeleccionado() {
		return tipoTratamientoSeleccionado;
	}


	public void setTipoTratamientoSeleccionado(SpaTipoTratamiento tipoTratamientoSeleccionado) {
		this.tipoTratamientoSeleccionado = tipoTratamientoSeleccionado;
	}


	public List<SpaTipoTratamiento> getSpaTiposTratamiento() {
		return spaTiposTratamiento;
	}


	public void setSpaTiposTratamiento(List<SpaTipoTratamiento> spaTiposTratamiento) {
		this.spaTiposTratamiento = spaTiposTratamiento;
	}


	public List<SpaTipoDesecho> getSpaTiposDesechos() {
		return spaTiposDesechos;
	}


	public void setSpaTiposDesechos(List<SpaTipoDesecho> spaTiposDesechos) {
		this.spaTiposDesechos = spaTiposDesechos;
	}

}
