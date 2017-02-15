package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SPA_UNIDAD_MEDIDA database table.
 * 
 */
@Entity
@Table(name="SPA_UNIDAD_MEDIDA")
@NamedQueries({
	@NamedQuery(name="SpaUnidadMedida.findAll", query="select u from SpaUnidadMedida u "),
	@NamedQuery(name="SpaUnidadMedida.findAbreviacion", query="select u from SpaUnidadMedida u where u.abreviacion = :abreviacion"),	
	@NamedQuery(name="SpaUnidadMedida.findActiveAll", query="select u from SpaUnidadMedida u where u.activo = 'A'"),
	@NamedQuery(name="SpaUnidadMedida.findAplicatioScope", query="select u from SpaUnidadMedida u where u.activo = 'A' and u.alcance='APLICACION'"),
	@NamedQuery(name="SpaUnidadMedida.findSolicitudScope", query="select u from SpaUnidadMedida u where u.activo = 'A' and u.alcance='SOLICITUDES'")})
public class SpaUnidadMedida implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_UNIDAD_MEDIDA_IDUNIDADMEDIDA_GENERATOR", sequenceName="SPA_ID_UNIDAD_MEDIDA_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_UNIDAD_MEDIDA_IDUNIDADMEDIDA_GENERATOR")
	@Column(name="ID_UNIDAD_MEDIDA")
	private long idUnidadMedida;

	private String abreviacion;

	private String activo;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	private String descripcion;
	
	@Column(name="ALCANCE")
	private String alcance;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	//bi-directional many-to-one association to SpaDesechoSolicitud
	@OneToMany(mappedBy="spaUnidadMedida")
	private List<SpaDesechoSolicitud> spaDesechoSolicituds;

	//bi-directional many-to-one association to SpaItemDesincorporar
	@OneToMany(mappedBy="spaUnidadMedida")
	private List<SpaItemDesincorporar> spaItemDesincorporars;

	public SpaUnidadMedida() {
	}

	public long getIdUnidadMedida() {
		return this.idUnidadMedida;
	}

	public void setIdUnidadMedida(long idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
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

	public String getAlcance() {
		return alcance;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
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

	public List<SpaDesechoSolicitud> getSpaDesechoSolicituds() {
		return this.spaDesechoSolicituds;
	}

	public void setSpaDesechoSolicituds(List<SpaDesechoSolicitud> spaDesechoSolicituds) {
		this.spaDesechoSolicituds = spaDesechoSolicituds;
	}

	public List<SpaItemDesincorporar> getSpaItemDesincorporars() {
		return this.spaItemDesincorporars;
	}

	public void setSpaItemDesincorporars(List<SpaItemDesincorporar> spaItemDesincorporars) {
		this.spaItemDesincorporars = spaItemDesincorporars;
	}

	@Override
	public String toString() {
		return abreviacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idUnidadMedida ^ (idUnidadMedida >>> 32));
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
		SpaUnidadMedida other = (SpaUnidadMedida) obj;
		if (idUnidadMedida != other.idUnidadMedida)
			return false;
		return true;
	}
	
}