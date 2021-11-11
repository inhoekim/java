package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


class EchoThread extends Thread{
    private Socket socket = null;
    private Vector<Vector<Socket>> roomList = null;
    private int roomNum;

    public EchoThread(Socket socket, Vector<Vector<Socket>> roomList) {
        this.socket = socket;
        this.roomList = roomList;
    }

    public void sendMsg(String msg) {
        for (Socket s : roomList.get(roomNum)) {
            if (socket == s) continue;
            try {
                PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                pw.println(msg);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.roomNum = Integer.parseInt(br.readLine()) - 1;
            roomList.get(roomNum).add(socket);
            while(true) {
                String msg = br.readLine();
                if(msg == null) {
                    System.out.println("<클라이언트(" + socket + ")가 연결을 종료하였습니다>");
                    roomList.get(roomNum).remove(socket);
                    break;
                }
                sendMsg(msg);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiChatServer {
    public static Vector<Vector<Socket>> roomList = new Vector<>();
    public static Vector<Socket> room1 = new Vector<>();
    public static Vector<Socket> room2 = new Vector<>();
    public static Vector<Socket> room3 = new Vector<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(3000);
            roomList.add(room1);
            roomList.add(room2);
            roomList.add(room3);
        while(true) {
                System.out.println("<서버 대기중..>");
                Socket socket = server.accept();
                System.out.println("<새로운 클라이언트(" + socket + ")가 접속하였습니다>");
                new EchoThread(socket, roomList).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
