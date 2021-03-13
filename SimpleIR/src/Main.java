import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder=docFactory.newDocumentBuilder();
			
			//collection element
			org.w3c.dom.Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);
			
			org.w3c.dom.Element docs=doc.createElement("docs");
			doc.appendChild(docs);
			
			//파일 읽어오기
			String path="src/data/";
			File dir=new File(path);
			File files[]=dir.listFiles();
			
			int i=0;
			for(File file : files) {
				if(file.isFile()) {
	
			        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			        String line="";
			        String html="";
					while((line=br.readLine())!=null) {
						html+=line+"\n";
					}
					Document docu=Jsoup.parse(html);
					
					
					String tle=docu.select("title").text();
					System.out.println(tle);
					
					
					String text="";
					Elements elements=docu.select("p");
					for(Element el : elements) {
						text+=el.text()+"\n";
					}
					

					//code element
					org.w3c.dom.Element code=doc.createElement("doc");
					docs.appendChild(code);
					code.setAttribute("id",Integer.toString(i)); //i는 파일 순서

					//title element
					org.w3c.dom.Element title=doc.createElement("title");
					code.appendChild(title);
					title.appendChild(doc.createTextNode(tle));

					//body element
					org.w3c.dom.Element body=doc.createElement("body");
					code.appendChild(body);
					body.appendChild(doc.createTextNode(text));

					
				}
				i++;
			}
			

			//xml파일로 쓰기
			TransformerFactory transformerFactory= TransformerFactory.newInstance();

			Transformer transformer=transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");

			DOMSource source=new DOMSource(doc);
			StreamResult result=new StreamResult(new FileOutputStream(new File("src/Collection.xml")));

			transformer.transform(source,result);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
