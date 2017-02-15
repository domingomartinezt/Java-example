package ve.com.toyota.spa.util;

import java.io.InputStream;
import java.io.OutputStream;
 
import ve.com.toyota.spa.util.InputStreamToByteArrayOutputStream;
import ve.com.toyota.spa.util.OutputStremToByteArrayInputStream;
 
import static ve.com.toyota.spa.util.Closer.*;
 
public class DuplicateStream {
 
         private InputStream inputStream;
 
         public DuplicateStream(InputStream inputStream){
                this.inputStream = inputStream;
         }
 
         public InputStream cloneStream() {
                OutputStream outputStream = getOutputStreamByInputStream();
                InputStream inputStreamReturned = getInputStreamByOutputStream(outputStream);
                inputStream = getInputStreamByOutputStream(outputStream);
                closeOutpuStream(outputStream);
                return inputStreamReturned;
        }
 
        private OutputStream getOutputStreamByInputStream(){
            return new InputStreamToByteArrayOutputStream().getOutpuStream(inputStream);
        }
 
        private InputStream getInputStreamByOutputStream(OutputStream outputStream){
             return new OutputStremToByteArrayInputStream().getInputStream(outputStream);
        }
 }
