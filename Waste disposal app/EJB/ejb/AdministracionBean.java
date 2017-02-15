package ve.com.toyota.spa.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ve.com.toyota.spa.entities.SpaCategoria;
import ve.com.toyota.spa.entities.SpaClasificacion;
import ve.com.toyota.spa.entities.SpaDesecho;
import ve.com.toyota.spa.entities.SpaEstatus;
import ve.com.toyota.spa.entities.SpaTipoDesecho;
import ve.com.toyota.spa.entities.SpaTipoDocumento;
import ve.com.toyota.spa.entities.SpaTipoTratamiento;
import ve.com.toyota.spa.entities.SpaUnidadMedida;

/**
 * Clase que contiene operaciones de administración del sistema, consultas, actualizaciones e inserciones en base de datos.
 * @author dmartinez
 * @since SPA 
 * @see Stateless
 * @see TransactionManagement
 */
@Stateless(name = "AdministracionBean")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AdministracionBean
{

	/**
	 * Objeto que gestiona las operaciones de almacenamiento de las entidades de base datos.
	 */
    @PersistenceContext(unitName="spaJPA")
    private EntityManager em;


	/**
	 * Metodo que guarda en base de datos una nueva categoria, si existe lanza una exepcion.
	 * @param SpaCategoria
	 */
    public SpaCategoria persistSpaCategoria(SpaCategoria spaCategoria) {
        em.persist(spaCategoria);
        return spaCategoria;
    }

	/**
	 * Metodo que actualiza en base de datos una categoria.
	 * @param SpaCategoria
	 */
    public SpaCategoria mergeSpaCategoria(SpaCategoria spaCategoria) {
        return em.merge(spaCategoria);
    }

	/**
	 * Metodo que elimina una categoria de base de datos.
	 * @param SpaCategoria
	 */
    public void removeSpaCategoria(SpaCategoria spaCategoria) {
        spaCategoria = em.find(SpaCategoria.class, spaCategoria.getIdCategoria());
        em.remove(spaCategoria);
    }

	/**
	 * Metodo que genera la lista completa de las categorias guardadas en base de datos.
	 * @return List<SpaCategoria> Lista de categorias completa.
	 */
    public List<SpaCategoria> getSpaCategoriaFindAll() {
        return em.createNamedQuery("SpaCategoria.findAll", SpaCategoria.class).getResultList();
    }

	/**
	 * Metodo que guarda en base una nueva clasificacion, si existe lanza una exepcion.
	 * @param SpaClasificacion
	 */
    public SpaClasificacion persistSpaClasificacion(SpaClasificacion spaClasificacion) {
        em.persist(spaClasificacion);
        return spaClasificacion;
    }

	/**
	 * Metodo que actualiza en base de datos una clasificacion.
	 * @param SpaClasificacion
	 */
    public SpaClasificacion mergeSpaClasificacion(SpaClasificacion spaClasificacion) {
        return em.merge(spaClasificacion);
    }

	/**
	 * Metodo que elimina una clasificacion de base de datos.
	 * @param SpaClasificacion
	 */
    public void removeSpaClasificacion(SpaClasificacion spaClasificacion) {
        spaClasificacion = em.find(SpaClasificacion.class, spaClasificacion.getIdClasificacion());
        em.remove(spaClasificacion);
    }

	/**
	 * Metodo que genera la lista completa de las clasificaciones guardadas en base de datos.
	 * @return List<SpaClasificacion> Lista de clasificaciones completa.
	 */
    public List<SpaClasificacion> getSpaClasificacionFindAll() {
        return em.createNamedQuery("SpaClasificacion.findAll", SpaClasificacion.class).getResultList();
    }

	/**
	 * Metodo que guarda en base una nuevo desecho, si existe lanza una exepcion.
	 * @param SpaDesecho
	 */
    public SpaDesecho persistSpaDesecho(SpaDesecho spaDesecho) {
        em.persist(spaDesecho);
        return spaDesecho;
    }

	/**
	 * Metodo que actualiza en base de datos un desecho.
	 * @param SpaDesecho
	 */
    public SpaDesecho mergeSpaDesecho(SpaDesecho spaDesecho) {
        return em.merge(spaDesecho);
    }

	/**
	 * Metodo que elimina un desecho de base de datos.
	 * @param SpaDesecho
	 */
    public void removeSpaDesecho(SpaDesecho spaDesecho) {
        spaDesecho = em.find(SpaDesecho.class, spaDesecho.getIdDesecho());
        em.remove(spaDesecho);
    }

	/**
	 * Metodo que genera la lista completa de los desechos guardados en base de datos.
	 * @return List<SpaDesecho> Lista de desechos completa.
	 */
    public List<SpaDesecho> getSpaDesechoFindAll() {
        return em.createNamedQuery("SpaDesecho.findAll", SpaDesecho.class).getResultList();
    }

	/**
	 * Metodo que guarda en base una nuevo estatus, si existe lanza una exepcion.
	 * @param SpaEstatus
	 */
    public SpaEstatus persistSpaEstatus(SpaEstatus spaEstatus) {
        em.persist(spaEstatus);
        return spaEstatus;
    }

	/**
	 * Metodo que actualiza en base de datos un estatus.
	 * @param SpaEstatus
	 */
    public SpaEstatus mergeSpaEstatus(SpaEstatus spaEstatus) {
        return em.merge(spaEstatus);
    }

	/**
	 * Metodo que elimina un estatus de base de datos.
	 * @param SpaEstatus
	 */
    public void removeSpaEstatus(SpaEstatus spaEstatus) {
        spaEstatus = em.find(SpaEstatus.class, spaEstatus.getIdEstatus());
        em.remove(spaEstatus);
    }

	/**
	 * Metodo que genera la lista completa de los estatus guardados en base de datos.
	 * @return List<SpaEstatus> Lista de desechos completa.
	 */
    public List<SpaEstatus> getSpaEstatusFindAll() {
        return em.createNamedQuery("SpaEstatus.findAll", SpaEstatus.class).getResultList();
    }
    
	/**
	 * Metodo que guarda en base una nuevo tipo de desecho, si existe lanza una exepcion.
	 * @param SpaTipoDesecho
	 */
    public SpaTipoDesecho persistSpaTipoDesecho(SpaTipoDesecho spaTipoDesecho) {
        em.persist(spaTipoDesecho);
        return spaTipoDesecho;
    }

	/**
	 * Metodo que actualiza en base de datos un tipo de desecho.
	 * @param SpaTipoDesecho
	 */
    public SpaTipoDesecho mergeSpaTipoDesecho(SpaTipoDesecho spaTipoDesecho) {
        return em.merge(spaTipoDesecho);
    }

	/**
	 * Metodo que elimina un tipo de desecho de base de datos.
	 * @param SpaTipoDesecho
	 */
    public void removeSpaTipoDesecho(SpaTipoDesecho spaTipoDesecho) {
        spaTipoDesecho = em.find(SpaTipoDesecho.class, spaTipoDesecho.getIdTipoDesecho());
        em.remove(spaTipoDesecho);
    }

	/**
	 * Metodo que genera la lista completa de los tipos de desechos guardados en base de datos.
	 * @return List<SpaTipoDesecho> Lista de desechos completa.
	 */
    public List<SpaTipoDesecho> getSpaTipoDesechoFindAll() {
        return em.createNamedQuery("SpaTipoDesecho.findAll", SpaTipoDesecho.class).getResultList();
    }

	/**
	 * Metodo que guarda en base una nuevo tipo de documento, si existe lanza una exepcion.
	 * @param SpaTipoDocumento
	 */
    public SpaTipoDocumento persistSpaTipoDocumento(SpaTipoDocumento spaTipoDocumento) {
        em.persist(spaTipoDocumento);
        return spaTipoDocumento;
    }

	/**
	 * Metodo que actualiza en base de datos un tipo de documento.
	 * @param SpaTipoDocumento
	 */
    public SpaTipoDocumento mergeSpaTipoDocumento(SpaTipoDocumento spaTipoDocumento) {
        return em.merge(spaTipoDocumento);
    }

	/**
	 * Metodo que elimina un tipo de documento de base de datos.
	 * @param SpaTipoDocumento
	 */
    public void removeSpaTipoDocumento(SpaTipoDocumento spaTipoDocumento) {
        spaTipoDocumento = em.find(SpaTipoDocumento.class, spaTipoDocumento.getIdTipoDocumento());
        em.remove(spaTipoDocumento);
    }

	/**
	 * Metodo que genera la lista completa de los tipos de documentos guardados en base de datos.
	 * @return List<SpaTipoDocumento> Lista de desechos completa.
	 */
    public List<SpaTipoDocumento> getSpaTipoDocumentoFindAll() {
        return em.createNamedQuery("SpaTipoDocumento.findAll", SpaTipoDocumento.class).getResultList();
    }

	/**
	 * Metodo que guarda en base una nuevo tipo de tratamiento, si existe lanza una exepcion.
	 * @param SSpaTipoTratamiento
	 */
    public SpaTipoTratamiento persistSpaTipoTratamiento(SpaTipoTratamiento spaTipoTratamiento) {
        em.persist(spaTipoTratamiento);
        return spaTipoTratamiento;
    }

	/**
	 * Metodo que actualiza en base de datos un tipo de tratamiento.
	 * @param SpaTipoTratamiento
	 */
    public SpaTipoTratamiento mergeSpaTipoTratamiento(SpaTipoTratamiento spaTipoTratamiento) {
        return em.merge(spaTipoTratamiento);
    }

	/**
	 * Metodo que elimina un tipo de tratamiento de base de datos.
	 * @param SpaTipoTratamiento
	 */
    public void removeSpaTipoTratamiento(SpaTipoTratamiento spaTipoTratamiento) {
        spaTipoTratamiento = em.find(SpaTipoTratamiento.class, spaTipoTratamiento.getIdTratamiento());
        em.remove(spaTipoTratamiento);
    }

	/**
	 * Metodo que genera la lista completa de los tipos de tratamiento guardados en base de datos.
	 * @return List<SpaTipoTratamiento> Lista de desechos completa.
	 */
    public List<SpaTipoTratamiento> getSpaTipoTratamientoFindAll() {
        return em.createNamedQuery("SpaTipoTratamiento.findAll", SpaTipoTratamiento.class).getResultList();
    }

	/**
	 * Metodo que guarda en base una nueva unidad de medida, si existe lanza una exepcion.
	 * @param SpaUnidadMedida
	 */
    public SpaUnidadMedida persistSpaUnidadMedida(SpaUnidadMedida spaUnidadMedida) {
        em.persist(spaUnidadMedida);
        return spaUnidadMedida;
    }

	/**
	 * Metodo que actualiza en base de datos uns unidad de medida.
	 * @param SpaUnidadMedida
	 */
    public SpaUnidadMedida mergeSpaUnidadMedida(SpaUnidadMedida spaUnidadMedida) {
        return em.merge(spaUnidadMedida);
    }

	/**
	 * Metodo que elimina una unidad de medida de base de datos.
	 * @param SpaUnidadMedida
	 */
    public void removeSpaUnidadMedida(SpaUnidadMedida spaUnidadMedida) {
        spaUnidadMedida = em.find(SpaUnidadMedida.class, spaUnidadMedida.getIdUnidadMedida());
        em.remove(spaUnidadMedida);
    }

	/**
	 * Metodo que genera la lista completa de las unidades de medida guardadas en base de datos.
	 * @return List<SpaUnidadMedida> Lista de desechos completa.
	 */
    public List<SpaUnidadMedida> getSpaUnidadMedidaFindAll() {
        return em.createNamedQuery("SpaUnidadMedida.findAll", SpaUnidadMedida.class).getResultList();
    }
    
	/**
	 * Metodo que busca un tipo de desecho por id en la base de datos.
	 * @return SpaTipoDesecho tipo desecho encontrado.
	 */
    public SpaTipoDesecho getSpaTipoDesechoFindById(Long idTipoDesecho) {
    	return em.find(SpaTipoDesecho.class, idTipoDesecho);
    }
    
    
	/**
	 * Metodo que busca un tipo de tratamiento por id en la base de datos.
	 * @return SpaTipoTratamiento tipo de tratamiento encontrado.
	 */
    public SpaTipoTratamiento getSpaTipoTratamientoFindById(Long idTipoTratamiento) {
    	return em.find(SpaTipoTratamiento.class, idTipoTratamiento);
    }    
    
	/**
	 * Metodo que busca una categoria por id en la base de datos.
	 * @return SpaCategoria categoria encontrada.
	 */
    public SpaCategoria getSpaCategoriaFindById(Long idCategoria) {
    	return em.find(SpaCategoria.class, idCategoria);
    }
       
    
	/**
	 * Metodo que busca una clasificacion por id en la base de datos.
	 * @return SpaClasificacion clasificacion encontrada.
	 */
    public SpaClasificacion getSpaClasificacionFindById(Long idClasificacion) {
    	return em.find(SpaClasificacion.class, idClasificacion);
    }
     
    
	/**
	 * Metodo que busca una unidad de medida por id en la base de datos.
	 * @return SpaUnidadMedida unidad de medida encontrada.
	 */
    public SpaUnidadMedida getSpaUnidadMedidaFindById(Long idUnidadMedida) {
    	return em.find(SpaUnidadMedida.class, idUnidadMedida);
    }    
    
	/**
	 * Metodo que genera la lista de las unidades de medida activas guardadas en base de datos.
	 * @return List<SpaUnidadMedida> Lista de unidades de medida activas.
	 */
    public List<SpaUnidadMedida> getSpaUnidadMedidaFindActiveAll() {
        return em.createNamedQuery("SpaUnidadMedida.FindActiveAll", SpaUnidadMedida.class).getResultList();
    }
    
	/**
	 * Metodo que busca la unica unidad de medida cuyo uso es en toda la aplicacion.
	 * @return SpaUnidadMedida Unidad  de medida activa y su uso es en toda la aplicacion.
	 */
    public SpaUnidadMedida getSpaUnidadMedidaFindAplicatioScope() {
        return em.createNamedQuery("SpaUnidadMedida.findAplicatioScope", SpaUnidadMedida.class).getSingleResult();
    } 
    
	/**
	 * Metodo que busca una unidad de medida por su abreviacion.
	 * @return SpaUnidadMedida unidades de medida encontrada.
	 */
    public SpaUnidadMedida getSpaUnidadMedidaFindAbreviacion(String abreviacion) {
        return em.createNamedQuery("SpaUnidadMedida.findAbreviacion", SpaUnidadMedida.class).setParameter("abreviacion", abreviacion).getSingleResult();
    }     
    
	/**
	 * Metodo que genera la lista de las unidades de medida activas y cuyo uso es a nivel de las solicitudes.
	 * @return List<SpaUnidadMedida> Lista de unidades de medida activas y su uso es en las solicitudes.
	 */
    public List<SpaUnidadMedida> getSpaUnidadMedidaFindSolicitudScope() {
        return em.createNamedQuery("SpaUnidadMedida.findSolicitudScope", SpaUnidadMedida.class).getResultList();
    }    
    
	/**
	 * Metodo que genera la lista de los tipos de tratamiento activos guardados en base de datos.
	 * @return List<SpaTipoTratamiento> Lista de tratmientos activos.
	 */
    public List<SpaTipoTratamiento> getSpaTipoTratamientoFindActiveAll() {
        return em.createNamedQuery("SpaTipoTratamiento.FindActiveAll", SpaTipoTratamiento.class).getResultList();
    }
    
	/**
	 * Metodo que genera la lista de los tipos de documentos activos guardados en base de datos.
	 * @return List<SpaTipoDocumento> Lista de documentos activos.
	 */
    public List<SpaTipoDocumento> getSpaTipoDocumentoFindActiveAll() {
        return em.createNamedQuery("SpaTipoDocumento.FindActiveAll", SpaTipoDocumento.class).getResultList();
    }    
    
	/**
	 * Metodo que genera la lista de los tipos de desechos activos guardados en base de datos.
	 * @return List<SpaTipoDesecho> Lista de tipos de desechos activos.
	 */
    public List<SpaTipoDesecho> getSpaTipoDesechoFindActiveAll() {
        return em.createNamedQuery("SpaTipoDesecho.FindActiveAll", SpaTipoDesecho.class).getResultList();
    } 
    
	/**
	 * Metodo que genera la lista de los desechos activos guardados en base de datos.
	 * @return List<SpaDesecho> Lista de desechos activos.
	 */
    public List<SpaDesecho> getSpaDesechoFindActiveAll() {
        return em.createNamedQuery("SpaDesecho.FindActiveAll", SpaDesecho.class).getResultList();
    }
    
	/**
	 * Metodo que busca un desecho por id en la base de datos.
	 * @return SpaDesecho descho encontrado.
	 */
    public SpaDesecho getSpaDesechoFindById(Long idDesecho) {
    	return em.find(SpaDesecho.class, idDesecho);
    }    
    
	/**
	 * Metodo que genera la lista de las clasificaciones activas guardadas en base de datos.
	 * @return List<SpaClasificacion> Lista de clasificaciones activas.
	 */
    public List<SpaClasificacion> getSpaClasificacionFindActiveAll() {
        return em.createNamedQuery("SpaClasificacion.findActiveAll", SpaClasificacion.class).getResultList();
    }
    
	/**
	 * Metodo que genera la lista de las categorias activas guardadas en base de datos.
	 * @return List<SpaCategoria> Lista de categorias activas.
	 */
    public List<SpaCategoria> getSpaCategoriaFindActiveAll() {
        return em.createNamedQuery("SpaCategoria.findActiveAll", SpaCategoria.class).getResultList();
    }    
    
	/**
	 * Metodo que genera la lista de los estatus activos guardados en base de datos.
	 * @return List<SpaEstatus> Lista de estatus activos.
	 */
    public List<SpaEstatus> getSpaEstatusFindActiveAll() {
        return em.createNamedQuery("SpaEstatus.findActiveAll", SpaEstatus.class).getResultList();
    }    
    
}
