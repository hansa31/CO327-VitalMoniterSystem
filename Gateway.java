import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Gateway{

    //Endpoint for UDP
    private DatagramSocket socket;

    //Endpoint for TCP
    //private ServerSocket socketTcp;

    // buffer to hold data
    private byte[] buffer = new byte[256];

    //Initialize the datagram socket
    public Gateway() throws IOException{
        //udp socket
        this.socket = new DatagramSocket(6000);
    }

    //get the received monitor
    public Monitor receivedMonitor = null;

    //store monitor IDs
    List<String> ids = new ArrayList<>();

    public void initConnection() throws ClassNotFoundException{

        while(true){
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                //Monitor receivedMonitor = convertToMonitor(packet.getData());
                
                receivedMonitor = convertToMonitor(packet.getData());
                ids.add(receivedMonitor.getMonitorID());

                /*
                if (ids.contains(receivedMonitor.getMonitorID())){
                    break;
                }
                */

                String monitorDis=receivedMonitor.monitor_str();
                //System.out.println(monitorDis);

                //multithrading
                myThreadThing m1 = new myThreadThing(receivedMonitor);
                Thread myThread = new Thread(m1);
                //initTcpConnection(receivedMonitor);
                myThread.start();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //return receivedMonitor;
        }

    }

    //this should be multi-threaded
    public void initTcpConnection(Monitor receivedMonitor){
        try {
            Socket clientSocket = new Socket(receivedMonitor.getIp(),receivedMonitor.getPort());
            System.out.println("Connected");        //To debug
            //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();
            System.out.println(message);
            /*
            while(true){
                //System.out.println("Connected");
                //Socket server = serverSocket.accept();
                //System.out.println("Just connected to "+ server.getRemoteSocketAddress());
            }
            */
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        
    }

  
    /*
    // Convert byte[] to Monitor
    public static Monitor convertToMonitor2(byte[] bytes)throws IOException, ClassNotFoundException {
        //init Monitor object
        Monitor monitor = null;
        InputStream is = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Monitor)ois.readObject();
        }  
  }*/

    
    //convert byte array to monitor object
    private static Monitor convertToMonitor(byte[] data){
        //init Monitor object
        Monitor monitor = null;

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try (ObjectInputStream ois = new ObjectInputStream(bis)){
            monitor =  (Monitor)ois.readObject(); 
            //System.out.println(monitor);
            //monitor.monitor_str(); 
        } catch (IOException | ClassNotFoundException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        //throw new RuntimeException();
        return monitor;
    }
    

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Gateway gateway = new Gateway();
        gateway.initConnection();
        //System.out.println("HHHH");
        //gateway.initTcpConnection(gateway.receivedMonitor);
    }


    /*
    //establish the connection
    public void initConnection()throws IOException{
        while(true){
            try{
                //create datagram packet (To implement a connectionless packet delivery service)
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

                //get the IP address
                InetAddress inetAddress = datagramPacket.getAddress();

                // get the port
                int port = datagramPacket.getPort();

                datagramSocket.receive(datagramPacket);

                //System.out.println(inetAddress + " " + port);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException{

        //create data gram socket for endpoint
        //server will be listning to packets sent to 6000
        DatagramSocket datagramSocket = new DatagramSocket(6000);

        Gateway gateway = new Gateway(datagramSocket);
        gateway.initConnection();

    }
*/
}