package CFG;

import java.util.LinkedList;
import java.util.Random;

public class nonterminal extends production {
	
	LinkedList<rule> possibleRules;
	productionRule rule;
	boolean isP;

	
	public void setRules(LinkedList<rule> rules, boolean prodType){
		this.possibleRules = rules;
		this.isP = prodType;
	}
	
	//Define a custom to string for this class
	@Override public String toString(){
		if(isP){
			return "P";
		}else{
			return "Q";
		}
	}
	
	//Use the generateProduction method to set the internal production rule
	public void generateRule() throws prodRuleProbException{
		this.rule = generateProduction();
	}
	
	/*
	 * Called as a method of an instantiated production object, this function pushes the evaluation of the production back onto the seqQueue
	 */
	public void evaluateProduction(LinkedList<production> seqQueue){
		//For every production in the production rule of this nonterminal, push it onto the bottom of seqQueue
		for (production prod : this.rule.rule){
			seqQueue.push(prod);
		}
	}
	
	

	/*
	 * INPUT: A linked list of possible rules that this production can evaluate to
	 * OUTPUT: A single rule selected based upon the probability weight assigned to each rule
	 */
	productionRule generateProduction() throws prodRuleProbException{
		LinkedList<rule> rules = this.possibleRules;
		//Check that the production rules have a proper probability mass
		if(checkProductionList(rules)){
			//Generate a random number from uniform dist
			Random r = new Random();
			double randomValue = r.nextDouble();
			
			double accumulatedProbMass = 0.0;
			for (CFG.rule rule : rules){
				//if the randomly drawn value falls within the current range
				if((randomValue > accumulatedProbMass)&&(randomValue < accumulatedProbMass+rule.probability)){
					return (productionRule) rule;
				}
				//Increment the accumulated 
				accumulatedProbMass += rule.probability;
			}
		}
		//If a rule wasn't returned above, then something has gone wrong and null will be returned triggering a null pointer error.
		return null;
	}	
}
