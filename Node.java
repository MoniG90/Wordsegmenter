import java.io.Serializable;
import java.util.ArrayList;


public class Node implements Serializable{
	
	private char character;
	private ArrayList<Node> children;
	private Node parent;
	private boolean isRed; // true if word ends on this node
	private int hits;
	
	public Node(char c,boolean isRed){
		children = new ArrayList<Node>();
		character = c;
		this.isRed=isRed;
		hits = 0;
	}
	
	public char getChar(){
		return character;
	}
	public void setParent(Node n){
		parent = n;
	}
	public ArrayList<Node> getArrayList(){
		return children;
	}
	
	public boolean isRed(){
		return isRed;
	}
	public void setRed(boolean b){
		isRed = b;
	}
	public void increaseHit(){
		hits+=1;
	}
	public int getHits(){
		return hits;
	}
	
}
