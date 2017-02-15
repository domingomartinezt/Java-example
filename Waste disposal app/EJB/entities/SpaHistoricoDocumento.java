package ve.com.toyota.spa.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;




/**
 * The persistent class for the SPA_HISTORICO_DOCUMENTO database table.
 * 
 */
@Entity
@Cacheable(false)
@Table(name="SPA_HISTORICO_DOCUMENTO")
@NamedQueries({ 
	@NamedQuery(name="SpaHistoricoDocumento.FindAllLastHistorical", query="select h from SpaSolicitudes s, SpaHistoricoDocumento h where " +
			"s.idSolicitud = h.spaSolicitude.idSolicitud and "+
			"h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = s.idSolicitud) order by s.idSolicitud desc"),	
	@NamedQuery(name="SpaHistoricoDocumento.FindLastHistoricalByEstatusAndIdAprueba", query="select h from SpaSolicitudes s, SpaHistoricoDocumento h where " +
			"s.idSolicitud = h.spaSolicitude.idSolicitud and s.personIdAprueba = :personIdAprueba and " +
			"(h.spaEstatus.codEstatus = :codEstatus or :codEstatus is null) and "+
			"h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = s.idSolicitud) order by s.idSolicitud desc"),
	@NamedQuery(name="SpaHistoricoDocumento.FindLastHistoricalByEstatusAndIdElabora", query="select h from SpaSolicitudes s, SpaHistoricoDocumento h where " +
			"s.idSolicitud = h.spaSolicitude.idSolicitud and s.personIdElabora = :personIdElabora and " +
			"(h.spaEstatus.codEstatus = :codEstatus or :codEstatus is null) and "+
			"h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = s.idSolicitud) order by s.idSolicitud desc"),
	@NamedQuery(name="SpaHistoricoDocumento.FindLastHistoricalByEstatusAndIdSolicitud", query="select h from SpaHistoricoDocumento h where " +
			"h.spaSolicitude.idSolicitud = :idSolicitud and h.spaEstatus.codEstatus = :codEstatus and "
			+ "h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = :idSolicitud and "
			+ "hs.spaEstatus.codEstatus = :codEstatus)"),
	@NamedQuery(name="SpaHistoricoDocumento.FindLastHistorical", query="select h from SpaHistoricoDocumento h where " +
			"h.spaSolicitude.idSolicitud = :idSolicitud and "
			+ "h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = :idSolicitud) order by h.idHistoricoDocumento desc"),			
	@NamedQuery(name="SpaHistoricoDocumento.FindLastHistoricalAprovedAndExecuted", query="select h from SpaSolicitudes s, SpaHistoricoDocumento h where " +
			"s.idSolicitud = h.spaSolicitude.idSolicitud and h.spaEstatus.codEstatus in ('APR','PRO','EJC') and (h.spaEstatus.codEstatus = :codEstatus or :codEstatus is null) and "+
			"h.creationDate = (select max(hs.creationDate) from SpaHistoricoDocumento hs where hs.spaSolicitude.idSolicitud = s.idSolicitud) order by h.idHistoricoDocumento desc")})
public class SpaHistoricoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_HISTORICO_DOCUMENTO_IDHISTORICODOCUMENTO_GENERATOR", sequenceName="SPA_ID_HIST_DOCUMENTO_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_HISTORICO_DOCUMENTO_IDHISTORICODOCUMENTO_GENERATOR")
	@Column(name="ID_HISTORICO_DOCUMENTO")
	private long idHistoricoDocumento;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="OBSERVACIONES")	
	private String observaciones;

	//bi-directional many-to-one association to SpaEstatus
	@ManyToOne
	@JoinColumn(name="ID_ESTATUS")
	private SpaEstatus spaEstatus;

	//bi-directional many-to-one association to SpaSolicitude
	@ManyToOne
	@JoinColumn(name="ID_SOLICITUD")
	private SpaSolicitudes spaSolicitude;

	public SpaHistoricoDocumento() {
	}

	public long getIdHistoricoDocumento() {
		return this.idHistoricoDocumento;
	}

	public void setIdHistoricoDocumento(long idHistoricoDocumento) {
		this.idHistoricoDocumento = idHistoricoDocumento;
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

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public SpaEstatus getSpaEstatus() {
		return this.spaEstatus;
	}

	public void setSpaEstatus(SpaEstatus spaEstatus) {
		this.spaEstatus = spaEstatus;
	}

	public SpaSolicitudes getSpaSolicitude() {
		return this.spaSolicitude;
	}

	public void setSpaSolicitude(SpaSolicitudes spaSolicitude) {
		this.spaSolicitude = spaSolicitude;
	}

}