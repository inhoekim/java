import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Final {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		FileOutputStream fr = null;
		System.out.println("파일에 저장할 문자열을 입력해보세요");
		String input = sc.nextLine();
		byte [] buffer = input.getBytes();
		try {
			fr = new FileOutputStream("test.txt");
			fr.write(buffer);
			System.out.println("test.txt에 올바르게 저장되었습니다");
		}catch(IOException ie) {
			System.out.println(ie.getMessage());
		}finally {
			try{
				if(fr !=null) fr.close();
			}catch(IOException ie) {
				System.out.println(ie.getMessage());
			}
		}
	}
}
