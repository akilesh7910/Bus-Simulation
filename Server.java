
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Server {
    
    public static void main(String argc[]){
        Server s = new Server();
        s.start();
    }

    ServerSocket ssocket = null;
    CarRepository repository = null;
    
    Server() {
        repository = new CarRepository();
        try {
            ssocket = new ServerSocket(Config.PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Semaphore s = new Semaphore(50);
        while (true) {
            try {
                s.acquire();
                Socket socket = ssocket.accept();
                ServerThread st = new ServerThread(repository,socket);
                es.submit(st);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            finally{
                s.release();
            }
        }        
    }
}

class ServerThread extends Thread {
    
    CarRepository repository;
    Socket s = null;
    ObjectOutputStream o = null;
    ObjectInputStream i = null;
        
    ServerThread(CarRepository repository,Socket socket) {
        this.repository = repository;
        this.s = socket;
    }

    public void run() {        
        try {
            o = new ObjectOutputStream(s.getOutputStream());
           
            StringBuffer mainMenu = new StringBuffer();
            mainMenu.append("\n1.Add a car");
            mainMenu.append("\n2.Sell a car");
            mainMenu.append("\n3.Get a car information");
            mainMenu.append("\n4.Get cars for sale");
            mainMenu.append("\n5.Get Cars of a make");
            mainMenu.append("\n6.Get Total Value of Sales");
            mainMenu.append("\n7.exit");
            mainMenu.append("\nEnter your option : ");
            int selection = 7;
            do{
                Operations.write(o, mainMenu.toString());
                if(i==null){
                    i = new ObjectInputStream(s.getInputStream());
                }
                selection = (Integer) Operations.read(i);
                if(selection==1){
                    Car newCar = (Car)Operations.read(i);
                    // adding a new car to the repository
                    repository.addNewCar(newCar);
                    System.out.println("New car added to the system.Car details are : ");
                    newCar.showDetails();
                    System.out.println("");
                }
                else if(selection==2){
                    String registration = (String) Operations.read(i);
                    //Selling a car
                    repository.sellACar(registration);
                }
                else if(selection==3){
                    String registration = (String) Operations.read(i);
                    Car car = repository.requestInformation(registration);
                    Operations.write(o, car);
                }
                else if(selection==4){
                    List<Car> cars = repository.getCarsForSale();
                    Operations.write(o,cars);
                }
                else if(selection==5){
                    String make = (String) Operations.read(i);
                    List<Car> cars = repository.getCarsOfAMake(make);
                    Operations.write(o,cars);
                }
                else if(selection==6){
                    long totalSales = repository.getTotalValueOfAllSales();
                    Operations.write(o, totalSales);
                }
                else if(selection==7){
                    System.out.println("Bye bye ... "+s.toString());
                }
            }while(selection!=7);            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (i != null) {
                    i.close();
                }
                if (o != null) {
                    o.close();
                }
                if (s != null) {
                    s.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

class CarRepository {

    // Cars
    List<Car> carsList = new ArrayList<Car>();

    // Adding a new car.
    public void addNewCar(Car c) {
        carsList.add(c);
    }

    public void sellACar(String r) {
        for (Car c : carsList) {
            if (c.getRegistration().equals(r)) {
                //Marked as sold out.
                c.setForSale(false);
            }
        }
    }

    public Car requestInformation(String r) {
        Car car = null;
        for (Car c : carsList) {
            if (c.getRegistration().equals(r)) {
                car = c;
            }
        }
        return car;
    }

    public List<Car> getCarsForSale() {
        List<Car> subList = new ArrayList<Car>();
        for (Car c : carsList) {
            if (c.isForSale()) {
                subList.add(c);
            }
        }
        return subList;
    }

    public List<Car> getCarsOfAMake(String make) {
        List<Car> subList = new ArrayList<Car>();
        for (Car c : carsList) {
            if (c.getMake().equals(make)) {
                subList.add(c);
            }
        }
        return subList;
    }

    public long getTotalValueOfAllSales() {
        long valueOfAllSales = 0;
        for (Car c : carsList) {
            if (!c.isForSale()) {
                valueOfAllSales = valueOfAllSales + c.getPrice();
            }
        }
        return valueOfAllSales;
    }
}
 