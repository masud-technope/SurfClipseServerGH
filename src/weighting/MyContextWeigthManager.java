package weighting;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import codestack.MyExceptionManager;

import scoring.ResultStackTraceMatcher;
import scoring.ResultTitleMatcher;
import scoring.SourceCodeContextMatcher;
import core.Result;
import core.StaticData;

public class MyContextWeigthManager {

	/**
	 * @param args
	 */
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
	
	
	protected String getCodeContext(int key)
	{
		//code for getting the code context
		String folder=StaticData.Lucene_Data_Base+"/completeds/ccontext";
		String qfile=folder+"/"+key+".txt";
		File f1=new File(qfile);
		String content=new String();
		if(!f1.exists())return content;
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
	
		
	protected void get_context_relevance_score()
	{
		//code for getting content relevance scores
		try{
			//for(int i=51;i<=150;i++){
				int key=125;
				//if(i==125)continue;
				ArrayList<Result> results=ResultTitleMatcher.formulate_result_collection(key);
				results=ResultTitleMatcher.load_document_source(results, key);
				System.out.println("==== source loaded successfully :"+key+" ========");
				
				String searchQuery=new String();
				searchQuery=getSearchQuery(key);
				
				//get stack trace matching scores
				String querystacktrace=getStackTrace(key);
				String currentException=MyExceptionManager.getCurrentExceptionName(querystacktrace);
				
				ResultTitleMatcher titleMatcher=new ResultTitleMatcher(results, searchQuery, currentException );
				ArrayList<Result> computedResults=new ArrayList<>();
				for(Result result:results){
					Result mresult=titleMatcher.getCodeStacks(result);
					computedResults.add(mresult);
					System.err.println("Context extracted:"+mresult.resultURL);
				}
				//ArrayList<Result> computedResults=titleMatcher.calculate_title_match_score();
				
				ResultStackTraceMatcher stackMatcher=new ResultStackTraceMatcher(computedResults, querystacktrace);
				ArrayList<Result> computed1Results=stackMatcher.calculate_stacktrace_score();
				
				//get code context matching scores
				String codecontext=getCodeContext(key);
				SourceCodeContextMatcher ccontextMatcher=new SourceCodeContextMatcher(computed1Results, codecontext);
				ArrayList<Result> computed2Results=ccontextMatcher.calculate_codecontext_score();
				
				//saving the scores
				saveDocContextValues(computed2Results, key);
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
	
	protected void saveDocContextValues(ArrayList<Result> results, int key)
	{
		//code for saving document content values
		try{
			//HashMap<String, Integer> linklabels=getLinkLabels(key);
			String dfile=StaticData.Lucene_Data_Base+"/completeds/doccxt/"+key+".txt";
			FileWriter fwriter=new FileWriter(new File(dfile));
			for(Result result:results){
				String url=result.resultURL;
				//if(linklabels.containsKey(url.trim())){
					String line=result.stackTraceContentMatchScore+"\t"+result.stackTraceStructuralMatchScore+"\t"+result.stackTraceMatchScore+"\t"+
				    result.sourceContextMatchScore;
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
		new MyContextWeigthManager().get_context_relevance_score();
	}	
}
