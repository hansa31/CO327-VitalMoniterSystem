import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class myThreadThing implements Runnable{

    private Monitor receivedMonitor = null;

    public myThreadThing(Monitor receivedMonitor){
        // store the monitor parameter
        this.receivedMonitor = receivedMonitor;
    }

    @Override
    public void run() {
        //have the tcp here
        try {
            Socket clientSocket = new Socket(receivedMonitor.getIp(),receivedMonitor.getPort());
            //System.out.println("Connected");        //To debug
            //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            while(true){
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();
                System.out.println(message);
            }
            
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
    
}
