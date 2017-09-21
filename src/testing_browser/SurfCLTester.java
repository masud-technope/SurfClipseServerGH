package testing_browser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import core.StaticData;

public class SurfCLTester {

	/**
	 * @param args
	 */
	HashMap<String, ArrayList<String>> orginalSolution;
	HashMap<String, ArrayList<String>> scSolution;
	String newQuery=StaticData.Lucene_Data_Base+"/completeds/newquery/SetB";
	int total_solution=0;
	int solution_retrieved=0;
	public SurfCLTester()
	{
		this.orginalSolution=new HashMap<>();
		this.scSolution=new HashMap<>();
	}
	
	
	protected void collect_org_solutions()
	{
		//code for collecting original solutions
		try
		{
			String path=StaticData.Lucene_Data_Base+"/completeds/solution";
			File f=new File(path);
			if(f.isDirectory())
			{
				File[] files=f.listFiles();
				for(File f1:files)
				{
					
					String key=f1.getName();
					//if(!new File(newQuery+"/"+key).exists())continue;
					
					try
					{
					Scanner scanner=new Scanner(f1);
					scanner.nextLine();
					ArrayList<String> temp=new ArrayList<>();
					while(scanner.hasNext())
					{
						String sol=scanner.nextLine().trim();
						if(!sol.isEmpty())
						{
							URL u=new URL(sol);
							temp.add(sol);
						}
					}
					scanner.close();
					//now add the arraylist to hmap
					this.orginalSolution.put(f1.getName(), temp);
					//System.out.println(f1.getName()+"="+temp.size());
					}catch(Exception exc){
						
					}
				}
			}
		}catch(Exception exc){}
	}
	protected void collect_google_solutions()
	{
		//code for collecting google solutions
		try
		{
			String path=StaticData.Lucene_Data_Base+"/completeds/sclipsep"; //sclipse
			File f=new File(path);
			if(f.isDirectory())
			{
				File[] files=f.listFiles();
				for(File f1:files)
				{
					
					String key=f1.getName();
					if(!new File(newQuery+"/"+key).exists())continue;
					
					try
					{
					Scanner scanner=new Scanner(f1);
					//scanner.nextLine();
					ArrayList<String> temp=new ArrayList<>();
					while(scanner.hasNext())
					{
						String sol=scanner.nextLine().trim();
						if(!sol.isEmpty())
						{
							URL u=new URL(sol);
							temp.add(sol);
						}
					}
					scanner.close();
					//now add the arraylist to hmap
					this.scSolution.put(f1.getName(), temp);
					//System.out.println(f1.getName()+"="+temp.size());
					}catch(Exception exc){
						
					}
				}
			}
		}catch(Exception exc){}
	}
	
	
	protected double calculate_precision(int K, ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		double precision_sum=0;
		double precision_avg=0;
		double retrieved_relevant_items=0;
		
		if(googleList.size()==0)return 0;
		
		int indexLimit=googleList.size()<K?googleList.size():K;
		int hindex=0;
		for(int i=0;i<indexLimit;i++)
		{
			String link=googleList.get(i);
			String _link=link.trim().split("\\s+")[0];
			if(solutionList.contains(_link))
			{
				retrieved_relevant_items++;
				//double instant_precision=(retrieved_relevant_items/(i+1));
				//precision_sum+=instant_precision;
				hindex=i;
			}
		}
		if(retrieved_relevant_items==0)return 0;
		
		//System.out.println("Last solution found:"+hindex);
		
		precision_avg=retrieved_relevant_items/K;
		return precision_avg;
	}
	
	
	protected double calculate_precision_at_k(int K, ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		double precision_sum=0;
		double precision_avg=0;
		double retrieved_relevant_items=0;
		
		if(googleList.size()==0)return 0;
		
		int indexLimit=googleList.size()<K?googleList.size():K;
		int hindex=0;
		for(int i=0;i<indexLimit;i++)
		{
			String link=googleList.get(i);
			String _link=link.trim().split("\\s+")[0];
			if(solutionList.contains(_link))
			{
				retrieved_relevant_items++;
				double instant_precision=(retrieved_relevant_items/(i+1));
				precision_sum+=instant_precision;
				hindex=i;
			}
		}
		if(retrieved_relevant_items==0)return 0;
		
		//System.out.println("Last solution found:"+hindex);
		
		precision_avg=precision_sum/retrieved_relevant_items;
		return precision_avg;
	}
	
	protected void calculate_mean_avg_precision(int K)
	{
		double sum=0;
		
		int answer_found=0;
		int querysize=this.scSolution.keySet().size();
		
		for(String key:this.scSolution.keySet())
		{
			if(!new File(newQuery+"/"+key).exists())continue;
			
			//System.out.println(key);
			ArrayList<String> glist=this.scSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			//int K=30;
			double patk=calculate_precision_at_k(K, glist, slist);
			if(patk>0)answer_found++;
			else System.err.println(key);
			//else System.out.println(key);
			//System.out.println(key+": Precision at "+K+" "+patk);
			sum+=patk;
		}
		
		
		System.out.println("Mean average precision:"+(sum/querysize));
		System.out.println("Number of query:"+querysize);
		System.out.println("Answer found:"+answer_found);
	}
	
	
	protected void calculate_mean_precision(int K)
	{
		double sum=0;
		
		int answer_found=0;
		int querysize=this.scSolution.keySet().size();
		
		for(String key:this.scSolution.keySet())
		{
			//if(!new File(newQuery+"/"+key).exists())continue;
			
			//System.out.println(key);
			ArrayList<String> glist=this.scSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			//int K=30;
			double patk=calculate_precision(K, glist, slist);
			if(patk>0)answer_found++;
			else System.err.println(key);
			//else System.out.println(key);
			//System.out.println(key+": Precision at "+K+" "+patk);
			sum+=patk;
		}
		
		System.out.println("Query size:"+querysize);
		
		System.out.println("Mean precision:"+(sum/querysize));
		System.out.println("Number of query:"+querysize);
		System.out.println("Answer found:"+answer_found);
	}
	
	
	protected double calculate_recall_at_k(int K, ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		
		double recall_each=0;
		double retrieved_relevant_items=0;
		
		if(googleList.size()==0)return 0;
	
		int indexLimit=googleList.size()<K?googleList.size():K;
		for(int i=0;i<indexLimit;i++)
		{
			String link=googleList.get(i);
			String _link=link.split("\\s+")[0];
			if(solutionList.contains(_link))
			{
				retrieved_relevant_items++;
			}
		}
		solution_retrieved+=retrieved_relevant_items;
		total_solution+=solutionList.size();
		
		if(retrieved_relevant_items==0)return 0;
		
		recall_each=retrieved_relevant_items/solutionList.size();
		return recall_each;
	}
	
	protected void calculate_mean_recall(int K)
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.scSolution.keySet().size();
		for(String key:this.scSolution.keySet())
		{
			//if(!new File(newQuery+"/"+key).exists())continue;
			
			//System.out.println(key);
			ArrayList<String> glist=this.scSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			//int K=30;
			double patk=calculate_recall_at_k(K, glist, slist);
			if(patk>0)answer_found++;
			
			//System.out.println(key+": Recall at "+K+" "+patk);
			sum+=patk;
		}
		System.out.println("Mean  recall:"+(sum/querysize));
		//System.out.println("Answer found:"+answer_found);
	}
	
	protected int get_fff_position(int K, ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		
		int fff_position=0;
		if(googleList.size()==0)return 0;
	
		K=googleList.size()<K?googleList.size():K;
		for(int i=0;i<K;i++)
		{
			String link=googleList.get(i);
			String _link=link.trim().split("\\s+")[0];
			if(!solutionList.contains(_link))
			{
				fff_position=i+1;
				break;
			}
		}
		return fff_position;
		
	}
	
	protected double get_receiprocal_rank(int K, ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		double ftp_position=0;
		if(googleList.size()==0)return 0;
	
		K=googleList.size()<K?googleList.size():K;
		for(int i=0;i<K;i++)
		{
			String link=googleList.get(i);
			String _link=link.trim().split("\\s+")[0];
			if(solutionList.contains(_link))
			{
				ftp_position=1.0/(i+1);
				break;
			}
		}
		return ftp_position;
	}
	
	protected void calculate_avg_ftp_rank(int K)
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.scSolution.keySet().size();
		for(String key:this.scSolution.keySet())
		{
			//if(!new File(newQuery+"/"+key).exists())continue;
			//System.out.println(key);
			ArrayList<String> glist=this.scSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			double patk=get_receiprocal_rank(K, glist, slist);
			//System.out.println(key+": FTP Rank :"+patk);
			if(patk>0)answer_found++;
			sum+=patk;
		}
		System.out.println("Mean Reciprocal Rank:"+(sum/querysize));
	}
	
	
	protected void calculate_avg_fff_position(int K)
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.scSolution.keySet().size();
		for(String key:this.scSolution.keySet())
		{
			//if(!new File(newQuery+"/"+key).exists())continue;
			//System.out.println(key);
			ArrayList<String> glist=this.scSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			double patk=get_fff_position(K,glist, slist);
			//System.out.println(key+": FFP :"+patk);
			if(patk>0)answer_found++;
			sum+=patk;
		}
		System.out.println("Mean FFF position:"+(sum/querysize));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SurfCLTester tester=new SurfCLTester();
		int K=30;
		tester.collect_org_solutions();
		tester.collect_google_solutions();
		
		tester.calculate_mean_avg_precision(K);
		tester.calculate_mean_precision(K);
		tester.calculate_mean_recall(K);
		tester.calculate_avg_fff_position(K);
		tester.calculate_avg_ftp_rank(K);
		System.out.println("Total solution: "+tester.total_solution+", Solution retrieved:"+tester.solution_retrieved);

	}

}
