package CFG;

import java.util.LinkedList;
import java.util.Random;

public class terminal extends production {
	
	productionTerminal value;

	public terminal(double probability, LinkedList<rule> terminals) throws prodRuleProbException {
		super(probability);
		this.value = generateTerminals(terminals);
	}
	
	private productionTerminal generateTerminals(LinkedList<rule> rules) throws prodRuleProbException{
		
		//Check that the production rules have a proper probability mass
		if(checkProductionList(rules)){
			//Generate a random number from uniform dist
			Random r = new Random();
			double randomValue = r.nextDouble();
			
			double accumulatedProbMass = 0.0;
			for (rule rule : rules){
				//if the randomly drawn value falls within the current range
				if((randomValue > accumulatedProbMass)&&(randomValue < accumulatedProbMass+rule.probability)){
					return (productionTerminal) rule;
				}
				//Increment the accumulated 
				accumulatedProbMass += rule.probability;
			}
		}
		//If a rule wasn't returned above, then something has gone wrong and null will be returned triggering a null pointer error.
		return null;
	}

}
