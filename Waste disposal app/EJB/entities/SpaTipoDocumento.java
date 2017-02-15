package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SPA_TIPO_DOCUMENTO database table.
 * 
 */
@Entity
@Table(name="SPA_TIPO_DOCUMENTO")
@NamedQueries({
	@NamedQuery(name="SpaTipoDocumento.findAll", query="select t from SpaTipoDocumento t "),
	@NamedQuery(name="SpaTipoDocumento.findByCodigo", query="select t from SpaTipoDocumento t where t.activo = 'A' and t.codigo = :codigo"),
@NamedQuery(name="SpaTipoDocumento.findActiveAll", query="select t from SpaTipoDocumento t where t.activo = 'A'")})
public class SpaTipoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_TIPO_DOCUMENTO_IDTIPODOCUMENTO_GENERATOR", sequenceName="SPA_ID_TIPO_DOCUMENTO_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_TIPO_DOCUMENTO_IDTIPODOCUMENTO_GENERATOR")
	@Column(name="ID_TIPO_DOCUMENTO")
	private long idTipoDocumento;

	private String abreviacion;

	private String activo;

	private String codigo;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	private String descripcion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	//bi-directional many-to-one association to SpaSolicitude
	@OneToMany(mappedBy="spaTipoDocumento")
	private List<SpaSolicitudes> spaSolicitudes;

	public SpaTipoDocumento() {
	}

	public long getIdTipoDocumento() {
		return this.idTipoDocumento;
	}

	public void setIdTipoDocumento(long idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getAbreviacion() {
		return this.abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public List<SpaSolicitudes> getSpaSolicitudes() {
		return this.spaSolicitudes;
	}

	public void setSpaSolicitudes(List<SpaSolicitudes> spaSolicitudes) {
		this.spaSolicitudes = spaSolicitudes;
	}

}