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
		//index.post는 정보검색 시 특정 단어가 어느 파링레 나왔는지를 알려주는 역파일(inverted file)
		
		try { //DocFactory에 대한 try catch 문
				
			//index.post 만들기
			FileOutputStream fileoutputstream=new FileOutputStream("src/data/index.post");
			
			ObjectOutputStream objectoutputstream=new ObjectOutputStream(fileoutputstream);
			
			
			
			//index.xml에서 읽어온 바디 부분들을 형식에 맞게 hashmap에 추가 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
			
			//Collection.xml 파일 읽어오기
			File file=new File(index);
			Document doc=docBuilder.parse(file);
			
			NodeList doclist=doc.getElementsByTagName("doc");
			N=doclist.getLength();//총 문서 수는 나중에 weight계산시 계속 필요하니까 전역변수 선언
			for(int i=0;i<N;i++) {
				Node docchild=doclist.item(i);
				String[] text=docchild.getChildNodes().item(3).getTextContent().split("#");
				Element edocchild=(Element)docchild;
				Float id=Float.parseFloat(edocchild.getAttribute("id"));
		
				for(int j=0;j<text.length;j++) {
					String[] temp=text[j].split(":");
					String key=temp[0];
					Float frequency=Float.parseFloat(temp[1]);
					
					if(hashmap.containsKey(key)) { //이미 있다면 전 body에서 추가된 것 value값만 추가하자
						ArrayList altemp=hashmap.get(key); //key값의 value 가져오기 문서 아이디 값, 빈도수 2개씩 저장
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
					//weight계산시 총 문서 출현 빈도를 알아야 하기 때문에 일단은 각 문서의 빈도수만 저장
				}
			}
			
			//hashmap의 모든 key값에 대해 가중치갑 계산
			Iterator<String> iter=hashmap.keySet().iterator();
			while(iter.hasNext()) {
				String key=iter.next();
				ArrayList<Float> values=(ArrayList)hashmap.get(key);
				
				int df=values.size()/2; //문서 출현 횟수 아이디,빈도수 2개씩 저장했으므로 나누기 2
				for(int i=1;i<values.size();i+=2) { //2개씩 저장했으니까 빈도수 2칸 차이로 저장되어있음
					float tf=values.get(i);
					values.set(i,getWeight(tf,df,N)); //빈도수 -> 가중치로 변경해서 저장
				}
				
			}
			System.out.println(hashmap.get("라면"));
			objectoutputstream.writeObject(hashmap);
			objectoutputstream.close();
			//hashmap index.post에 저장
			
			/*
			//index.post 읽어오기
			FileInputStream fileinputstream=new FileInputStream("src/data/index.post");
			ObjectInputStream objectinputstream=new ObjectInputStream(fileinputstream);
			
			Object object= objectinputstream.readObject();
			objectinputstream.close();
			
			System.out.println("읽어올 객체의 type="+object.getClass());
			
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
		//tf 는 해당 문서에서 등장 횟수 df는 몇개의 문서에서 등장하는지 N은 총 문서의 개수
		float weight=Math.round(tf*Math.log((double)N/df)*100);
		weight=weight/100;
		return weight;
	}
	
	float[] similarity; //유사도 담을 배열 미리 선언
	public void getQeury(String qr) {
		similarity=new float[N];
		initArray(similarity);
		KeywordExtractor ke=new KeywordExtractor();
		KeywordList kl=ke.extractKeyword(qr, true);
		
		String keyword[]=new String[kl.size()]; //분석 단어 저장
		
		for(int i=0;i<kl.size();i++) {
			Keyword kwrd=kl.get(i);
			CalcSim(kwrd.getString(),kwrd.getCnt());
			//keyword[i]=kwrd.getString()+" "+kwrd.getCnt();
		}
		
		for(int i=0;i<3;i++){
			System.out.println(similarity[i]);
		}
	}
	
	private void CalcSim(String keyword,int weight) { //inner product 후 유사도 반환
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
