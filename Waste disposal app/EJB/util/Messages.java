package ve.com.toyota.spa.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Clase que gestiona operaciones con mensajes generales.
 * @author Domingo Martinez
 * @since SAP
 * @see FacesContext
 */
public class Messages {

	/**
	 * Método para enviar mensajes de información dentro de la misma ventana con el mensaje con parametro for="msgFrame"
	 * @param detalle del mensaje.
	 * @return mensaje a ser enviado.
	 */
	public static void createMessageFrame(FacesMessage.Severity severity, String summary, String message) {
		FacesMessage msg = new FacesMessage();
		msg.setDetail(message);
		msg.setSummary(summary);
		msg.setSeverity(severity);
		FacesContext.getCurrentInstance().addMessage("msgFrame", msg);
	}

}
