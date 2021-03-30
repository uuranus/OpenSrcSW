import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
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
			
			HashMap<String, ArrayList> hashmap=new HashMap<String,ArrayList>();
			
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
				Float id=Float.parseFloat(edocchild.getAttribute("id"));
		
				for(int j=0;j<text.length;j++) {
					String[] temp=text[j].split(":");
					String key=temp[0];
					Float frequency=Float.parseFloat(temp[1]);
					
					if(hashmap.containsKey(key)) { //�̹� �ִٸ� �� body���� �߰��� �� value���� �߰�����
						ArrayList altemp=hashmap.get(key); //key���� value �������� ���� ���̵� ��, �󵵼� 2���� ����
						altemp.add(id);
						altemp.add(frequency);
						hashmap.put(key, altemp);
					}
					else {
						ArrayList<Float> al=new ArrayList<Float>();
						al.add(id);
						al.add(frequency);
						hashmap.put(key, al);
					}
					//weight���� �� ���� ���� �󵵸� �˾ƾ� �ϱ� ������ �ϴ��� �� ������ �󵵼��� ����
				}
			}
			
			//hashmap�� ��� key���� ���� ����ġ�� ���
			Iterator<String> iter=hashmap.keySet().iterator();
			while(iter.hasNext()) {
				String key=iter.next();
				ArrayList<Float> values=(ArrayList)hashmap.get(key);
				
				int df=values.size()/2; //���� ���� Ƚ�� ���̵�,�󵵼� 2���� ���������Ƿ� ������ 2
				for(int i=1;i<values.size();i+=2) { //2���� ���������ϱ� �󵵼� 2ĭ ���̷� ����Ǿ�����
					float tf=values.get(i);
					values.set(i,getWeight(tf,df,N)); //�󵵼� -> ����ġ�� �����ؼ� ����
				}
				
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
				ArrayList<Float> values=(ArrayList)hashmap2.get(key);
				System.out.println(key+"="+values);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private float getWeight(float tf,int df,int N) {
		//tf �� �ش� �������� ���� Ƚ�� df�� ��� �������� �����ϴ��� N�� �� ������ ����
		float weight=Math.round(tf*Math.log((double)N/df)*100);
		weight=weight/100;
		return weight;
	}

}
