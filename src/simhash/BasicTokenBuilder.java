package simhash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.token.ITokenBuilder;
import ca.usask.cs.srlab.simcad.token.filter.ITokenFilter;
import ca.usask.cs.srlab.simcad.token.filter.NullFilter;

public final class BasicTokenBuilder implements ITokenBuilder {

	private ITokenFilter tokenFilterChain = NullFilter.INSTANCE; 

	public BasicTokenBuilder() {
		super();
	}

	@Override
	public void setFilterChain(ITokenFilter tokenFilter){
		tokenFilterChain = tokenFilter;
	}
	
/* (non-Javadoc)
 * @see ca.usask.cs.srlab.simcad.hash.ITokenBuilder#generateToken(java.lang.String)
 */
@Override
public Collection<String> generateToken(String codeFragment) {
	List<String> tokenList = new ArrayList<String>();
	int loc = CloneFragment.computeActualLineOfCode(codeFragment);
	String tokenSeparator = loc < 100 ? " \t\n\r\f":"\n";  //if block is big, split with line only!
	
	StringTokenizer stToken = new StringTokenizer(codeFragment, tokenSeparator);
	
    while(stToken.hasMoreElements()) {
    	String superToken = stToken.nextToken();
	    addToken(superToken, tokenList);
    }
    return tokenList;
}

private void addToken(String token, List<String> tokenList){
	String filteredToken = tokenFilterChain.doFilterAndInvokeNext(token);
	if(filteredToken != null)
		tokenList.add(filteredToken);
}




/*
 * Test code
//Map<String, Short> tokenMap = new 
//TreeMap<String, Short>();

if(method == 1){
	String[] parts = rawData.split("\\W+");//"(?<=\\G...)");
	for(String token : parts){
    	short num = tokenMap.get(token) == null ? 0 : (short)(tokenMap.get(token));
		tokenMap.put(token, ++num);
    }
}
else if(method == 2){
	String[] parts = rawData.split("(?<=\\G...)");
	for(String token : parts){
    	short num = tokenMap.get(token) == null ? 0 : (short)(tokenMap.get(token));
		tokenMap.put(token, ++num);
    }
}if(method == 3){
	
	
}else if(method == 4){
	
//	String tokenSeparator;
//	tokenSeparator = "\n\r\f";  //if block is big, split with line only!
//	
//	StringTokenizer stToken = new StringTokenizer(refinedData, tokenSeparator);
//	
	//String refinedData = doDataRerinement(rawData);
	
	String lines[] = rawData.split("\n");
	int lineNumberTag = 1010;
    //while(stToken.hasMoreElements()) {
    for(String superToken:lines){
    	superToken = doLineRerinement(superToken);
    	if(superToken.length() == 0) continue;
		//String superToken = stToken.nextToken();
		String modToken = superToken.trim() + lineNumberTag++; 
		short num = tokenMap.get(modToken) == null ? 0 : (short)(tokenMap.get(modToken));
        tokenMap.put(modToken, ++num);
	}
	
}else if(method == 5){
	
	//String refinedData = doDataRerinement(rawData);
	
	String lines[] = rawData.split("\n");
	int lineNumberTag = 1010;
    //while(stToken.hasMoreElements()) {
    for(String superToken:lines){
    	if(superToken.length() == 0) continue;
		//String superToken = stToken.nextToken();
    	String subTokens[] = superToken.split("//");
    	for(String subToken : subTokens){
    		String modToken = null;
    		if(subToken.startsWith(":")){
    			modToken = subToken.substring(1).trim() + lineNumberTag++; 
    		}else
    			modToken = subToken.trim();
			short num = tokenMap.get(modToken) == null ? 0 : (short)(tokenMap.get(modToken));
        	tokenMap.put(modToken, ++num);
    	}
	}
}
*/

}
