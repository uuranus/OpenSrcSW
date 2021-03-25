import java.io.IOException;

public class kuir {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		if(args.length==0) {
			System.out.println("매개변수를 입력해주세요");
			System.exit(0);
		}
		else {
			if(args[0].equals("-c")) { //Collection.xml 이 들어갈 디렉토리 경로
				String directory=args[1];
				makeCollection mc=new makeCollection();
				mc.makeCollectionxml(directory);
			}
			else if(args[0].equals("-k")) { //index.xml 만들기 Collection.xml 파일을 전해줌
				String collection=args[1];
				makeKeyword mk=new makeKeyword();
				mk.makeKeywordxml(collection);
			}
			else if(args[0].equals("-i")) {
				String index=args[1];
				indexer mp=new indexer();
				mp.makePost(index);
			}
		}
		
	}
	

}
