package ve.com.toyota.spa.beans;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ve.com.toyota.common.ejb.pt.TrabajadorBean;
import ve.com.toyota.common.entities.pt.TrabajadorTDV;


/**
 * Clase que gestiona las operaciones básica de cada session.
 * @author Domingo Martinez
 * @since SPA
 * @see FacesContext
 * @see ManagedBean
 * @see SessionScoped
 */
@ManagedBean(name = "sessionBean")
@SessionScoped
public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090495758045758413L;

	/**
	 * Objeto que gestiona las operaciones de consulta al directorio activo.
	 */
	@EJB(lookup = "java:global/tdvCommon/TrabajadorBean")
	private TrabajadorBean trabajadorEJB;	

	/**
	 * Nombre completo del usuario autenticado.
	 */
	private TrabajadorTDV trabajadorTDV;

	/**
	 * @return the nombreCompleto
	 */
	public String getInfoTrabajador(String username) {
		username = username.toUpperCase();
		if (username != null) {
			setTrabajadorTDV(trabajadorEJB.obtenerTrabajador(username));
			if (trabajadorTDV != null) {
				return trabajadorTDV.getNombre();
			} else {
				trabajadorTDV = new TrabajadorTDV();
			}
		}
		return "Información del trabajador en oracle no encontrada.";
	}

	/**
	 * Método que desconecta y libera la sessión del usuario auténtico.
	 * @return Cadena con la ruta url de salida.
	 */
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index?faces-redirect=true";
	}

	/*
	 * GETTERS & SETTERS
	 */

	public TrabajadorTDV getTrabajadorTDV() {
		return trabajadorTDV;
	}

	public void setTrabajadorTDV(TrabajadorTDV trabajadorTDV) {
		this.trabajadorTDV = trabajadorTDV;
	}
}

