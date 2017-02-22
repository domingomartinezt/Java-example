package ve.com.toyota.spa.converter;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ve.com.toyota.spa.ejb.AdministracionBean;
import ve.com.toyota.spa.entities.SpaDesecho;

/**
 * Clase utilizada por los componentes de vista JSF, para el formateo y conversión del objeto Categoria
 * @author Domingo Martinez
 * @since SPA 
 * @see Converter
 * @see Serializable
 * @see EJB
 */

@ManagedBean(name= "desechoConverter")
@RequestScoped

public class DesechoConverter implements Converter, Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB
	private transient AdministracionBean administracionEJB;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		if (submittedValue == null || submittedValue.isEmpty()) {
			return null;
		} else {
			try {
				return (SpaDesecho)administracionEJB.getSpaDesechoFindById(Long.valueOf(submittedValue));
			} catch (Exception e) {
				createMessageFrame(FacesMessage.SEVERITY_ERROR, "Error de validacion.", "Falta seleccionar el desecho, o el seleccionado no es valido.");
			}
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {	
		if (!(value instanceof SpaDesecho)) {
			return null;
		} else {
			SpaDesecho spaDesecho = (SpaDesecho)value;
			return String.valueOf(spaDesecho.getIdDesecho());
		}
	}

}