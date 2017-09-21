package resproc;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import core.Result;
import core.StaticData;

public class MyResultProcessor {

	/**
	 * @param args
	 */
	ArrayList<Result> EntryList;
	public MyResultProcessor()
	{
		this.EntryList=new ArrayList<>();
	}
	
	
	protected void load_result_scores(int exceptionID)
	{
		// code for loading the result scores
		String resultFolder = StaticData.Lucene_Data_Base
				+ "/completeds/sclipse/" + exceptionID+".txt";
		try {
			File file = new File(resultFolder);
			if(!file.exists())return;
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] parts = line.split("\\s+");
				Result result = new Result();
				// url
				result.resultURL = parts[0];
				// total score
				try {
					result.totalScore_content_context_popularity = Double
							.parseDouble(parts[1].trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				// content
				try {
					result.content_score = Double.parseDouble(parts[2].trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				// context
				try {
					result.context_score = Double.parseDouble(parts[3].trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				// popularity
				try {
					result.popularity_score = Double.parseDouble(parts[4]
							.trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				// confidence
				try {
					result.search_result_confidence = Double
							.parseDouble(parts[5].trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				this.EntryList.add(result);
			}
		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}
	
	protected void rejustify_the_results(int exceptionID)
	{
		//code for re-justifying the results
		this.load_result_scores(exceptionID);
		if(this.EntryList.size()==0)return;
		this.recompute_total_results();
		this.sort_results_ctxp();
		String key=exceptionID+".txt";
		this.save_the_results(key, this.EntryList);
		System.out.println("Result computed and save for :"+key);
		//clearing the result items
		this.EntryList.clear();
	}
	
	protected void recompute_total_results()
	{
		//required weights
		/*double content_weight=0.35;
		double context_weight=0.85;
		double popularity_weight=0.20;
		double result_confidence_weight=0.10;*/
		
		
		double content_weight=0.35;//0.35;
		double context_weight=0.85;//0.85;
		double popularity_weight=0.20;//0.20;
		double result_confidence_weight=0.10;//0.10;

		for(Result result:this.EntryList)
		{
			result.totalScore_content_context=content_weight*result.content_score+ context_weight*result.context_score;
			result.totalScore_context_popularity=context_weight*result.context_score+popularity_weight*result.popularity_score;
			result.totalScore_content_context_popularity=result.content_score*content_weight+result.context_score*context_weight
			+
			result.popularity_score*popularity_weight+result.search_result_confidence*result_confidence_weight;
		}
	}
	
	protected void save_the_results(String key, ArrayList<Result> tempList) {
		String sclipse = StaticData.Lucene_Data_Base + "/completeds/results/silo";
		String targetFile = sclipse + "/" + key;
		try {
			PrintWriter writer = new PrintWriter(new File(targetFile));
			
			for (int i = 0; i < tempList.size(); i++) {
				Result result=(Result) tempList.get(i);
				String line = result.resultURL;
				line=line+"\t"+result.totalScore_content_context_popularity+"\t"+result.content_score+"\t"+result.context_score+"\t"+result.popularity_score+"\t"+result.search_result_confidence;
				writer.write(line + "\n");
				//if(i==30)break; //only top 30 results
			}
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	

	protected void sort_results_ctxp()
	{
		Collections.sort(this.EntryList, new CustomComparator_ctxp());
	}
	
	protected void sort_results_ctx()
	{
		Collections.sort(this.EntryList, new CustomComparator_ctx());
	}
	
	protected void sort_results_cxp()
	{
		Collections.sort(this.EntryList, new CustomComparator_cxp());
	}
	
	
	public class CustomComparator_ctxp implements Comparator<Result> {
	    @Override
	    public int compare(Result o1, Result o2) {
	    	if(o1.totalScore_content_context_popularity>o2.totalScore_content_context_popularity)return -1;
	    	else if(o1.totalScore_content_context_popularity<o2.totalScore_content_context_popularity)return 1;
	    	else return 0;
	    }
	}
	
	public class CustomComparator_ctx implements Comparator<Result> {
	    @Override
	    public int compare(Result o1, Result o2) {
	    	if(o1.totalScore_content_context>o2.totalScore_content_context)return -1;
	    	else if(o1.totalScore_content_context<o2.totalScore_content_context)return 1;
	    	else return 0;
	    }
	}
	
	public class CustomComparator_cxp implements Comparator<Result> {
	    @Override
	    public int compare(Result o1, Result o2) {
	    	if(o1.totalScore_context_popularity>o2.totalScore_context_popularity)return -1;
	    	else if(o1.totalScore_context_popularity<o2.totalScore_context_popularity)return 1;
	    	else return 0;
	    }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MyResultProcessor processor=new MyResultProcessor();
		for(int i=1;i<84;i++)
		{
			processor.rejustify_the_results(i);
		}

	}

}
