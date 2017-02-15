package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the SPA_DESECHO_SOLICITUD database table.
 * 
 */
@Entity
@Cacheable(false)
@Table(name="SPA_DESECHO_SOLICITUD")
public class SpaDesechoSolicitud implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_DESECHO_SOLICITUD_IDDESECHOSOLICITUD_GENERATOR", sequenceName="SPA_ID_DESECHO_SOL_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_DESECHO_SOLICITUD_IDDESECHOSOLICITUD_GENERATOR")
	@Column(name="ID_DESECHO_SOLICITUD")
	private long idDesechoSolicitud;

	private double cantidad;

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

	@Column(name="NOMBRE_DESECHO")
	private String nombreDesecho;

	//bi-directional many-to-one association to SpaDesecho
	@ManyToOne
	@JoinColumn(name="ID_DESECHO")
	private SpaDesecho spaDesecho;

	//bi-directional many-to-one association to SpaSolicitude
	@ManyToOne
	@JoinColumn(name="ID_SOLICITUD")
	private SpaSolicitudes spaSolicitude;

	//bi-directional many-to-one association to SpaUnidadMedida
	@ManyToOne
	@JoinColumn(name="ID_UNIDAD_MEDIDA")
	private SpaUnidadMedida spaUnidadMedida;

	public SpaDesechoSolicitud() {
	}

	public long getIdDesechoSolicitud() {
		return this.idDesechoSolicitud;
	}

	public void setIdDesechoSolicitud(long idDesechoSolicitud) {
		this.idDesechoSolicitud = idDesechoSolicitud;
	}

	public double getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
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

	public String getNombreDesecho() {
		return this.nombreDesecho;
	}

	public void setNombreDesecho(String nombreDesecho) {
		this.nombreDesecho = nombreDesecho;
	}

	public SpaDesecho getSpaDesecho() {
		return this.spaDesecho;
	}

	public void setSpaDesecho(SpaDesecho spaDesecho) {
		this.spaDesecho = spaDesecho;
	}

	public SpaSolicitudes getSpaSolicitude() {
		return this.spaSolicitude;
	}

	public void setSpaSolicitude(SpaSolicitudes spaSolicitude) {
		this.spaSolicitude = spaSolicitude;
	}

	public SpaUnidadMedida getSpaUnidadMedida() {
		return this.spaUnidadMedida;
	}

	public void setSpaUnidadMedida(SpaUnidadMedida spaUnidadMedida) {
		this.spaUnidadMedida = spaUnidadMedida;
	}

}