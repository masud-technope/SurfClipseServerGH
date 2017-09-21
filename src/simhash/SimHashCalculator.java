package simhash;
import ca.usask.cs.srlab.simcad.hash.DefaultRegularHashGenerator;
import ca.usask.cs.srlab.simcad.hash.SimhashGenerator;

public class SimHashCalculator {

	/**
	 * @param args
	 */
	
	String firstSentence;
	String secondSentence;
	
	public SimHashCalculator(String sentence1,String sentence2)
	{
		this.firstSentence=sentence1;
		this.secondSentence=sentence2;
	}
	
	protected int[] convert2binary(long number)
	{
		int[] bits=new int[64]; //64 bits array
		//code for converting number to binary
		try
		{
			int count=0;
			while(number>0)
			{
				bits[count]=(int)(number%2);
				number=number/2;
				count++;
			}			
		}catch(Exception exc){
			System.err.println(exc.getMessage());
		}
		return bits;
	}
	
	public int getHammingDistance()
	{
		//code for getting Hamming distance
		int hamming_distance=64;
		try
		{
			SimhashGenerator simhashGenerator = new SimhashGenerator(new BasicTokenBuilder(), new DefaultRegularHashGenerator());
			long simhash1[] = simhashGenerator.getSimhash(this.firstSentence);
			long simhash2[]=simhashGenerator.getSimhash(this.secondSentence);
			long hash1=simhash1[0];
			long hash2=simhash2[0];
			int[] bitseq1=convert2binary(hash1);
			int[] bitseq2=convert2binary(hash2);
			int distance=0;
			for(int i=0;i<64;i++)
			{
				if(bitseq1[i]!=bitseq2[i])
				distance++;
			}
			hamming_distance=distance;
		}catch(Exception exc){
		//System.err.println(exc.getMessage());	
		}
		return hamming_distance;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str1="Welcome to Masud's World";
		String str2="Hello World test2";
		SimHashCalculator manager=new SimHashCalculator(str1, str2);
		System.out.println("Sim difference:"+manager.getHammingDistance());	
	}

}
