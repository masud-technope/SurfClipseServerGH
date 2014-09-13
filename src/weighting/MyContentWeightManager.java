package weighting;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import codestack.MyExceptionManager;
import scoring.ResultTitleMatcher;
import core.Result;
import core.StaticData;

public class MyContentWeightManager {

	/**
	 * @param args
	 */
	public MyContentWeightManager()
	{
		//default constructor
	}
	
	protected String getSearchQuery(int key)
	{
		//code for getting search query
		String folder=StaticData.Lucene_Data_Base+"/completeds/query";
		String qfile=folder+"/"+key+".txt";
		File f1=new File(qfile);
		String content=new String();
		try{
			Scanner sc=new Scanner(f1);
			while(sc.hasNext())
			{
				String line=sc.nextLine();
				if(line.trim().isEmpty())continue;
				content=line;
			}
		}catch(Exception exc){
		}
		return content;
	}
	
	protected String getStackTrace(int key)
	{
		//code for getting search query
		String folder=StaticData.Lucene_Data_Base+"/completeds/strace";
		String qfile=folder+"/"+key+".txt";
		File f1=new File(qfile);
		String content=new String();
		try{
			Scanner sc=new Scanner(f1);
			while(sc.hasNext())
			{
				String line=sc.nextLine();
				if(line.trim().isEmpty())continue;
				content+=line+"\n";
			}
		}catch(Exception exc){
		}
		return content;
	}
	
	
	protected void get_content_relevance_score()
	{
		//code for getting content relevance scores
		try{
			//for(int i=126;i<=150;i++){
				int key=125;
				//if(i==43)continue;
				ArrayList<Result> results=ResultTitleMatcher.formulate_result_collection(key);
				results=ResultTitleMatcher.load_document_source(results, key);
				String stackTrace=getStackTrace(key);
				String currentException=MyExceptionManager.getCurrentExceptionName(stackTrace);
				String searchQuery=getSearchQuery(key);
				ResultTitleMatcher titleMatcher=new ResultTitleMatcher(results, searchQuery, currentException);
				ArrayList<Result> computedResults=titleMatcher.calculate_title_match_score();
				saveDocContentValues(computedResults, key);
			//}
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
	
	protected void saveDocContentValues(ArrayList<Result> results, int key)
	{
		//code for saving document content values
		try{
			//HashMap<String, Integer> linklabels=getLinkLabels(key);
			String dfile=StaticData.Lucene_Data_Base+"/completeds/doccont/"+key+".txt";
			FileWriter fwriter=new FileWriter(new File(dfile));
			for(Result result:results){
				String url=result.resultURL;
				//if(linklabels.containsKey(url.trim())){
					String line=result.title_title_MatchScore+"\t"+result.title_description_MatchScore+"\t"
					+result.title_codestack_MathScore+"\t"+
				    result.title_content_MatchScore;
					line+="\t"+url;
					//line+="\t"+linklabels.get(url);
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
		new MyContentWeightManager().get_content_relevance_score();
	}
}
