package ve.com.toyota.spa.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
 
import static ve.com.toyota.spa.util.InputStreamCopyOutputStream.*;
 
public class InputStreamToByteArrayOutputStream {
 
        public OutputStream getOutpuStream(InputStream inputStream) {
             ByteArrayOutputStream byteOuputStream = new ByteArrayOutputStream();
             copyInputStreamToOutputStream(inputStream,byteOuputStream);
             return byteOuputStream;
        }
}
