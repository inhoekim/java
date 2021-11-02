import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * 회원가입,조회,삭제,수정,전체조회의 기능을 갖는 프로그램 작성

<< 테이블 >>
아이디(PK) varchar2,비밀번호 varchar2, 이메일 varchar2, 전화번호 varchar2, 가입일 regdate
--
drop table mems;
create table mems
(
	id varchar2(10) primary key,
	pwd varchar2(10),
	email varchar2(15),
	phone varchar2(15),
	regdate date
);
--
#회원가입할때는 아이디중복 검사를 하세요.
예) 사용할 아이디입력 : song
song는 이미 사용중인 아이디입니다.

#검색은 아이디로 조회
 */

class JDBC{
	Connection con = null;
	Statement stmt = null;
	Scanner sc = new Scanner(System.in);
	//생성자
	public JDBC() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("<DB드라이버 로딩완료>");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			con = DriverManager.getConnection(url, "c##scott", "tiger");
			stmt = con.createStatement();
			System.out.println("<DB 접속완료>");
		} catch (ClassNotFoundException cnf) {
			System.out.println(cnf.getMessage());
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}
	}
	//DB 연결종료
	public void disconnect() {
		try {
			if(con == null) con.close();
			if(stmt == null) stmt.close();
		}catch(SQLException se) {
			System.out.println(se.getMessage());
		}
		System.out.println("<DB작업을 정상적으로 종료하였습니다>");
	}
	//Insert into MEMS (회원가입)
	public void insert() throws SQLException{
		System.out.print("아이디를 입력하세요: ");
		String id = sc.next();
		//아이디 중복검사(존재여부 검사)
		if(check_duplicate(id)) {
			System.out.println(id + "는 중복 아이디입니다. 다른 아이디를 이용해주세요.");
			return;
		}
		System.out.print("비밀번호를 입력하세요: ");
		String pwd = sc.next();
		System.out.print("이메일을 입력하세요: ");
		String email = sc.next();
		System.out.print("전화번호를 입력하세요: ");
		String phone = sc.next();
		//String java.lang.String.format(String format, Object... args)
		String sql = String.format("INSERT INTO MEMS VALUES('%s', '%s', '%s', '%s', SYSDATE)", 
				id, pwd, email, phone);
		int n = stmt.executeUpdate(sql);
		System.out.println("<ROWS(" + n + "개) INSERT 완료>");
	}
	//Delete from MEMS where ID = USER_INPUT (회원탈퇴)
	public void delete() throws SQLException {
		System.out.println("아이디를 입력하세요.");
		String id = sc.next();
		//아이디 중복검사(존재여부 검사)
		if(!check_duplicate(id)) {
			System.out.println("해당 아이디는 존재하지 않습니다.");
			return;
		}
		String sql = String.format("DELETE FROM MEMS WHERE ID = '%s'", id);
		stmt.executeUpdate(sql);
		System.out.println(id + " 회원의 탈퇴가 정상적으로 이루어졌습니다.");
	}
	//Update MEMS set PWD = USER_INPUT (비밀번호 변경)
	public void update() throws SQLException {
		System.out.println("아이디를 입력하세요.");
		String id = sc.next();
		//아이디 중복검사(존재여부 검사)
		if(!check_duplicate(id)) {
			System.out.println("해당 아이디는 존재하지 않습니다.");
			return;
		}
		System.out.println("변경할 비밀번호를 입력하세요.");
		String pwd = sc.next();
		String sql = String.format("UPDATE MEMS SET PWD = '%s' WHERE ID = '%s'", pwd, id);
		stmt.executeUpdate(sql);
		System.out.println(id + "의 비밀번호를 정상적으로 변경하였습니다.");
	}
	//Select * from MEMS
	public void select_all() throws SQLException{
		String sql = "SELECT * FROM MEMS";
		ResultSet rs = stmt.executeQuery(sql);
		System.out.println("<전체회원정보 출력>");
		while(rs.next()) {
			String id = rs.getString("ID");
			String pwd = rs.getString("PWD");
			String email = rs.getString("EMAIL");
			String phone = rs.getString("PHONE");
			Date regdate = rs.getDate("REGDATE");
			System.out.println("아이디:" + id + ", 비밀번호:" + pwd + ", 이메일:" + email + ", 번호:" + phone + ", 등록일:" + regdate);
		}
		System.out.println("<전체회원정보 출력완료>");
	}
	//Select * from MEMS M where M.ID = USER_INPUT
	public void select_id() throws SQLException {
		System.out.println("검색할 회원의 아이디를 입력하세요: ");
		String id = sc.next();
		String sql = String.format("SELECT * FROM MEMS M WHERE ID = '%s'", id);
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			String pwd = rs.getString("PWD");
			String email = rs.getString("EMAIL");
			String phone = rs.getString("PHONE");
			Date regdate = rs.getDate("REGDATE");
			System.out.println("<회원정보출력>");
			System.out.println("아이디:" + id + ", 비밀번호:" + pwd + ", 이메일:" + email + ", 번호:" + phone + ", 등록일:" + regdate);
		}
		else {
			System.out.println("존재하지 않는 회원입니다.");
		}
	}
	//SELECT * from MEMS WHERE id = USER_INPUT
	//위 쿼리에 해당하는 ROW가 있는지 검사하여 Boolean값을 리턴(있으면 true 없으면 false)
	public boolean check_duplicate(String userInput) throws SQLException{
		String sql = String.format("SELECT * FROM MEMS M WHERE ID = '%s'", userInput);
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) return true;
		else return false;
	}
}

public class Final {
	public static void main(String[] args) {
		JDBC jdbc = new JDBC();
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		while(flag) {
			System.out.println("-----------------------------------------------------------");
			System.out.println("0.프로그램종료 1.회원가입 2.회원조회 3.회원탈퇴 4.비밀번호수정 5.전체조회 ");
			try {
				int n = sc.nextInt();
				switch(n) {
					case 0:
						flag = false;
						break;
					case 1:
						jdbc.insert();
						break;
					case 2:
						jdbc.select_id();
						break;
					case 3:
						jdbc.delete();
						break;
					case 4:
						jdbc.update();
						break;
					case 5:
						jdbc.select_all();
						break;
					default :
						System.out.println("번호를 잘못입력하셨습니다.");
					}
			}catch(InputMismatchException ie) {
				System.out.println("번호를 잘못입력하셨습니다.");
				sc.next();
				continue;
			}catch(SQLException se) {
				System.out.println(se.getMessage());
			}
		}
		jdbc.disconnect();
		System.out.println("회원가입 프로그램이 종료되었습니다.");
	}
}
