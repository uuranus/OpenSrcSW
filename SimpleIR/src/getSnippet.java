import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class getSnippet {

	public void getsnippetjava(String filename, String[] queries) throws IOException{
		File file=new File("src/data/"+filename);
		BufferedReader br=new BufferedReader(new FileReader(file));
		String line="";
		int maxcount=0;
		while((line=br.readLine())!=null) {
			String temp[]=line.split(" ");
			int count=0;
			for(int i=0;i<queries.length;i++) {
				for(int j=0;j<temp.length;j++) {
					if(queries[i].equals(temp[j])) {
						count++;
					}
				}
			}
			if(count>maxcount){
				System.out.println(line);
			}
		}
	}
}
