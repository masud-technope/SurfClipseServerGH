package datacollector;

import java.util.ArrayList;
import core.Result;
import gaecore.YahooAPI2;

public class YahooThread implements Runnable {

	
	String searchQuery;
	YahooAPI2 yahooAPI;
	ArrayList<Result> Yahoo_Results;
	public YahooThread(String searchQuery)
	{
		//assigning search query
		this.searchQuery=searchQuery;
		yahooAPI=new YahooAPI2();
	}
	
	
	public void run()
	{
		//code for running the code
	  this.Yahoo_Results=yahooAPI.get_Yahoo_Results(this.searchQuery);
		
	}
	
}


