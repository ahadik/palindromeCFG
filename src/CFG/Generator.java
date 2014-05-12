package CFG;

import java.util.LinkedList;

public class Generator extends Grammar {

	/*
	 * INPUT: A start nonterminal
	 * OUTPUT: A sequence randomly generated by the production rules of the grammar.
	 */
	public static String generateSequence(nonterminal startProduction) throws prodRuleProbException{
		
		String seqProduction = "";
		production activeProduction = startProduction;
		LinkedList<production> seqQueue = new LinkedList<production>();
		seqQueue.addLast(activeProduction);
		
		while (!seqQueue.isEmpty()){
			//System.out.println(seqQueue);
			activeProduction = seqQueue.getFirst();
			seqQueue.removeFirst();
			//Reset the production rule for this nonterminal randomly from the set of possible rules set as a parameter
			
			//if the active production is a nonterminal
			if(activeProduction.getClass().equals(nonterminal.class)){
				((nonterminal) activeProduction).generateRule();
				//For every production of the newly generated rule for the current production, add it to seqQueue
				for(production prod : ((nonterminal) activeProduction).rule.rule){
					seqQueue.addFirst(prod);
				}
			}else if (activeProduction.getClass().equals(terminal.class)){
				//if this terminal did not have the value explicitly set upon instantiation
				if(!((terminal) activeProduction).setValue){
					//generate a random value
					((terminal) activeProduction).generateValue();
				}
				//if the next production is a P
				if (((terminal) activeProduction).value.value!=null){
					if(seqQueue.size()>0){
						if(seqQueue.peek().getClass().equals(nonterminal.class)){
							if((((nonterminal) seqQueue.peek()).isP)){
								/*
								 * if the next nonterminal is P, that means this is a nest P rule, and we need to make a new terminal that is 
								 * the complement of whatever value was randomly generated from generateValue() above 
								 */
								terminal complementaryTerminal = new terminal(false); 
								complementaryTerminal.setValue(complementaryTerminal(((terminal) activeProduction).value.value));
								complementaryTerminal.setValues(terminals);
								//insert the new complementary terminal after the P production that's up next 
								seqQueue.add(1,complementaryTerminal);
							}
						}
					}
				}
				if(((terminal) activeProduction).value.value!=null){
					//concatenate the value onto the 
					seqProduction=seqProduction+((terminal) activeProduction).value.value;
				}
			}
			
		}
		//return the filled out seqProduction
		return seqProduction;
	}


	public static productionTerminal complementaryTerminal(String value){
		if(value.equals("A")){
			return terminalT;
		}
		if(value.equals("T")){
			return terminalA;
		}
		if(value.equals("C")){
			return terminalG;
		}
		if(value.equals("G")){
			return terminalC;
		}
		System.out.println("Complementary value not found. Returning null.");
		return null;
	}
	
	
	
	
	public static void main(String[] args) throws prodRuleProbException {
		
		Generator generator = new Generator();
		
		nonterminal startProduction = generator.Qnonterminal;
		startProduction.generateProduction();
		
		String generatedSeq = generateSequence(startProduction);
		
		System.out.println(generatedSeq);
		
	}

}
