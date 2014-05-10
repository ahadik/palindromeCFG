package CFG;

import java.util.LinkedList;

public class productionTerminal extends rule {

	String value;
	
	//Define a constructor for this production rule
	public productionTerminal(String value, double probability){
		super(probability);
		this.value = value;
	}
	
	@Override public String toString(){
		return this.value;
	}
	
}
