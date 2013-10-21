package scoring;

import java.util.ArrayList;

import utility.DownloadResultEntryContent;

import core.Result;

public class ScoreCalculator implements Runnable {

	
	ArrayList<Result> segmentedResults;
	ResultTitleMatcher titleMatcher;
	ResultStackTraceMatcher stackTraceMatcher;
	SourceCodeContextMatcher sourcecodeContextMatcher;
	AlexaCompeteScore alexaCompeteScorer;
	SOVoteScore so_vote_Score;
	HistoryRecencyScore historyRecencyScore;
	DownloadResultEntryContent downloader;
	//public ArrayList<Result> segmentedResults;
	
	//other variables
	String stackTrace;
	String queryTitle;
	String code_context;
	String recentPageData;
	
	public ScoreCalculator(ArrayList<Result> myResults,String queryTitle,String stackTrace,String code_context, String recentPageData)
	{
		//assigning required variables
		this.segmentedResults=myResults;
		this.stackTrace=stackTrace;
		this.queryTitle=queryTitle;
		this.code_context=code_context;
		this.recentPageData=recentPageData;
	}
	
	public void run()
	{
		//code for executing the threads
		calculate_intermediate_scores();
	}
	
	public ArrayList<Result> get_computed_results()
	{
		//code for getting computed results
		return this.segmentedResults;
	}
	
	
	//calculating intermediate scores
		public ArrayList<Result> calculate_intermediate_scores()
		{
			//code for calculating intermediate scores
			downloader=new DownloadResultEntryContent(this.segmentedResults);
			this.segmentedResults=downloader.download_result_entry_content();
			System.out.println("...All..File content downloaded...");
			
			// calculate the scores
			//content similarity matching
			try {
				// title matching score: it downloads codestacks and textcontent 
				titleMatcher = new ResultTitleMatcher(this.segmentedResults,
						queryTitle);
				this.segmentedResults = titleMatcher.calculate_title_match_score();
				System.out.println("Title matching score done by"+Thread.currentThread().getName());
			} catch (Exception e) {
				System.err.println("Exception thrown by TitleMatcher:"+e.getMessage());
				e.printStackTrace();
			}
			
		
			//context similarity matching
			try {
				if(stackTrace!=null && !stackTrace.isEmpty())
				{
				// stack trace matching
				stackTraceMatcher = new ResultStackTraceMatcher(this.segmentedResults,
						stackTrace);
				this.segmentedResults = stackTraceMatcher.calculate_stacktrace_score();
				System.out.println("Stack Trace matching score done by"+Thread.currentThread().getName());
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("Exception thrown by StackTraceMatcher:"+e.getMessage());
				e.printStackTrace();
			}
			
			try {
				if(code_context!=null && !code_context.isEmpty())
				{
				// source code context matching
				sourcecodeContextMatcher = new SourceCodeContextMatcher(
						this.segmentedResults, code_context);
				this.segmentedResults = sourcecodeContextMatcher
						.calculate_codecontext_score();
				System.out.println("Source code context score done by"+Thread.currentThread().getName());
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("Exception thrown by CodeContextMatcher:"+e.getMessage());
				e.printStackTrace();
			}
			
			try
			{
				if(recentPageData!=null && !recentPageData.isEmpty())
				{
					this.historyRecencyScore=new HistoryRecencyScore(this.segmentedResults, recentPageData);
					this.segmentedResults=historyRecencyScore.load_history_scores();
					System.out.println("Recency score provided....by"+Thread.currentThread().getName());
				}
			}catch(Exception exc){
				System.err.println("Exception thrown by RecentHistoryMatcher:"+exc.getMessage());
				exc.printStackTrace();
			}
			
			//popularity calculation
			try {
				// Alexa compete rank score
				alexaCompeteScorer = new AlexaCompeteScore(this.segmentedResults);
				this.segmentedResults = alexaCompeteScorer
						.get_alexa_compete_rank_score();
				System.out.println("Alexa compete score done by "+Thread.currentThread().getName());
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("Exception thrown by Alexa Scores:"+e.getMessage());
				e.printStackTrace();
			}

			try {
				// SO vote score
				so_vote_Score = new SOVoteScore(this.segmentedResults);
				this.segmentedResults = so_vote_Score.get_SO_vote_score();
				System.out.println("SO Vote score done by"+Thread.currentThread().getName());
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("Exception thrown by SO Vote scorer:"+e.getMessage());
				e.printStackTrace();
			}
			//returning the array list
			return this.segmentedResults;
			
		}
}
