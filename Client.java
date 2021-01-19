
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client extends Thread{
    BufferedReader br = null;
    Socket s = null;
    ObjectInputStream i = null;
    ObjectOutputStream o = null;
    
    Client(){
        try {
            // For console reading.
            InputStreamReader isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);
            // Connecting
            s = new Socket("localhost",8888);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run(){
        int option = 7;
        try{
        i = new ObjectInputStream(s.getInputStream());
        o = new ObjectOutputStream(s.getOutputStream());
        do{
            String mainMenu = (String)Operations.read(i);
            System.out.println(mainMenu);
            option = Integer.parseInt(br.readLine());
            Operations.write(o, option);
            if(option == 1){
                Car car = new Car();
                System.out.print("Enter Registration Number : ");
                car.setRegistration(br.readLine());
                System.out.print("Enter Make : ");
                car.setMake(br.readLine());
                System.out.print("Enter Price : ");
                car.setPrice(Integer.parseInt(br.readLine()));
                System.out.print("Enter Mileage : ");
                car.setMileage(Double.parseDouble(br.readLine()));
                car.setForSale(true);
                Operations.write(o,car);
                System.out.println("Car added successfully.");
            }
            else if(option == 2){ 
                System.out.print("Enter registration number : ");
                String rn = br.readLine();
                Operations.write(o, rn);
                System.out.println("Car with the registration, "+rn+" sold out successfully.");
            }
            else if(option == 3){
                System.out.print("Enter registration number : ");
                String rn = br.readLine();
                Operations.write(o, rn);
                Car car = (Car) Operations.read(i);
                System.out.print("Required car information is : ");
                car.showDetails();
            }
            else if(option == 4){
                List<Car> cars = (List) Operations.read(i);
                for(Car c : cars)
                    c.showDetails();
            }
            else if(option == 5){
                System.out.print("Enter car maker : ");
                String m = br.readLine();
                Operations.write(o, m);
                List<Car> cars = (List) Operations.read(i);
                for(Car c : cars)
                    c.showDetails();
            }
            else if(option == 6){
                Long v = (Long)Operations.read(i);
                System.out.println("Total sales values is : "+v);
            }
            else if(option == 7){
                System.out.println("Disconnecting...");
            }
        }while(option!=7);
        }
        catch(Exception ex){}
        finally{
            try{
                if(i!=null)
                i.close();
                if(o!=null)
                o.close();
                if(s!=null)
                s.close();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    
    public static void main(String argc[]){
        Client c = new Client();
        c.start();
    }
}
