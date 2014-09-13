package misc;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import utility.StackTraceUtils;

import core.StaticData;

public class DatasetExcepList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String strace=StaticData.Lucene_Data_Base+"/completeds/strace";
		try{
			File dir=new File(strace);
			HashSet<String> excepList=new HashSet<>(); 
			HashMap<String, Integer> excepMap=new HashMap<>();
			if(dir.isDirectory()){
				File[] files=dir.listFiles();
				for(File f2:files){
					Scanner scanner=new Scanner(f2);
					String excep=new String();
					while(scanner.hasNext())
					{
						String line=scanner.nextLine().trim();
						//System.out.println(f2.getName()+" :"+ line);
						if(!line.isEmpty()){
							try{
							StackTraceUtils utils=new StackTraceUtils(line);
							excep=utils.extract_exception_name();
							}catch(Exception exc){}
							excepList.add(f2.getName()+" "+excep);
							//excepList.add(f2.getName()+" "+ line);
							/*if(excepMap.containsValue(excep)){
								int count=excepMap.get(excep).intValue();
								count++;
								excepMap.put(excep, count);
							}else{
								excepMap.put(excep, new Integer(1));
							}
							*/
							
							
							break;
						}
					}
//					if(!excep.isEmpty()){
//						excepList.add(excep);
//					}
				}
			}
			//showing the exception list
			for(String exc:excepList){
				//System.out.println(exc+" "+excepMap.get(exc));
				System.out.println(exc);
			}
			System.out.println("Total:"+excepList.size());
		}catch(Exception  exc){
			
		}
		
		
	}

}
