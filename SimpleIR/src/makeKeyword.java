import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class makeKeyword {

	public void makeKeywordxml(String collection) {
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
			
			//Collection.xml 파일 읽어오기
			File file=new File(collection);
			org.w3c.dom.Document doc=docBuilder.parse(file);
		
			if(file.isFile()) {
				
				//xml parsing
				XPathFactory xpathFactory=XPathFactory.newInstance();
				XPath xpath=xpathFactory.newXPath();
				XPathExpression expr=xpath.compile("//body");
				NodeList nodelist=(NodeList) expr.evaluate(doc,XPathConstants.NODESET);
				
				//body 수만큼
				for(int i=0;i<nodelist.getLength();i++) {
					Node node=nodelist.item(i);
					String text=node.getTextContent();
					//body에 있던 텍스트를 가져와 형태소 분석
					KeywordExtractor ke=new KeywordExtractor();
					KeywordList kl=ke.extractKeyword(text, true);
					text="";
					for(int j=0;j<kl.size();j++) {
						Keyword kwrd=kl.get(j);
						if(j==kl.size()-1) {
							text+=kwrd.getString()+":"+kwrd.getCnt();
						}
						else {
							text+=kwrd.getString()+":"+kwrd.getCnt()+"#";
						}
					}
					
					//body text 값 변경
					node.setTextContent(text);
				}
			}	

			//xml파일로 쓰기
			TransformerFactory transformerFactory= TransformerFactory.newInstance();

			Transformer transformer=transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");

			DOMSource source=new DOMSource(doc);
			StreamResult result=new StreamResult(new FileOutputStream(new File("src/data/index.xml")));

			transformer.transform(source,result);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
