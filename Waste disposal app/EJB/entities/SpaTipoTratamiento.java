package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SPA_TIPO_TRATAMIENTO database table.
 * 
 */
@Entity
@Table(name="SPA_TIPO_TRATAMIENTO")
@NamedQueries({
	@NamedQuery(name="SpaTipoTratamiento.findAll", query="select t from SpaTipoTratamiento t "),
	@NamedQuery(name="SpaTipoTratamiento.FindActiveAll", query="select t from SpaTipoTratamiento t where t.activo = 'A'")})
public class SpaTipoTratamiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_TIPO_TRATAMIENTO_IDTRATAMIENTO_GENERATOR", sequenceName="SPA_ID_TRATAMIENTO_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_TIPO_TRATAMIENTO_IDTRATAMIENTO_GENERATOR")
	@Column(name="ID_TRATAMIENTO")
	private long idTratamiento;

	private String activo;

	@Column(name="COD_TRATAMIENTO")
	private BigDecimal codTratamiento;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="DESCRIPCION_ESPANOL")
	private String descripcionEspanol;

	@Column(name="DESCRIPCION_INGLES")
	private String descripcionIngles;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	//bi-directional many-to-one association to SpaCategoria
	@OneToMany(mappedBy="spaTipoTratamiento")
	private List<SpaCategoria> spaCategorias;

	//bi-directional many-to-one association to SpaTipoDesecho
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DESECHO")
	private SpaTipoDesecho spaTipoDesecho;

	public SpaTipoTratamiento() {
	}

	public long getIdTratamiento() {
		return this.idTratamiento;
	}

	public void setIdTratamiento(long idTratamiento) {
		this.idTratamiento = idTratamiento;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public BigDecimal getCodTratamiento() {
		return this.codTratamiento;
	}

	public void setCodTratamiento(BigDecimal codTratamiento) {
		this.codTratamiento = codTratamiento;
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

	public List<SpaCategoria> getSpaCategorias() {
		return this.spaCategorias;
	}

	public void setSpaCategorias(List<SpaCategoria> spaCategorias) {
		this.spaCategorias = spaCategorias;
	}

	public SpaTipoDesecho getSpaTipoDesecho() {
		return this.spaTipoDesecho;
	}

	public void setSpaTipoDesecho(SpaTipoDesecho spaTipoDesecho) {
		this.spaTipoDesecho = spaTipoDesecho;
	}

	@Override
	public String toString() {
		return  descripcionEspanol;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idTratamiento ^ (idTratamiento >>> 32));
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
		SpaTipoTratamiento other = (SpaTipoTratamiento) obj;
		if (idTratamiento != other.idTratamiento)
			return false;
		return true;
	}	
	
}