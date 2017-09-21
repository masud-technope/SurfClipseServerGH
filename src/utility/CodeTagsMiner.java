package utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeTagsMiner {

	/**
	 * @param args
	 */
	String source_code;
	String resultContent;
	
	public CodeTagsMiner(String resultContent)
	{
		//assigning the source code to mine
		this.resultContent=resultContent;
	}
	
	
	protected void test_the_extraction()
	{
		DownloadResultEntryContent dre=
				new DownloadResultEntryContent(new ArrayList());
		
		String fileContent=dre.test_download();
		this.resultContent=fileContent;
		//System.out.println(this.resultContent);
	}
	
	public String mine_code_examples()
	{
		//code for mining <code> tag
		String code_pattern="<code>(.+?)</code>";
		String pre_pattern="<pre>(.+?)</pre>";
		String div_pattern="<div>(.+?)</div>";
		String pre_code_pattern="<pre><code>(.+?)</code></pre>";
		final Pattern pattern = Pattern.compile(div_pattern);
		final Matcher matcher = pattern.matcher(this.resultContent);
		System.out.println("Match found:"+matcher.groupCount());
		for(int i=0;i<matcher.groupCount();i++)
		{
			System.out.println(matcher.group(i));
			this.source_code+=matcher.group(i);
		}
		return this.source_code;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CodeTagsMiner miner=new CodeTagsMiner("");
		miner.test_the_extraction();
		miner.mine_code_examples();
	}
}
