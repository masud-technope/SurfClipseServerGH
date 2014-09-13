package weighting;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import core.StaticData;

public class SolutionReformer {

	/**
	 * @param args
	 */
	
	protected String getOldSolutions(int key)
	{
		//code for getting old solutions
		String content=new String();
		try{
			String SolutionFolder=StaticData.Lucene_Data_Base+"/completeds/solution";
			File f=new File(SolutionFolder+"/"+key+".txt");
			Scanner scanner=new Scanner(f);
			while(scanner.hasNext())
			{
				content+=scanner.nextLine()+"\n";
			}
			scanner.close();
		}catch(Exception exc){
		}
		return content;
	}
	
	protected String getHighlyRelevants(int key)
	{
		//code for getting highly relevant items
		String content=new String();
		try{
			String doccorpus=StaticData.Lucene_Data_Base+"/completeds/doccorpus";
			String fileURL=doccorpus+"/"+key+".txt";
			Scanner scanner=new Scanner(new File(fileURL));
			while(scanner.hasNext())
			{
				String line=scanner.nextLine();
				if(line.trim().startsWith("2")){
					content+=line.split("\\s+")[1];
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return content;
	}
	
	
	protected void reformSolutions()
	{
		//code for reforming the solutions
		try{
			
			String newSolutionFolder=StaticData.Lucene_Data_Base+"/completeds/newsolution";
			int added=0;
			for(int i=1;i<=50;i++){
				
				int key=i;
				String oldsols=getOldSolutions(key);
				String newsols=getHighlyRelevants(key);
				if(!newsols.trim().isEmpty())added++;
				String combinedContent=oldsols+"\n"+newsols;
				
				//saving the solutions
				FileWriter fwriter=new FileWriter(new File(newSolutionFolder+"/"+key+".txt"));
				fwriter.write(combinedContent);
				fwriter.close();
			}
			System.out.println("New item added:"+added);
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SolutionReformer().reformSolutions();
	}

}
