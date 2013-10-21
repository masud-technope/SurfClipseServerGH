package datacollector;

import java.util.ArrayList;

import core.Result;
import gaecore.SOAPIDemo;

public class SOThread implements Runnable {

	String searchQuery;
	SOAPIDemo soAPI;
	ArrayList<Result> SO_Results;
	public SOThread(String searchQuery)
	{
		this.searchQuery=searchQuery;
		soAPI=new SOAPIDemo();
	}
	
	public void run()
	{
		//code for running the code
	  this.SO_Results=soAPI.find_SO_results(this.searchQuery);
		
	}
	
	
}
