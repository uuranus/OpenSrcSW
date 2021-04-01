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

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class indexer {
	HashMap<String, ArrayList> hashmap=new HashMap<String,ArrayList>();
	int N;
	public void makePost(String index) throws IOException, ClassNotFoundException {
		//index.post�� �����˻� �� Ư�� �ܾ ��� �ĸ��� ���Դ����� �˷��ִ� ������(inverted file)
		
		try { //DocFactory�� ���� try catch ��
				
			//index.post �����
			FileOutputStream fileoutputstream=new FileOutputStream("src/data/index.post");
			
			ObjectOutputStream objectoutputstream=new ObjectOutputStream(fileoutputstream);
			
			
			
			//index.xml���� �о�� �ٵ� �κе��� ���Ŀ� �°� hashmap�� �߰� 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
			
			//Collection.xml ���� �о����
			File file=new File(index);
			Document doc=docBuilder.parse(file);
			
			NodeList doclist=doc.getElementsByTagName("doc");
			N=doclist.getLength();//�� ���� ���� ���߿� weight���� ��� �ʿ��ϴϱ� �������� ����
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
			System.out.println(hashmap.get("���"));
			objectoutputstream.writeObject(hashmap);
			objectoutputstream.close();
			//hashmap index.post�� ����
			
			/*
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
			*/
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
	
	float[] similarity; //���絵 ���� �迭 �̸� ����
	public void getQeury(String qr) {
		similarity=new float[N];
		initArray(similarity);
		KeywordExtractor ke=new KeywordExtractor();
		KeywordList kl=ke.extractKeyword(qr, true);
		
		String keyword[]=new String[kl.size()]; //�м� �ܾ� ����
		
		for(int i=0;i<kl.size();i++) {
			Keyword kwrd=kl.get(i);
			CalcSim(kwrd.getString(),kwrd.getCnt());
			//keyword[i]=kwrd.getString()+" "+kwrd.getCnt();
		}
		
		for(int i=0;i<3;i++){
			System.out.println(similarity[i]);
		}
	}
	
	private void CalcSim(String keyword,int weight) { //inner product �� ���絵 ��ȯ
		System.out.println(keyword+" "+Integer.toString(weight));
		for(int i=0;i<N;i++) {
			ArrayList<Float> al=hashmap.get(keyword);
			if(al.contains((float)i)) {	
				int index=al.indexOf((float)i);
				System.out.println(i);
				float wit=(float)al.get(index+1);
				System.out.println(similarity[i]);
				similarity[i]+=weight*wit;
				System.out.println(similarity[i]);
			}
			//System.out.println(al.size());
		}
		
	}
	private void initArray(float[] sim) {
		for(int i=0;i<sim.length;i++) {
			sim[i]=0.0f;
		}
	}

}
