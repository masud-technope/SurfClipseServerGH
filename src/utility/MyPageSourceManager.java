package utility;

import indexmanager.SResultIndexBuilder;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.hibernate.event.SaveOrUpdateEvent;

import core.Result;
import core.StaticData;

public class MyPageSourceManager {

	/**
	 * @param args
	 */
	protected static void save_document_source(ArrayList<Result> results, int key)
	{
		//code for saving the document source
		try{
			
			String folder=StaticData.Lucene_Data_Base+"/completeds/docsource";
			File f1=new File(folder+"/"+key);
			if(!f1.exists()){
				f1.mkdir();
			int count=0;
			for(Result res:results){	
				FileWriter writer=new FileWriter(new File(folder+"/"+key+"/"+count+".html"));
				writer.write(res.resultContent);
				writer.close();
				count++;
				System.out.println(res.resultURL);
			}}
		}catch(Exception exc){
		}
	}
	
	protected static void savetop10links(ArrayList<Result> results, int key)
	{
		//code for saving the top 10 links
		String folder=StaticData.Lucene_Data_Base+"/completeds/doccorpus";
		try{
			File f=new File(folder+"/"+key+".txt");
			FileWriter writer=new FileWriter(f);
			int count=0;
			for(Result res:results){
				writer.write(res.resultURL+"\n");
				count++;
				if(count==10)break;
				//System.out.println(res.resultURL);
			}
			writer.close();
		}catch(Exception exc){
			
		}
		
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//for(int i=116;i<150;i++){
			int key=43;
			ArrayList<Result> results=SResultIndexBuilder.load_sresult_index(key);
			DownloadResultEntryContent downloader=new DownloadResultEntryContent(results);
			ArrayList<Result> myResults=downloader.download_result_entry_content();
			save_document_source(results, key);
			//savetop10links(results, key);
			System.out.println("Done with:"+key);
		//}
		
	}

}
