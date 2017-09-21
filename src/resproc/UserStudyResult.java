package resproc;
import java.io.File;
import java.util.Scanner;

public class UserStudyResult {

	/**
	 * @param args
	 */
	double q1[];
	double q2[];
	double q3[];
	double q4[];
	double q5[];
	final int total_user_count=5;
	double correct1[];
	double correct2[];
	double correct3[];
	double correct4[];
	double correct5[];
	
	public UserStudyResult()
	{
		//code for user study result
		q1=new double[total_user_count];
		q2=new double[total_user_count];
		q3=new double[total_user_count];
		q4=new double[total_user_count];
		q5=new double[total_user_count];
		//code for correct arrays
		correct1=new double[total_user_count];
		correct2=new double[total_user_count];
		correct3=new double[total_user_count];
		correct4=new double[total_user_count];
		correct5=new double[total_user_count];
	}
	protected void load_the_user_data(String fileName)
	{
		//code for loading data
		try
		{
			Scanner scanner=new Scanner(new File(fileName));
			int count=0;
			while(scanner.hasNext())
			{
				String line=scanner.nextLine();
				String[] parts=line.trim().split("\t");
				
				for(int i=0;i<parts.length;i++)
				{
					String[] parts2=parts[i].trim().split("/");
					switch(i)
					{
					case 0:
						correct1[count]=Double.parseDouble(parts2[0]);
						q1[count]=Double.parseDouble(parts2[1]);
						break;
					case 1:
						correct2[count]=Double.parseDouble(parts2[0]);
						q2[count]=Double.parseDouble(parts2[1]);
						break;
					case 2:
						correct3[count]=Double.parseDouble(parts2[0]);
						q3[count]=Double.parseDouble(parts2[1]);
						break;
					case 3:
						correct4[count]=Double.parseDouble(parts2[0]);
						q4[count]=Double.parseDouble(parts2[1]);
						break;
					case 4:
						correct5[count]=Double.parseDouble(parts2[0]);
						q5[count]=Double.parseDouble(parts2[1]);
						break;
					}
				}
				count++;
			}
		}catch(Exception exc){
			System.err.println(exc.getMessage());
		}		
	}
	
	protected void perform_the_calculation()
	{
		//code for performing calculation
		double q1sum=0;
		double c1sum=0;
		for(int i=0;i<q1.length;i++)
		{
			q1sum+=q1[i];
			c1sum+=correct1[i];
		}
		double q1avg=q1sum/total_user_count;
		double c1avg=c1sum/total_user_count;
		double agreement1=c1avg/q1avg;
		System.out.println("Average Total: "+q1avg
				+", Correct average: "+c1avg+" Agreement: "+agreement1);
		
		
		double q2sum=0;
		double c2sum=0;
		for(int i=0;i<q2.length;i++)
		{
			q2sum+=q2[i];
			c2sum+=correct2[i];
		}
		double q2avg=q2sum/total_user_count;
		double c2avg=c2sum/total_user_count;
		double agreement2=c2avg/q2avg;
		System.out.println("Average Total: "+q2avg
				+", Correct average: "+c2avg+" Agreement: "+agreement2);
		
		
		double q3sum=0;
		double c3sum=0;
		for(int i=0;i<q3.length;i++)
		{
			q3sum+=q3[i];
			c3sum+=correct3[i];
		}
		double q3avg=q3sum/total_user_count;
		double c3avg=c3sum/total_user_count;
		double agreement3=c3avg/q3avg;
		System.out.println("Average Total: "+q3avg
				+", Correct average: "+c3avg+" Agreement: "+agreement3);
		
		double q4sum=0;
		double c4sum=0;
		for(int i=0;i<q4.length;i++)
		{
			q4sum+=q4[i];
			c4sum+=correct4[i];
		}
		double q4avg=q4sum/total_user_count;
		double c4avg=c4sum/total_user_count;
		double agreement4=c4avg/q4avg;
		System.out.println("Average Total: "+q4avg
				+", Correct average: "+c4avg+" Agreement: "+agreement4);
		
		double q5sum=0;
		double c5sum=0;
		for(int i=0;i<q5.length;i++)
		{
			q5sum+=q5[i];
			c5sum+=correct5[i];
		}
		double q5avg=q5sum/total_user_count;
		double c5avg=c5sum/total_user_count;
		double agreement5=c5avg/q5avg;
		System.out.println("Average Total: "+q5avg
				+", Correct average: "+c5avg+" Agreement: "+agreement5);
		
		double avg_agreement=(agreement1+agreement2+agreement3+agreement4+agreement5)/5;
		
		System.out.println("Avg agreement:"+avg_agreement);
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserStudyResult usresult=new UserStudyResult();
		String fileName="C:/Users/Masud/Dropbox/SurfClipse Code/user-study_result.txt";
		usresult.load_the_user_data(fileName);
		usresult.perform_the_calculation();
	}

}
