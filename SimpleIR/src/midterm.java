import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException  {
		if(args[0].equals("-f")){
			String filename=args[1];
			String query=args[3];
			
			String queries[]=query.split(" ");
			
			//eclipse������ Ŭ�����̸��� ���ϸ��� �����ؾ��ϴµ�
			//midterm Ŭ���������� getSnippet Ŭ������ �����ϵ��� �����Ͽ����ϴ�. 
			getSnippet gn=new getSnippet();
			gn.getsnippetjava(filename, queries);
			
		}

	}

}
