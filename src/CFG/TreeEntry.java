package CFG;

import java.util.LinkedList;

public class TreeEntry {
	
	public String label;
	public LinkedList<TreeEntry> children = new LinkedList<TreeEntry>();
	public TreeEntry parent;
	//When entry is created, these are entered indicating which element in the dp table this TreeEntry came from
	int referenceX;
	int referenceY;
	
	boolean visited = false;
	
	public void setParent(TreeEntry parent){
		this.parent=parent;
	}
	
	//Function for setting reference indices
	public void setReference(int X, int Y){
		this.referenceX = X;
		this.referenceY = Y;
	}
	
	//Function for setting static label variable
	public void setLabel(String label){
		this.label = label;
	}
	
	//Function for adding children to this TreeEntry
	public void addChild(TreeEntry child){
		this.children.add(child);
	}
	
	public String toJSON(){
		String json = "\"name\": \""+this.label+"\"";
		if(this.children.size()!=0){
			json+=",\n\"children\": [";
			boolean first = true;
			for(TreeEntry child : this.children){
				if(child.label!=null){
					if(!first){
						json+=", ";
					}
					first = false;
					json+="{\n"+child.toJSON()+"\n}";
				}
				
			}
			json+="]";
		}else{
			json+=",\n\"size\": 0";
		}
		
		return json;
	}
	
	@Override public String toString(){
		if(children.size()>0){
			return this.label;
		}else{
			return this.label+"*";
		}
	}

}
