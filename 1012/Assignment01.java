import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/*
영어단어와 뜻을 키보드로 입력받아 PrintWriter를 사용해서 파일로 저장해 보세요.
  파일 -> eng.txt
  love,사랑하다
  apple,사과
  yellow,노랑

 "0"입력하면 종료
 */

public class Assignment01 {
	public static void main(String[] args) {
		PrintWriter pw = null;
		Scanner sc = new Scanner(System.in);
		try {
			pw = new PrintWriter(new FileOutputStream("eng.txt"));
			String input = "";
			System.out.println("영어단어와 그 한국어 뜻을 입력하세요. 예) love,사랑하다");
			while(!(input = sc.next()).equals("0")) {
				pw.println(input);
				pw.flush();
			}
			System.out.println("ent.txt 파일에 저장되었습니다.");
			pw.close();
		}catch(FileNotFoundException fnf) {
			System.out.println(fnf.getMessage());
		}
	}
}
