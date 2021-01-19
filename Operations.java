
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Operations {
    
    public static void write(ObjectOutputStream os, Object o){
        try {
            os.writeObject(o);
            os.flush();
        } catch (IOException ex) {
            System.out.println(ex);
        }        
    }
    
    public static Object read(ObjectInputStream is){
        try {
            return is.readObject();
        } catch (Exception ex) {
            System.out.println(ex);
        } 
        return null;
    }
}
