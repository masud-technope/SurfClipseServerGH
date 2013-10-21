package resproc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MiscJob {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	
	
	
	protected void make_query_folder()
	{
		String fileName="C:/MyWorks/SurfClipse/data/queries.txt";
		try
		{
		Scanner scanner=new Scanner(new File(fileName));
		int line_num=0;
		while(scanner.hasNext())
		{
			String line=scanner.nextLine();
			if(!line.isEmpty())
			{
			line_num++;
			//save_the_query_n_result(line_num, line);
			}
		}
		}catch(Exception exc){
			
		}
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MiscJob job=new MiscJob();
		job.make_query_folder();
		System.out.println("Done");
		
		

	}

}
