package CFG;

import java.util.LinkedList;

public class Grammar {

	//Probability weights for terminals
	static double termProb = .25;
	static double Aprob = .25;
	static double Tprob = .25;
	static double Gprob = .25;
	static double Cprob = .25;
	
	//Probability weights for Q production rules
	static double Qcontinue = .95;
	static double Qpalindrome = .049;
	static double Qend = .001;
	
	//Probability weights for P production rules
	double Pnest = .65;
	double Pterminate = .35;

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
	static terminal Qterminal = new terminal(false);
	static terminal Pterminal = new terminal(false);
	static terminal Endterminal = new terminal(true);
	
	
	
	static //Instantiate list of terminals for random selection
	LinkedList<rule> terminals;
	static LinkedList<rule> endterminal = new LinkedList<rule>();
	
	//Define productions for Q productions
	LinkedList<production> extend = new LinkedList<production>();
	productionRule extendRule;
	LinkedList<production> palindromeStart = new LinkedList<production>();
	productionRule palindromeStartRule;
	LinkedList<production> terminateQ = new LinkedList<production>();
	productionRule terminateQRule;
	
	//Define productions for P productions
	LinkedList<production> extendPalindrome = new LinkedList<production>();
	productionRule extendPalindromeRule;
	productionRule terminatePalindromeRule;
	
	
	LinkedList<rule> Qrules = new LinkedList<rule>();
	LinkedList<rule> Prules = new LinkedList<rule>();
	
	public Grammar(){
		//Add all terminals to the list of terminals 
		Generator.terminals = new LinkedList<rule>();
		Generator.terminals.add(terminalA);
		Generator.terminals.add(terminalT);
		Generator.terminals.add(terminalC);
		Generator.terminals.add(terminalG);
		Generator.endterminal.add(terminalEnd);
		
		//Set the possible values of the Qterminal
		Qterminal.setValues(this.terminals);
		Pterminal.setValues(this.terminals);
		Endterminal.setValues(this.endterminal);
		
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
		this.extendPalindrome.addFirst(Pnonterminal);
		//this.extendPalindrome.addLast(Pterminal);
		
		
		this.extendPalindromeRule = new productionRule(extendPalindrome, Pnest);
		this.terminatePalindromeRule = new productionRule(terminateQ, Pterminate);
		
		//Generate list of Q rules
		this.Qrules.add(extendRule);
		this.Qrules.add(palindromeStartRule);
		this.Qrules.add(terminateQRule);
		
		//Generate list of P rules
		this.Prules.add(extendPalindromeRule);
		this.Prules.add(terminatePalindromeRule);
		
		//Set the possible rules for the P and Q nonterminal
		Generator.Qnonterminal.setRules(Qrules,false);
		Generator.Pnonterminal.setRules(Prules,true);
	}
	
}
