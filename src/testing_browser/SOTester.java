package testing_browser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import core.StaticData;

public class SOTester {

	/**
	 * @param args
	 */
	HashMap<String, ArrayList<String>> orginalSolution;
	HashMap<String, ArrayList<String>> soSolution;
	
	public SOTester()
	{
		this.orginalSolution=new HashMap<>();
		this.soSolution=new HashMap<>();
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
			String path=StaticData.Lucene_Data_Base+"/completeds/results/SO";
			File f=new File(path);
			if(f.isDirectory())
			{
				File[] files=f.listFiles();
				for(File f1:files)
				{
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
					this.soSolution.put(f1.getName(), temp);
					//System.out.println(f1.getName()+"="+temp.size());
					}catch(Exception exc){
						
					}
				}
			}
		}catch(Exception exc){}
	}
	
	protected double calculate_precision_at_k(int K, ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		double precision_sum=0;
		double precision_avg=0;
		double retrieved_relevant_items=0;
		
		if(googleList.size()==0)return 0;
		
		
		int indexLimit=googleList.size()<K?googleList.size():K;
		for(int i=0;i<indexLimit;i++)
		{
			String link=googleList.get(i);
			if(solutionList.contains(link))
			{
				retrieved_relevant_items++;
				double instant_precision=(retrieved_relevant_items/(i+1));
				precision_sum+=instant_precision;
			}
		}
		if(retrieved_relevant_items==0)return 0;
		
		precision_avg=precision_sum/retrieved_relevant_items;
		return precision_avg;
	}
	
	protected void calculate_mean_avg_precision()
	{
		double sum=0;
		
		int answer_found=0;
		int querysize=this.soSolution.keySet().size();
		for(String key:this.soSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.soSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			int K=10;
			double patk=calculate_precision_at_k(K, glist, slist);
			if(patk>0)answer_found++;
			System.out.println(key+": Precision at "+K+" "+patk);
			sum+=patk;
		}
		System.out.println("Mean average precision:"+(sum/querysize));
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
			if(solutionList.contains(link))
			{
				retrieved_relevant_items++;
			}
		}
		if(retrieved_relevant_items==0)return 0;
		
		recall_each=retrieved_relevant_items/solutionList.size();
		return recall_each;
	}
	
	protected void calculate_mean_recall()
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.soSolution.keySet().size();
		for(String key:this.soSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.soSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			int K=5;
			double patk=calculate_recall_at_k(K, glist, slist);
			if(patk>0)answer_found++;
			System.out.println(key+": Recall at "+K+" "+patk);
			sum+=patk;
		}
		System.out.println("Mean  recall:"+(sum/querysize));
		//System.out.println("Answer found:"+answer_found);
	}
	
	protected int get_fff_position(ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		
		int fff_position=0;
		if(googleList.size()==0)return 0;
	
		
		for(int i=0;i<googleList.size();i++)
		{
			String link=googleList.get(i);
			if(!solutionList.contains(link))
			{
				fff_position=i+1;
				break;
			}
		}
		return fff_position;
		
	}
	
	protected double get_receiprocal_rank(ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		double ftp_position=0;
		if(googleList.size()==0)return 0;
	
		for(int i=0;i<googleList.size();i++)
		{
			String link=googleList.get(i);
			if(solutionList.contains(link))
			{
				ftp_position=1.0/(i+1);
				break;
			}
		}
		return ftp_position;
	}
	
	protected void calculate_avg_ftp_rank()
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.soSolution.keySet().size();
		for(String key:this.soSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.soSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			double patk=get_receiprocal_rank(glist, slist);
			System.out.println(key+": FTP Rank :"+patk);
			if(patk>0)answer_found++;
			sum+=patk;
		}
		System.out.println("Mean Reciprocal Rank:"+(sum/answer_found));
	}
	
	
	protected void calculate_avg_fff_position()
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.soSolution.keySet().size();
		for(String key:this.soSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.soSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			double patk=get_fff_position(glist, slist);
			System.out.println(key+": FFP :"+patk);
			if(patk>0)answer_found++;
			sum+=patk;
		}
		System.out.println("Mean FFF position:"+(sum/answer_found));
	}
	
	
	
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SOTester tester=new SOTester();
		tester.collect_org_solutions();
		tester.collect_google_solutions();
		tester.calculate_mean_avg_precision();
		//tester.calculate_mean_recall();
		//tester.calculate_avg_fff_position();
		//tester.calculate_avg_ftp_rank();
	}
}
