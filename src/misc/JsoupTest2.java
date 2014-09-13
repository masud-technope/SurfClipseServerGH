package misc;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import codestack.CodeStackMiner;

public class JsoupTest2 {

	/**
	 * @param args
	 */
	String pageContent;
	
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
	
	protected void exploreCodeStacks()
	{
		String exceptionName="java.util.concurrent.ExecutionException";
		String url="http://stackoverflow.com/questions/20047152/java-util-concurrent-executionexception-java-lang-outofmemoryerror-permgen-spa";
		pageContent=getContent(url);
		CodeStackMiner miner=new CodeStackMiner(exceptionName, pageContent);
		miner.minePageCodeStacks();
	}
	
	
	protected void extractPageItems(){
		//code for extracting page items
		try{
			String url="http://stackoverflow.com/questions/20047152/java-util-concurrent-executionexception-java-lang-outofmemoryerror-permgen-spa";
			pageContent=getContent(url);
			Document document=Jsoup.parse(pageContent);
			Element body=document.select("body").first();
			ArrayList<Element> codestack_html=new ArrayList<>();
			Elements blocks=body.select("blockquote");
			
			Elements codes=body.select("code");
			System.out.println("codes:"+codes.size());
			for(Element elem:codes){
				System.out.println(elem.text());
				codestack_html.add(elem);
				//System.err.println("============");
			}
			Elements pres=body.select("pre");
			System.out.println("pres:"+pres.size());
			for(Element elem:pres){
				System.out.println(elem.text());
				//System.err.println("============");
				codestack_html.add(elem);
			}
			
		}catch(Exception exc){
		}
	}
	
	
	private static String convertCharset(String text) {
	    Charset charset = Charset.forName("ISO-8859-1");
	    CharsetDecoder decoder = charset.newDecoder();
	    CharsetEncoder encoder = charset.newEncoder();
	    try {
	        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
	        CharBuffer cbuf = decoder.decode(bbuf);
	        return cbuf.toString();
	    } catch (CharacterCodingException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	static Font getFont(){
		Font menuFont=null;
        try {
            menuFont = Font.createFont( Font.TRUETYPE_FONT, new FileInputStream("font.ttf"));

        } catch (FileNotFoundException e) {
            System.out.println("Cant find file.");
            e.printStackTrace();
        } catch (FontFormatException e) {
            System.out.println("Wrong file type.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unknown error.");
            e.printStackTrace();
        }
        return menuFont;
}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new JsoupTest2().exploreCodeStacks();
		//System.out.println(convertCharset("856&ddidmmd"));
		getFont();
	}

}
