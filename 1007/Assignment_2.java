import java.util.Calendar;
import java.util.Scanner;

public class Test_Final2 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("년도 : ");
		int year = sc.nextInt();
		System.out.print("달 : ");
		int month = sc.nextInt();
		
		System.out.println("--------------------------------");
		System.out.printf("        %s년   %s월       \n", year, month);
		makingCalendar(year, month - 1);
		System.out.println("--------------------------------");
		
	}
	
	public static void makingCalendar(int year, int month){
		String [] week = {"일","월","화","수","목","금","토"};
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		int lastDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
		for(String w : week) System.out.print(w + "  ");
		System.out.println();
		String whiteSpace = "   ";
		for(int i = 0; i < calendar.get(calendar.DAY_OF_WEEK) - 1; i++) System.out.print(whiteSpace);
		System.out.print(1 + whiteSpace);
		calendar.add(calendar.DATE, 1);
		for(int i = 2; i <= lastDay; i++) {
			//if 일요일 이면 한줄 건너뛰기
			int w = calendar.get(calendar.DAY_OF_WEEK);
			if(w == 1) System.out.println();
			System.out.print(i + whiteSpace);
			calendar.add(calendar.DATE, 1);
			
		}
		System.out.println();
	}
}
