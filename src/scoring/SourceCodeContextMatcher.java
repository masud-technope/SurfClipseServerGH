package scoring;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import similarity.LCS;
import utility.DownloadResultEntryContent;
import utility.MyTokenizer;
import utility.RegexMatcher;
import gaecore.BingAPI;
import core.Result;

public class SourceCodeContextMatcher {
	/**
	 * @param args
	 */

	public ArrayList<Result> EntryList;
	public String codecontext;
	ArrayList<String> codeContextTokens;

	public SourceCodeContextMatcher(ArrayList<Result> EntryList,
			String codeContext) {
		// use the already download page source from stack trace matcher
		this.EntryList = EntryList;
		this.codecontext = codeContext;
		this.codeContextTokens = new ArrayList<String>();
		// Tokenization of the code context
		MyTokenizer tokenizer = new MyTokenizer(this.codecontext);
		this.codeContextTokens.addAll(tokenizer.tokenize_code_item());
	}

	public SourceCodeContextMatcher(ArrayList<Result> EntryList) {
		this.EntryList = EntryList;
	}

	protected double get_code_context_match_score(String code) {
		// code for getting context match score
		MyTokenizer cand_tokenizer = new MyTokenizer(code);
		ArrayList<String> cand_tokens = cand_tokenizer.tokenize_code_item();
		LCS lcsmaker = new LCS(this.codeContextTokens, cand_tokens);
		ArrayList<String> lcs = lcsmaker.getLCS_Dynamic(
				this.codeContextTokens.size(), cand_tokens.size());
		// System.out.println("lcs: "+lcs.size());
		// now perform the normalization
		double normalized_matching_score = 0;
		if (lcs.size() == 0)
			return 0;
		else
			normalized_matching_score = (lcs.size() * 1.0)
					/ this.codeContextTokens.size();
		return normalized_matching_score;
	}

	public ArrayList<Result> calculate_codecontext_score() {
		// code for calculating stack trace score
		try {
			// calculating the hash map values
			for (int i = 0; i < this.EntryList.size(); i++) {
				Result result = (Result) this.EntryList.toArray()[i];
				// collecting the stack trace code
				ArrayList<String> codestacks = new ArrayList<String>();
				codestacks.addAll(result.codeStacksContent);
				System.out.println("codestack: "
						+ result.codeStacksContent.size());
				double max_code_matching_score = 0;
				if (codestacks.size() > 0) {
					for (String code : codestacks) {
						try
						{
						//if(RegexMatcher.matches_stacktrace(code)) continue; //skip if it is a stack trace
						double match_score = get_code_context_match_score(code);
						if (match_score > max_code_matching_score) {
							max_code_matching_score = match_score;
							if (max_code_matching_score > result.max_matching_score) {
								result.max_matching_score = max_code_matching_score;
								result.representativeText = code;
							}
						}}catch (Exception e) {
							// TODO: handle exception
							//cancel this iteration
							continue;
						}
					}
				}
				// assigning code context score
				result.sourceContextMatchScore = max_code_matching_score;
			}
		} catch (Exception exc) {
		}
		return this.EntryList;
	}

	public void show_the_codecontext_match_scores() {
		// code for showing the title match scores
		for (int i = 0; i < this.EntryList.size(); i++) {
			Result result = (Result) this.EntryList.get(i);
			System.out.println(result.resultURL + " "
					+ result.sourceContextMatchScore);
		}
	}

	protected void show_the_score(ArrayList<Result> results) {
		// code for showing the stack trace score
		for (Result result : results) {
			System.out.println(result.sourceContextMatchScore + " "
					+ result.resultURL);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BingAPI bapi = new BingAPI();
		String query = "Generate MD5 hash in Java";
		ArrayList<Result> results = bapi.find_Bing_Results(query);
		DownloadResultEntryContent downloader = new DownloadResultEntryContent(
				results);
		results = downloader.download_result_entry_content();
		// load the stack trace
		String codeContext = new String();
		try {
			String fileName = "D:/My MSc/CMPT 811/SurfClipse Tool Demo/data/ccontext.txt";
			Scanner scanner = new Scanner(new File(fileName));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				codeContext += line + "\n";
			}
			scanner.close();
		} catch (Exception exc) {
		}

		ResultTitleMatcher titleMatcher = new ResultTitleMatcher(results, query, " " );
		ArrayList<Result> results1 = titleMatcher.calculate_title_match_score();
		SourceCodeContextMatcher matcher = new SourceCodeContextMatcher(
				results1, codeContext);
		ArrayList<Result> results2 = matcher.calculate_codecontext_score();
		matcher.show_the_score(results2);
	}
}
