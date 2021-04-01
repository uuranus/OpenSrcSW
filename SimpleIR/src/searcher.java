import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class searcher {
	public void getSim(String post, String query) {
		//post는 index.post의 경로
		//index.post 읽어오기
		try {
			FileInputStream fileinputstream=new FileInputStream("src/data/index.post");
			ObjectInputStream objectinputstream=new ObjectInputStream(fileinputstream);
			
			Object object= objectinputstream.readObject();
			objectinputstream.close();

			HashMap hashmap=(HashMap)object;
			Iterator<String> it=hashmap.keySet().iterator();
			
			KeywordExtractor ke=new KeywordExtractor();
			KeywordList kl=ke.extractKeyword(query, true);
			
			float[] weight=new float[5];
			
			for(int i=0;i<5;i++) { //문서별 유사도 계산
				 weight[i]=calcSim(i,kl,hashmap);
			}
			
			//문서 타이틀을 가져오기 우해 Collection.xml사용
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
			
			File file=new File("src/data/Collection.xml");
			
			Document doc=docBuilder.parse(file);
			NodeList doclist=doc.getElementsByTagName("doc");
			
			getTop3(weight,doclist);
			
		}
		catch(Exception e) {
			e.printStackTrace();;
		}
	}	
	
	private float calcSim(int id, KeywordList kl,HashMap hashmap) {
		float weight=0.0f;
		for(int i=0;i<kl.size();i++) {
			String keyword=kl.get(i).getString();
			ArrayList<Float> al=(ArrayList<Float>)hashmap.get(keyword);
			if(al.contains((float)id)) {
				int index=al.indexOf((float)id);
				float wit=al.get(index+1);
				weight+=wit*1;//쿼리 형태소 가중치는 1로 고정
			}	
		}
		weight=Math.round(weight*100);
		weight=weight/100;
		return weight;
	}
	
	private void initArrayList(ArrayList al) {
		for(int i=0;i<5;i++) {
			al.add(0.0f);
		}
	}
	
	private void getTop3(float[] weight, NodeList doclist) {
		boolean isChecked[]=new boolean[5];
		float max=0.0f;
		int maxindex=0;
		int k=3;
		while(k>0) {
			max=-1.0f;
			maxindex=0;
			for(int i=0;i<weight.length;i++) {
				if(max<weight[i]) {
					if(isChecked[i])
						continue;
					max=weight[i];
					maxindex=i;
				}	
			}
			Node doc=doclist.item(maxindex);
			System.out.println(doc.getChildNodes().item(1).getTextContent());
			isChecked[maxindex]=true;
			k--;
		}
			
	}
}
