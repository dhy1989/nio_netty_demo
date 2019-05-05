package bio;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author dinghy
 * @date 2019/5/5
 */
public class Client {
    private Socket socket = null;

    public Client(String hostName, int port) throws IOException {
        socket = new Socket(hostName, port);
        System.out.println("client starter!");
    }

    public void handle() {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader =null;
        PrintWriter writer=null;
        try {
             reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
             writer=new PrintWriter(socket.getOutputStream(),true);
             String message=null;
            while (true){
             message=scanner.nextLine();
             if(message.equals("exit")){
                 break;
             }
                writer.println("from client"+message);
                writer.flush();
                System.out.println(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(writer!=null){
                writer.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 9000);
        client.handle();
    }
}
