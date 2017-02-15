package ve.com.toyota.spa.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SPA_DESECHO database table.
 * 
 */
@Entity
@Table(name="SPA_DESECHO")
@NamedQueries({
	@NamedQuery(name="SpaDesecho.findAll", query="select d from SpaDesecho d "),
	@NamedQuery(name="SpaDesecho.findActiveAll", query="select d from SpaDesecho d where d.activo = 'A'")})
public class SpaDesecho implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_DESECHO_IDDESECHO_GENERATOR", sequenceName="SPA_ID_DESECHO_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_DESECHO_IDDESECHO_GENERATOR")
	@Column(name="ID_DESECHO")
	private long idDesecho;

	private String activo;

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

	//bi-directional many-to-one association to SpaClasificacion
	@ManyToOne
	@JoinColumn(name="ID_CLASIFICACION")
	private SpaClasificacion spaClasificacion;

	//bi-directional many-to-one association to SpaDesechoSolicitud
	@OneToMany(mappedBy="spaDesecho")
	private List<SpaDesechoSolicitud> spaDesechoSolicituds;

	public SpaDesecho() {
	}

	public long getIdDesecho() {
		return this.idDesecho;
	}

	public void setIdDesecho(long idDesecho) {
		this.idDesecho = idDesecho;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
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

	public SpaClasificacion getSpaClasificacion() {
		return this.spaClasificacion;
	}

	public void setSpaClasificacion(SpaClasificacion spaClasificacion) {
		this.spaClasificacion = spaClasificacion;
	}

	public List<SpaDesechoSolicitud> getSpaDesechoSolicituds() {
		return this.spaDesechoSolicituds;
	}

	public void setSpaDesechoSolicituds(List<SpaDesechoSolicitud> spaDesechoSolicituds) {
		this.spaDesechoSolicituds = spaDesechoSolicituds;
	}

	@Override
	public String toString() {
		return  descripcionEspanol;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idDesecho ^ (idDesecho >>> 32));
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
		SpaDesecho other = (SpaDesecho) obj;
		if (idDesecho != other.idDesecho)
			return false;
		return true;
	}	

}