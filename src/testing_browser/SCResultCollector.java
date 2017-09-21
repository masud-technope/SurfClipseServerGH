package testing_browser;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import resproc.result_analysis;

import core.Result;
import core.SearchResultProvider;
import core.StaticData;

public class SCResultCollector {

	/**
	 * @param args
	 */
	
	static String getQuery(String filePath) {
		// code for getting query
		String content = new String();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (!line.isEmpty()) {
					content = line;
					break;
				}
			}
			scanner.close();
		} catch (Exception exc) {
		}
		
		return content;
	}
	
	static String getStackTrace(String filePath) {
		// code for getting stack trace
		String content = new String();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			//scanner.nextLine(); // skips the exception name
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (line.startsWith("!MESSAGE"))
					continue;
				if (!line.isEmpty()) {
					content += line + "\n";
				}
			}
			scanner.close();
		} catch (Exception exc) {
		}
		return content;
	}
	
	static String getCodeContext(String filePath) {
		// code for getting code context
		String content = new String();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (!line.isEmpty()) {
					content += line + "\n";
				}
			}
			scanner.close();
		} catch (Exception exc) {
		}
		return content;
	}
	
	
	static void save_the_results(String key, ArrayList<Result> tempList) {
		String sclipse = StaticData.Lucene_Data_Base + "/completeds/sclipset";
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String query = StaticData.Lucene_Data_Base + "/completeds/query";
		String strace = StaticData.Lucene_Data_Base + "/completeds/strace";
		String ccontext = StaticData.Lucene_Data_Base + "/completeds/ccontext";
		File f = new File(query);
		if (f.isDirectory()) {
			
			//String[] problemList={"7.txt","15.txt","43.txt","66.txt","71.txt","72.txt","75.txt"};
			//ArrayList<String> blist=new ArrayList<>(Arrays.asList(problemList));
			//File[] qfiles = f.listFiles();
			//for (int i=62;i<84;i++) {
				try
				{
				long start=System.currentTimeMillis();
				int counter=61; //36;
				File f1=new File(query+"/"+counter+".txt");
				//if(!f1.exists())continue;
				String key = f1.getName();
					
				//if(blist.contains(key))continue;
	
				String queryContent = new String();
				String traceContent = new String();
				String codeContent = new String();
				
				String recentPagedata = new String();
				// query
				queryContent = getQuery(f1.getAbsolutePath());
				System.out.println("Started:"+counter+" "+queryContent);
				// trace
				try {
					traceContent = getStackTrace(strace + "/" + key);
				} catch (Exception exc) {
				}
				// code context
				try {
					codeContent = getCodeContext(ccontext + "/" + key);
				} catch (Exception exc) {
				}
				// now conduct the search and collect results
				SearchResultProvider provider = new SearchResultProvider(
						queryContent, traceContent, codeContent, recentPagedata);
				provider.currentException=counter;
				ArrayList<Result> finalResults = provider
						.provide_the_final_results();
				save_the_results(key, finalResults);
				System.out.println(key+":"+queryContent );
				//delay 2 seconds
				Thread.sleep(2000);
				//break;
				
				long end=System.currentTimeMillis();
				System.out.println("Time passed:"+(end-start)/1000);
				
				}catch(Exception exc){
					exc.printStackTrace();
				}
			}
		//}  
	}
}
