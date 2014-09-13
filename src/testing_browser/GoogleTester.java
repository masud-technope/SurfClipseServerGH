package testing_browser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Sets;

import core.StaticData;

public class GoogleTester {

	/**
	 * @param args
	 */
	HashMap<String, ArrayList<String>> orginalSolution;
	HashMap<String, ArrayList<String>> googleSolution;
	int total_solution=0;
	int solution_retrieved=0;
	
	public GoogleTester()
	{
		this.orginalSolution=new HashMap<>();
		this.googleSolution=new HashMap<>();
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
							//URL u=new URL(sol);
							temp.add(sol);
						}
					}
					scanner.close();
					//now add the arraylist to hmap
					this.orginalSolution.put(f1.getName(), temp);
					System.out.println(f1.getName()+"="+temp.size());
					}catch(Exception exc){
						
					}
				}
			}
		}catch(Exception exc){}
	}
	protected void collect_google_solutions(String engine)
	{
		//code for collecting google solutions
		try
		{
			//String path=StaticData.Lucene_Data_Base+"/completeds/browser/"+engine;
			String path=StaticData.QCDataset+"/"+engine+"/results";
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
							//URL u=new URL(sol);
							temp.add(sol);
						}
					}
					scanner.close();
					//now add the arraylist to hmap
					this.googleSolution.put(f1.getName(), temp);
					this.total_solution+=this.orginalSolution.get(f1.getName()).size();
					
					System.out.println(f1.getName()+"="+temp.size());
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
		for(int i=0;i<indexLimit;i++)
		{
			String link=googleList.get(i).trim();
			if(solutionList.contains(link))
			{
				retrieved_relevant_items++;
				//double instant_precision=(retrieved_relevant_items/(i+1));
				//precision_sum+=instant_precision;
			}
		}
		//System.out.println("Item found:"+retrieved_relevant_items);
		
		if(retrieved_relevant_items==0)return 0;
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
		for(int i=0;i<indexLimit;i++)
		{
			String link=googleList.get(i).trim();
			if(solutionList.contains(link))
			{
				retrieved_relevant_items++;
				double instant_precision=(retrieved_relevant_items/(i+1));
				precision_sum+=instant_precision;
			}
		}
		
		//System.out.println("Item found:"+retrieved_relevant_items);
		
		if(retrieved_relevant_items==0)return 0;
		
		precision_avg=precision_sum/retrieved_relevant_items;
		return precision_avg;
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
		//total_solution+=solutionList.size();
		
		if(retrieved_relevant_items==0)return 0;
		
		recall_each=retrieved_relevant_items/solutionList.size();
		return recall_each;
	}
	
	protected void calculate_mean_precision(int K)
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.googleSolution.keySet().size();
		for(String key:this.googleSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.googleSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			//int K=30;
			double patk=calculate_precision(K, glist, slist);
			if(patk>0)answer_found++;
			//System.out.println(key+": Precision at "+K+" "+patk);
			else System.out.println(key);
			sum+=patk;
		}
		System.out.println("Mean Precision:"+(sum/querysize));
		System.out.println("Number of query:" + querysize);
		System.out.println("Answer found:"+answer_found);  
	}
	
	
	protected void calculate_mean_avg_precision(int K)
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.googleSolution.keySet().size();
		for(String key:this.googleSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.googleSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			//int K=30;
			double patk=calculate_precision_at_k(K, glist, slist);
			if(patk>0){answer_found++;}
			else System.err.println(key);
			//System.out.println(key+": Precision at "+K+" "+patk);
			//System.out.println(key);
			sum+=patk;
		}
		System.out.println("Mean average precision:"+(sum/querysize));
		System.out.println("Number of query:" + querysize);
		System.out.println("Answer found:"+answer_found);  
	}
	
	
	protected void calculate_mean_recall(int K)
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.googleSolution.keySet().size();
		for(String key:this.googleSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.googleSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			//int K=30;
			double patk=calculate_recall_at_k(K, glist, slist);
			if(patk>0)answer_found++;
			//else System.err.println(key);
			//System.out.println(key+": Recall at "+K+" "+patk);
			sum+=patk;
		}
		System.out.println("Mean average recall:"+(sum/querysize));
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
	
	protected void calculate_avg_fff_position()
	{
		double sum=0;
		int answer_found=0;
		int querysize=this.googleSolution.keySet().size();
		for(String key:this.googleSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.googleSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			double patk=get_fff_position(glist, slist);
			//System.out.println(key+": FFP :"+patk);
			if(patk>0)answer_found++;
			sum+=patk;
		}
		System.out.println("Mean FFF position:"+(sum/querysize));
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
		int querysize=this.googleSolution.keySet().size();
		for(String key:this.googleSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.googleSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			double patk=get_receiprocal_rank(glist, slist);
			//System.out.println(key+": FTP Rank :"+patk);
			if(patk>0)answer_found++;
			sum+=patk;
		}
		System.out.println("Mean Reciprocal Rank:"+(sum/querysize));
	}
	
	
	protected void collect_individual_solutions(String engine)
	{
		//code for collecting google solutions
		try
		{
			String path=StaticData.Lucene_Data_Base+"/completeds/browser/"+engine;
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
							//URL u=new URL(sol);
							temp.add(sol);
						}
					}
					scanner.close();
					//now add the arraylist to hmap
					this.googleSolution.put(f1.getName(), temp);
					//System.out.println(f1.getName()+"="+temp.size());
					}catch(Exception exc){
					}
				}
			}
		}catch(Exception exc){}
	}
	
	
	protected int calculate_solution_found(int K, ArrayList<String> googleList, ArrayList<String> solutionList)
	{
		double precision_sum=0;
		double precision_avg=0;
		double retrieved_relevant_items=0;
		
		if(googleList.size()==0)return 0;
		
		int indexLimit=googleList.size()<K?googleList.size():K;
		for(int i=0;i<indexLimit;i++)
		{
			String link=googleList.get(i).trim();
			if(solutionList.contains(link))
			{
				retrieved_relevant_items++;
				//double instant_precision=(retrieved_relevant_items/(i+1));
				//precision_sum+=instant_precision;
			}
		}
		//System.out.println("Item found:"+retrieved_relevant_items);
		
		//if(retrieved_relevant_items==0)return 0;
		return retrieved_relevant_items>0?1:0;
	}
	
	
	protected HashSet<String> get_solved_testcases()
	{
		HashSet<String> temp=new HashSet<>();
		int K=30;
		for(String key:this.googleSolution.keySet())
		{
			//System.out.println(key);
			ArrayList<String> glist=this.googleSolution.get(key);
			ArrayList<String> slist=this.orginalSolution.get(key);
			//int K=30;
			double patk=calculate_solution_found(K, glist, slist);
			if(patk>0)temp.add(key);
		}
		return temp;
	}
	
	
	
	protected void analyze_the_dataset()
	{
		//code for analyzing the dataset
		HashSet<String> commonSet=new HashSet<>();
		this.googleSolution.clear();
		collect_individual_solutions("Google");
		HashSet<String> google=get_solved_testcases();
		this.googleSolution.clear();
		collect_individual_solutions("Bing");
		HashSet<String> bing=get_solved_testcases();
		this.googleSolution.clear();
		collect_individual_solutions("Yahoo");
		HashSet<String> yahoo=get_solved_testcases();
		//this.googleSolution.clear();
		//collect_individual_solutions("SO");
		//HashSet<String> so=get_solved_testcases();
		
	
		//@SuppressWarnings("unchecked")
		//ArrayList<String> temp1=(ArrayList<String>) CollectionUtils.disjunction(google, bing);
		//System.out.println("Unique google from Bing:"+Sets.difference(google, bing).size());;
		Set<String> common1=Sets.intersection(google, bing);
		System.out.println("Common google and Bing:"+common1.size());
		
		//@SuppressWarnings("unchecked")
		//ArrayList<String> temp2=(ArrayList<String>) CollectionUtils.disjunction(google, yahoo);
		//System.out.println("Unique google from Yahoo:"+Sets.difference(google, yahoo).size());
		Set<String> common2=Sets.intersection(google, yahoo);
		System.out.println("Common google and Yahoo:"+common2.size());
		
		Set<String> allcommon=Sets.intersection(common1, common2);
		System.out.println("Common in 3:"+allcommon.size());
		
		System.out.println("Unique Google:"+Sets.difference(google, allcommon).size());
		System.out.println("Unique Bing:"+Sets.difference(bing, allcommon).size());
		System.out.println("Unique Yahoo:"+Sets.difference(yahoo, allcommon).size());
		
		
		
		/*@SuppressWarnings("unchecked")
		ArrayList<String> temp22=(ArrayList<String>) CollectionUtils.disjunction(google, yahoo);
		System.out.println("Unique google from SO:"+Sets.difference(google, so).size());
		Set<String> common22=Sets.intersection(google, so);
		System.out.println("Common google from SO:"+common22.size());*/
	
//		@SuppressWarnings("unchecked")
//		ArrayList<String> temp3=(ArrayList<String>) CollectionUtils.disjunction(bing, yahoo);
//		System.out.println("Unique Bing from Yahoo:"+Sets.difference(bing, yahoo).size());
//		Set<String> common3=Sets.intersection(bing, yahoo);
//		System.out.println("Common Bing from Yahoo:"+common3.size());
//		@SuppressWarnings("unchecked")
//		ArrayList<String> temp4=(ArrayList<String>) CollectionUtils.disjunction(yahoo, bing);
//		System.out.println("Unique Yahoo from Bing:"+Sets.difference(yahoo, bing).size());
//		Set<String> common4=Sets.intersection(yahoo, bing);
//		System.out.println("Common Bing from Yahoo:"+common4.size());
			
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GoogleTester tester=new GoogleTester();
		tester.collect_org_solutions();
		tester.collect_google_solutions("proposed");
		int K=20;
		//tester.analyze_the_dataset();
		//tester.calculate_mean_precision(K);
		tester.calculate_mean_avg_precision(K);
		tester.calculate_mean_recall(K);
		//tester.calculate_avg_fff_position();
		//tester.calculate_avg_ftp_rank();
		System.out.println("Total solution: "+tester.
				
				total_solution+", Solution retrieved:"+tester.solution_retrieved);
		System.out.println("Total recall:"+((double)tester.solution_retrieved)/tester.total_solution);
	}
}
