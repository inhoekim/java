import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*
  아래와 같은 eng.txt파일이 있을때 파일을 읽어와 아래와 같은 작업이 
  이루어지도록 프로그램을 작성해 보세요.
  ## 파일 eng.txt
  love,사랑하다
  apple,사과
  yellow,노랑
  ....

  ##프로그램
  영어단어:apple
  apple의 뜻:사과
 */

public class Assignment02 {
	public static void main(String[] args) {
		BufferedReader dis = null;
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("찾고싶은 영어 단어를 입력하세요. 종료키(exit!)");
			
			while(true) {
				String input = sc.next();
				if (input.equals("exit!")) break;
				dis = new BufferedReader(new FileReader("eng.txt"));
				String temp = dis.readLine();
				boolean flag = true;
				while(temp != null && flag) {
					String [] strList = temp.split(",");
					String word = strList[0].trim();
					if(input.equals(word)) {
						String meaning = strList[1].trim();
						System.out.println(word + "의 뜻: " + meaning);
						flag = false;
						break;
					}
					else {
						temp = dis.readLine();
					}
				}
				if(flag) System.out.println(input + "은 eng.txt에 존재하지 않는 단어입니다.");
			}
			System.out.println("종료되었습니다");
		}catch(FileNotFoundException fnf) {
			System.out.println(fnf.getMessage());

		}catch(IOException ie) {
			System.out.println(ie.getMessage());
		}
	}
}
