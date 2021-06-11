package naverAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class week15 {

	public static void main(String[] args) {
		String clientId="vIEyK1tPBEdp54KtiYQs";
		String clientSecret="2AjhMdZDqn";
		String text;
		HttpURLConnection con;
		try {
			Scanner s= new Scanner(System.in);
			System.out.print("검색어를 입력하세요: ");
			String search=s.nextLine();
			text = URLEncoder.encode(search,"UTF-8");
			String apiURL="https://openapi.naver.com/v1/search/movie.json?query="+text;
			URL url=new URL(apiURL);
			con= (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			
			int responseCode=con.getResponseCode();
			BufferedReader br;
			if(responseCode==200) { //정상 호출
				br= new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
			else { //에러 발생
				br= new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response=new StringBuffer();
			while((inputLine=br.readLine())!=null) {
				response.append(inputLine);
			}

			br.close();
			
			JSONParser jsonParser=new JSONParser();
			JSONObject jsonObject= (JSONObject)jsonParser.parse(response.toString());

			if(jsonObject.get("errorMessage")!=null) {
				System.out.println("인증에 실패했습니다.");
				return;
			}
			
			JSONArray infoArray=(JSONArray)jsonObject.get("items");

			for(int i=0;i<infoArray.size();i++) {
				System.out.println("=item_"+i+"==============================");
				JSONObject itemObject=(JSONObject)infoArray.get(i);
				System.out.println("title:\t"+itemObject.get("title"));
				System.out.println("Link:\t"+itemObject.get("link"));
				System.out.println("image:\t"+itemObject.get("image"));
				System.out.println("subtitle:\t"+itemObject.get("subtitle"));
				System.out.println("pubDate:\t"+itemObject.get("pubDate"));
				System.out.println("director:\t"+itemObject.get("director"));
				System.out.println("actor:\t"+itemObject.get("actor"));
				System.out.println("userRating:\t"+itemObject.get("userRating")+"\n");
			}
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		
	}

}
