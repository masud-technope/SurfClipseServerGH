package misc;
import gaecore.BingAPI;

import java.util.ArrayList;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scoring.ResultStackTraceMatcher;

import core.Result;

public class HrefExtractor {

	/**
	 * @param args
	 */
	
	ArrayList<Result> EntryList;
	HashSet<String> urlset;
	public int[] outboundLinks;
	int hrefDir[][];
	
	public HrefExtractor(ArrayList<Result> EntryList)
	{
		//assign objects
		this.EntryList=EntryList;
		urlset=new HashSet<String>();
		//now add the URL strings
		for(Result result:EntryList)
		{
			String resultURL=result.resultURL;
			urlset.add(resultURL);
		}
		int total_links=urlset.size();
		
		//initiate the Href directory
		hrefDir=new int[total_links][total_links];
		outboundLinks=new int[total_links];
		
	}
	
	protected int gethashSetIndex(String resultURL)
	{
		int position=-1;
		//code for getting Hashset index
		int count=0;
		for(String str:this.urlset)
		{
			if(str.equals(resultURL))
			{
				position=count;
				break;
			}
			count++;
		}
		return position;
	}
	
	
	public void extract_href_tags(Result result)
	{
		//code for extracting href rags
		try
		{
			//selected result URL
			String selected_resultURL=result.resultURL;
		    int selected_position=gethashSetIndex(selected_resultURL);
		    //System.out.println("Selected URL:"+selected_resultURL);
			
		    //now explore the other links
			Document document=Jsoup.parse(result.resultContent);
			Elements links=document.select("a[href]");
			
			//out bound links
			int extlinkcount=0;
			for(Element element:links)
			{
				String href=element.attr("href");
				if(href.startsWith("http"))extlinkcount++;
				//if(href.startsWith("http://"))
				//System.out.println(href);
				int current_position=gethashSetIndex(href);
				if(current_position>0 && current_position!=selected_position)
				{
					int temp=this.hrefDir[selected_position][current_position];
					temp+=1;
					this.hrefDir[selected_position][current_position]=temp;
					
					System.out.println("selected:"+selected_resultURL+" >>>"+"Current:"+href);
					//System.out.println();
				}
			}
			//setting out bound link counts
			this.outboundLinks[selected_position]=extlinkcount;
			
			System.out.println("===================");
		}catch(Exception exc){
		}
	}
	
	public int[][] develop_href_statistics()
	{
		//code for developing the href stats
		for(Result result:this.EntryList)
		{
			extract_href_tags(result);
		}
		return this.hrefDir;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String query="Invalid preference page path: XML Syntax";
		BingAPI bing=new BingAPI();
		ArrayList<Result> entries=bing.find_Bing_Results(query);
		ResultStackTraceMatcher matcher=new ResultStackTraceMatcher(entries, "This is a stack trace ");
		entries=matcher.calculate_stacktrace_score();
		HrefExtractor hrefex=new HrefExtractor(entries);
		hrefex.develop_href_statistics();

	}

}
