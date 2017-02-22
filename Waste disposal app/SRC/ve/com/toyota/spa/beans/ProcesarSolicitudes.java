package ve.com.toyota.spa.beans;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import oracle.sql.BFILE;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ve.com.toyota.common.ejb.pt.TrabajadorBean;
import ve.com.toyota.common.entities.Mail;
import ve.com.toyota.common.entities.pt.TrabajadorTDV;
import ve.com.toyota.common.mail.Email;
import ve.com.toyota.common.util.GeneralUtilities;
import ve.com.toyota.spa.ejb.AdministracionBean;
import ve.com.toyota.spa.ejb.DocumentoBean;
import ve.com.toyota.spa.entities.SpaCategoria;
import ve.com.toyota.spa.entities.SpaClasificacion;
import ve.com.toyota.spa.entities.SpaDesecho;
import ve.com.toyota.spa.entities.SpaDesechoSolicitud;
import ve.com.toyota.spa.entities.SpaEstatus;
import ve.com.toyota.spa.entities.SpaHistoricoDocumento;
import ve.com.toyota.spa.entities.SpaImagen;
import ve.com.toyota.spa.entities.SpaItemDesincorporar;
import ve.com.toyota.spa.entities.SpaTipoDesecho;
import ve.com.toyota.spa.entities.SpaTipoTratamiento;
import ve.com.toyota.spa.entities.SpaUnidadMedida;
import ve.com.toyota.spa.util.DuplicateStream;

/**
 * Clase controladora de la vista "procesarSolicitudes.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * Maneja el proceso de los desechos de las solicitudes 
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "procesarSolicitudes")
@ViewScoped
public class ProcesarSolicitudes {

	private final static Logger log = Logger.getLogger(AdministrarNuevaSolicitud.class);
	
	private List<SpaHistoricoDocumento> spaHistoricoDocumento;
	private SpaHistoricoDocumento spaHistoricoDocumentoSeleccionado;
	private SpaHistoricoDocumento spaHistoricoDocumentoAprobado = new SpaHistoricoDocumento();
	private SpaHistoricoDocumento spaHistoricoDocumentoNuevo = new SpaHistoricoDocumento();
	private SpaItemDesincorporar spaItemDesincorporarSeleccionado;
	private List<SpaEstatus> spaListaEstatus;
	private String estatusSeleccionado = null;
	private TrabajadorTDV trabajadorTDV;
	private TrabajadorTDV trabajadorSolicitante;	
	private String nombreAprobador = "";
	private DuplicateStream duplicateStream;
    private StreamedContent file;
    private List<SpaTipoDesecho> spaTiposDesechos;
    private SpaTipoDesecho spaTipoDesecho;
    private List<SpaTipoTratamiento> spaTiposTratamientos;
    private SpaTipoTratamiento spaTipoTratamiento;
    private List<SpaCategoria> spaCategorias;
    private SpaCategoria spaCategoria;
    private List<SpaClasificacion> spaClasificaciones;
    private SpaClasificacion spaClasificacion;
    private List<SpaDesecho> spaDesechos;
    private SpaDesecho spaDesecho;
    private SpaDesechoSolicitud spaDesechoSolicitud = new SpaDesechoSolicitud();
    private List<SpaDesechoSolicitud> spaDesechoSolicitudes;
    private SpaDesechoSolicitud spaDesechoSolicitudSeleccionado;

	/**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient DocumentoBean documentoEJB;
	
	@EJB
	private transient AdministracionBean administracionEJB;
	
	/**
	 * Objeto que gestiona los datos del trabajador.
	 */		
	@EJB(lookup = "java:global/tdvCommon/TrabajadorBean")
	private TrabajadorBean trabajadorEJB;		
	
	/**
	 * Objeto que gestiona el envío de correos electrónicos.
	 */
	@EJB(lookup = "java:global/tdvCommon/Email")
	private Email email;
	
	/**
	 * Método de pre-carga de información.
	 */	
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log
		setTrabajadorTDV(trabajadorEJB.obtenerTrabajador(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase()));
		setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoAprovedAndExecuted(null));
		setSpaListaEstatus(documentoEJB.getAllSpaEstatusActives());
		setSpaTiposDesechos(administracionEJB.getSpaTipoDesechoFindActiveAll());
	}
	
	/**
	 * Método que busca los datos del aprobador del historico de la solicitud seleccionada.
	 */	
	public void buscarDatosAprobador() throws Exception {
		spaHistoricoDocumentoNuevo = new SpaHistoricoDocumento();
		setSpaHistoricoDocumentoAprobado(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdSolicitud("APR",spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud()));
		setTrabajadorSolicitante(trabajadorEJB.obtenerTrabajadorByPersonId(new BigDecimal(spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getPersonIdElabora())));
		if (spaHistoricoDocumentoAprobado.getCreatedBy()!=null){
			nombreAprobador = trabajadorEJB.obtenerTrabajador(spaHistoricoDocumentoAprobado.getCreatedBy()).getNombreCompleto();
		}else{
			nombreAprobador = "";
		}

	}	
	
	/**
	 * Método que filtra las solicitudes por estatus
	 */		
	public void filtrarSolicitudes() {
		setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoAprovedAndExecuted(estatusSeleccionado));
	}	
	
	/**
	 * Método que busca el archivo abjunto asociado de una solicitud
	 */	    
    public StreamedContent getFile() {  
        BFILE bfile;
		try {
			SpaImagen spaImagen = spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getSpaAprobacionJunta().getSpaImagen();
			bfile = documentoEJB.recuperateImage(spaImagen.getIdImagen());
	        bfile.openFile();     
			duplicateStream = new DuplicateStream(bfile.getBinaryStream());
			file = new DefaultStreamedContent(duplicateStream.cloneStream(), spaImagen.getContentType(), bfile.getName());   
			return file;
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Bajando archivo.","Error. No se encontro el archivo en el servidor. Contacte al help desk.");
			log.error("Error. Bajando archivo.",e);
			return null;
		} 
    }   
    
	/**
	 * Método que actualiza item a desincorporar para definir las cantidades recibidas por el departamento de ambiente
	 */	      
	public void actualizarItemDesincorporar() {
		try {
			if(spaItemDesincorporarSeleccionado!=null){
				if((spaItemDesincorporarSeleccionado.getCantidadDesincorporar() >= spaItemDesincorporarSeleccionado.getCantidadRecibida()) && (spaItemDesincorporarSeleccionado.getCantidadRecibida() > 0)){
					spaItemDesincorporarSeleccionado.setLastUpdateDate(Calendar.getInstance().getTime());
					spaItemDesincorporarSeleccionado.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
					documentoEJB.mergeSpaItemDesincorporar(spaItemDesincorporarSeleccionado);
					createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "La cantidad de items a desincorporar ha sido guardada.");
				}else{
					createMessageFrame(FacesMessage.SEVERITY_WARN,"Cantidad incorrecta.", "Indique una cantidad correcta recibida de los items a desincorporar.");
				}
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un item a desincorporar a recibir sus cantidades.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Actualizando item desincorporar.", "El item a desincorporar seleccionado no se pudo acutalizar, verifique que los datos.");
			log.error("Actualizando item desincorporar. ",e);
		}

	}	
	
	/**
	 * Método que cancela la actualizacion del item a actualizar seleccionado
	 */		
	public void cancelarActualizacionItemDesincorporar() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada recepción del item", "Se ha cancelado la recepción del item a desincorporar seleccionado.");
	}	
	
	/**
	 * Método que cambia el estatus de la solicitud a procesada y envia una notificacion al solicitante
	 */		
	public void cambiarEstatusEnProceso() {
		try {
			SpaEstatus spaEstatus = documentoEJB.getSpaEstatusByCodigo("PRO");
			spaHistoricoDocumentoNuevo.setSpaEstatus(spaEstatus);
			spaHistoricoDocumentoNuevo.setSpaSolicitude(spaHistoricoDocumentoSeleccionado.getSpaSolicitude());
			spaHistoricoDocumentoNuevo.setCreationDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setLastUpdateDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaHistoricoDocumentoNuevo.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			documentoEJB.persistSpaHistoricoDocumento(spaHistoricoDocumentoNuevo);
			spaHistoricoDocumentoNuevo = new SpaHistoricoDocumento();
			setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoAprovedAndExecuted(null));
			setSpaHistoricoDocumentoSeleccionado(documentoEJB.getLastSpaHistoricoDocumento(spaHistoricoDocumentoSeleccionado.getSpaSolicitude()));
			createMessageFrame(FacesMessage.SEVERITY_INFO, "En Proceso.", "La solicitud con númmero "+spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud()+" se colocó en proceso.");
			
			// Enviar notificacion
			InputStream emailFile = this.getClass().getClassLoader().getResourceAsStream("notificacionCambioEstatus.html");
			String body = GeneralUtilities.convertStreamToString(emailFile);
			Mail mail = new Mail();
			mail.setFrom("spa@toyota.com.ve");
			mail.setTo(trabajadorSolicitante.getEmail());
			mail.setCc("");
			mail.setBcc("");
			mail.setSuject("Sistema de Procesos Ambientales, Cambio de estatus de la solicitud.");
			body = String.format(body, spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud(), spaEstatus.getDescripcion());
			mail.setBody(body);
			if (email.sendEmail(mail)) {
				createMessageFrame(FacesMessage.SEVERITY_INFO, "Mensaje de cambio de estatus", "Se ha enviado un mensaje al solicitante indicando el cambio de estatus de la solicitud.");
			} else {
				createMessageFrame(FacesMessage.SEVERITY_ERROR, "Error enviando mensaje.", "No se ha podido enviar el mensaje al solicitante indicando el cambio de estatus de la solicitud.");
			}				
			
		}			
		catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Procesando la solicitud.","Error. procesando la solicitud. Contacte al help desk.");
			log.error("Error. Procesando la solicitud",e);
		}
	}	
	
	/**
	 * Método que rechaza una solicitud y envia una notificacion de rechazo
	 */ 	
	public void rechazar() {
		try {			
			SpaEstatus spaEstatus = documentoEJB.getSpaEstatusByCodigo("RCH");
			spaHistoricoDocumentoNuevo.setSpaEstatus(spaEstatus);
			spaHistoricoDocumentoNuevo.setSpaSolicitude(spaHistoricoDocumentoSeleccionado.getSpaSolicitude());
			spaHistoricoDocumentoNuevo.setCreationDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setLastUpdateDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaHistoricoDocumentoNuevo.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			documentoEJB.persistSpaHistoricoDocumento(spaHistoricoDocumentoNuevo);
			spaHistoricoDocumentoNuevo = new SpaHistoricoDocumento();
			setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoAprovedAndExecuted(null));
			setSpaHistoricoDocumentoSeleccionado(documentoEJB.getLastSpaHistoricoDocumento(spaHistoricoDocumentoSeleccionado.getSpaSolicitude()));
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Rechazada.", "La solicitud con número "+spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud()+" ha sido rechazada.");
			
			// Enviar notificacion
			InputStream emailFile = this.getClass().getClassLoader().getResourceAsStream("notificacionSolicitudRechazada.html");
			String body = GeneralUtilities.convertStreamToString(emailFile);
			Mail mail = new Mail();
			mail.setFrom("spa@toyota.com.ve");
			mail.setTo(trabajadorSolicitante.getEmail());
			mail.setCc("");
			mail.setBcc("");
			mail.setSuject("Sistema de Procesos Ambientales, Solicitud rechazada.");
			body = String.format(body, trabajadorTDV.getNombreCompleto(), spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud());
			mail.setBody(body);
			if (email.sendEmail(mail)) {
				createMessageFrame(FacesMessage.SEVERITY_INFO, "Mensaje de aprobacion enviado", "Se ha enviado un mensaje al solicitante indicando que se rechazó la solicitud.");
			} else {
				createMessageFrame(FacesMessage.SEVERITY_ERROR, "Error enviando mensaje.", "No se ha podido enviar el mensaje  al solicitante indicando que se rechazó la solicitud.");
			}			
			
		}			
		catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Rechazando la solicitud.","Error. Rechazando la solicitud. Contacte al help desk.");
			log.error("Error. Rechazando la solicitud",e);
		}
	}	
	
	/**
	 * Método que actualiza un desecho seleccionado
	 */	
	public void actualizarDeschoSolicitud() {
		try {
			if(spaDesechoSolicitudSeleccionado!=null){
				if(spaDesechoSolicitudSeleccionado.getCantidad() > 0){
					spaDesechoSolicitudSeleccionado.setLastUpdateDate(Calendar.getInstance().getTime());
					spaDesechoSolicitudSeleccionado.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
					documentoEJB.mergeSpaDesechoSolicitud(spaDesechoSolicitudSeleccionado);
					createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "El desecho seleccionado ha sido actualizado.");
				}else{
					createMessageFrame(FacesMessage.SEVERITY_WARN,"Cantidad incorrecta.", "Indique una cantidad correcta para el desecho seleccionado.");
				}
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un desecho a modificar.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Actualizando desecho.", "El desecho seleccionado no se pudo actualizar, verifique que los datos esten correctos.");
			log.error("Actualizando desecho solicitud. ",e);
		}

	}	
	
	/**
	 * Método que cancela la actualizacion del desecho seleccionado
	 */		
	public void cancelarActualizacionDesechoSolicitud() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada modificacion de desecho", "Se ha cancelado del desecho seleccionado.");
	}		
	
	/**
	 * Método que guarda un nuevo desecho generado al procesar la solicitud
	 */	
	public void agregarDesecho() {
		try {
			spaDesechoSolicitud.setCreationDate(Calendar.getInstance().getTime());
			spaDesechoSolicitud.setLastUpdateDate(Calendar.getInstance().getTime());
			spaDesechoSolicitud.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaDesechoSolicitud.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaDesechoSolicitud.setSpaDesecho(spaDesecho);
			SpaUnidadMedida spaUnidadMedida = administracionEJB.getSpaUnidadMedidaFindAplicatioScope();
			spaDesechoSolicitud.setSpaUnidadMedida(spaUnidadMedida);
			spaDesechoSolicitud.setSpaSolicitude(spaHistoricoDocumentoSeleccionado.getSpaSolicitude());
			documentoEJB.persistSpaDesechoSolicitud(spaDesechoSolicitud);
			spaDesechoSolicitud = new SpaDesechoSolicitud();
			setSpaHistoricoDocumentoSeleccionado(documentoEJB.getSpaHistoricoDocumento(spaHistoricoDocumentoSeleccionado.getIdHistoricoDocumento()));
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Registro nuevo desecho a la solicitud.", "Se registro un nuevo desecho para la solicitud.");
		}			
		catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Registro nuevo desecho a la solicitud.","Error. registrando un nuevo desecho para la solicitud. Contacte al help desk.");
			log.error("Error. Registro nuevo desecho a la solicitud",e);
		}
	}	
	
	/**
	 * Método que elimina un desecho
	 */		
	public void eliminarDesecho() {
		try {
			if(spaDesechoSolicitudSeleccionado!=null){ 
				documentoEJB.removeSpaDesechoSolicitud(spaDesechoSolicitudSeleccionado);
				setSpaHistoricoDocumentoSeleccionado(documentoEJB.getSpaHistoricoDocumento(spaHistoricoDocumentoSeleccionado.getIdHistoricoDocumento()));
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "El desecho seleccionado ha sido eliminado.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un tipo de desecho para ser eliminado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Eliminando desecho.", "El desecho no se pudo eliminar, contacte al help desck.");
			log.error("Eliminando descho. ",e);
		}

	}		
	
	/**
	 * Método que cambia el estatus de la solicitud a ejecutada y envia una notificacion al solicitante
	 */		
	public void cambiarEstatusEjecutada() {
		try {
			SpaEstatus spaEstatus = documentoEJB.getSpaEstatusByCodigo("EJC");
			spaHistoricoDocumentoNuevo.setSpaEstatus(spaEstatus);
			spaHistoricoDocumentoNuevo.setSpaSolicitude(spaHistoricoDocumentoSeleccionado.getSpaSolicitude());
			spaHistoricoDocumentoNuevo.setCreationDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setLastUpdateDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaHistoricoDocumentoNuevo.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			documentoEJB.persistSpaHistoricoDocumento(spaHistoricoDocumentoNuevo);
			spaHistoricoDocumentoNuevo = new SpaHistoricoDocumento();
			setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoAprovedAndExecuted(null));
			setSpaHistoricoDocumentoSeleccionado(documentoEJB.getLastSpaHistoricoDocumento(spaHistoricoDocumentoSeleccionado.getSpaSolicitude()));
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Ejecutada.", "La solicitud con númmero "+spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud()+" se ejecutó.");
			
			// Enviar notificacion
			InputStream emailFile = this.getClass().getClassLoader().getResourceAsStream("notificacionCambioEstatus.html");
			String body = GeneralUtilities.convertStreamToString(emailFile);
			Mail mail = new Mail();
			mail.setFrom("spa@toyota.com.ve");
			mail.setTo(trabajadorSolicitante.getEmail());
			mail.setCc("");
			mail.setBcc("");
			mail.setSuject("Sistema de Procesos Ambientales, Cambio de estatus de la solicitud.");
			body = String.format(body, spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud(), spaEstatus.getDescripcion());
			mail.setBody(body);
			if (email.sendEmail(mail)) {
				createMessageFrame(FacesMessage.SEVERITY_INFO, "Mensaje de cambio de estatus", "Se ha enviado un mensaje al solicitante indicando el cambio de estatus de la solicitud.");
			} else {
				createMessageFrame(FacesMessage.SEVERITY_ERROR, "Error enviando mensaje.", "No se ha podido enviar el mensaje al solicitante indicando el cambio de estatus de la solicitud.");
			}	
			
		}			
		catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Ejecutando la solicitud.","Error. Ejecutando la solicitud. Contacte al help desk.");
			log.error("Error. Ejecutando la solicitud",e);
		}
	}		

    @SuppressWarnings("unchecked")
	public void handleTipoDesechoChange() {  
        if(spaTipoDesecho !=null && !spaTipoDesecho.equals(""))  
        	spaTiposTratamientos = spaTipoDesecho.getSpaTipoTratamientos();  
        else  
        	spaTiposTratamientos = (List<SpaTipoTratamiento>) new SpaTipoTratamiento();  
    }
    
    @SuppressWarnings("unchecked")
	public void handleTipoTratamientoChange() {  
        if(spaTipoTratamiento !=null && !spaTipoTratamiento.equals(""))  
        	spaCategorias = spaTipoTratamiento.getSpaCategorias();  
        else  
        	spaCategorias = (List<SpaCategoria>) new SpaCategoria();  
    }  
    
    @SuppressWarnings("unchecked")
	public void handleCategoriaChange() {  
        if(spaCategoria !=null && !spaCategoria.equals(""))  
        	spaClasificaciones = spaCategoria.getSpaClasificacions();  
        else  
        	spaClasificaciones = (List<SpaClasificacion>) new SpaClasificacion();  
    }  
    
    @SuppressWarnings("unchecked")
	public void handleClasificacionChange() {  
        if(spaClasificacion !=null && !spaClasificacion.equals(""))  
        	spaDesechos = spaClasificacion.getSpaDesechos();  
        else  
        	spaDesechos = (List<SpaDesecho>) new SpaDesecho();  
    }   
	
	
	public SpaHistoricoDocumento getSpaHistoricoDocumentoSeleccionado() {
		return spaHistoricoDocumentoSeleccionado;
	}


	public void setSpaHistoricoDocumentoSeleccionado(SpaHistoricoDocumento spaHistoricoDocumentoSeleccionado) {
		this.spaHistoricoDocumentoSeleccionado = spaHistoricoDocumentoSeleccionado;
	}


	public TrabajadorTDV getTrabajadorTDV() {
		return trabajadorTDV;
	}


	public void setTrabajadorTDV(TrabajadorTDV trabajadorTDV) {
		this.trabajadorTDV = trabajadorTDV;
	}

	public List<SpaHistoricoDocumento> getSpaHistoricoDocumento() {
		return spaHistoricoDocumento;
	}


	public void setSpaHistoricoDocumento(List<SpaHistoricoDocumento> spaHistoricoDocumento) {
		this.spaHistoricoDocumento = spaHistoricoDocumento;
	}


	public SpaHistoricoDocumento getSpaHistoricoDocumentoAprobado() {
		return spaHistoricoDocumentoAprobado;
	}


	public void setSpaHistoricoDocumentoAprobado(SpaHistoricoDocumento spaHistoricoDocumentoAprobado) {
		this.spaHistoricoDocumentoAprobado = spaHistoricoDocumentoAprobado;
	}


	public List<SpaEstatus> getSpaListaEstatus() {
		return spaListaEstatus;
	}


	public void setSpaListaEstatus(List<SpaEstatus> spaListaEstatus) {
		this.spaListaEstatus = spaListaEstatus;
	}


	public String getEstatusSeleccionado() {
		return estatusSeleccionado;
	}


	public void setEstatusSeleccionado(String estatusSeleccionado) {
		this.estatusSeleccionado = estatusSeleccionado;
	}


	public SpaHistoricoDocumento getSpaHistoricoDocumentoNuevo() {
		return spaHistoricoDocumentoNuevo;
	}


	public void setSpaHistoricoDocumentoNuevo(SpaHistoricoDocumento spaHistoricoDocumentoNuevo) {
		this.spaHistoricoDocumentoNuevo = spaHistoricoDocumentoNuevo;
	}


	public SpaItemDesincorporar getSpaItemDesincorporarSeleccionado() {
		return spaItemDesincorporarSeleccionado;
	}


	public void setSpaItemDesincorporarSeleccionado(SpaItemDesincorporar spaItemDesincorporarSeleccionado) {
		this.spaItemDesincorporarSeleccionado = spaItemDesincorporarSeleccionado;
	}


	public SpaTipoDesecho getSpaTipoDesecho() {
		return spaTipoDesecho;
	}


	public void setSpaTipoDesecho(SpaTipoDesecho spaTipoDesecho) {
		this.spaTipoDesecho = spaTipoDesecho;
	}


	public List<SpaTipoDesecho> getSpaTiposDesechos() {
		return spaTiposDesechos;
	}


	public void setSpaTiposDesechos(List<SpaTipoDesecho> spaTiposDesechos) {
		this.spaTiposDesechos = spaTiposDesechos;
	}


	public List<SpaTipoTratamiento> getSpaTiposTratamientos() {
		return spaTiposTratamientos;
	}


	public void setSpaTiposTratamientos(List<SpaTipoTratamiento> spaTiposTratamientos) {
		this.spaTiposTratamientos = spaTiposTratamientos;
	}


	public SpaTipoTratamiento getSpaTipoTratamiento() {
		return spaTipoTratamiento;
	}


	public void setSpaTipoTratamiento(SpaTipoTratamiento spaTipoTratamiento) {
		this.spaTipoTratamiento = spaTipoTratamiento;
	}


	public List<SpaCategoria> getSpaCategorias() {
		return spaCategorias;
	}


	public void setSpaCategorias(List<SpaCategoria> spaCategorias) {
		this.spaCategorias = spaCategorias;
	}


	public SpaCategoria getSpaCategoria() {
		return spaCategoria;
	}


	public void setSpaCategoria(SpaCategoria spaCategoria) {
		this.spaCategoria = spaCategoria;
	}


	public List<SpaClasificacion> getSpaClasificaciones() {
		return spaClasificaciones;
	}


	public void setSpaClasificaciones(List<SpaClasificacion> spaClasificaciones) {
		this.spaClasificaciones = spaClasificaciones;
	}


	public SpaClasificacion getSpaClasificacion() {
		return spaClasificacion;
	}


	public void setSpaClasificacion(SpaClasificacion spaClasificacion) {
		this.spaClasificacion = spaClasificacion;
	}


	public List<SpaDesecho> getSpaDesechos() {
		return spaDesechos;
	}


	public void setSpaDesechos(List<SpaDesecho> spaDesechos) {
		this.spaDesechos = spaDesechos;
	}


	public SpaDesecho getSpaDesecho() {
		return spaDesecho;
	}


	public void setSpaDesecho(SpaDesecho spaDesecho) {
		this.spaDesecho = spaDesecho;
	}


	public SpaDesechoSolicitud getSpaDesechoSolicitud() {
		return spaDesechoSolicitud;
	}


	public void setSpaDesechoSolicitud(SpaDesechoSolicitud spaDesechoSolicitud) {
		this.spaDesechoSolicitud = spaDesechoSolicitud;
	}


	public List<SpaDesechoSolicitud> getSpaDesechoSolicitudes() {
		return spaDesechoSolicitudes;
	}


	public void setSpaDesechoSolicitudes(List<SpaDesechoSolicitud> spaDesechoSolicitudes) {
		this.spaDesechoSolicitudes = spaDesechoSolicitudes;
	}


	public SpaDesechoSolicitud getSpaDesechoSolicitudSeleccionado() {
		return spaDesechoSolicitudSeleccionado;
	}


	public void setSpaDesechoSolicitudSeleccionado(SpaDesechoSolicitud spaDesechoSolicitudSeleccionado) {
		this.spaDesechoSolicitudSeleccionado = spaDesechoSolicitudSeleccionado;
	}
	
	public String getNombreAprobador() {
		return nombreAprobador;
	}

	public void setNombreAprobador(String nombreAprobador) {
		this.nombreAprobador = nombreAprobador;
	}	
	
	public TrabajadorTDV getTrabajadorSolicitante() {
		return trabajadorSolicitante;
	}

	public void setTrabajadorSolicitante(TrabajadorTDV trabajadorSolicitante) {
		this.trabajadorSolicitante = trabajadorSolicitante;
	}	
}
