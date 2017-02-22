package ve.com.toyota.sap.util;

import static ve.com.toyota.spa.util.Messages.createMessageFrame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

import ve.com.toyota.spa.beans.Reportes;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Clase de utilidad encargada de recibir las peticiones para reportes
 * @author dmartinez
 * @since SPA
 */
public class ReportRuntines {
	
	private final static Logger log = Logger.getLogger(Reportes.class);
	
	/**
	 * Objeto que gestiona el acceso al recurso de datos
	 */
	@Resource(name="jdbc/spaDB")
	private DataSource ds;
	
	private Connection con;
	
	/**
	 * Si la inyeccion del recurso falla se usa este metedo
	 */	
	public ReportRuntines() throws SQLException{
		if(ds==null){
			try {
				Context ctx = new InitialContext();
				ds = (DataSource)ctx.lookup("java:comp/env/jdbc/spaDB");
			} catch (NamingException e) {
				e.printStackTrace();
			}
			if(ds==null)
				throw new SQLException("Can't get data source");
			//get database connection
			con = ds.getConnection();
			if(con==null)
				throw new SQLException("Can't get database connection");
		}
	}	
	
	/**
	 * Método de pre-carga de información.
	 */		
	@PostConstruct
	public void init() {
		URL url = Loader.getResource("log4j.properties");  	// Esto carga el fichero como recurso si está en el CLASSPATH
		PropertyConfigurator.configure(url);				// Carga configuracion del log
	}	
	
	/** 
	 * Genera el pdf para un nombre de informe dado 
	 * @param aux = Lista de objetos que mostraremos en el informe 
	 * @param parametros = Resto de parametros que viajan al informe
	 * @param response = Donde se enviara el reporte
	 * @param nameReport = Nombre del compilado del reporte   
	 * @throws JRException 
	 */  
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void toPDF(List aux, HashMap parametros, HttpServletResponse response, String nameReport) throws JRException, IOException {
		try
		{	
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition", "attachment; filename=\""+nameReport+"\"");				
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(nameReport+".jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(aux));
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		}
		catch (JRException e)
		{
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Generando reporte en PDF con objetos.","Error. Contacte al help desk.");
			log.error("Generando reporte en PDF con objetos.",e);			
		}    
	}
	
	/** 
	 * Genera el pdf para un nombre de informe dado usando conexion a BD
	 * @param parametros = Resto de parametros que viajan al informe
	 * @param response = Donde se enviara el reporte
	 * @param nameReport = Nombre del compilado del reporte   
	 * @throws JRException 
	 * @throws SQLException 
	 */  	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void toPDF(HashMap parametros, HttpServletResponse response, String nameReport) throws JRException, IOException, SQLException {
		try
		{		
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition", "attachment; filename=\""+nameReport+"\"");			
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(nameReport+".jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, con);
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		}
		catch (JRException e)
		{
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Generando reporte en PDF con DB.","Error. Contacte al help desk.");
			log.error("Generando reporte en PDF con DB.",e);	
		}    
	}	
	
	/** 
	 * Genera el xls para un nombre de informe dado usando conexion a BD
	 * @param parametros = Resto de parametros que viajan al informe
	 * @param response = Donde se enviara el reporte
	 * @param nameReport = Nombre del compilado del reporte   
	 * @throws JRException 
	 * @throws SQLException 
	 */  	
	@SuppressWarnings({ "unchecked", "rawtypes" })	
	public void toXls(HashMap parametros, HttpServletResponse response, String nameReport) throws IOException
	 {
	 try{   

		 InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(nameReport+".jasper");
		 JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
		 JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, con);
		 OutputStream out = response.getOutputStream();

		 ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		 JRXlsExporter exporterXLS = new JRXlsExporter();                

		 exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		 exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, arrayOutputStream);
		 exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		 exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		 exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		 exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		 exporterXLS.exportReport();
		 
		 response.setContentType("application/vnd.ms-excel");
		 response.setHeader("Content-disposition", "attachment; filename="+nameReport);
		 response.setContentLength(arrayOutputStream.toByteArray().length);
		 out.write(arrayOutputStream.toByteArray());
		 out.flush();
		 out.close();

	 	}catch(JRException e)
	 	{
			createMessageFrame(FacesMessage.SEVERITY_ERROR,"Error. Generando reporte en XLS con DB.","Error. Contacte al help desk.");
			log.error("Generando reporte en XLS con DB.",e);
	 	}
	 }	
	
	
}


