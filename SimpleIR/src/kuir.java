
import java.io.IOException;
import java.util.Scanner;


public class kuir {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		if(args.length==0) {
			System.out.println("�Ű������� �Է����ּ���");
			System.exit(0);
		}
		else {
			if(args[0].equals("-c")) { //Collection.xml �� �� ���丮 ���
				String directory=args[1];
				makeCollection mc=new makeCollection();
				mc.makeCollectionxml(directory);
			}
			else if(args[0].equals("-k")) { //index.xml ����� Collection.xml ������ ������
				String collection=args[1];
				makeKeyword mk=new makeKeyword();
				mk.makeKeywordxml(collection);
			}
			else if(args[0].equals("-i")) {
				String index=args[1];
				indexer mp=new indexer();
				mp.makePost(index);
				Scanner scan=new Scanner(System.in);
				System.out.println("query�� �Է��ϼ���");
				String query=scan.nextLine();
				mp.getQeury(query);
			}
			else if(args[0].equals("-s")) {
				String post=args[1];
				searcher sc=new searcher();
				sc.
			}

		}
		
	}
	

}
