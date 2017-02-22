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
import ve.com.toyota.spa.entities.SpaUnidadMedida;

/**
 * Clase utilizada por los componentes de vista JSF, para el formateo y conversión del objeto UnidadMedida
 * @author Domingo Martinez
 * @since SPA 
 * @see Converter
 * @see Serializable
 * @see EJB
 */

@ManagedBean(name= "unidadMedidaConverter")
@RequestScoped

public class UnidadMedidaConverter implements Converter, Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB
	private transient AdministracionBean administracionEJB;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		if (submittedValue == null || submittedValue.isEmpty()) {
			return null;
		} else {
			try {
				return (SpaUnidadMedida)administracionEJB.getSpaUnidadMedidaFindById(Long.valueOf(submittedValue));
			} catch (Exception e) {
				createMessageFrame(FacesMessage.SEVERITY_ERROR, "Error de validacion.", "Falta seleccionar la unidad de medida, o la seleccionada no es valida.");
			}
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {	
		if (!(value instanceof SpaUnidadMedida)) {
			return null;
		} else {
			SpaUnidadMedida spaUnidadMedida = (SpaUnidadMedida)value;
			return String.valueOf(spaUnidadMedida.getIdUnidadMedida());
		}
	}

}
