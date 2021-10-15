/*
	도서정보를 갖는 클래스(도서번호,가격,도서명)를 만들고 ArrayList에 저장합니다..
	1. 전체 도서정보를 출력하세요
	2. 가격이 낮은 순으로 정렬해서 출력되도록 해보세요.
	3. 가격이 20000이상이 도서 정보를 내림차순정렬해서 출력해 보세요.
	4. 전체 도서명을 오름차순정렬해서 출력되도록 해보세요
 */

import java.util.ArrayList;
import java.util.stream.Stream;

class Book implements Comparable<Book>{
	private int bookNum;
	private int price;
	private String bookName;
	
	public Book(int bookNum, String bookName, int price) {
		this.bookName = bookName;
		this.bookNum = bookNum;
		this.price = price;
	}
	
	public String getBookName() {
		return bookName;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getBookNum() {
		return bookNum;
	}
	@Override
	public String toString() {
		return "도서번호: " + bookNum + ", 도서이름: " + bookName + ", 도서가격: " + price;
	}
	@Override
	public int compareTo(Book book) { // 도서명 오름차순으로 정렬
		return bookName.compareTo(book.getBookName());
	}
}

public class Assignment01 {
	public static void main(String[] args) {
		ArrayList<Book> bookList = new ArrayList<>();
		bookList.add(new Book(1,"자바의 정석",18000));
		bookList.add(new Book(2,"스프링의 정석",22000));
		bookList.add(new Book(3,"자바의 모든것",28000));
		bookList.add(new Book(4,"홍보책자",0));
		bookList.add(new Book(5,"책갈피",500));
		//1. 전체 도서정보를 출력하세요 || forEach -> Consumer<T> -> void accept​(T t) 사용
		System.out.println("=========================================");
		System.out.println("전체 도서정보를 출력합니다");
		bookList.forEach(b -> System.out.println(b));
		//2. 가격이 낮은 순으로 정렬해서 출력되도록 해보세요 || Stream.sorted -> Comparator<T> -> int compare​(T o1, T o2) 사용
		System.out.println("=========================================");
		System.out.println("가격이 낮은 순으로 정렬합니다");
		Stream<Book> s = bookList.stream();
		s.sorted((book1, book2) ->Integer.compare(book1.getPrice(), book2.getPrice()))
		.forEach(book -> System.out.println(book));
		//3. 가격이 20000이상이 도서 정보를 내림차순정렬해서 출력해 보세요 || Stream.filter -> Predicate<T> -> boolean test(T t) 사용, 이 후 필터링한 Stream을 sorted로 다시 정렬
		System.out.println("=========================================");
		System.out.println("20000원 이상의 도서를 가격 내림차순으로 정렬합니다");
		s = bookList.stream();
		Stream<Book> s_20000 = s.filter(book -> (book.getPrice() >= 20000));
		s_20000.sorted((book1, book2) -> Integer.compare(book1.getPrice(), book2.getPrice()) * -1)
		.forEach(book -> System.out.println(book));
		//4. 전체 도서명을 오름차순정렬해서 출력되도록 해보세요 || Stream.sorted() -> Comparable<T> -> int compareTo(T t) 사용
		System.out.println("=========================================");
		System.out.println("전체 도서를 이름 오름차순으로 정렬합니다");
		s = bookList.stream();
		s.sorted().forEach(book -> System.out.println(book));
		System.out.println("=========================================");
	}
}
