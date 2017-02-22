package ve.com.toyota.spa.beans;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import ve.com.toyota.common.entities.pt.TrabajadorTDV;
import ve.com.toyota.sap.util.ReportRuntines;
import ve.com.toyota.spa.ejb.DocumentoBean;
import ve.com.toyota.spa.entities.SpaHistoricoDocumento;
import ve.com.toyota.spa.entities.SpaImagen;
import ve.com.toyota.spa.entities.SpaSolicitudes;
import ve.com.toyota.spa.util.DuplicateStream;

/**
 * Clase controladora de la vista "planillaMD1.xhtml" y "wasteEpi.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * Las peticiones de reportes y les asigna formatos de salida 
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "reportes")
@ViewScoped
public class Reportes {

	private final static Logger log = Logger.getLogger(Reportes.class);
	
	private Date fechaInicio;
	private Date fechaFin;
	private String formato;
	private List<SpaHistoricoDocumento> spaHistoricoDocumento;
	private List<SpaHistoricoDocumento> filteredHistoricos;
	private SpaHistoricoDocumento spaHistoricoDocumentoSeleccionado;
	private DuplicateStream duplicateStream;
    private StreamedContent file;	

	
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
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log
		setSpaHistoricoDocumento(documentoEJB.getAllLastSpaHistoricoDocumento());
	}

	/**
	 * Método que genera el reporte WasteEpi
	 */	
	public void reporteWasteEpi() {
		try {		
				if(formato.equals("PDF")){
					ReportRuntines reportRuntines = new ReportRuntines();				
					FacesContext context = FacesContext.getCurrentInstance();
					HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
					HashMap<String, Date> parametros = new HashMap<String, Date>();
					parametros.put("FECHA_INICIO", getFechaInicio());
					parametros.put("FECHA_FIN", getFechaFin());
					reportRuntines.toPDF(parametros, response, "wasteEpi");
					context.getApplication().getStateManager().saveView(context);
					context.responseComplete();
				}else{
					ReportRuntines reportRuntines = new ReportRuntines();				
					FacesContext context = FacesContext.getCurrentInstance();
					HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
					HashMap<String, Date> parametros = new HashMap<String, Date>();
					parametros.put("FECHA_INICIO", getFechaInicio());
					parametros.put("FECHA_FIN", getFechaFin());
					reportRuntines.toXls(parametros, response, "wasteEpi");
					context.getApplication().getStateManager().saveView(context);
					context.responseComplete();					
				}
			}			
			catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Generando el reporte WasteEPI.","Error. Generando el reporte WasteEPI. Contacte al help desk.");
			log.error("Generando el reporte WasteEPI.",e);
		}
			
	}

	/**
	 * Método que genera la planilla MD1
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
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Imprimiendo reporte planilla MD1.","Error. Imprimiendo reporte planilla MD1. Contacte al help desk.");
			log.error("Imprimiendo reporte planilla MD1.",e);
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

	public Date getFechaInicio() {
		return fechaInicio;
	}


	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	public Date getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}


	public String getFormato() {
		return formato;
	}


	public void setFormato(String formato) {
		this.formato = formato;
	}


	public List<SpaHistoricoDocumento> getSpaHistoricoDocumento() {
		return spaHistoricoDocumento;
	}


	public void setSpaHistoricoDocumento(List<SpaHistoricoDocumento> spaHistoricoDocumento) {
		this.spaHistoricoDocumento = spaHistoricoDocumento;
	}


	public SpaHistoricoDocumento getSpaHistoricoDocumentoSeleccionado() {
		return spaHistoricoDocumentoSeleccionado;
	}


	public void setSpaHistoricoDocumentoSeleccionado(SpaHistoricoDocumento spaHistoricoDocumentoSeleccionado) {
		this.spaHistoricoDocumentoSeleccionado = spaHistoricoDocumentoSeleccionado;
	}


	public List<SpaHistoricoDocumento> getFilteredHistoricos() {
		return filteredHistoricos;
	}


	public void setFilteredHistoricos(List<SpaHistoricoDocumento> filteredHistoricos) {
		this.filteredHistoricos = filteredHistoricos;
	}
	
	
}
