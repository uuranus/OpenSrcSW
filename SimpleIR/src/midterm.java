import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException  {
		if(args[0].equals("-f")){
			String filename=args[1];
			String query=args[3];
			
			String queries[]=query.split(" ");
			
			//eclipse에서는 클래스이름과 파일명이 동일해야하는데
			//midterm 클래스내에서 getSnippet 클래스를 실행하도록 구성하였씁니다. 
			getSnippet gn=new getSnippet();
			gn.getsnippetjava(filename, queries);
			
		}

	}

}
