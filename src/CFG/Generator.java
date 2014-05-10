package CFG;

import java.util.LinkedList;

public class Generator {

	//Probability weights for terminals
	static double Aprob = .25;
	static double Tprob = .25;
	static double Gprob = .25;
	static double Cprob = .25;
	
	//Probability weights for Q production rules
	static double Qcontinue = .95;
	static double Qpalindrome = .09;
	static double Qend = .01;
	
	//Probability weights for P production rules
	double Pnest = .8;
	double Pterminate = .2;
	
	//define terminals for A,T,C,G
	static productionTerminal terminalA = new productionTerminal("A", Aprob);
	static productionTerminal terminalT = new productionTerminal("T", Tprob);
	static productionTerminal terminalC = new productionTerminal("C", Cprob);
	static productionTerminal terminalG = new productionTerminal("G", Gprob);
	static productionTerminal terminalEnd = new productionTerminal(null, 1.0);
	
	//define generic nonterminals P and Q
	static nonterminal Pnonterminal = new nonterminal();
	static nonterminal Qnonterminal = new nonterminal();
	
	//define generic terminals
	static terminal Qterminal = new terminal();
	static terminal Pterminal = new terminal();
	static terminal Endterminal = new terminal();
	
	
	
	//Instantiate list of terminals for random selection
	LinkedList<rule> terminals;
	
	//Define productions for Q productions
	LinkedList<production> extend;
	productionRule extendRule;
	LinkedList<production> palindromeStart;
	productionRule palindromeStartRule;
	LinkedList<production> terminateQ;
	productionRule terminateQRule;
	
	//Define productions for P productions
	LinkedList<production> extendPalindrome;
	productionRule extendPalindromeRule;
	productionRule terminatePalindromeRule;
	
	
	LinkedList<rule> Qrules;
	LinkedList<rule> Prules;
	
	
	public Generator(){
		//Add all terminals to the list of terminals 
		this.terminals = new LinkedList<rule>();
		this.terminals.add(terminalA);
		this.terminals.add(terminalT);
		this.terminals.add(terminalC);
		this.terminals.add(terminalG);
		
		//Set the possible values of the Qterminal
		Qterminal.setValues(this.terminals);
		Pterminal.setValues(this.terminals);
		
		//Define the terminals and nonterminals for Q rules
		
		//extend rule
		this.extend = new LinkedList<production>();
		this.extend.addLast(Qnonterminal);
		this.extend.addFirst(Qterminal);
		
		this.extendRule = new productionRule(extend, Qcontinue);
		
		//palindrome rule
		this.palindromeStart = new LinkedList<production>();
		this.palindromeStart.addFirst(Pnonterminal);
		this.palindromeStart.addLast(Qnonterminal);
		
		this.palindromeStartRule = new productionRule(palindromeStart, Qpalindrome);

		//end Q rule
		this.terminateQ = new LinkedList<production>();
		this.terminateQ.add(Endterminal);
		
		this.terminateQRule = new productionRule(terminateQ,Qend);
		
		
		//extend palindrome
		this.extendPalindrome.addLast(Pterminal);
		this.extendPalindrome.addLast(Pnonterminal);
		//this.extendPalindrome.addLast(Pterminal);
		
		
		this.extendPalindromeRule = new productionRule(extendPalindrome, Pnest);
		this.terminatePalindromeRule = new productionRule(terminateQ, Pterminate);
		
		//Generate list of Q rules
		this.Qrules = new LinkedList<rule>();
		this.Qrules.add(extendRule);
		this.Qrules.add(palindromeStartRule);
		this.Qrules.add(terminateQRule);
		
		//Generate list of P rules
		LinkedList<rule> Prules = new LinkedList<rule>();
		this.Prules.add(extendPalindromeRule);
		this.Prules.add(terminatePalindromeRule);
		
		//Set the possible rules for the P and Q nonterminal
		Generator.Qnonterminal.setRules(Qrules,false);
		Generator.Pnonterminal.setRules(Prules,true);
		
	}
	
	public static String generateSequence(nonterminal startProduction) throws prodRuleProbException{
		
		String seqProduction = "";
		production activeProduction = startProduction;
		LinkedList<production> seqQueue = new LinkedList<production>();
		seqQueue.addLast(activeProduction);
		
		while (!seqQueue.isEmpty()){
			activeProduction = seqQueue.getFirst();
			//Reset the production rule for this nonterminal randomly from the set of possible rules set as a parameter
			
			//if the active production is a nonterminal
			if(activeProduction.getClass().equals(nonterminal.class)){
				((nonterminal) activeProduction).generateRule();
				//For every production of the newly generated rule for the current production, add it to seqQueue
				for(production prod : ((nonterminal) activeProduction).rule.rule){
					seqQueue.addFirst(prod);
				}
			}else if (activeProduction.getClass().equals(terminal.class)){
				((terminal) activeProduction).generateValue();
				//if the next production is a P
				if(((nonterminal) seqQueue.peek()).isP){
					/*
					 * if the next nonterminal is P, that means this is a nest P rule, and we need to make a new terminal that is 
					 * the complement of whatever value was randomly generated from generateValue() above 
					 */
					terminal complementaryTerminal = new terminal(); 
					complementaryTerminal.setValue(complementaryTerminal(((terminal) activeProduction).value.value));
					//insert the new complementary terminal after the P production that's up next 
					seqQueue.add(1,complementaryTerminal);
				}
				if(((terminal) activeProduction).value.value!=null){
					//concatenate the value onto the 
					seqProduction=seqProduction+((terminal) activeProduction).value.value;
				}
			}
			System.out.println(seqQueue);
		}
		//return the filled out seqProduction
		return seqProduction;
	}


	public static productionTerminal complementaryTerminal(String value){
		if(value.equals("A")){
			return terminalA;
		}
		if(value.equals("T")){
			return terminalT;
		}
		if(value.equals("C")){
			return terminalC;
		}
		if(value.equals("G")){
			return terminalG;
		}
		System.out.println("Complementary value not found. Returning null.");
		return null;
	}
	
	
	
	
	public static void main(String[] args) throws prodRuleProbException {
		
		Generator generator = new Generator();
		
		nonterminal startProduction = generator.Pnonterminal;
		startProduction.generateProduction();
		
		String generatedSeq = generateSequence(startProduction);
		
		System.out.println(generatedSeq);
		
	}

}
