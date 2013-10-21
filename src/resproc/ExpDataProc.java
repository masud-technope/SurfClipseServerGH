package resproc;

import java.io.File;
import java.util.Scanner;

public class ExpDataProc {

	/**
	 * @param args
	 */
	
	String expfile;
	double ct[];
	double se[];
	double ctx[];
	double ctp[];
	double cts[];
	double ctxp[];
	double ctxs[];
	double ctxps[];
	final int max_results=25;
	
	public ExpDataProc(String exfile)
	{
		this.expfile=new String(exfile);
		//initiating results
		ct=new double[max_results];
		se=new double[max_results];
		ctx=new double[max_results];
		ctp=new double[max_results];
		cts=new double[max_results];
		ctxp=new double[max_results];
		ctxs=new double[max_results];
		ctxps=new double[max_results];
	}
	
	protected void load_experimental_data()
	{
		//code for loading experimental data
		try
		{
		Scanner scanner=new Scanner(new File(this.expfile));
		int count=0;
		while(scanner.hasNext())
		{
			
			String line=scanner.nextLine().trim();
			if(line.isEmpty())break;
			String[] values=line.split("\\s+");
			ct[count]=Double.parseDouble(values[0]);
			se[count]=Double.parseDouble(values[1]);
			ctx[count]=Double.parseDouble(values[2]);
			ctp[count]=Double.parseDouble(values[3]);
			cts[count]=Double.parseDouble(values[4]);
			ctxp[count]=Double.parseDouble(values[5]);
			ctxs[count]=Double.parseDouble(values[6]);
			ctxps[count]=Double.parseDouble(values[7]);
			count++;
		}}catch(Exception exc){
			System.err.println(exc.getMessage());
		}
	}
	
	protected void get_the_mean_ranks()
	{
		//code for getting mean ranks
		
	}
	
	protected void get_results_found()
	{
		//code for getting results found
		int result_found=0;
		double sum=0;
		for(int i=0;i<this.se.length;i++)
		{
			if(se[i]<=20)
			{
				//System.out.print(cts[i]+" ");
				sum+=se[i];
				result_found++;
			}
		}
		System.out.println("\nResult found:"+result_found);
		System.out.println("Average rank:"+(sum)/result_found);
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file="D:/My MSc/CMPT 811/SurfClipse Tool Demo/data/exp/experiment_data.txt";
		ExpDataProc expdata=new ExpDataProc(file);
		expdata.load_experimental_data();
		expdata.get_results_found();
	}

}
