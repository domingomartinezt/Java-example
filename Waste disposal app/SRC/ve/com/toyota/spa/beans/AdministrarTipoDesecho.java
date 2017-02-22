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
import static ve.com.toyota.spa.util.Messages.createMessageFrame;

/**
 * Clase controladora de la vista "tiposDesechos.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "administrarTipoDesecho")
@ViewScoped
public class AdministrarTipoDesecho{

	private final static Logger log = Logger.getLogger(AdministrarTipoDesecho.class);

	/**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient AdministracionBean administracionEJB;

	private SpaTipoDesecho spaTipoDesecho = new SpaTipoDesecho();
	
	private SpaTipoDesecho tipoDesechoSeleccionado;

	private List<SpaTipoDesecho> spaTiposDesechos;

	/**
	 * Método de pre-carga de información.
	 */
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log	
		try {
			spaTiposDesechos = administracionEJB.getSpaTipoDesechoFindAll();
		} catch (Exception e) {
			log.error("Buscando todos los desechos. ",e);
		}
	}

	/**
	 * Método que guarda un nuevo tipo de desecho
	 */		
	public void guardarDesecho() {
		try {
			spaTipoDesecho.setActivo("A");
			spaTipoDesecho.setCreationDate(Calendar.getInstance().getTime());
			spaTipoDesecho.setLastUpdateDate(Calendar.getInstance().getTime());
			spaTipoDesecho.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaTipoDesecho.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			administracionEJB.persistSpaTipoDesecho(spaTipoDesecho);
			spaTipoDesecho = new SpaTipoDesecho();
			spaTiposDesechos = administracionEJB.getSpaTipoDesechoFindAll();
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Guardado.", "Nuevo tipo de desecho creado.");			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error.", "El tipo de desecho debe ser unico, verifique los datos.");
			log.error("Creando tipo de desecho. ",e);
		}		
	}
	
	/**
	 * Método que elimina un tipo de desecho seleccionado
	 */		
	public void eliminarDesecho() {
		try {
			if(tipoDesechoSeleccionado!=null){ 
				administracionEJB.removeSpaTipoDesecho(tipoDesechoSeleccionado);
				spaTiposDesechos = administracionEJB.getSpaTipoDesechoFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "El tipo de desecho seleccionado ha sido borrado.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un tipo de desecho para ser eliminado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error", "El tipo de desecho seleccionado no se pudo eliminar, verifique que no tenga tipos de tratamientos asociados.");
			log.error("Eliminando tipo de desecho. ",e);
		}

	}	
	
	/**
	 * Método que actualiza un tipo de desecho seleccionado
	 */		
	public void actualizarDesecho() {
		try {
			if(tipoDesechoSeleccionado!=null){ 
				tipoDesechoSeleccionado.setLastUpdateDate(Calendar.getInstance().getTime());
				tipoDesechoSeleccionado.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				administracionEJB.mergeSpaTipoDesecho(tipoDesechoSeleccionado);
				spaTiposDesechos = administracionEJB.getSpaTipoDesechoFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "El tipo de desecho seleccionado ha sido actualizado.");				
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un tipo de desecho para ser actualizado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error", "El tipo de desecho seleccionado no se pudo actualizar, verifique que los datos no esten repetidos.");
			log.error("Eliminando tipo de desecho. ",e);
		}

	}	
	
	/**
	 * Método que cancela la actualizacion de un tipo de desecho seleccionado
	 */		
	public void cancelarActualizacionDesecho() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada actualización.", "Se ha cancelado la actulización de datos para el tipo de desecho seleccionado.");
	}	

	public SpaTipoDesecho getSpaTipoDesecho() {
		return spaTipoDesecho;
	}

	public void setSpaTipoDesecho(SpaTipoDesecho spaTipoDesecho) {
		this.spaTipoDesecho = spaTipoDesecho;
	}

	/**
	 * @return the spaTiposDesechos
	 */
	public List<SpaTipoDesecho> getSpaTiposDesechos() {
		return spaTiposDesechos;
	}

	/**
	 * @param spaTiposDesechos the spaTiposDesechos to set
	 */
	public void setSpaTiposDesechos(List<SpaTipoDesecho> spaTiposDesechos) {
		this.spaTiposDesechos = spaTiposDesechos;
	}
	
	public SpaTipoDesecho getTipoDesechoSeleccionado() {
		return tipoDesechoSeleccionado;
	}


	public void setTipoDesechoSeleccionado(SpaTipoDesecho tipoDesechoSeleccionado) {
		this.tipoDesechoSeleccionado = tipoDesechoSeleccionado;
	}	

}
