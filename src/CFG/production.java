package CFG;

import java.util.LinkedList;

public class production {
	
	LinkedList<rule> possibleTerminals;
	
	/*
	 * INPUT: A linked list of production rule
	 * OUTPUT: A boolean indicating if the total probability mass of the production rule list is 1
	 */
	protected boolean checkProductionList(LinkedList<rule> rules) throws prodRuleProbException{
		double totalMass = 0.0;
		for (rule rule : rules){
			totalMass+=rule.probability;
		}
		//Return true if the probability mass falls within a tight range around 1, false otherwise
		if((totalMass < 1.000001)&&(totalMass > .999999)){
			return true;
		}else{
			throw new prodRuleProbException("Probability mass is not 1 for production rule. Probability mass is: "+totalMass);
		}
	}
}
