import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException  {
		//args[0]== java args[1]== midterm�̶�� �����ϰ� �ۼ��߾��ϴ�
		//�ǽ��ڵ�� ���� �÷Ƚ��ϴ�...
		//Ű���� ���� �˻��ϴ� �ͱ����� �����߾��ϴ�.
		if(args[2].equals("-f")){
			String filename=args[3];
			String query=args[5];
			
			String queries[]=query.split(" ");
			
			//eclipse������ Ŭ�����̸��� ���ϸ��� �����ؾ��ϴµ�
			//midterm Ŭ���������� getSnippet Ŭ������ �����ϵ��� �����Ͽ����ϴ�. 
			getSnippet gn=new getSnippet();
			gn.getsnippetjava(filename, queries);
			
			
		}

	}

}
