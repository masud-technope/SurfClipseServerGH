package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utility.RegexMatcher;
import utility.StackTraceUtils;

import core.StackTrace;

public class JsoupTest {

	/**
	 * @param args
	 * @throws  
	 */
	static String getContent(String url)
	{
		String temp=new String();
		try {
		URL u=new URL(url);
		BufferedReader br=new BufferedReader(new InputStreamReader(u.openStream()));
		String line=null;
		while((line=br.readLine())!=null)
		{
			temp+=line;
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	
	static void discard_code_stack_from_content(String pageHtml)
	{
		//code for discarding code stack from content
		Document document=Jsoup.parse(pageHtml);
		Elements elems=document.select("body");
		Element body=elems.first();
		
		Elements codestacks=body.select("code");
		String bodyhtml=body.toString();
		for(Element cs:codestacks)
		{
			String temp=bodyhtml.replace(cs.toString(), "");
			bodyhtml=temp;
		}
		//System.out.println(bodyhtml);
		Document doc2=Jsoup.parse(bodyhtml);
		System.out.println("====================");
		Element _body=doc2.select("body").first();
		//System.out.println(_body.text());
	}
	
	static void capture_the_disguised_code(String pageHtml)
	{
		//code for capturing the disguised code
		Document document=Jsoup.parse(pageHtml);
		ArrayList<Element> master=new ArrayList<>();
		Elements elems=document.select("code");
		master.addAll(elems);
		Elements elems1=document.select("pre");
		master.addAll(elems1);
		Elements elems2=document.select("blockquote");
		master.addAll(elems2);
		
		int stack_developed=0;
		for(Element elem:master)
		{
			String content=elem.text();
			if(RegexMatcher.matches_stacktrace(content)){
				//System.out.println(content);
				StackTraceUtils st_utils = new StackTraceUtils(content);
				StackTrace strace = st_utils.analyze_stack_trace();
				if (!strace.stackTraceTokens.isEmpty()) {
					stack_developed++;
					System.out.println(strace.stackTraceTokens);
					// System.out.println("Tokens:"+strace.stackTraceTokens);
				}
			}
		}
		if(stack_developed==0){
			System.err.println("Accessing p");
			Elements quotes3=document.select("p");
			for(Element quote:quotes3)
			{
				String stack=quote.text();
				try
				{
					if(RegexMatcher.matches_stacktrace(stack))
					{
						StackTraceUtils st_utils = new StackTraceUtils(stack);
						StackTrace strace = st_utils.analyze_stack_trace();
						System.out.println(strace.primaryContent);
					}else
					{
						//do nothing..
					}
					
				}catch(Exception exc){
					
				}
			}
		}
		
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
		String url="https://github.com/TooTallNate/Java-WebSocket/issues/122";
		String content=getContent(url);
		//discard_code_stack_from_content(content);
		capture_the_disguised_code(content);
		}catch(Exception exc){
			exc.printStackTrace();
		}		
	}

}
