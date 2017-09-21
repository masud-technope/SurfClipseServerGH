package core;

import indexmanager.SResultIndexBuilder;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import scoring.ResultScoreManager;
import scoring.ScoreCalculator;
import utility.ResultEntryMerger;

public class SearchResultProvider {

	SurfClipseSearch search;
	String searchQuery;
	String stackTrace;
	String sourceCodeContext;
	String recentPageData;
	private static final Logger log = Logger.getRootLogger();
	public int currentException=0;
	
	public SearchResultProvider()
	{
		//default constructor
	}
	
	public SearchResultProvider(String searchQuery,String stackTrace,String sourceCodeContext, String recentPageData)
	{
		//code for providing the search results
		this.searchQuery=searchQuery;
		this.stackTrace=stackTrace;
		this.sourceCodeContext=sourceCodeContext;
		this.recentPageData=recentPageData;
		//this.search=new SurfClipseSearch();
		
		//collect data from search engines
		//===========================
		//commented temporarily
		this.search=new SurfClipseSearch(this.searchQuery);
		//================================
	}
	
	
	public ArrayList<Result> provide_the_final_results() {
		// code for providing the final results
		// merge the results
		//===========================
		//commented temporarily
		ResultEntryMerger merger = new ResultEntryMerger(search.Google_Results,
				search.Bing_Results, search.Yahoo_Results, search.SO_Results);
		
		search.my_big_array = merger.merge();
		//================================
		
		//search.my_big_array = SResultIndexBuilder.load_sresult_index(currentException);
		
		// do the processing for score calculation
		search.my_big_array = perform_parallel_score_computation(search);
		// perform scoring and sorting of the results
		try {
			ResultScoreManager manager = new ResultScoreManager(
					search.my_big_array);
			// calculate relative scores
			manager.calculate_relative_scores();
			//prepare final scores
			search.my_big_array = manager.prerpare_final_score();
			// sorting and saving the results
			search.my_big_array = manager.sort_the_result_ctxp();
			// sort_n_save_results(manager, fileName2);
			//manager.save_the_result_score_ctxp("ctxp.txt");
		} catch (Exception exc) {
			System.out.println("Final score calculation failed."
					+ exc.getStackTrace());
		}
		// returning the
		return search.my_big_array;
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
		System.out.println("Thread #"+index+": URL index:"+index+" to "+(endIndex-1));
		//returning temporary list
		return tempList;
	}
	
	
	
	protected ArrayList<Result> perform_parallel_score_computation(SurfClipseSearch search)
	{
		
		// master result list
		ArrayList<Result> masterList = new ArrayList<Result>();

		// now perform the parallel task on the search operations
		int number_of_processors = 10;// Runtime.getRuntime().availableProcessors();

		// step size
		int stepSize = 0;
		
		double _stepsize=(double)search.my_big_array.size()/number_of_processors;
		
		stepSize =(int)Math.ceil(_stepsize); 
		if (stepSize<=1){
			stepSize = search.my_big_array.size() % number_of_processors;
			ScoreCalculator scal = new ScoreCalculator(search.my_big_array,
					this.searchQuery, this.stackTrace, this.sourceCodeContext, this.recentPageData);
			masterList.addAll(scal.get_computed_results());
			return masterList;
		}

		ArrayList<Thread> myThreads = new ArrayList<Thread>();
		ArrayList<ScoreCalculator> scals = new ArrayList<ScoreCalculator>();

		System.out.println("Processors found:" + number_of_processors);
		
		log.info("Processors found"+number_of_processors);

		// parallelize the score computation
		for (int i = 0; i < number_of_processors; i++) {
			ArrayList<Result> tempList = provide_segmented_links(
					search.my_big_array, i * stepSize, stepSize);
			ScoreCalculator scal = new ScoreCalculator(tempList,
					this.searchQuery, this.stackTrace, this.sourceCodeContext, this.recentPageData);
			Runnable runnable = scal;
			Thread t = new Thread(runnable);
			t.setName("Thread: #"+i);
			myThreads.add(t);
			scals.add(scal);
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
			System.out.println("Starting thread:"+t.getName());
		}

		// checking the thread status and collecting results
		int running = number_of_processors;
		while (running > 0) {
			for (int k = 0; k < myThreads.size(); k++) {
				Thread t1 = myThreads.get(k);
				if (!t1.isAlive()) {
					ScoreCalculator scal1 = (ScoreCalculator) scals.get(k);
					masterList.addAll(scal1.get_computed_results());
					myThreads.remove(k);
					scals.remove(k);
					running--;
					System.out.println("Completed Thread :" + k+" "+t1.getName());
					System.out.println("Threads remaining....");
					for(Thread t2:myThreads)System.out.println(t2.getName());
				}
			}
		}
		//System.out.println("Total results extracted:" + masterList.size());
		// returning masterList
		return masterList;
	}
}

	
	
	

