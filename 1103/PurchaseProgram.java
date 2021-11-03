import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
<< 구매테이블 >> 구매번호(PK),구매자아이디,상품명,가격,수량,구매일
<< 결제테이블 >> 결제번호(PK) 구매번호(FK) 결제금액 결제수단
위의 정보를 갖는 테이블을 만들고 구매기능, 트랜잭션 기능을 구현 (PreparedStatement를 사용)

[실행결과]
1. 제품구매  2.구매정보수정 3.구매정보조회  4.구매취소 
..

1.제품구매: 구매자아이디,상품명,가격,수량,결제수단 입력 받아서 저장
2.구매정보조회: 전체구매정보 조회
3.구매정보수정: 상품명,가격,수량,결제금액,결제수단을 변경
4.구매취소: 구매정보와 결제정보가 모두 삭제

--------------------------------
DROP SEQUENCE PAYMENT_SEQ;
DROP SEQUENCE PURCHASE_SEQ;
CREATE SEQUENCE PURCHASE_SEQ;
CREATE SEQUENCE PAYMENT_SEQ;
DROP TABLE PAYMENT;
DROP TABLE PURCHASE;
CREATE TABLE PURCHASE
(
    PNUM NUMBER(4) Primary Key,
    PID VARCHAR2(10),
    ITEM NVARCHAR2(10),
    PRICE NUMBER(10),
    QUANTITY NUMBER(4),
    DAY DATE
);

CREATE TABLE PAYMENT
(
    PM_NUM NUMBER(4) Primary KEY,
    PNUM REFERENCES PURCHASE(PNUM),
    PAY NUMBER(10),
    WAY NVARCHAR2(10)
);
*/

class JDBC {
	Connection con = null;
	Scanner sc = new Scanner(System.in);
	public JDBC() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("<<드라이버 로딩완료>>");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			con = DriverManager.getConnection(url, "c##scott", "tiger");
			con.setAutoCommit(false);
			System.out.println("<<db접속 완료>>");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//DB종료(con객체 삭제)
	public void disconnect() {
		try {
			if(con != null) con.close();
			System.out.println("<db접속 종료>");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//제품구매(구매자아이디,상품명,가격,수량,결제수단 입력 받아서 저장)
	public void purchase() {
		String pid, item, way;
		int price, quantity;
		try {
			System.out.print("구매자 아이디를 입력해주세요: ");
			pid = sc.next();
			System.out.print("상품명을 입력해주세요: ");
			item = sc.next();
			System.out.print("가격을 입력해주세요: ");
			price = sc.nextInt();
			System.out.print("수량을 입력해주세요: ");
			quantity = sc.nextInt();
			System.out.print("결제수단을 입력해주세요: ");
			way = sc.next();
		}catch(InputMismatchException e) {
			System.out.println("잘못된 입력입니다. 처음으로 돌아갑니다.");
			sc.nextLine();
			return;
		}
		String sql1 = "Insert into PURCHASE values(PURCHASE_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
		//멀티스레드 환경에서 이거 오류생길것같은데.. Check
		String sql2 = "Insert into PAYMENT values(PAYMENT_SEQ.NEXTVAL, PURCHASE_SEQ.CURRVAL, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			//PURCHASE 테이블에 INSERT
			pstmt = con.prepareStatement(sql1);
			pstmt.setString(1, pid);
			pstmt.setString(2, item);
			pstmt.setInt(3, price);
			pstmt.setInt(4, quantity);
			int n = pstmt.executeUpdate();
			System.out.println("<INSERT INTO PURCHASE::" + n + "개 ROWS 완료>");
			//PAYMENT 테이블에 INSERT
			pstmt = con.prepareStatement(sql2);
			pstmt.setInt(1, price*quantity);
			pstmt.setString(2, way);
			n =  pstmt.executeUpdate();
			System.out.println("<INSERT INTO PAYMENT::" + n + "개 ROWS 완료>");
			con.commit();
			System.out.println("<PURCHASE로직 커밋완료>");
		}catch(SQLException e) {
			try {
				System.out.println("<오류발생>");
				con.rollback();
				System.out.println("<롤백완료>");
			}catch(SQLException e1) {
				e1.printStackTrace();
			}	
		}finally {
			try {
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//구매정보수정(상품명,가격,수량,결제금액,결제수단을 변경)
	public void update() {
		int pnum, price, quantity;
		String item, way;
		try {
			System.out.print("변경하고 싶은 결제번호를 입력하세요: ");
			pnum = sc.nextInt();
			System.out.print("변경할 상품명을 입력해주세요: ");
			item = sc.next();
			System.out.print("변경할 가격을 입력해주세요: ");
			price = sc.nextInt();
			System.out.print("변경할 수량을 입력해주세요: ");
			quantity = sc.nextInt();
			System.out.print("변경할 결제수단을 입력해주세요: ");
			way = sc.next();
		}catch(InputMismatchException e) {
			System.out.println("잘못된 입력입니다. 처음으로 돌아갑니다");
			sc.nextLine();
			return;
		}
		String sql1 = "Update PURCHASE set ITEM = ?, PRICE = ?, QUANTITY = ? where PNUM = ?";
		String sql2 = "Update PAYMENT set way = ?, pay = ? where PNUM = ?";
		PreparedStatement pstmt = null;
		try {
			//PURCHASE 테이블 UPDATE
			pstmt = con.prepareStatement(sql1);
			pstmt.setString(1, item);
			pstmt.setInt(2, price);
			pstmt.setInt(3, quantity);
			pstmt.setInt(4, pnum);
			int n = pstmt.executeUpdate();
			System.out.println("<UPDATE PURCHASE::" + n + "개 ROWS 완료>");
			//PAYMENT 테이블에 INSERT
			pstmt = con.prepareStatement(sql2);
			pstmt.setString(1, way);
			pstmt.setInt(2, price*quantity);
			pstmt.setInt(3, pnum);
			n =  pstmt.executeUpdate();
			System.out.println("<UPDATE PAYMENT::" + n + "개 ROWS 완료>");
			con.commit();
			System.out.println("<UPDATE로직 커밋완료>");
		}catch(SQLException e) {
			try {
				System.out.println("<오류발생>");
				con.rollback();
				System.out.println("<롤백완료>");
			}catch(SQLException e1) {
				e1.printStackTrace();
			}	
		}finally {
			try {
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	//구매정보조회(전체구매정보 조회)
	public void printlist() {
		PreparedStatement pstmt = null;
		String sql = "Select * from PURCHASE NATURAL JOIN PAYMENT";
		try {
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				do {			
					int pnum = rs.getInt("PNUM");
					String pid = rs.getString("PID");
					String item = rs.getString("ITEM");
					int price = rs.getInt("PRICE");
					int quantity = rs.getInt("QUANTITY");
					Date day = rs.getDate("DAY");
					int pay = rs.getInt("PAY");
					String way = rs.getString("WAY");
					System.out.println("결제번호:" + pnum + ", 구매자:" + pid + ", 상품명:" + item + ", 가격:" + price
									  + ", 수량:" + quantity + ", 구입일:" + day + ", 총결제금액:" + pay
									  + ", 결제방법:" + way);
				}while(rs.next());
			}
			else {
				System.out.println("구매정보가 존재하지 않습니다.");
			}
		}catch(SQLException e) {
			System.out.println("<오류발생>");
			System.out.println("<명령취소>");
		}finally{
			try {
				if(pstmt != null)pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//구매취소(구매정보와 결제정보가 모두 삭제)
	public void delete() {
		int pnum;
		try {
			System.out.print("삭제하고 싶은 결제번호를 입력하세요: ");
			pnum = sc.nextInt();
		}catch(InputMismatchException e) {
			System.out.println("잘못된 입력입니다. 처음으로 돌아갑니다.");
			sc.nextLine();
			return;
		}
		String sql1 = "Delete from PAYMENT where PNUM = ?";
		String sql2 = "Delete from PURCHASE where PNUM = ?";
		
		PreparedStatement pstmt = null;
		try {
			//PURCHASE 테이블에서 DELETE
			pstmt = con.prepareStatement(sql1);
			pstmt.setInt(1, pnum);
			int n = pstmt.executeUpdate();
			System.out.println("<DELETE FROM PAYMENT::" + n + "개 ROWS 완료>");
			//PAYMENT 테이블에서 DELETE
			pstmt = con.prepareStatement(sql2);
			pstmt.setInt(1, pnum);
			n = pstmt.executeUpdate();
			System.out.println("<DELETE FROM PURCHASE::" + n + "개 ROWS 완료>");
			con.commit();
			System.out.println("<DELETE로직 커밋완료>");
		}catch(SQLException e) {
			try {
				System.out.println("<오류발생>");
				con.rollback();
				System.out.println("<롤백완료>");
			}catch(SQLException e1) {
				e1.printStackTrace();
			}	
		}finally {
			try {
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

public class PurchaseProgram {
	public static void main(String[] args) {
		JDBC jdbc = new JDBC();
		boolean flag = true;
		Scanner sc = new Scanner(System.in);
		while(flag) {
			try {
				
				System.out.println("-------------------------------------------------------------");
				System.out.println("1. 제품구매  2.구매정보수정 3.구매정보조회  4.구매취소  5. 프로그램종료");
				int num = sc.nextInt();
				switch(num) {
					case 1:
						jdbc.purchase();
						break;
					case 2:
						jdbc.update();
						break;
					case 3:
						jdbc.printlist();
						break;
					case 4:
						jdbc.delete();
						break;
					case 5:
						flag = false;
						jdbc.disconnect();
						System.out.println("프로그램을 종료하였습니다.");
						break;
					default:
						System.out.println("잘못된 번호를 입력하셨습니다. 다시 입력해주세요.");
				}
			}catch(InputMismatchException ie) {
				System.out.println("잘못된 번호를 입력하셨습니다. 다시 입력해주세요.");
				continue;
			}
		}
	}
}
