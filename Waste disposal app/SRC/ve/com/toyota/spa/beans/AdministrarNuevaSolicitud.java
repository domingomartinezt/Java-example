package ve.com.toyota.spa.beans;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
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

import ve.com.toyota.common.ejb.pt.TrabajadorBean;
import ve.com.toyota.common.entities.Mail;
import ve.com.toyota.common.entities.pt.FndFlexValuesVl;
import ve.com.toyota.common.entities.pt.TrabajadorTDV;
import ve.com.toyota.common.mail.Email;
import ve.com.toyota.common.util.GeneralUtilities;
import ve.com.toyota.spa.ejb.AdministracionBean;
import ve.com.toyota.spa.ejb.DocumentoBean;
import ve.com.toyota.spa.entities.SpaAprobacionJunta;
import ve.com.toyota.spa.entities.SpaHistoricoDocumento;
import ve.com.toyota.spa.entities.SpaItemDesincorporar;
import ve.com.toyota.spa.entities.SpaSolicitudes;
import ve.com.toyota.spa.entities.SpaUnidadMedida;

/**
 * Clase controladora de la vista "nuevaSolicitud.xhtml", se realiza la pre-carga de los datos y los métodos necesarios para su funcionamiento.
 * Maneja la creacion de nuevas solicitudes.
 * @author dmartinez
 * @since SPA
 * @see ManagedBean
 * @see ViewScoped
 * @see EJB
 */
@ManagedBean (name = "nuevaSolicitud")
@ViewScoped
public class AdministrarNuevaSolicitud{

	private final static Logger log = Logger.getLogger(AdministrarNuevaSolicitud.class);

	private List<SpaItemDesincorporar> listaItemDesincorporar;
	private SpaItemDesincorporar spaItemDesincorporar = new SpaItemDesincorporar();
	private SpaItemDesincorporar itemDesincorporarSeleccionado;
	private List<SpaUnidadMedida> spaUnidadesMedida;
	private SpaSolicitudes spaSolicitudes = new SpaSolicitudes();
	private SpaAprobacionJunta spaAprobacionJunta = new SpaAprobacionJunta();
	private boolean reqAprobacion = false;
	
	private TrabajadorTDV trabajadorTDV;
	private List<TrabajadorTDV> jerarquiaTrabajadorTDV;
	private List<FndFlexValuesVl> centrosCosto;
	private String descripcionCargo;
	private String descripcionDepartamento;		

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
		setSpaUnidadesMedida(administracionEJB.getSpaUnidadMedidaFindSolicitudScope());
		listaItemDesincorporar = new ArrayList<SpaItemDesincorporar>();
		
		setCentrosCosto(trabajadorEJB.obtenerCentrosCosto());

		setTrabajadorTDV(trabajadorEJB.obtenerTrabajador(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase()));
	
		setDescripcionCargo(trabajadorTDV.getCargo().getName());
		setDescripcionDepartamento(trabajadorTDV.getDepartamento().getDescription());
		setJerarquiaTrabajadorTDV(trabajadorEJB.obtenerJerarquiaTrabajador(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase()));
	}

	/**
	 * Método que agrega un item a desincorporar a la solicitud
	 */		
	public void agregarItemDesincorporar() {
		try {
			spaItemDesincorporar.setCreationDate(Calendar.getInstance().getTime());
			spaItemDesincorporar.setLastUpdateDate(Calendar.getInstance().getTime());
			spaItemDesincorporar.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaItemDesincorporar.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
			spaItemDesincorporar.setSpaSolicitude(spaSolicitudes);
			listaItemDesincorporar.add(spaItemDesincorporar);
			spaItemDesincorporar = new SpaItemDesincorporar();
			createMessageFrame(FacesMessage.SEVERITY_INFO, "Agregado.", "Nuevo item a desincorporar agregado a la solicitud.");			
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error.", "No se pudo agregar el item en la solicitud. "+e);
			log.error("Agregando item a desincorporar en la solicitud. ",e);
		}		
	}
	
	/**
	 * Método que elimina un item a desincorporar a la solicitud
	 */		
	public void eliminarItemDesincorporar() {
		try {
			if(itemDesincorporarSeleccionado!=null){
				listaItemDesincorporar.remove(itemDesincorporarSeleccionado);
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Eliminado.", "El item a desincorporar ha sido eliminado de la solicitud.");
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un item para ser eliminado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error.", "El item seleccionado no se pudo eliminar de la solicitud.");
			log.error("Eliminando item a desincorporar de la solicitud. ",e);
		}

	}	
	
	/**
	 * Método que modifica un item a desincorporar de la solicitud
	 */			
	public void actualizarItemDesincorporar() {
		try {
			if(itemDesincorporarSeleccionado!=null){ 
				spaItemDesincorporar.setLastUpdateDate(Calendar.getInstance().getTime());
				spaItemDesincorporar.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				listaItemDesincorporar.remove(itemDesincorporarSeleccionado);
				listaItemDesincorporar.add(itemDesincorporarSeleccionado);
				createMessageFrame(FacesMessage.SEVERITY_INFO,"Actualizado.", "El item a desincorporar seleccionado ha sido actualizado.");				
			}else{
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Seleccione un item a desincorporar para ser actualizado.");
			}
		} catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error", "El item a desincorporar seleccionado no se pudo actualizar.");
			log.error("Actualizando item a desincorporar seleccionado. ",e);
		}

	}
	
	/**
	 * Método crea una nueva solicitud de desincorporacion y envia una notificacion de correo
	 */		
	public void guardarSolicitud() {
		try {
			List<SpaHistoricoDocumento> spaHistoricoDocumentos = new ArrayList<SpaHistoricoDocumento>();
			if(listaItemDesincorporar.isEmpty()){
				createMessageFrame(FacesMessage.SEVERITY_WARN,"Acción requerida.", "Debe agregar por lo menos un item a desincorporar en la solicitud.");	
			}else{
				if (reqAprobacion){
					spaSolicitudes.setIndReqAprobacionJunta("Y");
					spaSolicitudes.setSpaAprobacionJunta(spaAprobacionJunta);
				}else{
					spaSolicitudes.setIndReqAprobacionJunta("N");
				}			
				spaSolicitudes.setCreationDate(Calendar.getInstance().getTime());
				spaSolicitudes.setLastUpdateDate(Calendar.getInstance().getTime());
				spaSolicitudes.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				spaSolicitudes.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				spaSolicitudes.setPersonIdElabora(trabajadorTDV.getPersonId());
				spaSolicitudes.setSpaTipoDocumento(documentoEJB.getSpaTipoDocumentoByIdCodigo("MD1"));
				SpaHistoricoDocumento spaHistoricoDocumento = new SpaHistoricoDocumento();
				spaHistoricoDocumento.setSpaEstatus(documentoEJB.getSpaEstatusByCodigo("PAP"));
				spaHistoricoDocumento.setCreationDate(Calendar.getInstance().getTime());
				spaHistoricoDocumento.setLastUpdateDate(Calendar.getInstance().getTime());
				spaHistoricoDocumento.setCreatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				spaHistoricoDocumento.setLastUpdatedBy(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase());
				spaHistoricoDocumento.setSpaSolicitude(spaSolicitudes);
				spaHistoricoDocumentos.add(spaHistoricoDocumento);
				spaSolicitudes.setSpaHistoricoDocumentos(spaHistoricoDocumentos);
				spaSolicitudes.setSpaItemDesincorporar(listaItemDesincorporar);
				documentoEJB.persistSpaSolicitudes(spaSolicitudes);
				createMessageFrame(FacesMessage.SEVERITY_INFO, "Creada.", "La solicitud con númmero "+spaSolicitudes.getIdSolicitud()+" ha sido creada con éxito.");
				
				// Enviar notificacion
				InputStream emailFile = this.getClass().getClassLoader().getResourceAsStream("notificacionAprobacion.html");
				String body = GeneralUtilities.convertStreamToString(emailFile);
				TrabajadorTDV trabajadorAprueba = trabajadorEJB.obtenerTrabajadorByPersonId(new BigDecimal(spaSolicitudes.getPersonIdAprueba()));
				Mail mail = new Mail();
				mail.setFrom("spa@toyota.com.ve");
				mail.setTo(trabajadorAprueba.getEmail());
				mail.setCc("");
				mail.setBcc("");
				mail.setSuject("Sistema de Procesos Ambientales, Posee un documento de aprobacion pendiente.");
				body = String.format(body, trabajadorTDV.getNombreCompleto(), spaSolicitudes.getIdSolicitud());
				mail.setBody(body);
				if (email.sendEmail(mail)) {
					createMessageFrame(FacesMessage.SEVERITY_INFO, "Mensaje de aprobacion enviado", "Se ha enviado un mensaje al aprobador para que apruebe la solicitud.");
				} else {
					createMessageFrame(FacesMessage.SEVERITY_ERROR, "Error enviando mensaje.", "No se ha podido enviar el mensaje al aprobador para que apruebe la solicitud.");
				}
				
				// Recargar todos los valores a memoria.
				spaSolicitudes = new SpaSolicitudes();
				setSpaUnidadesMedida(administracionEJB.getSpaUnidadMedidaFindSolicitudScope());
				listaItemDesincorporar = new ArrayList<SpaItemDesincorporar>();
				setCentrosCosto(trabajadorEJB.obtenerCentrosCosto());
				setTrabajadorTDV(trabajadorEJB.obtenerTrabajador(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase()));
				setDescripcionCargo(trabajadorTDV.getCargo().getName());
				setDescripcionDepartamento(trabajadorTDV.getDepartamento().getDescription());
				setJerarquiaTrabajadorTDV(trabajadorEJB.obtenerJerarquiaTrabajador(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().toUpperCase()));
				spaAprobacionJunta = new SpaAprobacionJunta();					
			}
		}
			catch (Exception e) {
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Creando la solicitud de desincorporacion.","Error. Creando la solicitud de desincorporacion. Contacte al help desk.");
			log.error("Creando la solicitud de desincorporacion.",e);
		}
	}
				
	/**
	 * Método cancela la modificacion de un item a desincorporar de la solicitud
	 */		
	public void cancelarActualizacionDesecho() {
		createMessageFrame(FacesMessage.SEVERITY_INFO,"Cancelada actualización.", "Se ha cancelado la actulización de datos para el item a desincorporar selccionado.");
	}


	public List<SpaItemDesincorporar> getListaItemDesincorporar() {
		return listaItemDesincorporar;
	}


	public void setListaItemDesincorporar(List<SpaItemDesincorporar> listaItemDesincorporar) {
		this.listaItemDesincorporar = listaItemDesincorporar;
	}


	public SpaItemDesincorporar getSpaItemDesincorporar() {
		return spaItemDesincorporar;
	}


	public void setSpaItemDesincorporar(SpaItemDesincorporar spaItemDesincorporar) {
		this.spaItemDesincorporar = spaItemDesincorporar;
	}


	public SpaItemDesincorporar getItemDesincorporarSeleccionado() {
		return itemDesincorporarSeleccionado;
	}


	public void setItemDesincorporarSeleccionado(SpaItemDesincorporar itemDesincorporarSeleccionado) {
		this.itemDesincorporarSeleccionado = itemDesincorporarSeleccionado;
	}


	public List<SpaUnidadMedida> getSpaUnidadesMedida() {
		return spaUnidadesMedida;
	}


	public void setSpaUnidadesMedida(List<SpaUnidadMedida> spaUnidadesMedida) {
		this.spaUnidadesMedida = spaUnidadesMedida;
	}


	public TrabajadorTDV getTrabajadorTDV() {
		return trabajadorTDV;
	}


	public void setTrabajadorTDV(TrabajadorTDV trabajadorTDV) {
		this.trabajadorTDV = trabajadorTDV;
	}


	public List<TrabajadorTDV> getJerarquiaTrabajadorTDV() {
		return jerarquiaTrabajadorTDV;
	}


	public void setJerarquiaTrabajadorTDV(List<TrabajadorTDV> jerarquiaTrabajadorTDV) {
		this.jerarquiaTrabajadorTDV = jerarquiaTrabajadorTDV;
	}


	public List<FndFlexValuesVl> getCentrosCosto() {
		return centrosCosto;
	}


	public void setCentrosCosto(List<FndFlexValuesVl> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}


	public String getDescripcionCargo() {
		return descripcionCargo;
	}


	public void setDescripcionCargo(String descripcionCargo) {
		this.descripcionCargo = descripcionCargo;
	}


	public String getDescripcionDepartamento() {
		return descripcionDepartamento;
	}


	public void setDescripcionDepartamento(String descripcionDepartamento) {
		this.descripcionDepartamento = descripcionDepartamento;
	}


	public boolean isReqAprobacion() {
		return reqAprobacion;
	}


	public void setReqAprobacion(boolean reqAprobacion) {
		this.reqAprobacion = reqAprobacion;
	}


	public SpaSolicitudes getSpaSolicitudes() {
		return spaSolicitudes;
	}


	public void setSpaSolicitudes(SpaSolicitudes spaSolicitudes) {
		this.spaSolicitudes = spaSolicitudes;
	}


	public SpaAprobacionJunta getSpaAprobacionJunta() {
		return spaAprobacionJunta;
	}


	public void setSpaAprobacionJunta(SpaAprobacionJunta spaAprobacionJunta) {
		this.spaAprobacionJunta = spaAprobacionJunta;
	}
	

}
