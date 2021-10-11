import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Final2 {
	public static void main(String[] args) {
		FileReader fr = null;
		FileWriter fw = null;
		try {
			fr = new FileReader("test.txt");
			fw = new FileWriter("upper_test.txt");
			char [] chList = new char [100];
			fr.read(chList);
			String str = new String(chList);
			str = str.toUpperCase();
			fw.write(str);
			
		} catch(FileNotFoundException fe) {
			System.out.println(fe.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(fr != null) fr.close();
				if(fw != null) fw.close();
			} catch(IOException ie) {
				System.out.println(ie.getMessage());
			}
		}
	}
}
