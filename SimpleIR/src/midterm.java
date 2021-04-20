import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException  {
		//args[0]== java args[1]== midterm이라고 생각하고 작성했씁니다
		//실습코드랑 같이 올렸습니다...
		//키워드 개수 검사하는 것까지만 구현했씁니다.
		if(args[2].equals("-f")){
			String filename=args[3];
			String query=args[5];
			
			String queries[]=query.split(" ");
			
			//eclipse에서는 클래스이름과 파일명이 동일해야하는데
			//midterm 클래스내에서 getSnippet 클래스를 실행하도록 구성하였씁니다. 
			getSnippet gn=new getSnippet();
			gn.getsnippetjava(filename, queries);
			
			
		}

	}

}
