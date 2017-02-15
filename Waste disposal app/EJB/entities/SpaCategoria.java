package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SPA_CATEGORIA database table.
 * 
 */
@Entity
@Table(name="SPA_CATEGORIA")
@NamedQueries({
	@NamedQuery(name="SpaCategoria.findAll", query="select c from SpaCategoria c "),
	@NamedQuery(name="SpaCategoria.findActiveAll", query="select c from SpaCategoria c where c.activo = 'A'")})
public class SpaCategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_CATEGORIA_IDCATEGORIA_GENERATOR", sequenceName="SPA_ID_CATEGORIA_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_CATEGORIA_IDCATEGORIA_GENERATOR")
	@Column(name="ID_CATEGORIA")
	private long idCategoria;

	private String activo;

	@Column(name="COD_CATEGORIA")
	private long codCategoria;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="DESCRIPCION_ESPANOL")
	private String descripcionEspanol;

	@Column(name="DESCRIPCION_INGLES")
	private String descripcionIngles;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	//bi-directional many-to-one association to SpaTipoTratamiento
	@ManyToOne
	@JoinColumn(name="ID_TRATAMIENTO")
	private SpaTipoTratamiento spaTipoTratamiento;

	//bi-directional many-to-one association to SpaClasificacion
	@OneToMany(mappedBy="spaCategoria")
	private List<SpaClasificacion> spaClasificacions;

	public SpaCategoria() {
	}

	public long getIdCategoria() {
		return this.idCategoria;
	}

	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public long getCodCategoria() {
		return this.codCategoria;
	}

	public void setCodCategoria(long codCategoria) {
		this.codCategoria = codCategoria;
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

	public String getDescripcionEspanol() {
		return this.descripcionEspanol;
	}

	public void setDescripcionEspanol(String descripcionEspanol) {
		this.descripcionEspanol = descripcionEspanol;
	}

	public String getDescripcionIngles() {
		return this.descripcionIngles;
	}

	public void setDescripcionIngles(String descripcionIngles) {
		this.descripcionIngles = descripcionIngles;
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

	public SpaTipoTratamiento getSpaTipoTratamiento() {
		return this.spaTipoTratamiento;
	}

	public void setSpaTipoTratamiento(SpaTipoTratamiento spaTipoTratamiento) {
		this.spaTipoTratamiento = spaTipoTratamiento;
	}

	public List<SpaClasificacion> getSpaClasificacions() {
		return this.spaClasificacions;
	}

	public void setSpaClasificacions(List<SpaClasificacion> spaClasificacions) {
		this.spaClasificacions = spaClasificacions;
	}

	@Override
	public String toString() {
		return descripcionEspanol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idCategoria ^ (idCategoria >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpaCategoria other = (SpaCategoria) obj;
		if (idCategoria != other.idCategoria)
			return false;
		return true;
	}
	
}