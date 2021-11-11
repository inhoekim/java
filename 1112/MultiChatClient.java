package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;


class RecThread extends Thread{
    Socket socket;
    public RecThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                String msg = br.readLine();
                if (msg == null) {
                    System.out.println("<서버가 종료되었습니다>");
                    break;
                }
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SendThread extends Thread{
    Socket socket;

    public SendThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(System.in);
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("<접속할 채팅방을 선택하세요");
            System.out.println("1.1번방  2.2번방  3.3번방");
            String roomNum = sc.nextLine();
            pw.println(roomNum);
            System.out.println("<사용하실 닉네임을 입력하세요>");
            String userName = sc.nextLine();
            pw.println("<" + userName + "님이 채팅방에 접속하셨습니다>");
            while(true) {
                String msg = sc.nextLine();
                if(msg.equals("exit")) {
                    pw.println("<"+ userName + "님이 채팅방을 나가셨습니다>");
                    break;
                }
                pw.println(userName + ">>" + msg);
            }
            pw.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class MultiChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 3000);
            System.out.println("<서버에 접속되었습니다>");
            new RecThread(socket).start();
            new SendThread(socket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
