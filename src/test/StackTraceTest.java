package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StackTraceTest {

	/**
	 * @param args
	 */
	public StackTraceTest()
	{
		//code for loading the stack trace
	}
	protected String get_the_stackTrace(String resultContent)
	{
		
		String stacks="";
		try
		{
		Document document=Jsoup.parse(resultContent);
		//extracting codes
		Elements codes=document.select("code");
		for(Element element:codes)
		{
			String stack=element.text();
			stacks+=stack+"\n";
		}
		
		//extracting the pres
		Elements pres=document.select("pre");
		for(Element element:pres)
		{
			String stack=element.text();
			stacks+=stack+"\n";
		}
		
		//extracting block quotes
		Elements quotes=document.select("blockquote");
		for(Element quote:quotes)
		{
			String stack=quote.text();
			stacks+=stack+"\n";
		}
		}catch(Exception exc){}
		System.out.println(stacks);
		return stacks;
	}
	
	
	protected String test_download()
	{
		String temp = new String();
		try {
			String resultURL = "http://stackoverflow.com/questions/6554458/eclipse-unhandled-event-loop-exception";
			URL url = new URL(resultURL);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));
			//String temp = new String();
			String line = null;
			while ((line = br.readLine()) != null) {
				temp += line + "\n";
			}
			//System.out.println(temp);
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
		}
		return temp;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StackTraceTest test=new StackTraceTest();
		String content=test.test_download();
		//System.out.println(content);
		test.get_the_stackTrace(content);

	}

}
