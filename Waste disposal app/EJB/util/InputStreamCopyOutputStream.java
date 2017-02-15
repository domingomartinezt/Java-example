package ve.com.toyota.spa.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
public class InputStreamCopyOutputStream {
 
    private static byte[] bytes = new byte[1024*1024*2];
 
    static public void copyInputStreamToOutputStream(InputStream inputStream,OutputStream outPutStream){
          try {
             int read = 0;
             while ((read = inputStream.read(bytes)) != -1)
                   outPutStream.write(bytes,0,read);
             inputStream.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
   }
}
