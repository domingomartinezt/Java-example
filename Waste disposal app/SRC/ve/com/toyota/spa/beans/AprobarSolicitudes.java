package ve.com.toyota.spa.beans;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

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
import ve.com.toyota.sap.util.ReportRuntines;
import ve.com.toyota.spa.ejb.DocumentoBean;
import ve.com.toyota.spa.entities.SpaEstatus;
import ve.com.toyota.spa.entities.SpaHistoricoDocumento;
import ve.com.toyota.spa.entities.SpaImagen;
import ve.com.toyota.spa.entities.SpaSolicitudes;
import ve.com.toyota.spa.util.DuplicateStream;

/**
 * Clase controladora de la vista "aprobarSolicitudes.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * Maneja la aprobacion de las solicitudes, muestra las solicitudes a los aprobadores.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "aprobarSolicitudes")
@ViewScoped
public class AprobarSolicitudes {

	private final static Logger log = Logger.getLogger(AdministrarNuevaSolicitud.class);
	
	private List<SpaHistoricoDocumento> spaHistoricoDocumento;
	private SpaHistoricoDocumento spaHistoricoDocumentoSeleccionado;
	private SpaHistoricoDocumento spaHistoricoDocumentoAprobado = new SpaHistoricoDocumento();
	private SpaHistoricoDocumento spaHistoricoDocumentoNuevo = new SpaHistoricoDocumento();
	private List<SpaEstatus> spaListaEstatus;
	private String estatusSeleccionado = null;
	private TrabajadorTDV trabajadorTDV;
	private TrabajadorTDV trabajadorSolicitante;	
	private String nombreAprobador = "";
	private DuplicateStream duplicateStream;
    private StreamedContent file;
	
    /**
	 * Objeto que gestiona las operaciones con la base de datos.
	 */
	@EJB
	private transient DocumentoBean documentoEJB;
	
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
		setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdAprueba(null, trabajadorTDV.getPersonId()));
		setSpaListaEstatus(documentoEJB.getAllSpaEstatusActives());
	}
	
	/**
	 * Método que busca los datos del aprobador del historico de la solicitud seleccionada.
	 */		
	public void buscarDatosAprobador() throws Exception {
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
		setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdAprueba(estatusSeleccionado, trabajadorTDV.getPersonId()));
	}	
	
	/**
	 * Método que imprime el reporte MD1 del historico de la solicitud seleccionada
	 */		
	public void reportePlanilla() {
		try {	
			TrabajadorTDV solicitante;			
			List<SpaSolicitudes> listObjetos = new ArrayList<SpaSolicitudes>();
			listObjetos.add(spaHistoricoDocumentoSeleccionado.getSpaSolicitude());	
			solicitante = trabajadorEJB.obtenerTrabajadorByPersonId(new BigDecimal(spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getPersonIdElabora()));
			
			ReportRuntines reportRuntines = new ReportRuntines();			
			FacesContext context = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("NOMBRE_SOLICITANTE", solicitante.getNombreCompleto());
			parametros.put("DEPARTAMENTO", solicitante.getDepartamento().getDescription());		
			reportRuntines.toPDF(listObjetos, parametros, response, "reporteMD1");
			context.getApplication().getStateManager().saveView(context);
			context.responseComplete();				
		}			
		catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando la solicitud de desincorporacion.","Error. Creando la solicitud de desincorporacion. Contacte al help desk.");
			log.error("Creando la solicitud de desincorporacion.",e);
		}
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
	 * Método que aprueba una solicitud y envia una notificacion de aprobacion
	 */    
	public void aprobar() {
		try {
			SpaEstatus spaEstatus = documentoEJB.getSpaEstatusByCodigo("APR");
			spaHistoricoDocumentoNuevo.setSpaEstatus(spaEstatus);
			spaHistoricoDocumentoNuevo.setSpaSolicitude(spaHistoricoDocumentoSeleccionado.getSpaSolicitude());
			spaHistoricoDocumentoNuevo.setCreationDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setLastUpdateDate(Calendar.getInstance().getTime());
			spaHistoricoDocumentoNuevo.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaHistoricoDocumentoNuevo.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			documentoEJB.persistSpaHistoricoDocumento(spaHistoricoDocumentoNuevo);
			spaHistoricoDocumentoNuevo = new SpaHistoricoDocumento();
			setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdAprueba(null, trabajadorTDV.getPersonId()));
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Aprobada.", "La solicitud con número "+spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud()+" ha sido aprobada.");

			// Enviar notificacion
			InputStream emailFile = this.getClass().getClassLoader().getResourceAsStream("notificacionSolicitudAprobada.html");
			String body = GeneralUtilities.convertStreamToString(emailFile);
			Mail mail = new Mail();
			mail.setFrom("spa@toyota.com.ve");
			mail.setTo(trabajadorSolicitante.getEmail());
			mail.setCc("listadedistribucionspa@toyota.com.ve");
			mail.setBcc("");
			mail.setSuject("Sistema de Procesos Ambientales, Solicitud aprobada.");
			body = String.format(body, trabajadorTDV.getNombreCompleto(), spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud());
			mail.setBody(body);
			if (email.sendEmail(mail)) {
				createMessageFrame(FacesMessage.SEVERITY_INFO, "Mensaje de aprobacion enviado", "Se ha enviado un mensaje al solicitante y al departamento de ambiente indicando que se aprobo la solicitud.");
			} else {
				createMessageFrame(FacesMessage.SEVERITY_ERROR, "Error enviando mensaje.", "No se ha podido enviar el mensaje  al solicitante y al departamento de ambiente indicando que se aprobo la solicitud.");
			}
			
		}			
		catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Aprobando la solicitud.","Error. Aprobando la solicitud. Contacte al help desk.");
			log.error("Error. Aprobando la solicitud",e);
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
			setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdAprueba(null, trabajadorTDV.getPersonId()));
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
