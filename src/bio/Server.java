package bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author dinghy
 * @date 2019/5/5
 */
public class Server {
    private ServerSocket server = null;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
        System.out.println("server started!");
    }

    public void handle() {
        while (true) {
            try {
                final Socket socket = server.accept();
                new Thread() {
                    @Override
                    public void run() {
                        BufferedReader reader = null;
                        PrintWriter writer = null;
                        try {
                            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                            writer = new PrintWriter(
                                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                            String readMessage = null;
                            while (true) {
                                System.out.println("server reading");
                                if ((readMessage = reader.readLine()) == null) {
                                    break;
                                }
                                System.out.println(readMessage);

                                    writer.println("server replay: " +"消息已收到");
                                    writer.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
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
                }.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9000);
        server.handle();
    }
}
