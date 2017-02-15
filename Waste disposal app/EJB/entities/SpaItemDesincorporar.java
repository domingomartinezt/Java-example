package ve.com.toyota.spa.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the SPA_ITEM_DESINCORPORAR database table.
 * 
 */
@Entity
@Table(name="SPA_ITEM_DESINCORPORAR")
public class SpaItemDesincorporar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPA_ITEM_DESINCORPORAR_IDITEMDESINCORPORAR_GENERATOR", sequenceName="SPA_ID_ITEM_DESIN_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPA_ITEM_DESINCORPORAR_IDITEMDESINCORPORAR_GENERATOR")
	@Column(name="ID_ITEM_DESINCORPORAR")
	private long idItemDesincorporar;

	@Column(name="CANTIDAD_DESINCORPORAR")
	private double cantidadDesincorporar;

	@Column(name="CANTIDAD_RECIBIDA")
	private double cantidadRecibida;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="DESCRIPCION_ITEM")
	private String descripcionItem;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name="LAST_UPDATE_LOGIN")
	private String lastUpdateLogin;

	@Column(name="LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name="SERIAL_ITEM")
	private String serialItem;

	//bi-directional many-to-one association to SpaSolicitude
	@ManyToOne
	@JoinColumn(name="ID_SOLICITUD")
	private SpaSolicitudes spaSolicitude;

	//bi-directional many-to-one association to SpaUnidadMedida
	@ManyToOne
	@JoinColumn(name="ID_UNIDAD_MEDIDA")
	private SpaUnidadMedida spaUnidadMedida;

	public SpaItemDesincorporar() {
	}

	public long getIdItemDesincorporar() {
		return this.idItemDesincorporar;
	}

	public void setIdItemDesincorporar(long idItemDesincorporar) {
		this.idItemDesincorporar = idItemDesincorporar;
	}

	public double getCantidadDesincorporar() {
		return this.cantidadDesincorporar;
	}

	public void setCantidadDesincorporar(double cantidadDesincorporar) {
		this.cantidadDesincorporar = cantidadDesincorporar;
	}

	public double getCantidadRecibida() {
		return this.cantidadRecibida;
	}

	public void setCantidadRecibida(double cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
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

	public String getDescripcionItem() {
		return this.descripcionItem;
	}

	public void setDescripcionItem(String descripcionItem) {
		this.descripcionItem = descripcionItem;
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

	public String getSerialItem() {
		return this.serialItem;
	}

	public void setSerialItem(String serialItem) {
		this.serialItem = serialItem;
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
	
    public int getRowKey()
    {
    	return this.hashCode();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((descripcionItem == null) ? 0 : descripcionItem.hashCode());
		result = prime * result + (int) (idItemDesincorporar ^ (idItemDesincorporar >>> 32));
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + ((serialItem == null) ? 0 : serialItem.hashCode());
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
		SpaItemDesincorporar other = (SpaItemDesincorporar) obj;
		if (cantidadDesincorporar != other.cantidadDesincorporar)
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (descripcionItem == null) {
			if (other.descripcionItem != null)
				return false;
		} else if (!descripcionItem.equals(other.descripcionItem))
			return false;
		if (idItemDesincorporar != other.idItemDesincorporar)
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (serialItem == null) {
			if (other.serialItem != null)
				return false;
		} else if (!serialItem.equals(other.serialItem))
			return false;
		return true;
	}

}