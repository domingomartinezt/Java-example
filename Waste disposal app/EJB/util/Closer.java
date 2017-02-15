package ve.com.toyota.spa.util;

import java.io.IOException;
import java.io.OutputStream;
 
public class Closer {
 
    static public void closeOutpuStream(OutputStream outputStream){
      try {
         outputStream.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
 
}
