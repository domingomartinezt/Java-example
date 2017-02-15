package ve.com.toyota.spa.entities;

import java.io.Serializable;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the SPA_IMAGEN database table.
 * 
 */
@Entity
@Table(name="SPA_IMAGEN")
@NamedQueries({
	@NamedQuery(name="SpaImagen.findById", query="select I from SpaImagen I where I.idImagen = :idImagen ")})
@NamedNativeQueries({
	@NamedNativeQuery(name = "crearRegistroImagen", query = "INSERT INTO SPA.SPA_IMAGEN (imagen, id_imagen, file_name, content_type) VALUES (BFILENAME(?,?),?,?,?)"),
	@NamedNativeQuery(name = "nextSecuenceImage", query = "SELECT SPA.SPA_ID_IMAGEN_SEQ.NEXTVAL FROM DUAL"),
	@NamedNativeQuery(name = "recuperarImagen", query = "SELECT i.imagen FROM SPA_IMAGEN i WHERE i.id_imagen = ?")
})
public class SpaImagen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_IMAGEN_IDIMAGEN_GENERATOR", sequenceName="SPA_ID_IMAGEN_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_IMAGEN_IDIMAGEN_GENERATOR")
	@Column(name="ID_IMAGEN")
	private long idImagen;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Lob
	@Column(name = "IMAGEN")
	private char[] imagen;
	
	@Column(name="CONTENT_TYPE")
	private String contentType;
	
	@Column(name="FILE_NAME")
	private String fileName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	//bi-directional one-to-one association to SpaAprobacionJunta
	@OneToOne(mappedBy="spaImagen")
	private SpaAprobacionJunta spaAprobacionJuntas;

	public SpaImagen() {
	}

	public long getIdImagen() {
		return this.idImagen;
	}

	public void setIdImagen(long idImagen) {
		this.idImagen = idImagen;
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

	public char[] getImagen() {
		return this.imagen;
	}

	public void setImagen(char[] imagen) {
		this.imagen = imagen;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public SpaAprobacionJunta getSpaAprobacionJuntas() {
		return this.spaAprobacionJuntas;
	}

	public void setSpaAprobacionJuntas(SpaAprobacionJunta spaAprobacionJuntas) {
		this.spaAprobacionJuntas = spaAprobacionJuntas;
	}

}