package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the SPA_APROBACION_JUNTA database table.
 * 
 */
@Entity
@Table(name="SPA_APROBACION_JUNTA")
public class SpaAprobacionJunta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_APROBACION_JUNTA_IDAPROBACIONJUNTA_GENERATOR", sequenceName="SPA_ID_APROBACION_JUNTA_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_APROBACION_JUNTA_IDAPROBACIONJUNTA_GENERATOR")
	@Column(name="ID_APROBACION_JUNTA")
	private long idAprobacionJunta;

	private String background;

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

	@Column(name="OBJETIVE_DESCRIPTION")
	private String objetiveDescription;

	@Column(name="REQUEST")
	private String request;	
	
	private String quatation;

	private String reason;

	//bi-directional one-to-one association to SpaImagen
	@OneToOne
	@JoinColumn(name="ID_IMAGEN", unique = true, nullable = true)
	private SpaImagen spaImagen;

	//bi-directional one-to-one association to SpaSolicitude
	@OneToOne(mappedBy="spaAprobacionJunta")
	private SpaSolicitudes spaSolicitudes;

	public SpaAprobacionJunta() {
	}

	public long getIdAprobacionJunta() {
		return this.idAprobacionJunta;
	}

	public void setIdAprobacionJunta(long idAprobacionJunta) {
		this.idAprobacionJunta = idAprobacionJunta;
	}

	public String getBackground() {
		return this.background;
	}

	public void setBackground(String background) {
		this.background = background;
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

	public String getObjetiveDescription() {
		return this.objetiveDescription;
	}

	public void setObjetiveDescription(String objetiveDescription) {
		this.objetiveDescription = objetiveDescription;
	}

	public String getQuatation() {
		return this.quatation;
	}

	public void setQuatation(String quatation) {
		this.quatation = quatation;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public SpaImagen getSpaImagen() {
		return this.spaImagen;
	}

	public void setSpaImagen(SpaImagen spaImagen) {
		this.spaImagen = spaImagen;
	}

	public SpaSolicitudes getSpaSolicitudes() {
		return this.spaSolicitudes;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public void setSpaSolicitudes(SpaSolicitudes spaSolicitudes) {
		this.spaSolicitudes = spaSolicitudes;
	}

}