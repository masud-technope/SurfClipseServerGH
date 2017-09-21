package core;

//code for accumulating the search function
//Author: Md. Masudur Rahman
//Date: 02 November 2012

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import datacollector.DataCollectorThreadManager;
import ranking.ResultVoteWeightManager;

public class SurfClipseSearch {
	/**
	 * @param args
	 */
	
	//declared classes
	gaecore.SOAPIDemo soAPI;
	gaecore.GoogleAPI googAPI;
	gaecore.BingAPI bingAPI;
	gaecore.YahooAPI2 yahooAPI;

	//search engine results
	public ArrayList<Result> SO_Results;
	public ArrayList<Result> Google_Results;
	public ArrayList<Result> Bing_Results;
	public ArrayList<Result> Yahoo_Results;
	
	//vote score
	HashMap<String,Double> voteScores=new HashMap<String, Double>();
	public ArrayList<Result> my_big_array=null;

	//more input to search
	String stacktrace;
	String codecontext;
	public String searchQuery;
	
	
	public SurfClipseSearch(){
		//initialization
		my_big_array=new ArrayList<Result>();
		
	}
	
	
	//creating the search results
	public SurfClipseSearch(String searchQuery)
	{
		//code for constructor
		try
		{
			//storing the search query
			this.searchQuery=searchQuery;
			//initialization
			my_big_array=new ArrayList<Result>();
			
			//initiating the ArrayList
			this.Google_Results=new ArrayList<Result>();
			this.Bing_Results=new ArrayList<Result>();
			this.Yahoo_Results=new ArrayList<Result>();
			this.SO_Results=new ArrayList<Result>();
			
			/*soAPI=new SOAPIDemo();
			googAPI=new GoogleAPI();
			bingAPI=new BingAPI();
		    yahooAPI=new YahooAPI2();*/
			
		    DataCollectorThreadManager dataCollector=new DataCollectorThreadManager(this.searchQuery);
		    int resultPulledfrom=dataCollector.collect_search_data();
		    System.out.println("Result collected from:"+resultPulledfrom);
		    if(resultPulledfrom>0)
		    {
		    this.Google_Results=dataCollector.Google_Results;
		    this.Bing_Results=dataCollector.Bing_Results;
		    this.Yahoo_Results=dataCollector.Yahoo_Results;
		    //this.SO_Results=dataCollector.SO_Results;
		    }
		    
		    
			/*this.SO_Results=soAPI.find_SO_results(searchQuery);
			//my_big_array.addAll(SO_Results);
			System.out.println("From StackOverflow: "+SO_Results.size()+ " results");			
			this.Google_Results=googAPI.find_Google_Results(searchQuery);
			//my_big_array.addAll(Google_Results);
			System.out.println("From Google: "+Google_Results.size()+ " results");
			this.Bing_Results=bingAPI.find_Bing_Results(searchQuery);
			//my_big_array.addAll(Bing_Results);
			System.out.println("From Bing: "+Bing_Results.size()+ " results");
			this.Yahoo_Results=yahooAPI.get_Yahoo_Results(searchQuery);
			//my_big_array.addAll(Yahoo_Results);
			System.out.println("From Yahoo: "+Yahoo_Results.size()+ " results");
			
			
			//initiating the sort map
			this.sortedResult=new Hashtable<String, Integer>();*/
			
			//here should be the result ranking and sorting
			
		}catch(Exception exc){
			exc.printStackTrace();
		}	
	}
	

	protected void get_result_vote_score()
	{
		//code for getting result vote score
		try
		{
			ResultVoteWeightManager voteManager=new ResultVoteWeightManager(this.SO_Results, this.Google_Results, 
					this.Bing_Results, this.Yahoo_Results);
			voteManager.create_vote_score();
			this.voteScores=voteManager.AllResults;
			//showing the scores
			voteManager.show_freq_score();
		}catch(Exception exc){
			
		}
	}
	
	
	protected ArrayList sort_search_results(HashMap<String,Integer> searchMap)
	{
		//code for sorting the  search results
		 ArrayList<Map.Entry<String, Integer>> l = new ArrayList<Map.Entry<String, Integer>>(searchMap.entrySet());
	       Collections.sort(l, new Comparator<Map.Entry<String, Integer>>(){
	         public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
	            return o2.getValue().compareTo(o1.getValue());
	        }});
	       return l;
	}
	
	
	//code for showing the search results
	public void show_the_search_results(ArrayList my_big_array)
	{
		try
		{
			HashMap<String,Integer> myurls=new HashMap<String,Integer>();
			for(int i=0;i<my_big_array.size();i++)
			{
				Result resultEntry=(Result)my_big_array.toArray()[i];
				String postUrl=resultEntry.resultURL;
				if(myurls.containsKey(postUrl))
				{
					//updating existing weights
					int curr_score=myurls.get(postUrl).intValue();
				    curr_score++;
					myurls.put(postUrl, new Integer(curr_score));
				}else
				{
					//adding the first weight
					myurls.put(postUrl, new Integer(1));
				}
			}
			//sorting the results
			ArrayList<Map.Entry<String, Integer>> sorted=sort_search_results(myurls);
			Iterator<Map.Entry<String, Integer>> iter1=sorted.iterator();
			while(iter1.hasNext())
			{
				Map.Entry<String, Integer> mapEntry=iter1.next();
				System.out.println(mapEntry.getKey());
		    	System.out.println(mapEntry.getValue());
		    	System.out.println();
			}
		}catch(Exception exc){
		}
	}
	
	
	
	
	public static void main(String args[])
	{
		//code for surfing for helps
		try
		{
			String searchQuery="";
			Scanner scanner=new Scanner(System.in);
			//Scanner scanner=new Scanner(new File(queryFile));
			while(scanner.hasNext())
			{
				searchQuery=scanner.nextLine();
				SurfClipseSearch search=new SurfClipseSearch(searchQuery);
				search.show_the_search_results(search.my_big_array);
				//search.get_result_vote_score();
			}
		}catch(Exception exc){
			System.err.println(exc.getMessage());
		}
	}
}
