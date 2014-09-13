package weighting;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import scoring.ResultTitleMatcher;
import core.Result;
import core.StaticData;

public class MyResultRankManager {

	/**
	 * @param args
	 */
	//content weights
	public double TITLE_WEIGHT=1.0000;// .5;// 0.2568;// 0.4516;
	public double DESC_WEIGHT=.3866;//.6;// 0.2750;// 0.3257;
	public double CODESTACK_WEIGHT=.6881;//.3;// 1.0000;
	public double CONTENT_WEIGHT=.2808;//.2;//0.2295;// 0.1885;
	//context weights
	public double CONT_CXT_WEIGHT= 1.0000;//.5;//1.000;
	public double STRUCT_CXT_WEIGHT=.1102;//.5;//0.0011;//0.005;
	public double CODE_CXT_WEIGHT=.4768;//.4;// .0771;// 0.0845;
	//pop and confidence
	public double ALEXA_WEIGHT=0.0174;// 0.0024;//0.0174;
	public double SOVOTE_WEIGHT=1.0000;
	
	//macro weights
	public double CONTENT_RELEVANCE_WEIGHT=.5105;// 0.35;// 0.3737;
	public double CONTEXT_RELEVANCE_WEIGHT=1.000;// 0.85;// 1.0000;
	public double POPULARITY_WEIGHT=.3820;// 0.20;// 0.0542;
	public double CONFIDENCE_WEIGHT=0.1152;// 0.1958;// 0.0103;
	
	
	protected ArrayList<Result> loadPopConfMatchScores(ArrayList<Result> results,int key)
	{
		//code for loading content match scores
		try{
			String fileURL=StaticData.Lucene_Data_Base+"/completeds/docpop/"+key+".txt";
			Scanner scanner=new Scanner(new File(fileURL));
			for(Result result:results){
				if(scanner.hasNext()){
					//collecting a line
					String line=scanner.nextLine().trim();
					if(line.isEmpty())line=scanner.nextLine().trim();
					
					if(!line.isEmpty()){
					String[] parts=line.split("\\s+");
					double alexa= Double.parseDouble(parts[0].trim());
					double sovote=Double.parseDouble(parts[1].trim());
					double confidence=Double.parseDouble(parts[2].trim());
					//double content=Double.parseDouble(parts[3].trim());
					//storing the values
					result.AlexaCompeteRankScore=alexa;
					result.SOVoteScore=sovote;
					//result.title_codestack_MathScore=codestack;
					result.search_result_confidence=confidence;
					}
				}
			}
		}catch(Exception exc){
		}
		return results;
	}
	
	protected ArrayList<Result> loadContextMatchScores(ArrayList<Result> results,int key)
	{
		//code for loading content match scores
		try{
			String fileURL=StaticData.Lucene_Data_Base+"/completeds/doccxt/"+key+".txt";
			Scanner scanner=new Scanner(new File(fileURL));
			for(Result result:results){
				if(scanner.hasNext()){
					//collecting a line
					String line=scanner.nextLine().trim();
					if(line.isEmpty())line=scanner.nextLine().trim();
					
					if(!line.isEmpty()){
					String[] parts=line.split("\\s+");
					double tokenMatch= Double.parseDouble(parts[0].trim());
					double structureMatch=Double.parseDouble(parts[1].trim());
					double stackTotal=Double.parseDouble(parts[2].trim());
					double codeMatch=Double.parseDouble(parts[3].trim());
					//storing the values
					result.stackTraceContentMatchScore=tokenMatch;
					result.stackTraceStructuralMatchScore=structureMatch;
					//result.title_codestack_MathScore=codestack;
					result.sourceContextMatchScore=codeMatch;
					}
				}
			}
		}catch(Exception exc){
		}
		return results;
	}
	
	protected ArrayList<Result> loadContentMatchScores(ArrayList<Result> results,int key)
	{
		//code for loading content match scores
		try{
			String fileURL=StaticData.Lucene_Data_Base+"/completeds/doccont/"+key+".txt";
			Scanner scanner=new Scanner(new File(fileURL));
			for(Result result:results){
				if(scanner.hasNext()){
					//collecting a line
					String line=scanner.nextLine().trim();
					if(line.isEmpty())line=scanner.nextLine().trim();
					
					if(!line.isEmpty()){
					String[] parts=line.split("\\s+");
					double title= Double.parseDouble(parts[0].trim());
					double description=Double.parseDouble(parts[1].trim());
					double codestack=Double.parseDouble(parts[2].trim());
					double content=Double.parseDouble(parts[3].trim());
					//storing the values
					result.title_title_MatchScore=title;
					result.title_description_MatchScore=description;
					result.title_codestack_MathScore=codestack;
					result.title_content_MatchScore=content;
					}
				}
			}
		}catch(Exception exc){
		}
		return results;
	}
	
	protected ArrayList<Result> getFinalScores(ArrayList<Result> results, int key)
	{
		//code for getting final scores
		try{
			for(Result result:results){
				//content relevance
				result.content_score=result.title_title_MatchScore*TITLE_WEIGHT+result.title_description_MatchScore*DESC_WEIGHT+
						result.title_codestack_MathScore*CODESTACK_WEIGHT+result.title_content_MatchScore*CONTENT_WEIGHT;
				//context relevance
				result.context_score=result.stackTraceContentMatchScore*CONT_CXT_WEIGHT+result.stackTraceStructuralMatchScore*STRUCT_CXT_WEIGHT+
						result.sourceContextMatchScore*CODE_CXT_WEIGHT;
				//popularity
				result.popularity_score=result.AlexaCompeteRankScore*ALEXA_WEIGHT+result.SOVoteScore*SOVOTE_WEIGHT;
				//confidence
				//already stored..
				
				//now get the final scores
				result.totalScore_content_context_popularity=result.content_score*CONTENT_RELEVANCE_WEIGHT+result.context_score*CONTEXT_RELEVANCE_WEIGHT+
						result.popularity_score*POPULARITY_WEIGHT+result.search_result_confidence*CONFIDENCE_WEIGHT;
				
			}
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return results;
	}
	
	protected void saveDocAllValues(ArrayList<Result> results, int key)
	{
		//code for saving document content values
		try{
			HashMap<String, Integer> linklabels=getLinkLabels(key);
			String dfile=StaticData.Lucene_Data_Base+"/completeds/docall50/"+key+".txt";
			FileWriter fwriter=new FileWriter(new File(dfile));
			for(Result result:results){
				String url=result.resultURL;
				if(linklabels.containsKey(url.trim())){
					String line=result.content_score+"\t"+result.context_score+"\t"+result.popularity_score+"\t"+
				    result.search_result_confidence;
					line+="\t"+linklabels.get(url);
					fwriter.write(line+"\n");
				}
			}
			fwriter.close();
			System.err.println("Done with:"+key);
		}catch(Exception exc){
		}
	}
	
	protected void saveAllRawScores(ArrayList<Result> results, int key)
	{
		//code for saving the raw scores
		try{
			
			HashMap<String, Integer> linklabels=getLinkLabels(key);
			String dfile=StaticData.Lucene_Data_Base+"/completeds/raw50/"+key+".txt";
			FileWriter fwriter=new FileWriter(new File(dfile));
			for(Result result:results){
				String url=result.resultURL;
				if(linklabels.containsKey(url.trim())){
					String line=result.title_title_MatchScore+"\t";
					line+=result.title_description_MatchScore+"\t";
					line+=result.title_codestack_MathScore+"\t";
					line+=result.title_content_MatchScore+"\t";
					line+=result.stackTraceContentMatchScore+"\t";
					line+=result.stackTraceStructuralMatchScore+"\t";
					line+=result.sourceContextMatchScore+"\t";
					line+=result.AlexaCompeteRankScore+"\t";
					line+=result.SOVoteScore+"\t";
					line+=result.search_result_confidence+"\t";
					line+="\t"+linklabels.get(url);
					fwriter.write(line+"\n");
				}
			}
			fwriter.close();
			System.err.println("Done with:"+key);
			
		}catch(Exception exc){
			
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
	
	public class CustomComparator_cxtps implements Comparator<Result> {
	    @Override
	    public int compare(Result o1, Result o2) {
	    	Double value1=new Double(o1.totalScore_content_context_popularity);
	    	Double value2=new Double(o2.totalScore_content_context_popularity);
	    	return value2.compareTo(value1);
	    	//if(o1.totalScore_content_context_popularity>o2.totalScore_content_context_popularity)return -1;
	    	//else if(o1.totalScore_content_context_popularity<o2.totalScore_content_context_popularity)return 1;
	    	//else return 0;
	    }
	}
	
	public ArrayList<Result> sort_the_result_cxtps(ArrayList<Result> results)
	{
		//code for sorting the result
		Collections.sort(results, new CustomComparator_cxtps());
		return results;
	}
	
	protected void saveResults(ArrayList<Result> results, int key)
	{
		//code for saving the sorted results
		try{
			String resFolder=StaticData.Lucene_Data_Base+"/completeds/results/doc50/"+key+".txt";
			FileWriter fwriter=new FileWriter(new File(resFolder));
			for(Result result:results){
				String line=result.resultURL+"\t"+result.totalScore_content_context_popularity+"\t"+result.content_score+"\t"+result.context_score+
						"\t"+result.popularity_score+"\t"+result.search_result_confidence;
				fwriter.write(line+"\n");
			}	
			fwriter.close();
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	
	protected void getResultRanks()
	{
		//code for sending results
		try{
			
			//for(int i=1;i<=150;i++){
				int key=15;
				//if(i==125)continue;
				ArrayList<Result> results=ResultTitleMatcher.formulate_result_collection(key);
				results=loadContentMatchScores(results, key);
				results=loadContextMatchScores(results, key);
				results=loadPopConfMatchScores(results, key);
			    //now calculate the result scores
				results=getFinalScores(results, key);
				//save the scores
				//saveDocAllValues(results, key);
				ArrayList<Result> sorted=new ArrayList<>();
				sorted=sort_the_result_cxtps(results);
				saveResults(sorted, key);
				System.out.println("saved results for :"+key);
				//saveAllRawScores(results, key);
			//}
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyResultRankManager().getResultRanks();
	}

}
