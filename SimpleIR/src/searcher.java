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
		//post�� index.post�� ���
		//index.post �о����
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
			
			for(int i=0;i<5;i++) { //������ ���絵 ���
				 weight[i]=calcSim(i,kl,hashmap);
			}
			
			//���� Ÿ��Ʋ�� �������� ���� Collection.xml���
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
		float[] weight=innerProduct(id, kl, hashmap);
		double qsize= Math.sqrt(kl.size()); //���� ���¼��� �󵵼��� 1�� �����̱� ������ ��Ʈ ���¼� ������ ����
		double dsize=0;
		for(int i=1;i<weight.length;i++) {		
			dsize+=Math.pow(weight[i], 2);
		}
		dsize= Math.sqrt(dsize);
		float result=Math.round((weight[0]/(qsize*dsize))*100);
		result=result/100;
		return result;
	}
	private float[] innerProduct(int id, KeywordList kl,HashMap hashmap) {
		float[] weight=new float[kl.size()+1];
		for(int i=0;i<kl.size();i++) {
			String keyword=kl.get(i).getString();
			ArrayList<Float> al=(ArrayList<Float>)hashmap.get(keyword);
			if(al.contains((float)id)) {
				int index=al.indexOf((float)id);
				float wit=al.get(index+1);
				weight[0]+=wit*1;//���� ���¼� ����ġ�� 1�� ����
				weight[i+1]=wit; //0�� ����ġ �� ��� 1~kl.size()������ id ������ ���� �� �ܾ��� ����ġ ����
			}	
		}
		weight[0]=Math.round(weight[0]*100);
		weight[0]=weight[0]/100;
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
			if(max==0) {
				if(k==3) { //���� ���� ���絵�� 0�̶�� �ǹ�
					System.out.println("���絵�� �ִ� ������ �������� �ʽ��ϴ�.");
				}
				break;
			}
			Node doc=doclist.item(maxindex);
			System.out.println(doc.getChildNodes().item(1).getTextContent());
			isChecked[maxindex]=true;
			k--;
		}
			
	}
}
