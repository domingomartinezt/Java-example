package ve.com.toyota.spa.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SPA_ESTATUS database table.
 * 
 */
@Entity
@Table(name="SPA_ESTATUS")
@NamedQueries({
	@NamedQuery(name="SpaEstatus.findAll", query="select e from SpaEstatus e "),
	@NamedQuery(name="SpaEstatus.findByCodigo", query="select e from SpaEstatus e where e.activo = 'A' and e.dominio = 'DOCUMENTOS.SOLICITUDES' and e.codEstatus = :codigo"),
	@NamedQuery(name="SpaEstatus.findActiveAll", query="select e from SpaEstatus e where e.activo = 'A'")})
public class SpaEstatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_ESTATUS_IDESTATUS_GENERATOR", sequenceName="SPA_ID_ESTATUS_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_ESTATUS_IDESTATUS_GENERATOR")
	@Column(name="ID_ESTATUS")
	private long idEstatus;

	private String activo;

	@Column(name="COD_ESTATUS")
	private String codEstatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	private String descripcion;

	private String dominio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	//bi-directional many-to-one association to SpaHistoricoDocumento
	@OneToMany(mappedBy="spaEstatus")
	private List<SpaHistoricoDocumento> spaHistoricoDocumentos;

	public SpaEstatus() {
	}

	public long getIdEstatus() {
		return this.idEstatus;
	}

	public void setIdEstatus(long idEstatus) {
		this.idEstatus = idEstatus;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getCodEstatus() {
		return this.codEstatus;
	}

	public void setCodEstatus(String codEstatus) {
		this.codEstatus = codEstatus;
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

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDominio() {
		return this.dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
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

	public List<SpaHistoricoDocumento> getSpaHistoricoDocumentos() {
		return this.spaHistoricoDocumentos;
	}

	public void setSpaHistoricoDocumentos(List<SpaHistoricoDocumento> spaHistoricoDocumentos) {
		this.spaHistoricoDocumentos = spaHistoricoDocumentos;
	}

}