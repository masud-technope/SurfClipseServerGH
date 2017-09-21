package datacollector;

import java.util.ArrayList;

import gaecore.BingAPI;
import core.Result;

public class BingThread implements Runnable{

	String searchQuery;
	gaecore.BingAPI bingAPI;
	ArrayList<Result> Bing_Results;
	public BingThread(String searchQuery)
	{
		//assigning search query
		this.searchQuery=searchQuery;
		bingAPI=new BingAPI();
	}
	public void run()
	{
		//code for running the code
	  this.Bing_Results=bingAPI.find_Bing_Results(this.searchQuery);
		
	}
}
