package scoring;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import core.Result;
import core.StackTrace;
import similarity.CosineSimilarityMeasure;
import utility.DownloadResultEntryContent;
import utility.RegexMatcher;
import utility.StackTraceUtils;

public class ResultTitleMatcher {

	/**
	 * @param args
	 */
	
	public ArrayList<Result> EntryList;
	public String queryTitle;
	ArrayList<String> codestacks_content;
	ArrayList<String> codestacks_tokens;
	ArrayList<Element> codestacks_html;
	ArrayList<StackTrace> codestacks_processed;
	String textContent;
	String currentURL=null;
	
	public ResultTitleMatcher(ArrayList<Result> EntryList,String queryTitle)
	{
		this.EntryList=EntryList;
		this.queryTitle=queryTitle;
		this.codestacks_content=new ArrayList<String>();
		this.codestacks_tokens=new ArrayList<String>();
		this.codestacks_html=new ArrayList<Element>();
		this.codestacks_processed=new ArrayList<StackTrace>();
		textContent=new String();
	}
	protected void clear_globals()
	{
		this.textContent="";
		this.codestacks_content.clear();
		this.codestacks_tokens.clear();
		this.codestacks_processed.clear();
		this.codestacks_html.clear(); //culprit
	}
	
	protected double get_title_2_title_match_score(String title, String queryTitle)
	{
		//code for title-title matching
		CosineSimilarityMeasure cos_measure = new CosineSimilarityMeasure(
				title, queryTitle);
		return cos_measure.get_cosine_similarity_score();
	}
	
	protected double get_title_code_stack_match_score(String pageBodyContent)
	{
		//code for getting title-code stack trace matching
		if(pageBodyContent.isEmpty())return 0;
		ArrayList<String> stack_code_parts = get_codestack_content(pageBodyContent);
		//temporary storing
		this.codestacks_tokens=stack_code_parts;
		double max_match_score = 0;
		if (!stack_code_parts.isEmpty()) {
			for (String stack_code_part : stack_code_parts) {
				CosineSimilarityMeasure cos_measure = new CosineSimilarityMeasure(
						stack_code_part, this.queryTitle);
				double cos_score = cos_measure
						.get_cosine_similarity_score();
				if (cos_score > max_match_score)
					max_match_score = cos_score;
			}
		}
		return max_match_score;
	}
	
	protected double get_title_page_body_text_match(String resultContent)
	{
		//code for textual similarity between title and page body
		String pageBodyContent=resultContent;
		if(pageBodyContent.isEmpty())return 0;
		this.textContent=get_text_only_content(resultContent);
		CosineSimilarityMeasure cos_measure = new CosineSimilarityMeasure(
				this.textContent, this.queryTitle);
		double cos_score = cos_measure
				.get_cosine_similarity_score();
		return cos_score;
	}
	
	protected String get_text_only_content(String resultContent) {
		// code for text only content
		String textOnlyContent = new String();
		try {
			Document doc=Jsoup.parse(resultContent);
			Element bodyElem=doc.select("body").first();
			String bodyHtmlStr=bodyElem.toString();
			for(Element elem:codestacks_html)
			{
				String temp=bodyHtmlStr.replace(elem.toString(), "");
				bodyHtmlStr=temp;
			}
			Document doc2=Jsoup.parse(bodyHtmlStr);
			String codestackFreeContent=doc2.select("body").first().text();
			textOnlyContent=codestackFreeContent;
			//System.out.println(textOnlyContent);
		} catch (Exception exc) {
		System.err.println(exc.getMessage());
		}
		return textOnlyContent;
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
				result.title_codestack_MathScore=get_title_code_stack_match_score(result.resultContent);
				if(codestacks_content.size()>0)result.codeStacksContent.addAll(this.codestacks_content);
				if(codestacks_processed.size()>0)result.StacksProcessed.addAll(this.codestacks_processed);
				result.title_content_MatchScore=get_title_page_body_text_match(result.resultContent);
				result.textContent=this.textContent;
				//System.out.println("t2t:"+result.title_title_MatchScore+", t2cs:"+
				//result.title_codestack_MathScore+",t2b:"+result.title_content_MatchScore+" >"+result.resultURL+(result.resultContent.isEmpty()?true:false));
				//clearing globals
				clear_globals();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return this.EntryList;
	}
	
	protected void show_title_matching_score()
	{
		//code for showing the scores
		for(Result result:this.EntryList)
		{
			System.out.println(result.resultURL);
			System.out.println("title 2 title:"+result.title_title_MatchScore);
			System.out.println("title 2 codestack:"+result.title_codestack_MathScore);
			System.out.println("title 2 textcontent:"+result.title_content_MatchScore);
			System.out.println("==========================");
		}
	}
	
	protected ArrayList<String> get_codestack_content(String resultContent)
	{
		//code for extracting result body content
		ArrayList<String> myContent=new ArrayList<String>();
		Document document=Jsoup.parse(resultContent);
		Elements quotes=document.select("blockquote");
		codestacks_html.addAll(quotes);
	    //System.out.println(codestacks.size());
		Elements quotes1=document.select("code");
		codestacks_html.addAll(quotes1);
		//System.out.println(codestacks.size());
		Elements quotes2=document.select("pre");
		codestacks_html.addAll(quotes2);
		
		
		System.err.println(currentURL);
		
		//System.out.println(codestacks.size());
		int stack_developed=0;
		for(Element quote:codestacks_html)
		{
			String stack=quote.text();
			this.codestacks_content.add(stack);
			
			try
			{
			if (RegexMatcher.matches_stacktrace(stack)) {
				StackTraceUtils st_utils = new StackTraceUtils(stack);
				StackTrace strace = st_utils.analyze_stack_trace();
				this.codestacks_processed.add(strace);
				if (!strace.stackTraceTokens.isEmpty()) {
					myContent.add(strace.stackTraceTokens);
					stack_developed++;
					// System.out.println("Tokens:"+strace.stackTraceTokens);
				}
			}
			else myContent.add(stack);
			}catch(Exception exc){}
		}
		
		//specially handling stack trace not in code
		if(stack_developed==0){
		Elements quotes3=document.select("p");
		for(Element quote:quotes3)
		{
			String stack=quote.text();
			try
			{
				if(RegexMatcher.matches_stacktrace(stack))
				{
					StackTraceUtils st_utils = new StackTraceUtils(stack);
					StackTrace strace = st_utils.analyze_stack_trace();
					this.codestacks_processed.add(strace);
					this.codestacks_content.add(stack);
					this.codestacks_html.add(quote);
				}else
				{
					//do nothing..
				}
				
			}catch(Exception exc){
				
			}
		}
		}
		
		
		//System.out.println(myContent.size());
		//returning page content
		return myContent;
	}
	
	protected static ArrayList<Result> formulate_result_collection()
	{
		//code for collecting results
		ArrayList<Result> results=new ArrayList<Result>();
		Result result=new Result();
		result.title="java.lang.ClassNotFoundException: org.sqlite.JDBC";
		result.resultURL="http://stackoverflow.com/questions/16279858/java-lang-classnotfoundexception-org-sqlite-jdbc";
		results.add(result);
		return results;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			ArrayList<Result> results=formulate_result_collection();
			DownloadResultEntryContent downloader=new DownloadResultEntryContent(results);
			results=downloader.download_result_entry_content();
			String query="java.lang.ClassNotFoundException  org.sqlite.JDBC";
			ResultTitleMatcher matcher=new ResultTitleMatcher(results, query);
			matcher.calculate_title_match_score();
			matcher.show_title_matching_score();
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
}
