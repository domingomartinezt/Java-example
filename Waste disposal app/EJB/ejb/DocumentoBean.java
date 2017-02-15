package ve.com.toyota.spa.ejb;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Properties;

import oracle.sql.BFILE;
import ve.com.toyota.spa.entities.SpaAprobacionJunta;
import ve.com.toyota.spa.entities.SpaDesechoSolicitud;
import ve.com.toyota.spa.entities.SpaEstatus;
import ve.com.toyota.spa.entities.SpaHistoricoDocumento;
import ve.com.toyota.spa.entities.SpaImagen;
import ve.com.toyota.spa.entities.SpaItemDesincorporar;
import ve.com.toyota.spa.entities.SpaSolicitudes;
import ve.com.toyota.spa.entities.SpaTipoDocumento;


/**
 * Clase que contiene operaciones sobre documentos de desincorporacion de activos, consultas, actualizaciones e inserciones en base de datos.
 * @author dmartinez
 * @since SPA
 * @see Stateless
 * @see TransactionManagement
 */
@Stateless(name = "DocumentoBean")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoBean
{

	/**
	 * Objeto que gestiona las operaciones de almacenamiento de las entidades de base datos.
	 */
    @PersistenceContext(unitName="spaJPA")
    private EntityManager em;

	/**
	 * Objeto que contiene los parametros de configuracion para el guardado de archivos abjuntos.
	 */
    @Resource(lookup = "resource/spaAttachmentProperties")
    private Properties spaAttachmentDirectory;    
    

	/**
	 * Metodo que guarda en base de datos una aprobacion de junta, si existe una creada con el mismo ID lanza una exepcion.
	 * @param SpaAprobacionJunta
	 */
    public SpaAprobacionJunta persistSpaAprobacionJunta(SpaAprobacionJunta spaAprobacionJunta) {
        em.persist(spaAprobacionJunta);
        return spaAprobacionJunta;
    }

	/**
	 * Metodo que actualiza en base de datos una aprobacion de junta directiva.
	 * @param SpaAprobacionJunta
	 */
    public SpaAprobacionJunta mergeSpaAprobacionJunta(SpaAprobacionJunta spaAprobacionJunta) {
        return em.merge(spaAprobacionJunta);
    }

	/**
	 * Metodo que elimina una aprobacion de junta de base de datos.
	 * @param SpaAprobacionJunta 
	 */
    public void removeSpaAprobacionJunta(SpaAprobacionJunta spaAprobacionJunta) {
        spaAprobacionJunta = em.find(SpaAprobacionJunta.class, spaAprobacionJunta.getIdAprobacionJunta());
        em.remove(spaAprobacionJunta);
    }

	/**
	 * Metodo que guarda en base de datos un nuevo historico, si existe uno con el mismo ID lanza una exepcion.
	 * @param SpaHistoricoDocumento
	 */
    public SpaHistoricoDocumento persistSpaHistoricoDocumento(SpaHistoricoDocumento spaHistoricoDocumento) {
        em.persist(spaHistoricoDocumento);
        return spaHistoricoDocumento;
    }

	/**
	 * Metodo que actualiza en base de datos un historico de documento.
	 * @param SpaHistoricoDocumento
	 */
    public SpaHistoricoDocumento mergeSpaHistoricoDocumento(SpaHistoricoDocumento spaHistoricoDocumento) {
        return em.merge(spaHistoricoDocumento);
    }

	/**
	 * Metodo que elimina un historico de documento de la base de datos.
	 * @param SpaHistoricoDocumento
	 */
    public void removeSpaHistoricoDocumento(SpaHistoricoDocumento spaHistoricoDocumento) {
        spaHistoricoDocumento = em.find(SpaHistoricoDocumento.class, spaHistoricoDocumento.getIdHistoricoDocumento());
        em.remove(spaHistoricoDocumento);
    }


	/**
	 * Metodo que actualiza en base de datos una imagen.
	 * @param SpaImagen
	 */
    public SpaImagen mergeSpaImagen(SpaImagen spaImagen) {
        return em.merge(spaImagen);
    }

	/**
	 * Metodo que elimina una imagen de la base de datos.
	 * @param SpaImagen
	 */
    public void removeSpaImagen(SpaImagen spaImagen) {
        spaImagen = em.find(SpaImagen.class, spaImagen.getIdImagen());
        em.remove(spaImagen);
    }

	/**
	 * Metodo que guarda en base de datos un item a desincorporar, si existe uno con el mismo ID lanza una exepcion.
	 * @param SpaItemDesincorporar
	 */
    public SpaItemDesincorporar persistSpaItemDesincorporar(SpaItemDesincorporar spaItemDesincorporar) {
        em.persist(spaItemDesincorporar);
        return spaItemDesincorporar;
    }

	/**
	 * Metodo que actualiza en base de datos un item a desincorporar.
	 * @param SpaItemDesincorporar
	 */
    public SpaItemDesincorporar mergeSpaItemDesincorporar(SpaItemDesincorporar spaItemDesincorporar) {
        return em.merge(spaItemDesincorporar);
    }

	/**
	 * Metodo que elimina un item a desincorporar de la base de datos.
	 * @param SpaItemDesincorporar
	 */
    public void removeSpaItemDesincorporar(SpaItemDesincorporar spaItemDesincorporar) {
        spaItemDesincorporar = em.find(SpaItemDesincorporar.class, spaItemDesincorporar.getIdItemDesincorporar());
        em.remove(spaItemDesincorporar);
    }

	/**
	 * Metodo que elimina un detalle de los desechos de una solicitud de la base de datos.
	 * @param SpaDesechoSolicitud
	 */
    public void removeSpaDesechoSolicitud(SpaDesechoSolicitud spaDesechoSolicitud) {
    	spaDesechoSolicitud = em.find(SpaDesechoSolicitud.class, spaDesechoSolicitud.getIdDesechoSolicitud());
        em.remove(spaDesechoSolicitud);
    }
    
	/**
	 * Metodo que guarda en base de datos una nueva solicitud de desincorporacion, si existe una con el mismo ID lanza una exepcion.
	 * @param SpaSolicitudes.
	 * @return SpaSolicitudes guardada.
	 */
    public SpaSolicitudes persistSpaSolicitudes(SpaSolicitudes spaSolicitudes) {
        em.persist(spaSolicitudes);
        return spaSolicitudes;
    }

	/**
	 * Metodo que guarda en base de datos un nuevo detalle de una solicitud de desincorporacion, si existe un detalle con el mismo ID lanza una exepcion.
	 * @return SpaDesechoSolicitud
	 */
    public SpaDesechoSolicitud persistSpaDesechoSolicitud(SpaDesechoSolicitud spaDesechoSolicitud) {
        em.persist(spaDesechoSolicitud);
        return spaDesechoSolicitud;
    }    
    
	/**
	 * Metodo que actualiza en base de datos una solicitud.
	 * @return SpaSolicitudes
	 */
    public SpaSolicitudes mergeSpaSolicitudes(SpaSolicitudes spaSolicitudes) {
        return em.merge(spaSolicitudes);
    }
    
	/**
	 * Metodo que actualiza en base de datos un detalle de una solicitud de desincorporacion.
	 * @param SpaDesechoSolicitud
	 */
    public SpaDesechoSolicitud mergeSpaDesechoSolicitud(SpaDesechoSolicitud spaDesechoSolicitud) {
        return em.merge(spaDesechoSolicitud);
    }    

	/**
	 * Metodo que elimina una solicitud de la base de datos.
	 * @param SpaSolicitudes
	 */
    public void removeSpaSolicitudes(SpaSolicitudes spaSolicitudes) {
        spaSolicitudes = em.find(SpaSolicitudes.class, spaSolicitudes.getIdSolicitud());
        em.remove(spaSolicitudes);
    }

	/**
	 * Metodo que busca una solicitud por su ID.
	 * @return SpaSolicitudes Solicitud encontrada.
	 */
    public SpaSolicitudes getSpaSolicitudByIdSolicitud(Long idSolicitud) {
        return em.createNamedQuery("SpaSpaSolicitudes.ByIdSolicitud", SpaSolicitudes.class).setParameter("idSolicitud", idSolicitud).getSingleResult();
    }
    
	/**
	 * Metodo que busca una lista de solicitudes creadas por una persona.
	 * @return List<SpaSolicitudes> Solicitudes realizadas por la persona.
	 */
    public List<SpaSolicitudes> getSpaSolicitudesByIdElabora(int personIdElabora) {
        return em.createNamedQuery("SpaSpaSolicitudes.ByIdElabora", SpaSolicitudes.class).setParameter("personIdElabora", personIdElabora).getResultList();
    }
        
    
	/**
	 * Metodo que devuelve un tipo de documento por su codigo
	 * @return SpaTipoDocumento Tipo de documento devuelto que coincide con el codigo sumistrado.
	 */
    public SpaTipoDocumento getSpaTipoDocumentoByIdCodigo(String codigo) {
        return em.createNamedQuery("SpaTipoDocumento.findByCodigo", SpaTipoDocumento.class).setParameter("codigo", codigo).getSingleResult();
    }   
    

	/**
	 * Metodo que busca una imagen por su ID.
	 * @return SpaImagen Imagen encontrada.
	 */
    public SpaImagen getSpaImagen(BigDecimal secuencia) {
        return em.createNamedQuery("SpaImagen.findById", SpaImagen.class).setParameter("idImagen", secuencia.longValueExact()).getSingleResult();
    }      
        
    
	/**
	 * Metodo que devuelve un estatus por su codigo
	 * @return SpaEstatus Esatus devuelto que coincide con el codigo sumistrado.
	 */
    public SpaEstatus getSpaEstatusByCodigo(String codigo) {
        return em.createNamedQuery("SpaEstatus.findByCodigo", SpaEstatus.class).setParameter("codigo", codigo).getSingleResult();
    }     
    
	/**
	 * Metodo que busca un historico documento por el ID del historico
	 * @return SpaHistoricoDocumento Historico encontrado con el ID.
	 */
    public SpaHistoricoDocumento getSpaHistoricoDocumento(long idHistoricoDocumento) {
        return em.find(SpaHistoricoDocumento.class, idHistoricoDocumento);
    }      

	/**
	 * Metodo que busca solicitudes por un estatus dado y un tipo de documento
	 * @return List<SpaSolicitudes> Solicitudes encontradas con un estatus y tipo de documento dado.
	 */
    public List<SpaSolicitudes> getSpaSolicitudesByEstatusAndTipoDocumento(String codigoDocumento, String codEstatus) {
        return em.createNamedQuery("SpaTipoDesecho.ByEstatusAndTipoDocumento", SpaSolicitudes.class).
        		setParameter("codigoDocumento", codigoDocumento).setParameter("codEstatus", codEstatus).getResultList();
    }
    
	/**
	 * Metodo que busca solicitudes por un estatus dado, un tipo de documento y un aprobador
	 * @return List<SpaSolicitudes> Solicitudes encontradas con un estatus, tipo de documento dado y un aprobador.
	 */
    public List<SpaSolicitudes> getSpaSolicitudesByEstatusAndTipoDocumentoAndApprover(String codigoDocumento, String codEstatus, long personIdAprueba) {
        return em.createNamedQuery("SpaTipoDesecho.ByEstatusAndTipoDocumento", SpaSolicitudes.class).
        		setParameter("codigoDocumento", codigoDocumento).setParameter("codEstatus", codEstatus).
        		setParameter("personIdAprueba", personIdAprueba).getResultList();
    }
    
    
	/**
	 * Metodo que busca el ultimo historico por un estatus y el person id de la persona que lo elaboró
	 * @return List<SpaHistoricoDocumento> Ultimo historico encontrado con el estatus y el id de la persona dado.
	 */
    public List<SpaHistoricoDocumento> getLastSpaHistoricoDocumentoByEstatusAndIdElabora(String codEstatus, int personIdElabora) {
        return em.createNamedQuery("SpaHistoricoDocumento.FindLastHistoricalByEstatusAndIdElabora", SpaHistoricoDocumento.class)
        		.setParameter("codEstatus", codEstatus).
        		setParameter("personIdElabora", personIdElabora).getResultList();
    }
    
	/**
	 * Metodo que busca el ultimo historico por un estatus y el person id de la persona que lo aprueba
	 * @return List<SpaHistoricoDocumento> Ultimo historico encontrado con el estatus y el id de la persona que lo aproeba.
	 */
    public List<SpaHistoricoDocumento> getLastSpaHistoricoDocumentoByEstatusAndIdAprueba(String codEstatus, int personIdAprueba) {
        return em.createNamedQuery("SpaHistoricoDocumento.FindLastHistoricalByEstatusAndIdAprueba", SpaHistoricoDocumento.class)
        		.setParameter("codEstatus", codEstatus).
        		setParameter("personIdAprueba", personIdAprueba).getResultList();
    }
    
	/**
	 * Metodo que busca el ultimo historico por un estatus, filtra y busca solo los estatus aprobados, ejecutados y finalizados si no se proporciona un 
	 * codigo de estatus
	 * @return List<SpaHistoricoDocumento> Ultimo historico encontrado con el estatus.
	 */
    public List<SpaHistoricoDocumento> getLastSpaHistoricoDocumentoAprovedAndExecuted(String codEstatus) {
        return em.createNamedQuery("SpaHistoricoDocumento.FindLastHistoricalAprovedAndExecuted", SpaHistoricoDocumento.class)
        		.setParameter("codEstatus", codEstatus).getResultList();
    }  
    
	/**
	 * Metodo que busca el ultimo historico de una solicitud dada
	 * @return SpaHistoricoDocumento Ultimo historico encontrado de la solicitud dada.
	 */
    public SpaHistoricoDocumento getLastSpaHistoricoDocumento(SpaSolicitudes spaSolicitud) {
        return em.createNamedQuery("SpaHistoricoDocumento.FindLastHistorical", SpaHistoricoDocumento.class)
        		.setParameter("idSolicitud", spaSolicitud.getIdSolicitud()).getSingleResult();
    }        
    
	/**
	 * Metodo que busca el ultimo historico de una solicitud dado una solicitud y un estatus
	 * @return SpaHistoricoDocumento Ultimo historico encontrado de la solicitud y estatus dado.
	 */
    public SpaHistoricoDocumento getLastSpaHistoricoDocumentoByEstatusAndIdSolicitud(String codEstatus, long idSolicitud) {
    	List<SpaHistoricoDocumento> spaHistoricoDocumentos = em.createNamedQuery("SpaHistoricoDocumento.FindLastHistoricalByEstatusAndIdSolicitud", SpaHistoricoDocumento.class)
					.setParameter("codEstatus", codEstatus).
					setParameter("idSolicitud", idSolicitud).getResultList();
		if (spaHistoricoDocumentos != null && spaHistoricoDocumentos.size() == 1) {
			return spaHistoricoDocumentos.get(0);
		} else if (spaHistoricoDocumentos == null || spaHistoricoDocumentos.isEmpty()) {
			return new SpaHistoricoDocumento();
		} else {
			return new SpaHistoricoDocumento();
		}
    } 
    
	/**
	 * Metodo que busca todos los ultimos historicos de todos los documentos
	 * @return List<SpaHistoricoDocumento> Lista con todos los ultimos historicos de todos los documentos.
	 */
    public List<SpaHistoricoDocumento> getAllLastSpaHistoricoDocumento() {

    	List<SpaHistoricoDocumento> spaHistoricoDocumentos = em.createNamedQuery("SpaHistoricoDocumento.FindAllLastHistorical", SpaHistoricoDocumento.class)
					.getResultList();
		if (spaHistoricoDocumentos.size() > 0) {
			return spaHistoricoDocumentos;
		} else if (spaHistoricoDocumentos == null || spaHistoricoDocumentos.isEmpty()) {
			return spaHistoricoDocumentos;
		}
		return spaHistoricoDocumentos; 
    }     
    
	/**
	 * Metodo que busca todos los estatus activos en el sistema
	 * @return List<SpaEstatus>  Lista con todos los estatus activos en el sistema.
	 */
    public List<SpaEstatus> getAllSpaEstatusActives() {
        return em.createNamedQuery("SpaEstatus.findActiveAll", SpaEstatus.class).getResultList();
    }   
    
    
	/**
	 * Metodo que guarda un archivo en la base de datos por usando un query nativo
	 * @param String imageName Nombre del archivo
	 * @param String contentTipe Tipo de contenido del archivo
	 * @param byte[] fileBytes Un arreglo de bytes que forma el archivo a guardar.
	 * @return BigDecimal Retorna el Id del registro guardado en la base de datos.
	 */
    public BigDecimal saveFile(String imageName, String contentTipe, byte[] fileBytes) throws Exception {
    	try {
    		BigDecimal secuenciaFile;
    		if(fileBytes != null) {
    			FileOutputStream fileOuputStream =  new FileOutputStream(spaAttachmentDirectory.getProperty("url")+"\\"+imageName); 
    			fileOuputStream.write(fileBytes);
    			fileOuputStream.close();
    			secuenciaFile = (BigDecimal) em.createNamedQuery("nextSecuenceImage").getSingleResult();								
    			em.createNamedQuery("crearRegistroImagen").
    			setParameter(1, spaAttachmentDirectory.getProperty("directory")).
    			setParameter(2, imageName).
    			setParameter(3, secuenciaFile).
    			setParameter(4, imageName).
    			setParameter(5, contentTipe).executeUpdate();
    			return secuenciaFile;
    		}		else {
    			return new BigDecimal(0);
    		}
    	} catch (Exception e) {
    		return null;
    	}			
    }    
	
	/**
	 * Metodo que consulta y recupera un archivo desde la base de datos
	 * @param long idImagen Identificador del archivo o imagen guardada en base de datos.
	 * @return BFILE Objeto con el archivo consultado.
	 */
	public BFILE recuperateImage(long idImagen) throws Exception {
		BFILE bfile = null;
		try {
			bfile = (BFILE) em.createNamedQuery("recuperarImagen").setParameter(1, idImagen).getSingleResult();								
			return bfile;
		} catch (Exception e) {
			return null;
		}

	}	
    

}
