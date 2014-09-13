package weighting;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import core.StaticData;

public class CopyManager {

	/**
	 * @param args
	 */
	
	protected static void copyAllFiles()
	{
		//code for copying all files of a folder
		try{
			String folder=StaticData.Lucene_Data_Base+"/completeds/docall50";
			String outFile=StaticData.Lucene_Data_Base+"/completeds/weights/sectvalues.txt";
			FileWriter fwriter=new FileWriter(new File(outFile));
			File f=new File(folder);
			if(f.isDirectory()){
				File[] files=f.listFiles();
				for(File f2:files){
					String content=new String();
					Scanner scanner=new Scanner(f2);
					while(scanner.hasNext()){
						String line=scanner.nextLine();
						String[] parts=line.split("\\s+");
						String twords=new String();
						//String label=new String();
								//if(parts[4].trim().equals("2"))label="1";
						twords=parts[0]+"\t"+parts[1]+"\t"+parts[2]+"\t"+parts[3]; //content
						//twords+="\t"+parts[4]+"\t"+parts[5]+"\t"+parts[6]; //context
						//twords+="\t"+parts[7]+"\t"+parts[8]+"\t"+parts[9]; //popularity and confidence
						//twords+="\t"+parts[10];
						twords+="\t"+parts[4];
						content+=twords+"\n";
					}
					scanner.close();
					fwriter.write(content);
				}
				fwriter.close();
			}
		}catch(Exception exc){	
			exc.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		copyAllFiles();
	}

}
