package CFG;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class Parser extends Grammar {
	
	Entry[][] table;
	String sequence;
	
	public void printTable(){
		for (Entry[] row : this.table){
			for(Entry cell : row){
				System.out.print(cell+",");
			}
			System.out.print("\n");
		}
	}
	
	private TreeEntry constructTree(){
		
		TreeEntry root = new TreeEntry();
		//TreeEntry workingEntry = root;
		TreeEntry workingRoot = root;
		//Set the root's parent to null
		root.setParent(null);
		root.setLabel("S");
		
		int workingX = 0;
		int workingY = 0;
		
		int xIndex = 0;
		int yIndex = 0;
		
		//instantiate the values of the root
		boolean working = true;
		while(working){
			TreeEntry tempRoot = null;
			TreeEntry tempWorking = null;
			rowLoop:
			for(int i=yIndex; i<this.table.length; i++){
				tempRoot = new TreeEntry();
				tempWorking = tempRoot;
				for(int j=xIndex; j<this.table[i].length; j++){
					//System.out.println(i+","+j);
					//System.out.println(this.table[i][j]);
					if(this.table[i][j].productions.contains(Pnonterminal)){
						int height = this.table.length-i;
						double QProb = Math.pow(Qcontinue,height)*Math.pow(termProb, height/2);
						double PProb = Qpalindrome*Math.pow(Pnest,height/2);
						//If PProb is greater than QProb that means that producing a palindrome from this height in the dp table is more likely under the P production than the Q production
						if(PProb>QProb){
							tempWorking.setLabel("Q");
							//tempRoot = tempWorking;
							TreeEntry palindrome = new TreeEntry();
							palindrome.setParent(tempWorking);
							tempWorking.addChild(palindrome);
							palindrome.setLabel("P");
							//Set temporary working indices to guide decent to the bottom
							int tempWorkingX = j;
							int tempWorkingY = i;
							
							TreeEntry leftTerm = new TreeEntry();
							leftTerm.setLabel(this.sequence.substring(tempWorkingX,tempWorkingX+1));
							leftTerm.setParent(palindrome);
							palindrome.addChild(leftTerm);
							
							TreeEntry centerP = new TreeEntry();
							palindrome.addChild(centerP);
							
							TreeEntry rightTerm = new TreeEntry();
							rightTerm.setLabel(this.sequence.substring(tempWorkingX+height-1,tempWorkingX+height));
							rightTerm.setParent(palindrome);
							palindrome.addChild(rightTerm);
							TreeEntry paldindromeWorking = centerP;
							
							while(tempWorkingY<this.table.length-2){
								//System.out.println("TEST");
								int offset = this.table.length-tempWorkingY;
								//set the temp working indices to the next nested palindrome 
								tempWorkingX++;
								tempWorkingY=tempWorkingY+2;
								paldindromeWorking.setReference(tempWorkingX, tempWorkingY);
								
								paldindromeWorking.setLabel("P");
								
								TreeEntry left = new TreeEntry();
								left.setLabel(this.sequence.substring(tempWorkingX,tempWorkingX+1));
								left.setParent(paldindromeWorking);
								paldindromeWorking.addChild(left);
								
								TreeEntry center=null;
							//	System.out.println(tempWorkingY);
							//	System.out.println(tempWorkingX);
								if(this.table[tempWorkingY][tempWorkingX].subseq.length()>2){
									center = new TreeEntry();
									paldindromeWorking.addChild(center);
								}
								
								
								TreeEntry right = new TreeEntry();
								right.setLabel(this.sequence.substring(tempWorkingX+offset-3,tempWorkingX+offset-2));
								right.setParent(paldindromeWorking);
								paldindromeWorking.addChild(right);
								
								if(this.table[tempWorkingY][tempWorkingX].subseq.length()>2){
									paldindromeWorking = center;
								}
							}
							//update the xIndex and yIndex values so they begin indexing after the palindrome
							xIndex = j+(this.table.length-i);
							yIndex = i;//+((this.table.length-i)/2);
							//if the root of this palindrome is on the far right of the table, we exit after this palindrome
							if(j==(this.table[i].length-1)){
								working = false;
							}
							//break from the table loops
							break rowLoop;
							
						}else{
							//this is shit. need a better way of doing it
							tempWorking.setLabel("Q");
							TreeEntry terminal = new TreeEntry();
							terminal.setLabel(this.sequence.substring(j,j+1));
							terminal.setParent(tempWorking);
							tempWorking.addChild(terminal);
							TreeEntry nextEntry = new TreeEntry();
							tempWorking.addChild(nextEntry);
							nextEntry.setParent(tempWorking);
							tempWorking = nextEntry;
						}
					}else{
						tempWorking.setLabel("Q");
						TreeEntry terminal = new TreeEntry();
						terminal.setLabel(this.sequence.substring(j,j+1));
						terminal.setParent(tempWorking);
						tempWorking.addChild(terminal);
						TreeEntry nextEntry = new TreeEntry();
						tempWorking.addChild(nextEntry);
						nextEntry.setParent(tempWorking);
						tempWorking = nextEntry;
					}
					//if the bottom right of the table has been reached
					if((i==(this.table.length-1))&&(j==(this.table[this.table.length-1].length-1))){
						working = false;
					}
				}
			}
			
			workingRoot.addChild(tempRoot);
			tempRoot.setParent(workingRoot);
			
			workingRoot = tempWorking;

		}
		
		return root;
	}
	
	public LinkedList<TreeEntry> depthFirstSearch(TreeEntry root){
		
		String JSONout = "";
		
		LinkedList<TreeEntry> returnList = new LinkedList<TreeEntry>();
		LinkedList<TreeEntry> stack = new LinkedList<TreeEntry>();
		stack.push(root);
		root.visited=true;
		returnList.push(root);
		
		
		while(!stack.isEmpty()){
			TreeEntry workingEntry = stack.peek();
			boolean searched = true;
			boolean last = false;
			childLoop:
				
				
			for(int i=0;i<workingEntry.children.size();i++){
				TreeEntry child = workingEntry.children.get(i);
				if(!child.visited){
					stack.add(child);
					child.visited=true;
					returnList.add(child);

					searched = false;
					
					break childLoop;
				}
				if(i==(workingEntry.children.size()-1)){
					last = true;
				}
			}
			
			if(searched){
				TreeEntry popped = stack.pop();
				if(popped.label!=null){
					JSONout+="{\n\"name\": \""+popped.label+"\",\n";
				}
				if(popped.children.size()==0){
					if((!stack.isEmpty())&&(popped.label!=null)){
						JSONout+="},";
					}else{
						JSONout+="]";
					}
				}else{
					JSONout+="\"children\": [";
				}
				if(last){
					//System.out.println("}]");
				}
				//System.out.println("name: "+popped.label);
				//System.out.println("children: "+popped.children);
			}
			
		}
		System.out.println(returnList);
		
		System.out.println(JSONout);
		return returnList;
		
	}
	
	private boolean isComplementary(String first, String second){
		if(first.equals("A")){
			if(second.equals("T")){
				return true;
			}else{
				return false;
			}
		}else if(first.equals("T")){
			if(second.equals("A")){
				return true;
			}else{
				return false;
			}
		}else if(first.equals("C")){
			if(second.equals("G")){
				return true;
			}else{
				return false;
			}
		}else if(first.equals("G")){
			if(second.equals("C")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	//instantiate the dp table and fill it out with the proper length
	public Parser(String sequence) throws FileNotFoundException, UnsupportedEncodingException{
		
		this.sequence = sequence;
		//Set up the dp table including the different subsequences
		this.table = new Entry[sequence.length()][];
		for(int i=0; i<this.table.length; i++){
			this.table[i] = new Entry[i+1];
		}
		
		int finalIndex = this.table.length-1;
		for(int i=0; i<this.table.length; i++){
			for(int j=0; j<this.table[i].length; j++){
				Entry currEntry = new Entry(sequence.substring(j,j+(sequence.length()-i)));
				this.table[i][j]=currEntry;
			}
		}

		//instantiate the bottom row of the dp table with production rules
		for(int j=0; j<this.table[this.table.length-1].length; j++){
			this.table[this.table.length-1][j].productions.add(this.Qterminal);
		}
		
		for(int i=this.table.length-2; i>=0; i--){
			for(int j=0; j<this.table[i].length; j++){
				//If the size of the cell's sequence is two this is a base case. This should be handled as a nonbase case, but would require a restructuring of the grammar implementation
				if(this.table[i][j].subseq.length()==2){
					//if the two letters are complementary, add P as a possible nonterminal
					if(isComplementary(this.table[i][j].subsequence[1],this.table[i][j].subsequence[2])){
						this.table[i][j].productions.add(Pnonterminal);
					}
					this.table[i][j].productions.add(Qnonterminal);
				}else{
					
					//if the length is even
					if((this.table[i][j].subseq.length() & 1) == 0){
						//If the cell down 2 and to the right 1
						if(this.table[i+2][j+1].productions.contains(Pnonterminal)){
							//if the two characters on the side are complementary
							if(isComplementary(this.table[i][j].subsequence[1],this.table[i][j].subsequence[this.table[i][j].subsequence.length-1])){
								//add the p nonterminal to this cells valid productions
								this.table[i][j].productions.add(Pnonterminal);
							}
						}
					}
					this.table[i][j].productions.add(Qnonterminal);
				}
			}
		}
		
		//printTable();
		
		System.out.println("\n\nConstructing Tree...\n\n");
		TreeEntry root = constructTree();
		
		/*
		System.out.println(root.children);
		System.out.println(root.children.peek().children);
		System.out.println(root.children.peek().children.get(1).children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children.get(1).children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children.get(1).children.get(1).children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children.peek().children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children.peek().children.get(1).children);
		System.out.println(root.children.peek().children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children.get(1).children.peek().children.get(1).children.get(1).children);
		*/
		
		//LinkedList<TreeEntry> order = depthFirstSearch(root);
		System.out.println("\n\nPrinting Tree...\n\n");
		
		PrintWriter writer = new PrintWriter("data.json", "UTF-8");
		writer.print("{\n"+root.toJSON()+"\n}");
		writer.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		String sequence = args[0];
		Parser treeParse = new Parser(sequence);
		
	}

}