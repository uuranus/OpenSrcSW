import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class indexer {
	public void makePost(String index) throws IOException, ClassNotFoundException {
		//index.post�� �����˻� �� Ư�� �ܾ ��� �ĸ��� ���Դ����� �˷��ִ� ������(inverted file)
		
		try { //DocFactory�� ���� try catch ��
				
			//index.post �����
			FileOutputStream fileoutputstream=new FileOutputStream("src/data/index.post");
			
			ObjectOutputStream objectoutputstream=new ObjectOutputStream(fileoutputstream);
			
			HashMap<String, String> hashmap=new HashMap<String,String>();
			
			//index.xml���� �о�� �ٵ� �κе��� ���Ŀ� �°� hashmap�� �߰� 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
			
			//Collection.xml ���� �о����
			File file=new File(index);
			Document doc=docBuilder.parse(file);
			
			NodeList doclist=doc.getElementsByTagName("doc");
			int N=doclist.getLength();//�� ���� ���� ���߿� weight���� ��� �ʿ��ϴϱ� �������� ����
			for(int i=0;i<N;i++) {
				Node docchild=doclist.item(i);
				String[] text=docchild.getChildNodes().item(3).getTextContent().split("#");
				Element edocchild=(Element)docchild;
				String id=edocchild.getAttribute("id");
		
				for(int j=0;j<text.length;j++) {
					String[] temp=text[j].split(":");
					String key=temp[0];
					String frequency=id+" "+temp[1]+"#";
					
					//String value=Integer.toString(i)+" "+getWeight(temp[1],i,bodylist.getLength());
					if(hashmap.containsKey(key)) { //�̹� �ִٸ� �� body���� �߰��� �� value���� �߰�����
						String newfre=hashmap.get(key); //key���� value ��������
						newfre+=frequency;
						hashmap.put(key, newfre);
					}
					else {
						hashmap.put(key, frequency);
					}
					//weight���� �� ���� ���� �󵵸� �˾ƾ� �ϱ� ������ �ϴ��� �� ������ �󵵼��� ����
				}
			}
			
			//hashmap�� ��� key���� ���� ����ġ�� ���
			Iterator<String> iter=hashmap.keySet().iterator();
			while(iter.hasNext()) {
				String key=iter.next();
				String value=(String)hashmap.get(key);
				
				String temp[]=value.split("#");
				String[] newvalue=new String[5]; //null������ �ʱ�ȭ
				int df=temp.length; //���� ���� Ƚ�� 
				for(int i=0;i<df;i++) {
					String[] temp2=temp[i].split(" ");
					int temp0=Integer.parseInt(temp2[0]);
					newvalue[temp0]=getWeight(temp2[1],df,N);
				}
				

				String strnewvalue=makeNewvalueStr(newvalue);
				hashmap.put(key,strnewvalue);
			}
		
			objectoutputstream.writeObject(hashmap);
			objectoutputstream.close();
			//hashmap index.post�� ����
			

			//index.post �о����
			FileInputStream fileinputstream=new FileInputStream("src/data/index.post");
			ObjectInputStream objectinputstream=new ObjectInputStream(fileinputstream);
			
			Object object= objectinputstream.readObject();
			objectinputstream.close();
			
			System.out.println("�о�� ��ü�� type="+object.getClass());
			
			HashMap hashmap2=(HashMap)object;
			Iterator<String> it=hashmap2.keySet().iterator();
			
			while(it.hasNext()) {
				String key=it.next();
				String value=(String)hashmap2.get(key);
				System.out.println(key+"="+value);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String getWeight(String tf,int df,int N) {
		//tf �� �ش� �������� ���� Ƚ�� df�� ��� �������� �����ϴ��� N�� �� ������ ����
		int itf=Integer.parseInt(tf);
		double weight=itf*Math.log10((double)N/df);
		return Double.toString(weight);
	}
	
	private String makeNewvalueStr(String[] newvalue) {
		String strnewvalue="";
		for(int i=0;i<5;i++) {
			
			String temp2="";
			if(newvalue[i]==null)
				temp2="0.00";
			else
				temp2=newvalue[i];
				
			if(i==4) 
				strnewvalue+=Integer.toString(i)+" "+ temp2;
			else
				strnewvalue+=Integer.toString(i)+" "+ temp2+" ";
	
		}

		return strnewvalue;
	}
}
