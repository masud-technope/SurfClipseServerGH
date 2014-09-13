package scoring;
import indexmanager.SResultIndexBuilder;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import codestack.CodeStackMiner;
import codestack.MyExceptionManager;
import core.Result;
import core.StaticData;
import similarity.CosineSimilarityMeasure;
import utility.DownloadResultEntryContent;
import utility.StackTraceUtils;

public class ResultTitleMatcher {

	/**
	 * @param args
	 */
	
	public ArrayList<Result> EntryList;
	public String queryTitle;
	String currentURL=null;
	String currentExceptionName;
	ArrayList<String> codestack_tokenset;
	
	public ResultTitleMatcher(ArrayList<Result> EntryList,String queryTitle, String currentExceptionName)
	{
		this.EntryList=EntryList;
		this.queryTitle=queryTitle;
		this.currentExceptionName=currentExceptionName;
		this.codestack_tokenset=new ArrayList<>();
	}
	
	protected double get_title_2_title_match_score(String title, String queryTitle)
	{
		//code for title-title matching
		CosineSimilarityMeasure cos_measure = new CosineSimilarityMeasure(
				title, queryTitle);
		return cos_measure.get_cosine_similarity_score(true);
	}
	
	protected double get_title_desc_text_match(String pageDescription) {
		// code for textual similarity between title and the page description
		// extracted by search engines
		double cos_sim_score = 0;
		try {
			CosineSimilarityMeasure cos_measure = new CosineSimilarityMeasure(
					this.queryTitle, pageDescription);
			cos_sim_score = cos_measure.get_cosine_similarity_score(true);
		} catch (Exception ec) {
			cos_sim_score=0;
		}
		return cos_sim_score;
	}
	
	protected double get_title_code_stack_match_score(ArrayList<String> codestack_tokenset, ArrayList<String> codestackContent)
	{
		//code for getting title-code stack trace matching
		double max_match_score = 0;
		ArrayList<String> tempList=new ArrayList<>();
		if(codestack_tokenset.size()==0 && codestackContent.size()==0)return 0;
		else{
			tempList.addAll(codestack_tokenset);
			tempList.addAll(codestackContent);
			for (String stack_code_part: tempList) {
				CosineSimilarityMeasure cos_measure = new CosineSimilarityMeasure(
						stack_code_part, this.queryTitle);
				double cos_score = cos_measure
						.get_cosine_similarity_score(true);
				if (cos_score > max_match_score)
					max_match_score = cos_score;
			}
		}
		return max_match_score;
	}
	
	protected double get_title_page_body_text_match(String textOnlyContent)
	{
		//code for textual similarity between title and page body
		
		if(textOnlyContent.isEmpty())return 0;
		CosineSimilarityMeasure cos_measure = new CosineSimilarityMeasure(
				textOnlyContent, this.queryTitle);
		double cos_score = cos_measure
				.get_cosine_similarity_score(true);
		return cos_score;
	}
	
	

	protected String getQueryExceptionName()
	{
		//code for getting current exception name from query
		StackTraceUtils utils=new StackTraceUtils(queryTitle);
		String exceptionName=utils.extract_exception_name();
		System.out.println("Query exception name:"+ exceptionName);
		return exceptionName;
		
	}
	
	
	public Result getCodeStacks(Result result)
	{
		//code for populating the context elements
		//collect code stack information
		CodeStackMiner miner=new CodeStackMiner(this.currentExceptionName, result.resultContent);
		miner.minePageCodeStacks();
		if(miner.codestack_content.size()>0)result.codeStacksContent.addAll(miner.codestack_content); //code and other things
		if(miner.codestack_processed.size()>0)result.StacksProcessed.addAll(miner.codestack_processed); //stack trace only
		if(!miner.textOnlyPageContent.isEmpty())result.textContent=miner.textOnlyPageContent; //only text content
		//storing codes tack token set
		this.codestack_tokenset.addAll(miner.codestack_tokenset);
        return result;
	}
	
	
	public ArrayList<Result> calculate_title_match_score() {
		// code for title matching score
		try {
			for (int i = 0; i < this.EntryList.size(); i++) {
				Result result = (Result) this.EntryList.toArray()[i];
				String title = result.title;
				currentURL=result.resultURL;
				
				//title-title matching
				result.title_title_MatchScore = get_title_2_title_match_score(title, this.queryTitle);
				//title description matching
				result.title_description_MatchScore=get_title_desc_text_match(result.description);
				
				//collecting code stack elements
				result=getCodeStacks(result);
								
				result.title_codestack_MathScore=get_title_code_stack_match_score(this.codestack_tokenset,result.codeStacksContent);
				result.title_content_MatchScore=get_title_page_body_text_match(result.textContent);
				
				//*result.title_content_MatchScore=get_title_page_body_text_match(result.resultContent);
			    //result.textContent=this.textContent;
				//System.out.println("t2t:"+result.title_title_MatchScore+", t2cs:"+
				//result.title_codestack_MathScore+",t2b:"+result.title_content_MatchScore+" >"+result.resultURL+(result.resultContent.isEmpty()?true:false));
				//clearing globals
				//clear_globals();
				//clearing global item
				this.codestack_tokenset.clear();
				System.out.println("Content done with:"+result.resultURL);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return this.EntryList;
	}
	
	protected void show_title_matching_score()
	{
		//code for showing the scores
		System.out.println("Search query:" + this.queryTitle);
		int count = 0;
		File f = new File(StaticData.Lucene_Data_Base
				+ "/completeds/weights/contentscore.txt");
		try {
			FileWriter fwriter = new FileWriter(f);
			for (Result result : this.EntryList) {
				String line = count + "\t" + result.title_title_MatchScore
						+ "\t" + result.title_description_MatchScore + "\t"
						+ result.title_codestack_MathScore + "\t"
						+ result.title_content_MatchScore;
				fwriter.write(line + "\n");
				count++;
				if (count == 50)
					break;
			}
			fwriter.close();
		} catch (Exception exc) {
		}
	}
	
		
	public static ArrayList<Result> formulate_result_collection(int key)
	{
		//code for collecting results
		ArrayList<Result> results=new ArrayList<Result>();
		//Result result=new Result();
		//result.title="JTextPane BadLocation";
		//result.resultURL="http://ja.softuses.com/53940";
		//results.add(result);
		results=SResultIndexBuilder.load_sresult_index(key);
		//System.out.println("Result loaded:"+results.size());
		return results;
	}
	
	protected static void save_document_source(ArrayList<Result> results)
	{
		//code for saving the document source
		try{
			int count=0;
			for(Result res:results){
				String folder=StaticData.Lucene_Data_Base+"/completeds/docsource";
				FileWriter writer=new FileWriter(new File(folder+"/"+count+".html"));
				writer.write(res.resultContent);
				writer.close();
				count++;
				System.out.println(res.resultURL);
			}
		}catch(Exception exc){
		}
	}
	
	public static ArrayList<Result> load_document_source(ArrayList<Result> results, int key)
	{
		//code for loading the document source
		try{
			int count=0;
			for(Result res:results){
				String folder=StaticData.Lucene_Data_Base+"/completeds/docsource/"+key;
				File f=new File(folder+"/"+count+".html");
				Scanner scanner=new Scanner(f);
				String content=new String();
				while(scanner.hasNext())
				{
					String line=scanner.nextLine();
					content+=line+"\n";
				}
				res.resultContent=content;
				count++;
			}
		}catch(Exception exc){}
		return results;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			//ArrayList<Result> results=formulate_result_collection(20);
			ArrayList<Result> results=formulate_result_collection(44);
			DownloadResultEntryContent downloader=new DownloadResultEntryContent(results);
			results=downloader.download_result_entry_content();
			//save_document_source(results);
			//results=load_document_source(results,3);
			
			String query="javax.swing.text.BadLocationException: Invalid location";
			String currentException="javax.swing.text.BadLocationException";
			ResultTitleMatcher matcher=new ResultTitleMatcher(results, query, currentException);
			matcher.calculate_title_match_score();
			matcher.show_title_matching_score();
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
}
