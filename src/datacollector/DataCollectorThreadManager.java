package datacollector;

import java.util.ArrayList;

import core.Result;

public class DataCollectorThreadManager {

	ArrayList<Thread> tcollection=new ArrayList<Thread>();
	ArrayList<Runnable> rCollection=new ArrayList<Runnable>();
	
	public ArrayList<Result> Google_Results;
	public ArrayList<Result> Yahoo_Results;
	public ArrayList<Result> Bing_Results;
	public ArrayList<Result> SO_Results;
	
	public DataCollectorThreadManager(String searchQuery)
	{
		//initiating the result lists
		Google_Results=new ArrayList<Result>();
		Bing_Results=new ArrayList<Result>();
		Yahoo_Results=new ArrayList<Result>();
		SO_Results=new ArrayList<Result>();
		
		//creating the threads
		//Google
		GoogleThread gRunnable=new GoogleThread(searchQuery);
		rCollection.add(gRunnable);
		Thread googThread= new Thread(gRunnable);
		tcollection.add(googThread);
		googThread.start();
		
		//Bing
		BingThread bRunnable=new BingThread(searchQuery);
		rCollection.add(bRunnable);
		Thread bingThread=new Thread(bRunnable);
		tcollection.add(bingThread);
		bingThread.start();
		
		//Yahoo!
		YahooThread yRunnable=new YahooThread(searchQuery);
		rCollection.add(yRunnable);
		Thread yahooThread=new Thread(yRunnable);
		tcollection.add(yahooThread);
		yahooThread.start();
		
		//StackOverflow
		SOThread sRunnable=new SOThread(searchQuery);
		rCollection.add(sRunnable);
		Thread soThread=new Thread(sRunnable);
		tcollection.add(soThread);
		soThread.start();
	}
	
	public int collect_search_data()
	{
		//code for returning the collected data
		int completed=0;
		while(completed<4) //not yet collected
		{
			for(int i=0;i<tcollection.size();i++)
			{
				Thread t=tcollection.get(i);
				if(!t.isAlive())
				{
					Runnable r=rCollection.get(i);
					if(r instanceof GoogleThread)
					{
						this.Google_Results=((GoogleThread) r).Google_Results;
					}
					if(r instanceof BingThread)
					{
						this.Bing_Results=((BingThread) r).Bing_Results;
					}
					if(r instanceof YahooThread)
					{
						this.Yahoo_Results=((YahooThread) r).Yahoo_Results;
					}
					if(r instanceof SOThread)
					{
						this.SO_Results=((SOThread) r).SO_Results;
					}
					rCollection.remove(i);
					tcollection.remove(i);
					completed++;
				}
			}
		}
		return completed;
	}
}
