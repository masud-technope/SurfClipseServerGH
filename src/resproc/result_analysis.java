package resproc;

import java.io.File;
import java.util.*;


public class result_analysis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File fr= new File("C:/Users/Masud/Dropbox/SurfClipse Code/user-study_result.txt");
		int [] q1_sim=new int[20];
		int [] q1_ans=new int[20];
		int [] q2_sim=new int[20];
		int [] q2_ans=new int[20];
		int [] q3_sim=new int[20];
		int [] q3_ans=new int[20];
		int [] q4_sim=new int[20];
		int [] q4_ans=new int[20];
		int [] q5_sim=new int[20];
		int [] q5_ans=new int[20];
		int index=0;
		try
		{
			Scanner scanner=new Scanner(fr);
			String word = scanner.next();
			while(scanner.hasNext())
			{
				index++;
				System.out.println(word);
				//For Question 1
				q1_sim[index]=Integer.parseInt(word);
				word=scanner.next();
				word=scanner.next();
				q1_ans[index]=Integer.parseInt(word);
				word=scanner.next();
				//For Question 2
				q2_sim[index]=Integer.parseInt(word);
				word=scanner.next();
				word=scanner.next();
				q2_ans[index]=Integer.parseInt(word);
				word=scanner.next();
				//For Question 3
				q3_sim[index]=Integer.parseInt(word);
				word=scanner.next();
				word=scanner.next();
				q3_ans[index]=Integer.parseInt(word);
				word=scanner.next();
				//For Question 4
				q4_sim[index]=Integer.parseInt(word);
				word=scanner.next();
				word=scanner.next();
				q4_ans[index]=Integer.parseInt(word);
				word=scanner.next();
				//For Question 5
				q5_sim[index]=Integer.parseInt(word);
				word=scanner.next();
				word=scanner.next();
				q5_ans[index]=Integer.parseInt(word);
				word=scanner.next();
			}
		}
		catch(Exception exc)
		{
			System.out.println();
		}
		for(int i=1;i<=index;i++)
		{
			System.out.println(q1_sim[i]+"/"+q1_ans[i]+" "+q2_sim[i]+"/"+q2_ans[i]+" "+q3_sim[i]+"/"+q3_ans[i]+" "+q4_sim[i]+"/"+q4_ans[i]+" "+q5_sim[i]+"/"+q5_ans[i]);
		}
		int no_of_sim_sol=0;
		int no_of_ans_sol=0;
		double avg_sim=0.0;
		double avg_ans=0.0;
		//For question 1
		for(int i=1;i<=index;i++)
		{
			no_of_sim_sol+=q1_sim[i];
			no_of_ans_sol+=q1_ans[i];
		}
		
		System.out.println("Question ---- Avg No of match sol---- Avg No of answered sol------Agreement");
		System.out.println("---1-----------------"+((double)no_of_sim_sol/(double)index)+"---------------------"+((double)no_of_ans_sol/(double)index)+"-----------"+(((double)no_of_sim_sol/(double)index)/((double)no_of_ans_sol/(double)index))*100+"%");
		avg_sim+=(double)no_of_sim_sol/(double)index;
		avg_ans+=(double)no_of_ans_sol/(double)index;
		//For question 2
		no_of_sim_sol=0;
		no_of_ans_sol=0;
		for(int i=1;i<=index;i++)
		{
			no_of_sim_sol+=q2_sim[i];
			no_of_ans_sol+=q2_ans[i];
		}
		System.out.println("---2-----------------"+((double)no_of_sim_sol/(double)index)+"---------------------"+((double)no_of_ans_sol/(double)index)+"-----------"+(((double)no_of_sim_sol/(double)index)/((double)no_of_ans_sol/(double)index))*100+"%");
		avg_sim+=(double)no_of_sim_sol/(double)index;
		avg_ans+=(double)no_of_ans_sol/(double)index;
		
		//For question 3
		no_of_sim_sol=0;
		no_of_ans_sol=0;
		for(int i=1;i<=index;i++)
		{
			no_of_sim_sol+=q3_sim[i];
			no_of_ans_sol+=q3_ans[i];
		}
		System.out.println("---3-----------------"+((double)no_of_sim_sol/(double)index)+"---------------------"+((double)no_of_ans_sol/(double)index)+"-----------"+(((double)no_of_sim_sol/(double)index)/((double)no_of_ans_sol/(double)index))*100+"%");
		avg_sim+=(double)no_of_sim_sol/(double)index;
		avg_ans+=(double)no_of_ans_sol/(double)index;
		
		//For question 4
		no_of_sim_sol=0;
		no_of_ans_sol=0;
		for(int i=1;i<=index;i++)
		{
			no_of_sim_sol+=q4_sim[i];
			no_of_ans_sol+=q4_ans[i];
		}
		System.out.println("---4-----------------"+((double)no_of_sim_sol/(double)index)+"---------------------"+((double)no_of_ans_sol/(double)index)+"-----------"+(((double)no_of_sim_sol/(double)index)/((double)no_of_ans_sol/(double)index))*100+"%");
		avg_sim+=(double)no_of_sim_sol/(double)index;
		avg_ans+=(double)no_of_ans_sol/(double)index;
		
		//For question 4
		no_of_sim_sol=0;
		no_of_ans_sol=0;
		for(int i=1;i<=index;i++)
		{
			no_of_sim_sol+=q5_sim[i];
			no_of_ans_sol+=q5_ans[i];
		}
		System.out.println("---5-----------------"+((double)no_of_sim_sol/(double)index)+"---------------------"+((double)no_of_ans_sol/(double)index)+"-----------"+(((double)no_of_sim_sol/(double)index)/((double)no_of_ans_sol/(double)index))*100+"%");
		avg_sim+=(double)no_of_sim_sol/(double)index;
		avg_ans+=(double)no_of_ans_sol/(double)index;
		
		System.out.println("---avg---------------"+avg_sim/(double)index+"---------------------"+avg_ans/(double)index+"-----------"+((avg_sim/(double)index)/(avg_ans/(double)index))*100+"%");
	}

}
