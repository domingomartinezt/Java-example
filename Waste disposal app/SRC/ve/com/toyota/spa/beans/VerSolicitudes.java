package ve.com.toyota.spa.beans;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ve.com.toyota.common.ejb.pt.TrabajadorBean;
import ve.com.toyota.common.entities.pt.TrabajadorTDV;
import ve.com.toyota.sap.util.ReportRuntines;
import ve.com.toyota.spa.ejb.DocumentoBean;
import ve.com.toyota.spa.entities.SpaAprobacionJunta;
import ve.com.toyota.spa.entities.SpaEstatus;
import ve.com.toyota.spa.entities.SpaHistoricoDocumento;
import ve.com.toyota.spa.entities.SpaImagen;
import ve.com.toyota.spa.entities.SpaSolicitudes;
import ve.com.toyota.spa.util.DuplicateStream;

/**
 * Clase controladora de la vista "solicitudesCreadas.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * Maneja la visualizacion y carga de archivos abjuntos a la solicitud
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "verSolicitudes")
@ViewScoped
public class VerSolicitudes {

	private final static Logger log = Logger.getLogger(AdministrarNuevaSolicitud.class);
	
	private List<SpaHistoricoDocumento> spaHistoricoDocumento;
	private SpaHistoricoDocumento spaHistoricoDocumentoSeleccionado;
	private SpaHistoricoDocumento spaHistoricoDocumentoAprobado = new SpaHistoricoDocumento();
	private List<SpaEstatus> spaListaEstatus;
	private String estatusSeleccionado = null;
	private TrabajadorTDV trabajadorTDV;
	private String nombreAprobador = "";
	DuplicateStream duplicateStream;
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
	 * Método de pre-carga de información.
	 */	
	@PostConstruct
	public void init() {
		try {			
			URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
			PropertyConfigurator.configure(url);				// Carga configuracion del log
			setTrabajadorTDV(trabajadorEJB.obtenerTrabajador(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase()));
			setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdElabora(null, trabajadorTDV.getPersonId()));
			setSpaListaEstatus(documentoEJB.getAllSpaEstatusActives());
		}			
		catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Datos de oracle del usuario no encontrados.","Error. Datos de oracle del usuario no encontrados. Contacte al help desk.");
			log.error("Datos de oracle del usuario no encontrados.",e);
		}		
	}

	/**
	 * Método que imprime el reporte de aprobacion de junta del historico de la solicitud seleccionada si este posee aprobacion de junta
	 */			
	public void reporteAprobacion() {
		try {		
				List<SpaAprobacionJunta> listObjetos = new ArrayList<SpaAprobacionJunta>();
				listObjetos.add(spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getSpaAprobacionJunta());

				ReportRuntines reportRuntines = new ReportRuntines();				
				FacesContext context = FacesContext.getCurrentInstance();

				HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

				HashMap<String, String> parametros = new HashMap<String, String>();
				parametros.put("NOMBRE_SOLICITANTE", trabajadorTDV.getNombreCompleto());
				parametros.put("NOMBRE_ARPOBADOR", trabajadorEJB.obtenerTrabajadorByPersonId(new BigDecimal(spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getPersonIdAprueba())).getNombreCompleto());				
				parametros.put("DEPARTAMENTO", trabajadorTDV.getDepartamento().getDescription());
				parametros.put("CENTRO_COSTO", spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getCentroCosto());
				reportRuntines.toPDF(listObjetos, parametros, response, "aprobacionJunta");
				context.getApplication().getStateManager().saveView(context);
				context.responseComplete();				
			}			
			catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando la solicitud de desincorporacion.","Error. Creando la solicitud de desincorporacion. Contacte al help desk.");
			log.error("Creando la solicitud de desincorporacion.",e);
		}
			
	}	
	
	/**
	 * Método que busca los datos del aprobador del historico de la solicitud seleccionada.
	 */			
	public void buscarDatosAprobador() throws Exception {
		setSpaHistoricoDocumentoAprobado(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdSolicitud("APR",spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getIdSolicitud()));
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
		setSpaHistoricoDocumento(documentoEJB.getLastSpaHistoricoDocumentoByEstatusAndIdElabora(estatusSeleccionado, trabajadorTDV.getPersonId()));
	}	
	
	/**
	 * Método que imprime el reporte MD1 del historico de la solicitud seleccionada
	 */		
	public void reportePlanilla() {
		try {		
			List<SpaSolicitudes> listObjetos = new ArrayList<SpaSolicitudes>();
			listObjetos.add(spaHistoricoDocumentoSeleccionado.getSpaSolicitude());	
	
			ReportRuntines reportRuntines = new ReportRuntines();			
			FacesContext context = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("NOMBRE_SOLICITANTE", trabajadorTDV.getNombreCompleto());
			parametros.put("DEPARTAMENTO", trabajadorTDV.getDepartamento().getDescription());		
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
	 * Método que abjunta un archivo a la solicitud
	 */	
    public void handleFileUpload(FileUploadEvent event) {  
    	try {
        	BigDecimal imagenSecuence = documentoEJB.saveFile(Calendar.getInstance().getTime().getTime()+"_"+event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getContents());
			SpaImagen spaImagen = documentoEJB.getSpaImagen(imagenSecuence);
			spaImagen.setCreationDate(Calendar.getInstance().getTime());
			spaImagen.setLastUpdateDate(Calendar.getInstance().getTime());
			spaImagen.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaImagen.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			SpaAprobacionJunta spaAprobacionJunta = spaHistoricoDocumentoSeleccionado.getSpaSolicitude().getSpaAprobacionJunta();
			spaAprobacionJunta.setSpaImagen(spaImagen);
			spaImagen.setSpaAprobacionJuntas(spaAprobacionJunta);
			documentoEJB.mergeSpaAprobacionJunta(spaAprobacionJunta);
	        FacesMessage msg = new FacesMessage("Completado", Calendar.getInstance().getTime().getTime()+"_"+event.getFile().getFileName() + " ha sido subido."); 
	        FacesContext.getCurrentInstance().addMessage(null, msg);  			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Subiendo archivo.","Error. El archivo no pudo ser subido al servidor. Contacte al help desk.");
			log.error("Error. Subiendo archivo.",e);
		}
    } 	
  
	/**
	 * Método que descarga el archivo abjunto de una solicitud
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


	public String getNombreAprobador() {
		return nombreAprobador;
	}


	public void setNombreAprobador(String nombreAprobador) {
		this.nombreAprobador = nombreAprobador;
	}
	
	
	
	
}
