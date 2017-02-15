package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the SPA_CLASIFICACION database table.
 * 
 */
@Entity
@Table(name="SPA_CLASIFICACION")
@NamedQueries({
	@NamedQuery(name="SpaClasificacion.findAll", query="select c from SpaClasificacion c "),
	@NamedQuery(name="SpaClasificacion.findActiveAll", query="select c from SpaClasificacion c where c.activo = 'A'")})
public class SpaClasificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_CLASIFICACION_IDCLASIFICACION_GENERATOR", sequenceName="SPA_ID_CLASIFICACION_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_CLASIFICACION_IDCLASIFICACION_GENERATOR")
	@Column(name="ID_CLASIFICACION")
	private long idClasificacion;

	private String activo;

	@Column(name="COD_CLASIFICACION")
	private BigDecimal codClasificacion;

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

	//bi-directional many-to-one association to SpaCategoria
	@ManyToOne
	@JoinColumn(name="ID_CATEGORIA")
	private SpaCategoria spaCategoria;

	//bi-directional many-to-one association to SpaDesecho
	@OneToMany(mappedBy="spaClasificacion")
	private List<SpaDesecho> spaDesechos;

	public SpaClasificacion() {
	}

	public long getIdClasificacion() {
		return this.idClasificacion;
	}

	public void setIdClasificacion(long idClasificacion) {
		this.idClasificacion = idClasificacion;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public BigDecimal getCodClasificacion() {
		return this.codClasificacion;
	}

	public void setCodClasificacion(BigDecimal codClasificacion) {
		this.codClasificacion = codClasificacion;
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

	public SpaCategoria getSpaCategoria() {
		return this.spaCategoria;
	}

	public void setSpaCategoria(SpaCategoria spaCategoria) {
		this.spaCategoria = spaCategoria;
	}

	public List<SpaDesecho> getSpaDesechos() {
		return this.spaDesechos;
	}

	public void setSpaDesechos(List<SpaDesecho> spaDesechos) {
		this.spaDesechos = spaDesechos;
	}

	@Override
	public String toString() {
		return descripcionEspanol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idClasificacion ^ (idClasificacion >>> 32));
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
		SpaClasificacion other = (SpaClasificacion) obj;
		if (idClasificacion != other.idClasificacion)
			return false;
		return true;
	}

}