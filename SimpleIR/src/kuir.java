
public class kuir {

	public static void main(String[] args) {
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
		}
		
	}
	

}
