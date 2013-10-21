package utility;

public class ProcessorInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int cores=Runtime.getRuntime().availableProcessors();
		System.out.println("Available cores:"+cores);

	}

}
