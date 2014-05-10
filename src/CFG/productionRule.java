package CFG;

import java.util.LinkedList;

public class productionRule extends rule {
	LinkedList<production> rule;
	
	//Define a constructor for this production rule
	public productionRule(LinkedList<production> rule, double probability){
		super(probability);
		this.rule = rule;
	}
}
