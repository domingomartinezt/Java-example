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
import ve.com.toyota.spa.entities.SpaUnidadMedida;

/**
 * Clase controladora de la vista "unidadesMedida.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "administrarUnidadMedida")
@ViewScoped
public class AdministrarUnidadMedida {

	private final static Logger log = Logger.getLogger(AdministrarUnidadMedida.class);

	/**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient AdministracionBean administracionEJB;

	private SpaUnidadMedida spaUnidadMedida = new SpaUnidadMedida();
	
	private SpaUnidadMedida unidadMedidaSeleccionada = null;

	private List<SpaUnidadMedida> spaUnidadesMedida;
	
	/**
	 * Método de pre-carga de información.
	 */
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log	
		try {
			setSpaUnidadesMedida(administracionEJB.getSpaUnidadMedidaFindAll());
		} catch (Exception e) {
			log.error("Buscando todas las unidades de medida. ",e);
		}
	}

	/**
	 * Método que guarda una nueva unidad de medida
	 */		
	public void guardarUnidadMedida() {
		try {
			spaUnidadMedida.setActivo("A");
			spaUnidadMedida.setCreationDate(Calendar.getInstance().getTime());
			spaUnidadMedida.setLastUpdateDate(Calendar.getInstance().getTime());
			spaUnidadMedida.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaUnidadMedida.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			administracionEJB.persistSpaUnidadMedida(spaUnidadMedida);
			spaUnidadMedida = new SpaUnidadMedida();
			spaUnidadesMedida = administracionEJB.getSpaUnidadMedidaFindAll();
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Guardado.", "Nueva unidad de medida creada.");			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando unidad de medida.", "La unidad de medida debe ser unica, verifique los datos.");
			log.error("Creando unidad de medida.",e);
		}		
	}
	
	/**
	 * Método que elimina una unidade de medida seleccionada
	 */		
	public void eliminarUnidadMedida() {
		try {
			if(unidadMedidaSeleccionada!=null){ 
				administracionEJB.removeSpaUnidadMedida(unidadMedidaSeleccionada);
				spaUnidadesMedida = administracionEJB.getSpaUnidadMedidaFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "La unidad de medida seleccionada ha sido borrada.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione una unidad de medida para ser eliminada.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Eliminando unidad de medida.", "La unidad de medida no se pudo eliminar, verifique que no tenga solicitudes de desincorporación asociadas.");
			log.error("Eliminando unidad de medida.",e);
		}

	}	
	
	/**
	 * Método que actualiza una unidade de medida seleccioanda
	 */		
	public void actualizarUnidadMedida() {
		try {
			if(unidadMedidaSeleccionada!=null){ 
				unidadMedidaSeleccionada.setLastUpdateDate(Calendar.getInstance().getTime());
				unidadMedidaSeleccionada.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				administracionEJB.mergeSpaUnidadMedida(unidadMedidaSeleccionada);
				spaUnidadesMedida = administracionEJB.getSpaUnidadMedidaFindAll();
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "La unidad de medida seleccionada ha sido actualizada.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione una unidad de medida para ser actualizada.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Actualizando unidad de medida.", "La unidad de medida no se pudo actualizar, verifique que los datos no esten repetidos.");
			log.error("Actualizando unidad de medida.",e);
		}

	}

	/**
	 * Método que cancela la actualizacion de una unidade de medida seleccionada
	 */	
	public void cancelarActualizacionUnidadMedida() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada actualización.", "Se ha cancelado la actulización de datos para la unidad de medida seleccionada.");
	}


	public SpaUnidadMedida getSpaUnidadMedida() {
		return spaUnidadMedida;
	}


	public void setSpaUnidadMedida(SpaUnidadMedida spaUnidadMedida) {
		this.spaUnidadMedida = spaUnidadMedida;
	}


	public SpaUnidadMedida getUnidadMedidaSeleccionada() {
		return unidadMedidaSeleccionada;
	}


	public void setUnidadMedidaSeleccionada(SpaUnidadMedida unidadMedidaSeleccionada) {
		this.unidadMedidaSeleccionada = unidadMedidaSeleccionada;
	}


	public List<SpaUnidadMedida> getSpaUnidadesMedida() {
		return spaUnidadesMedida;
	}


	public void setSpaUnidadesMedida(List<SpaUnidadMedida> spaUnidadesMedida) {
		this.spaUnidadesMedida = spaUnidadesMedida;
	}

}
