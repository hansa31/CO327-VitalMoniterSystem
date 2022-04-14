import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Gateway{

    //Endpoint 
    private DatagramSocket datagramSocket;

    // buffer to hold data
    private byte[] buffer = new byte[256];

    //Initialize the datagram socket
    public Gateway(DatagramSocket datagramSocket){
        this.datagramSocket = datagramSocket;
    }

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

}