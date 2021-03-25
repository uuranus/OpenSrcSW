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
		//index.post는 정보검색 시 특정 단어가 어느 파링레 나왔는지를 알려주는 역파일(inverted file)
		
		try { //DocFactory에 대한 try catch 문
				
			//index.post 만들기
			FileOutputStream fileoutputstream=new FileOutputStream("src/data/index.post");
			
			ObjectOutputStream objectoutputstream=new ObjectOutputStream(fileoutputstream);
			
			HashMap<String, String> hashmap=new HashMap<String,String>();
			
			//index.xml에서 읽어온 바디 부분들을 형식에 맞게 hashmap에 추가 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
			
			//Collection.xml 파일 읽어오기
			File file=new File(index);
			Document doc=docBuilder.parse(file);
			
			NodeList doclist=doc.getElementsByTagName("doc");
			int N=doclist.getLength();//총 문서 수는 나중에 weight계산시 계속 필요하니까 전역변수 선언
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
					if(hashmap.containsKey(key)) { //이미 있다면 전 body에서 추가된 것 value값만 추가하자
						String newfre=hashmap.get(key); //key값의 value 가져오기
						newfre+=frequency;
						hashmap.put(key, newfre);
					}
					else {
						hashmap.put(key, frequency);
					}
					//weight계산시 총 문서 출현 빈도를 알아야 하기 때문에 일단은 각 문서의 빈도수만 저장
				}
			}
			
			//hashmap의 모든 key값에 대해 가중치갑 계산
			Iterator<String> iter=hashmap.keySet().iterator();
			while(iter.hasNext()) {
				String key=iter.next();
				String value=(String)hashmap.get(key);
				
				String temp[]=value.split("#");
				String[] newvalue=new String[5]; //null값으로 초기화
				int df=temp.length; //문서 출현 횟수 
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
			//hashmap index.post에 저장
			

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
				String value=(String)hashmap2.get(key);
				System.out.println(key+"="+value);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String getWeight(String tf,int df,int N) {
		//tf 는 해당 문서에서 등장 횟수 df는 몇개의 문서에서 등장하는지 N은 총 문서의 개수
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
