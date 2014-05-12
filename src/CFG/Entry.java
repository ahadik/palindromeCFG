package CFG;

import java.util.LinkedList;

public class Entry {
	
	String[] subsequence;
	String subseq;
	LinkedList<production> productions = new LinkedList<production>();
	
	public Entry(String sequence){
		this.subseq = sequence;
		this.subsequence = sequence.split("");
	}
	
	public void addProduction(nonterminal production){
		this.productions.add(production);
	}
	
	@Override public String toString(){
		String returnSeq = subseq+this.productions;
		return returnSeq;
	}

}
