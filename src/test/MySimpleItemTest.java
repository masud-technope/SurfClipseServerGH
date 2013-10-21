package test;

import gaecore.GoogleAPI;

import java.net.URLDecoder;
import java.util.ArrayList;

import scoring.ResultScoreManager;
import scoring.ScoreCalculator;

import core.Result;
import core.SearchResultProvider;

public class MySimpleItemTest {

	/**
	 * @param args
	 */
	
	//class members
	String queryTitle;
	String stackTrace;
	String codeContext;
	String recentPageData;
	ArrayList<Result> Primary_Results;
	
	public MySimpleItemTest(String queryTitle, String stackTrace,
			String codeContext, String recentPagedata) {
		this.Primary_Results = new ArrayList<Result>();
		String charset = "UTF-8";
		try {
			this.queryTitle = URLDecoder.decode(queryTitle, charset);
			this.stackTrace = URLDecoder.decode(stackTrace, charset);
			this.codeContext = URLDecoder.decode(codeContext, charset);
			this.recentPageData = URLDecoder.decode(recentPagedata, charset);
		} catch (Exception exc) {
		}
	}
	
	protected ArrayList<Result> get_result_lists(String searchQuery)
	{
		//code for getting the result
		String title="Eclipse not able to find org.sqlite.JDBC (Java)";
		String linkurl="http://forums.devshed.com/java-help-9/sqlite-and-jdbc-class-not-found-851019.html";
		GoogleAPI gapi=new GoogleAPI();
		this.Primary_Results=gapi.find_Google_Results(searchQuery);
		return this.Primary_Results;
	}
	
	
	public ArrayList<Result> perform_overall_test()
	{
		//code for all calculation
		get_result_lists(queryTitle);
		ScoreCalculator calc=new ScoreCalculator(this.Primary_Results, queryTitle, stackTrace, codeContext, recentPageData);
		this.Primary_Results=calc.calculate_intermediate_scores();
		ResultScoreManager manager=new ResultScoreManager(this.Primary_Results);
		manager.calculate_relative_scores();
		return manager.prerpare_final_score();
		//showing scores
		//show_all_scores(this.Primary_Results.get(0));	  
	}
	
	protected static void show_all_scores(ArrayList<Result> results)
	{
		//code for showing the scores
		for(Result result:results)
		{
		System.out.println("Title:"+result.title);
		System.out.println("Link:"+result.resultURL);
		System.out.println("Title to title:"+result.title_title_MatchScore);
		System.out.println("Title to code stack:"+result.title_codestack_MathScore);
		System.out.println("Title to Body:"+result.title_content_MatchScore);
		
		System.out.println("Stack to Stack:"+result.stackTraceMatchScore);
		System.out.println("Code context to Code:"+result.sourceContextMatchScore);
		System.out.println("Recency score:"+result.recentHistoryScore);
		
		System.out.println("SO vote:"+result.SOVoteScore);
		System.out.println("Alexa Score:"+result.AlexaCompeteRankScore);
		
		System.out.println("Representative Text:"+result.representativeText);
		System.out.println("======================");
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
		
		long started=System.currentTimeMillis();
		String searchQuery="http://srlabg53-2.usask.ca/wssurfclipse/surfclipse_app?searchquery=java.lang.ClassNotFoundException%3A+org.sqlite.JDBC&stacktrace=java.lang.ClassNotFoundException%3A+org.sqlite.JDBC%0D%0A%09at+java.net.URLClassLoader%241.run%28Unknown+Source%29%0D%0A%09at+java.net.URLClassLoader%241.run%28Unknown+Source%29%0D%0A%09at+java.security.AccessController.doPrivileged%28Native+Method%29%0D%0A%09at+java.net.URLClassLoader.findClass%28Unknown+Source%29%0D%0A%09at+java.lang.ClassLoader.loadClass%28Unknown+Source%29%0D%0A%09at+sun.misc.Launcher%24AppClassLoader.loadClass%28Unknown+Source%29%0D%0A%09at+java.lang.ClassLoader.loadClass%28Unknown+Source%29%0D%0A%09at+java.lang.Class.forName0%28Native+Method%29%0D%0A%09at+java.lang.Class.forName%28Unknown+Source%29%0D%0A%09at+core.ANotherTest.main%28ANotherTest.java%3A18%29%0D%0A&codecontext=%09%09%7B%0D%0A%09%09%2F%2Fcode+for+making+connection+with+a+sqlite+database%0D%0A%09%09%09Class.forName%28%22org.sqlite.JDBC%22%29%3B%0D%0A%09%09%09Connection+connection%3Dnull%3B%0D%0A%09%09%09connection%3DDriverManager.getConnection%28%22jdbc%3Asqlite%3A%22%2B%22%2F%22%2B%22test.db%22%29%3B%0D%0A%09%09%09Statement+statement%3Dconnection.createStatement%28%29%3B%0D%0A%09%09%09String+create_query%3D%22create+table+History+%28+LinkID+INTEGER+primary+key%2C+Title+TEXT+not+null%2C+LinkURL+TEXT+not+null%29%3B%22%3B%0D%0A%09%09%09boolean+created%3Dstatement.execute%28create_query%29%3B%0D%0A%09%09%09System.out.println%28%22Succeeded%22%29%3B%0D%0A%09%09%7Dcatch%28Exception+exc%29%7B%0D%0A%09%09%09exc.printStackTrace%28%29%3B%0D%0A%09%09%7D%0D%0A%09%09%0D%0A&recentpagedata=http%3A%2F%2Fwww.java2s.com%2FTutorial%2FSCJP%2F0020__Java-Source-And-Data-Type%2FDivisionbyzeroinintegerarithmeticproducesaruntimeArithmeticException.htm%3D0.0%2Chttp%3A%2F%2Fjava2novice.com%2Fjava_exception_handling_examples%2Fexception_ternminates_program%2F%3D0.0%2Chttp%3A%2F%2Fwww.java-forums.org%2Fnew-java%2F46648-exception-thread-main.html%3D0.0%2Chttp%3A%2F%2Fwww.daniweb.com%2Fsoftware-development%2Fjava%2Fthreads%2F292541%2Farithmetic-exception%3D0.0%2Chttp%3A%2F%2Fwww.javatpoint.com%2Ftry-catch-block%3D0.0%2Chttp%3A%2F%2Fzhidao.baidu.com%2Fquestion%2F324617811.html%3D0.0%2Chttp%3A%2F%2Fwww.dreamincode.net%2Fforums%2Ftopic%2F22662-exception-intermediates-throwing-and-chaining%2F%3D0.0%2Chttp%3A%2F%2Fwww.geeksforgeeks.org%2Fchecked-vs-unchecked-exceptions-in-java%2F%3D0.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F15500202%2Fexception-in-thread-main-java-lang-arithmeticexception-by-zero-finding-fa%3D0.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F10817706%2Fexception-in-thread-main-java-lang-arithmeticexception-by-zero%3D0.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F16802646%2Fusing-resources-from-expansion-files-android%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F15170738%2Fissues-with-asynchronous-reads-in-net-it-seems-that-my-callback-is-being-call%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F16929690%2Fandroid-force-close-on-json-parse-from-url-api%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F18672431%2Fjava-creating-a-heappriorityqueue-using-binary-heap-to-implement-priorityqueue%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F17806069%2Fbluetoothchat-like-app-not-communicating%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F18543347%2Finsert-pictures-in-database-php-mysql-with-username%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F18230497%2Flocal-sqlite-connection-on-jdeveloper-adf-mobile%3D1.0%2Chttps%3A%2F%2Fbitbucket.org%2Fxerial%2Fsqlite-jdbc%3D1.0%2Chttp%3A%2F%2Fwww.java-forums.org%2Fawt-swing%2F16304-classnotfoundexception-org-sqlite-jdbc.html%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F16279858%2Fjava-lang-classnotfoundexception-org-sqlite-jdbc%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F7060274%2Fwhat-do-i-have-to-do-to-avoid-error-of-out-of-memory-when-connection-by-jdbc%3D1.0%2Chttp%3A%2F%2Fsoftshare.wikidot.com%2Fjava-sqlite3%3D1.0%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F1525444%2Fhow-to-connect-sqlite-with-java%3D3.3546262790251185E-4%2Chttp%3A%2F%2Fwww.rqgg.net%2Ftopic%2Fpqihy-java-lang-classnotfoundexception-org-sqlite-jdbc.html%3D3.3546262790251185E-4%2Chttp%3A%2F%2Fstackoverflow.com%2Fquestions%2F18222719%2Fsql-error-with-creating-a-sequence%3D8.315287191035679E-7%2Chttp%3A%2F%2Fpastebin.com%2FfJvGHtZj%3D3.059023205018258E-7%2Chttp%3A%2F%2Fpastebin.com%2FgyVaY2M2%3D3.059023205018258E-7%2Chttp%3A%2F%2Fforums.devshed.com%2Fjava-help-9%2Fsqlite-and-jdbc-class-not-found-851019.html%3D3.059023205018258E-7";
		String splits[]=searchQuery.split("\\&");
		String charet="UTF-8";
		String queryTitle=URLDecoder.decode(splits[0].split("=")[1],charet);
		String stackTrace=URLDecoder.decode(splits[1].split("=")[1],charet);
		String codeContext=URLDecoder.decode(splits[2].split("=")[1],charet);
		String recentPageData=URLDecoder.decode(splits[3].split("=")[1],charet);
		MySimpleItemTest simple=new MySimpleItemTest(queryTitle, stackTrace, codeContext, recentPageData);
		ArrayList<Result> results=simple.perform_overall_test();
		//SearchResultProvider provider=new SearchResultProvider(queryTitle, stackTrace,codeContext,recentPageData);
		//ArrayList<Result> results=provider.provide_the_final_results();
		System.out.println("Total results extracted:"+results.size());
		long ended=System.currentTimeMillis();
		System.out.println("Time elapsed:"+(ended-started)/1000+" seconds");
		show_all_scores(results);
		}catch(Exception exc){}
		
	}

}
