package weighting;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import scoring.AlexaCompeteScore;
import scoring.ResultTitleMatcher;
import scoring.SEConfidenceScore;
import scoring.SOVoteScore;
import core.Result;
import core.StaticData;

public class MyPopConfScoreManager {

	/**
	 * @param args
	 */
	
	public MyPopConfScoreManager()
	{
		//default constructor
	}
	
	protected void get_pop_confidence_score()
	{
		//code for getting content relevance scores
		try{
			for(int i=51;i<=150;i++){
				int key=i;
				//if(i==43)continue;
				ArrayList<Result> results=ResultTitleMatcher.formulate_result_collection(key);
				results=ResultTitleMatcher.load_document_source(results, key);
				//Alexa scores
				AlexaCompeteScore alexaCompeteScore=new AlexaCompeteScore(results);
				ArrayList<Result> computedResult1=alexaCompeteScore.get_alexa_compete_rank_score();
				computedResult1=alexaCompeteScore.get_alexa_compete_relative_rank_score();
				//SO Vote Scores
				SOVoteScore soVoteScore=new SOVoteScore(computedResult1);
				ArrayList<Result> computed2=soVoteScore.get_SO_vote_score();
				computed2=soVoteScore.get_SO_relative_score();
				//SE Confidence score
				SEConfidenceScore secScore=new SEConfidenceScore(computed2);
				ArrayList<Result> computed3=secScore.get_normalized_confidence();
				saveDocContentValues(computed3, key);
			}
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	protected HashMap<String,Integer> getLinkLabels(int key)
	{
		//code for getting the link labels
		HashMap<String, Integer> linklabels=new HashMap<>();
		try{
			String doccorpus=StaticData.Lucene_Data_Base+"/completeds/doccorpus";
			String lfile=doccorpus+"/"+key+".txt";
			Scanner scanner=new Scanner(new File(lfile));
			while(scanner.hasNext()){
				String line=scanner.nextLine().trim();
				String[] parts=line.split("\\s+");
				int value=Integer.parseInt(parts[0].trim());
				String link=parts[1].trim();
				linklabels.put(link, value);
			}
		}catch(Exception exc){	
		}
		return linklabels;
	}
	
	protected void saveDocContentValues(ArrayList<Result> results, int key)
	{
		//code for saving document content values
		try{
			//HashMap<String, Integer> linklabels=getLinkLabels(key);
			String dfile=StaticData.Lucene_Data_Base+"/completeds/docpop/"+key+".txt";
			FileWriter fwriter=new FileWriter(new File(dfile));
			for(Result result:results){
				String url=result.resultURL;
				//if(linklabels.containsKey(url.trim())){
					String line=result.AlexaCompeteRankScore+"\t"+result.SOVoteScore+"\t"+result.search_result_confidence;
					//line+="\t"+linklabels.get(url);
					line+="\t"+url;
					fwriter.write(line+"\n");
				//}
			}
			fwriter.close();
			System.err.println("Done with:"+key);
		}catch(Exception exc){
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyPopConfScoreManager().get_pop_confidence_score();
	}
}
