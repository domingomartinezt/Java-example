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
import ve.com.toyota.spa.entities.SpaDesecho;
import ve.com.toyota.spa.entities.SpaClasificacion;

/**
 * Clase controladora de la vista "desechos.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "administrarDesecho")
@ViewScoped
public class AdministrarDesecho {

	private final static Logger log = Logger.getLogger(AdministrarDesecho.class);

	/**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient AdministracionBean administracionEJB;

	private SpaDesecho spaDesecho = new SpaDesecho();
	
	private SpaDesecho desechoSeleccionado = null;

	private List<SpaDesecho> spaDesechos;
	
	private List<SpaClasificacion> spaClasificaciones;

	/**
	 * Método de pre-carga de información.
	 */
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log	
		try {
			setSpaDesechos(administracionEJB.getSpaDesechoFindAll());
			setSpaClasificaciones(administracionEJB.getSpaClasificacionFindActiveAll());
		} catch (Exception e) {
			log.error("Buscando todos los desechos y clasificaciones. ",e);
		}
	}

	/**
	 * Método que guarda una nuevo desecho
	 */		
	public void guardarDesecho() {
		try {
			spaDesecho.setActivo("A");
			spaDesecho.setCreationDate(Calendar.getInstance().getTime());
			spaDesecho.setLastUpdateDate(Calendar.getInstance().getTime());
			spaDesecho.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaDesecho.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			administracionEJB.persistSpaDesecho(spaDesecho);
			spaDesecho = new SpaDesecho();
			spaDesechos = administracionEJB.getSpaDesechoFindAll();
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Guardado.", "Nueva desecho creado.");			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando desecho.", "El desecho debe ser unico, verifique los datos.");
			log.error("Creando desecho. ",e);
		}		
	}
	
	/**
	 * Método que elimina un desecho seleccionado
	 */		
	public void eliminarDesecho() {
		try {
			if(desechoSeleccionado!=null){ 
				administracionEJB.removeSpaDesecho(desechoSeleccionado);
				spaDesechos = administracionEJB.getSpaDesechoFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "El desecho seleccionado ha sido borrada.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un desecho para ser eliminado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Eliminando desecho.", "El desecho no se pudo eliminar, verifique que no tenga solicitudes de desincorporación asociadas.");
			log.error("Eliminando desecho. ",e);
		}

	}	
	
	/**
	 * Método que actualiza un desecho seleccionado
	 */		
	public void actualizarDesecho() {
		try {
			if(desechoSeleccionado!=null){ 
				desechoSeleccionado.setLastUpdateDate(Calendar.getInstance().getTime());
				desechoSeleccionado.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				administracionEJB.mergeSpaDesecho(desechoSeleccionado);
				spaDesechos = administracionEJB.getSpaDesechoFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "El desecho seleccionado ha sido actualizado.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un desecho para ser actualizado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Actualizando desecho.", "El desecho seleccionado no se pudo actualizar, verifique que los datos no esten repetidos.");
			log.error("Actualizando desecho. ",e);
		}

	}

	/**
	 * Método que cancela la actualizacion de un desecho seleccionado
	 */	
	public void cancelarActualizacionDesecho() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada actualización.", "Se ha cancelado la actulización de datos para el desecho seleccionado.");
	}

	public SpaDesecho getSpaDesecho() {
		return spaDesecho;
	}


	public void setSpaDesecho(SpaDesecho spaDesecho) {
		this.spaDesecho = spaDesecho;
	}


	public SpaDesecho getDesechoSeleccionado() {
		return desechoSeleccionado;
	}


	public void setDesechoSeleccionado(SpaDesecho desechoSeleccionado) {
		this.desechoSeleccionado = desechoSeleccionado;
	}


	public List<SpaDesecho> getSpaDesechos() {
		return spaDesechos;
	}


	public void setSpaDesechos(List<SpaDesecho> spaDesechos) {
		this.spaDesechos = spaDesechos;
	}


	public List<SpaClasificacion> getSpaClasificaciones() {
		return spaClasificaciones;
	}


	public void setSpaClasificaciones(List<SpaClasificacion> spaClasificaciones) {
		this.spaClasificaciones = spaClasificaciones;
	}	
}
