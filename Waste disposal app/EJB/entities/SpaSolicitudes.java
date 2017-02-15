package ve.com.toyota.spa.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;


/**
 * The persistent class for the SPA_SOLICITUDES database table.
 * 
 */
@Entity
@Table(name="SPA_SOLICITUDES")
@NamedQueries({ 
	@NamedQuery(name="SpaSpaSolicitudes.ByIdSolicitud", query="select s from SpaSolicitudes s where s.idSolicitud = :idSolicitud "),
	@NamedQuery(name="SpaSpaSolicitudes.ByEstatusAndTipoDocumento", query="select s from SpaSolicitudes s, SpaHistoricoDocumento h where " +
			"s.idSolicitud = h.spaSolicitude.idSolicitud and s.spaTipoDocumento.codigo = :codigoDocumento and h.creationDate = " +
			"(select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = s.idSolicitud and " +
			"hs.spaEstatus.codEstatus = :codEstatus)"),
	@NamedQuery(name="SpaSpaSolicitudes.ByEstatusAndTipoDocumentoAndApprover", query="select s from SpaSolicitudes s, SpaHistoricoDocumento h where " +
			"s.idSolicitud = h.spaSolicitude.idSolicitud and s.spaTipoDocumento.codigo = :codigoDocumento and s.personIdAprueba = :personIdAprueba " +
			"and h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = s.idSolicitud and " +
			"hs.spaEstatus.codEstatus = :codEstatus)"),
	@NamedQuery(name="SpaSpaSolicitudes.ByEstatusAndIdElabora", query="select s from SpaSolicitudes s, SpaHistoricoDocumento h where " +
			"s.idSolicitud = h.spaSolicitude.idSolicitud and s.personIdElabora = :personIdElabora " +
			"and h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = s.idSolicitud and " +
			"(hs.spaEstatus.codEstatus = :codEstatus or hs.spaEstatus.codEstatus is null))"),			
	@NamedQuery(name="SpaSpaSolicitudes.ByIdElabora", query="select s from SpaSolicitudes s where s.personIdElabora = :personIdElabora")})
public class SpaSolicitudes implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_SOLICITUDES_IDSOLICITUD_GENERATOR", sequenceName="SPA_SOLICITUD_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_SOLICITUDES_IDSOLICITUD_GENERATOR")
	@Column(name="ID_SOLICITUD")
	private long idSolicitud;

	@Column(name="CENTRO_COSTO")
	private String centroCosto;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="IND_REQ_APROBACION_JUNTA")
	private String indReqAprobacionJunta;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="MOTIVO")
	private String motivo;

	@Column(name="PERSON_ID_APRUEBA")
	private long personIdAprueba;

	@Column(name="PERSON_ID_ELABORA")
	private long personIdElabora;

	//bi-directional many-to-one association to SpaDesechoSolicitud
	@OneToMany(mappedBy="spaSolicitude")
	private List<SpaDesechoSolicitud> spaDesechoSolicituds;

	//bi-directional many-to-one association to SpaHistoricoDocumento
	@OneToMany(mappedBy="spaSolicitude", cascade = PERSIST)
	private List<SpaHistoricoDocumento> spaHistoricoDocumentos;

	//bi-directional many-to-one association to SpaItemDesincorporar
	@OneToMany(mappedBy="spaSolicitude", cascade = PERSIST)
	private List<SpaItemDesincorporar> spaItemDesincorporar;

	//bi-directional many-to-one association to SpaAprobacionJunta
	@OneToOne(cascade = PERSIST)
	@JoinColumn(name="ID_APROBACION_JUNTA")
	private SpaAprobacionJunta spaAprobacionJunta;

	//bi-directional many-to-one association to SpaTipoDocumento
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DOCUMENTO")
	private SpaTipoDocumento spaTipoDocumento;

	public SpaSolicitudes() {
	}

	public long getIdSolicitud() {
		return this.idSolicitud;
	}

	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getCentroCosto() {
		return this.centroCosto;
	}

	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getIndReqAprobacionJunta() {
		return this.indReqAprobacionJunta;
	}

	public void setIndReqAprobacionJunta(String indReqAprobacionJunta) {
		this.indReqAprobacionJunta = indReqAprobacionJunta;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateLogin() {
		return this.lastUpdateLogin;
	}

	public void setLastUpdateLogin(String lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public long getPersonIdAprueba() {
		return this.personIdAprueba;
	}

	public void setPersonIdAprueba(long personIdAprueba) {
		this.personIdAprueba = personIdAprueba;
	}

	public long getPersonIdElabora() {
		return this.personIdElabora;
	}

	public void setPersonIdElabora(long personIdElabora) {
		this.personIdElabora = personIdElabora;
	}

	public List<SpaDesechoSolicitud> getSpaDesechoSolicituds() {
		return this.spaDesechoSolicituds;
	}

	public void setSpaDesechoSolicituds(List<SpaDesechoSolicitud> spaDesechoSolicituds) {
		this.spaDesechoSolicituds = spaDesechoSolicituds;
	}

	public List<SpaHistoricoDocumento> getSpaHistoricoDocumentos() {
		return this.spaHistoricoDocumentos;
	}

	public void setSpaHistoricoDocumentos(List<SpaHistoricoDocumento> spaHistoricoDocumentos) {
		this.spaHistoricoDocumentos = spaHistoricoDocumentos;
	}

	public List<SpaItemDesincorporar> getSpaItemDesincorporar() {
		return this.spaItemDesincorporar;
	}

	public void setSpaItemDesincorporar(List<SpaItemDesincorporar> spaItemDesincorporar) {
		this.spaItemDesincorporar = spaItemDesincorporar;
	}

	public SpaAprobacionJunta getSpaAprobacionJunta() {
		return this.spaAprobacionJunta;
	}

	public void setSpaAprobacionJunta(SpaAprobacionJunta spaAprobacionJunta) {
		this.spaAprobacionJunta = spaAprobacionJunta;
	}

	public SpaTipoDocumento getSpaTipoDocumento() {
		return this.spaTipoDocumento;
	}

	public void setSpaTipoDocumento(SpaTipoDocumento spaTipoDocumento) {
		this.spaTipoDocumento = spaTipoDocumento;
	}

}