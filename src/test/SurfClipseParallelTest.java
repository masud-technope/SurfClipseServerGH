package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import scoring.ResultScoreManager;
import scoring.ScoreCalculator;
import utility.ResultEntryMerger;
import core.Result;
import core.SearchResultProvider;
import core.StaticData;
import core.SurfClipseSearch;

public class SurfClipseParallelTest {

	/**
	 * @param args
	 */
	SurfClipseSearch search;
	String searchQuery;
	String stackTrace;
	String code_context;
	String stackTraceFile;
	String recentPageData;
	ResultScoreManager manager;
	
	
	public SurfClipseParallelTest(String searchQuery,String fileID)
	{
		//assigning search query
				this.searchQuery=searchQuery;
				//code for search result test
				//search=new SurfClipseSearch(this.searchQuery);
				//collect the context
				collect_error_context(fileID);
	}
	
	protected void provide_the_final_results()
	{
		//code for providing the final results
		SearchResultProvider provider=new SearchResultProvider(this.searchQuery, this.stackTrace,
				this.code_context, this.recentPageData);
		ArrayList<Result> final_results=provider.provide_the_final_results();
		System.out.println("Final results found:"+final_results.size());
	}
	
	
	protected void collect_error_context(String fileID)
	{
		//code for collecting stack trace
		try {
			String temp = new String();
			//Scanner scanner=new Scanner(new
			//File(/*"D:/My MSc/CMPT 811/SurfClipse Tool Demo/data/stack.txt"*/"C:/MyWorks/SurfClipse/data/exception/"+fileID+".txt"));
			Scanner scanner = new Scanner(new File(StaticData.Base_Directory+"/exception/"
							+ fileID + ".txt"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				temp += line + "\n";
			}
			this.stackTrace = temp;
		} catch (Exception exc) {
		}

		//code for collecting code context
		/*try
		{
		String temp=new String();
		Scanner scanner=new Scanner(new File("D:/My MSc/CMPT 811/SurfClipse Tool Demo/data/code/"+
			fileID + ".txt"));
		while(scanner.hasNext())
		{
			String line=scanner.nextLine();
			temp+=line+"\n";
		}
		this.code_context=temp;
		}catch(Exception exc){}*/
	}
	
	protected void perform_the_test(String fileID)
	{
		try
		{	
		//populating the results
		//String searchQuery="invalid preference page path HTML syntax org.eclipse.ui";
		//search=new SurfClipseSearch(searchQuery);
		//merge the results from different search engine
			
		ResultEntryMerger merger=new ResultEntryMerger(search.Google_Results, search.Bing_Results, search.Yahoo_Results, search.SO_Results);
		search.my_big_array=merger.merge(); //to get a unique set of results
		System.out.println("Distinct entries:"+search.my_big_array.size());
		
		//do the processing for score calculation
		search.my_big_array=perform_parallel_score_computation(search);
		
		//String fileName="D:/My MSc/CMPT 811/SurfClipse Tool Demo/data/results/content/metric-"+fileID+".txt";
		//String fileName2="D:/My MSc/CMPT 811/SurfClipse Tool Demo/data/results/content/score-"+fileID+".txt";
		//String fileName="C:/MyWorks/SurfClipse/data/results/content/metric-"+fileID+".txt";
		//String fileName2="C:/MyWorks/SurfClipse/data/results/content-context/score-"+fileID+".txt";
		String fileName2=fileID+".txt";
		//String fileName="C:/MyWorks/SurfClipse/data/output.txt";
		try
		{
			ResultScoreManager manager=new ResultScoreManager(search.my_big_array);
			//calculate relative scores
			manager.calculate_relative_scores();
			search.my_big_array=manager.prerpare_final_score();
			//sorting and saving the results
			search.my_big_array=manager.sort_the_result_ctxp();
			//sort_n_save_results(manager, fileName2);
		}catch(Exception exc)
		{
			System.out.println("Final score calculation failed."+exc.getStackTrace());
		}
		}catch(Exception exc)
		{
			exc.printStackTrace();
		}	
	}
	
	
	protected void sort_n_save_results(ResultScoreManager manager, String fileName2)
	{
		//code for sorting and saving the result
		try
		{
			//now sort the result and save them
			try
			{
			manager.Final_Results=manager.sort_the_result_ct();
			manager.save_the_result_score_ct(fileName2);
			}catch(Exception  exc){
				exc.printStackTrace();
			}
			
			try
			{
			manager.Final_Results=manager.sort_the_result_ctx();
			manager.save_the_result_score_ctx(fileName2);
			}catch(Exception exc){
				exc.printStackTrace();
			}
			
			try
			{
			manager.Final_Results=manager.sort_the_result_ctp();
			manager.save_the_result_score_ctp(fileName2);
			}catch(Exception exc){
				exc.printStackTrace();
			}
			
			try
			{
			manager.Final_Results=manager.sort_the_result_ctxp();
			manager.save_the_result_score_ctxp(fileName2);
			}catch(Exception exc){
				exc.printStackTrace();
			}
			
			
			try
			{
			//saving the metrics
			manager.save_the_details_result(fileName2);
			}catch(Exception exc){
				exc.printStackTrace();
			}
			
			//manager.save_the_result_score(fileName2);
			System.out.println("All results  saved successfully."); 
			
		}catch(Exception exc){
			
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String searchQuery = "java.net.SocketException: Permission denied: connect";
		long start_time = System.currentTimeMillis();
		String fileID = "1";
		//SearchResultTest test1 = new SearchResultTest(fileID);
		//ArrayList<Result> myResult = test1.load_scores_from_file(fileID);
		//test1.perform_the_test2(myResult, fileID);

		SurfClipseParallelTest srtest=new SurfClipseParallelTest(searchQuery.trim(),fileID);
		//srtest.perform_the_test(fileID);
		srtest.provide_the_final_results();
		long end_time = System.currentTimeMillis();
		System.out.println("Time elapsed:" + (end_time - start_time) / 1000
				+ " seconds"); 
	}

	
	
	protected ArrayList<Result> provide_segmented_links(ArrayList<Result> totalLinks,int index,int stepSize)
	{
		//code for providing segmented links
		ArrayList<Result> tempList=new ArrayList<Result>();
		int endIndex=index+stepSize;
		if(endIndex>totalLinks.size())endIndex=totalLinks.size();
		for(int i=index;i<endIndex;i++)
		{
			Result result=totalLinks.get(i);
			tempList.add(result);
		}
		//returning tempo list
		return tempList;
	}
	
	
	
	protected ArrayList<Result> perform_parallel_score_computation(SurfClipseSearch search)
	{
		
		//master result list
		ArrayList<Result> masterList=new ArrayList<Result>();
		
		//now perform the parallel task on the search operations
		int number_of_processors=Runtime.getRuntime().availableProcessors();
		
		//step size
		int stepSize=0;
		stepSize=search.my_big_array.size()/number_of_processors;
		if(stepSize==0)stepSize=search.my_big_array.size()%number_of_processors;
		
		ArrayList<Thread> myThreads=new ArrayList<Thread>();
		ArrayList<ScoreCalculator> scals=new ArrayList<ScoreCalculator>();
		
		System.out.println("Processors found:"+number_of_processors);
		
		//parallelize the score computation
		for(int i=0;i<number_of_processors;i++)
		{
			ArrayList<Result> tempList=provide_segmented_links(search.my_big_array, i*stepSize, stepSize);
			ScoreCalculator scal=new ScoreCalculator(tempList, searchQuery, stackTrace, code_context,null);
			Runnable runnable=scal;
			Thread t=new Thread(runnable);
			myThreads.add(t);
			scals.add(scal);
			t.start();
		}
		
		//checking the thread status and collecting results
		int running=number_of_processors;
		while(running>0)
		{
			for(int k=0;k<myThreads.size();k++)
			{
				Thread t1=myThreads.get(k);
				if(!t1.isAlive())
				{
					ScoreCalculator scal1=(ScoreCalculator)scals.get(k);
					masterList.addAll(scal1.get_computed_results());
					myThreads.remove(k);
					scals.remove(k);
					running--;
					System.out.println("Completed Thread :"+k);
				}
			}
		}
		System.out.println("Total results extracted:"+masterList.size());
		//returning masterList
		return masterList;
	}
}
