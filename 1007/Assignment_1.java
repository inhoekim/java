import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

class Book{
	private int bookNum;
	private String bookName;
	private String bookValue;
	
	public Book(int bookNum, String bookName, String bookValue) {
		this.bookNum = bookNum;
		this.bookName = bookName;
		this.bookValue = bookValue;
	}
	
	public void print_all() {
		System.out.println("책번호: " + bookNum + ",책이름: " + bookName + ",가격: " + bookValue);
	}
	
	public int getBookNum() {
		return bookNum;
	}
}
public class Assignment_1 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean switch_on = true;
		HashMap<Integer, Book> hm = new HashMap<>();
		while(switch_on) {
			System.out.println("1.도서추가 2.도서검색 3.도서수정 4.도서삭제 5.전체보기 6.종료");
			int input = sc.nextInt();
			int input_num;
			switch(input) {
			
				case 1:{
					Book book = makingBook();
					hm.put(book.getBookNum(), book);
					System.out.println();
					break;
				}
				
				case 2:{
					System.out.println("검색할 책번호를 입력하세요");
					input_num = sc.nextInt();
					if (hm.get(input_num) == null) System.out.println("해당 책은 존재하지 않습니다");
					else hm.get(input_num).print_all();
					System.out.println();
					break;
				}
				
				case 3:{
					System.out.println("수정할 책번호를 입력하세요");
					input_num = sc.nextInt();
					if (hm.get(input_num) == null) System.out.println("해당 책은 존재하지 않습니다");
					else {
						hm.remove(input_num);
						Book book = makingBook();
						hm.put(book.getBookNum(), book);
					}
					System.out.println();
					break;
				}
				
				case 4:{
					System.out.println("삭제할 책번호를 입력하세요");
					input_num = sc.nextInt();
					if (hm.get(input_num) == null) System.out.println("해당 책은 존재하지 않습니다");
					else hm.remove(input_num);
					System.out.println();
					break;
				}
				
				case 5:{
					Set<Integer> setBook = hm.keySet();
					System.out.println("=============================");
					for(int i : setBook) {
						hm.get(i).print_all();
					}
					System.out.println("=============================");
					System.out.println();
					break;
				}
				
				case 6:{
					switch_on = false;
					System.out.println("프로그램을 종료합니다.");
				}
			}
		}
	}
	
	public static Book makingBook() {
		Scanner sc = new Scanner(System.in);
		String input_str, input_str2;
		int input_num;
		System.out.print("책번호 입력: ");
		input_num = sc.nextInt();
		System.out.print("책제목 입력: ");
		sc.nextLine();
		input_str = sc.nextLine();
		System.out.print("책가격 입력: ");
		input_str2 = sc.next();
		
		return new Book(input_num, input_str, input_str2);
	}
}
