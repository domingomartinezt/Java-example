package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SPA_TIPO_DESECHO database table.
 * 
 */
@Entity
@Table(name="SPA_TIPO_DESECHO")
@NamedQueries({
	@NamedQuery(name="SpaTipoDesecho.findAll", query="select t from SpaTipoDesecho t "),
	@NamedQuery(name="SpaTipoDesecho.FindActiveAll", query="select t from SpaTipoDesecho t where t.activo = 'A'")})
public class SpaTipoDesecho implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_TIPO_DESECHO_IDTIPODESECHO_GENERATOR", sequenceName="SPA_ID_TIPO_DESECHO_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_TIPO_DESECHO_IDTIPODESECHO_GENERATOR")
	@Column(name="ID_TIPO_DESECHO")
	private long idTipoDesecho;

	private String activo;

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

	@Column(name="NOMBRE_TIPO_DESECHO_ESPANOL")
	private String nombreTipoDesechoEspanol;

	@Column(name="NOMBRE_TIPO_DESECHO_INGLES")
	private String nombreTipoDesechoIngles;

	//bi-directional many-to-one association to SpaTipoTratamiento
	@OneToMany(mappedBy="spaTipoDesecho")
	private List<SpaTipoTratamiento> spaTipoTratamientos;

	public SpaTipoDesecho() {
	}

	public long getIdTipoDesecho() {
		return this.idTipoDesecho;
	}

	public void setIdTipoDesecho(long idTipoDesecho) {
		this.idTipoDesecho = idTipoDesecho;
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

	public String getNombreTipoDesechoEspanol() {
		return this.nombreTipoDesechoEspanol;
	}

	public void setNombreTipoDesechoEspanol(String nombreTipoDesechoEspanol) {
		this.nombreTipoDesechoEspanol = nombreTipoDesechoEspanol;
	}

	public String getNombreTipoDesechoIngles() {
		return this.nombreTipoDesechoIngles;
	}

	public void setNombreTipoDesechoIngles(String nombreTipoDesechoIngles) {
		this.nombreTipoDesechoIngles = nombreTipoDesechoIngles;
	}

	public List<SpaTipoTratamiento> getSpaTipoTratamientos() {
		return this.spaTipoTratamientos;
	}

	public void setSpaTipoTratamientos(List<SpaTipoTratamiento> spaTipoTratamientos) {
		this.spaTipoTratamientos = spaTipoTratamientos;
	}

	
	@Override
	public String toString() {
		return nombreTipoDesechoEspanol;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idTipoDesecho ^ (idTipoDesecho >>> 32));
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
		SpaTipoDesecho other = (SpaTipoDesecho) obj;
		if (idTipoDesecho != other.idTipoDesecho)
			return false;
		return true;
	}
	
}