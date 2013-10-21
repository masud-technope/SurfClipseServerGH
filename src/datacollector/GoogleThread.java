package datacollector;

import java.util.ArrayList;

import gaecore.GoogleAPI;
import core.Result;

public class GoogleThread implements Runnable {
	
	String searchQuery;
	GoogleAPI googAPI;
	ArrayList<Result> Google_Results;
	public GoogleThread(String searchQuery)
	{
		//assigning search query
		this.searchQuery=searchQuery;
		googAPI=new GoogleAPI();
	}
	
	public void run()
	{
		//code for running the code
	  this.Google_Results=googAPI.find_Google_Results(this.searchQuery);
		
	}

}
