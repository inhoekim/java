/*
   디렉토리 복사하는 프로그램을 작성하기
   예)
   복사할 폴더
   c:\java
   복사본 폴더
   c:\java_copy
   java폴더가 java_copy폴더에 복사됨 
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Assignment1 {
	// 기존 폴더의 경로
	private static String dirRoot;
	// 복사본 폴더의 경로
	private static String newDirRoot;
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("복사할 폴더의 경로를 입력하세요");
		dirRoot = sc.next();
		File dir = new File(dirRoot);
		// 올바르지 않는 경로는 바로 종료
		if(!dir.exists()) {
			System.out.println("\""+ dirRoot + "\"는 존재하지 않는 경로입니다");
			return;
		}
		// 경로에 해당하는 폴더를 찾은 경우 진행
		dirRoot = dir.getAbsolutePath(); // 사용자가 입력한 경로는 '\' 개수같은 것이 통일되지 않았을 확률이 높으므로 getAbsolutePath로 확실히 처리해줌
		System.out.println("복사본 폴더의 이름을 결정해주세요. 저장경로는 기존에 입력하신 경로입니다");
		String newDir = sc.next();
		
		newDirRoot = dirRoot + "\\..\\" + newDir; // 상대주소 결정법 '..' 을 이용해서 뒤로 한번 이동했다가 새로운 폴더이름인 newDir를 추가해주는 방식
		//작업진행
		makeDir(newDirRoot); // newDirRoot 폴더부터 생성하고 시작
		copyDir(dir);
		System.out.println("=================================================");
		System.out.println("작업이 종료되었습니다. 프로그램을 종료합니다");
	}
	
	public static void copyDir(File dir) {
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				String newDirPath = makePath(f.getAbsolutePath());
				makeDir(newDirPath);
				copyDir(f);
			}
			if(f.isFile()) {
				copyOneFile(f);
			}
		}
	}
	
	public static void copyOneFile(File f) {
		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			String newDirPath = makePath(f.getAbsolutePath());
			br = new BufferedReader(new FileReader(f));
			bw = new BufferedWriter(new FileWriter(newDirPath));
			String temp;
			while((temp = br.readLine()) != null) {
				bw.write(temp);
			}
			System.out.println(f.getName() + " 파일을 복사했습니다.");
		}catch(IOException ie) {
			System.out.println(ie.getMessage());
		}finally {
			try {
				if(bw != null) bw.close();
				if(br != null) br.close();
			}catch(IOException ie) {
				System.out.println(ie.getMessage());
			}
		}
	}
	
	public static void makeDir(String newDirPath) {
		File newDir = new File(newDirPath);
		if(!newDir.exists()) {
			newDir.mkdir(); //File.mkdir() : 디렉토리를 만들어주는 메서드
			System.out.println(newDir.getName() + " 폴더를 생성했습니다");
		}
	}
	//복사할 폴더의 경로에 알맞게 기존 파일의 path를 바꿔주는 함수
	public static String makePath(String path) {
		path = path.substring(dirRoot.length());// 기존 경로인 dirRoot가 포함된 부분을 잘라낸 뒤 newDirRoot경로의 뒤에 이어붙인다
		String newDirPath = newDirRoot + path;
		return newDirPath;
	}
}
