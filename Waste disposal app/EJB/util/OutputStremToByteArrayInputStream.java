package ve.com.toyota.spa.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
 
public class OutputStremToByteArrayInputStream {
 
   public InputStream getInputStream(OutputStream outputStream) {
          ByteArrayOutputStream byteOupPut = (ByteArrayOutputStream)outputStream;
         return new ByteArrayInputStream(byteOupPut.toByteArray());
   }
}