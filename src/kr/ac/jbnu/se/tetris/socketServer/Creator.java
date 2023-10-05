package kr.ac.jbnu.se.tetris.socketServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Creator {
    public static int tcpSeverPort = 9999;
    public Creator(int portNum){
        tcpSeverPort = portNum;
        try{
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(tcpSeverPort));
            System.out.println("Starting tcp Server: "+tcpSeverPort);
            System.out.println("[Waiting]\n");
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Connected "+socket.getLocalPort()+" Port, From "+socket.getRemoteSocketAddress().toString()+"\n");
                Server tcpServer = new Server(socket);
                tcpServer.start();
            }
        }catch (IOException io){
            io.getStackTrace();
        }
    }
    public class Server extends Thread {
        private Socket socket;

        public Server(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                while (true) {
                    // Socket에서 가져온 출력스트림
                    OutputStream os = this.socket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);

                    // Socket에서 가져온 입력스트림
                    InputStream is = this.socket.getInputStream();
                    DataInputStream dis = new DataInputStream(is);

                    // read int
                    int recieveLength = dis.readInt();

                    // receive bytes
                    byte receiveByte[] = new byte[recieveLength];
                    dis.readFully(receiveByte, 0, recieveLength);
                    String receiveMessage = new String(receiveByte);
                    System.out.println("receiveMessage : " + receiveMessage);
                    System.out.println("[ Data Receive Success ]\n");

                    // send bytes
                    String sendMessage = "서버에서 보내는 데이터";
                    byte[] sendBytes = sendMessage.getBytes("UTF-8");
                    int sendLength = sendBytes.length;
                    dos.writeInt(sendLength);
                    dos.write(sendBytes, 0, sendLength);
                    dos.flush();

                    System.out.println("sendMessage : " + sendMessage);
                    System.out.println("[ Data Send Success ]");
                }
            } catch (EOFException e) {
                // readInt()를 호출했을 때 더 이상 읽을 내용이 없으면 EOFException이 발생한다.
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (this.socket != null) {
                        System.out.print("\n[ Socket closed ] ");
                        System.out.println("Disconnected :" + this.socket.getInetAddress().getHostAddress() + ":"
                                + this.socket.getPort());
                        this.socket.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}