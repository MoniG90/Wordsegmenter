import java.io.Serializable;
import java.util.ArrayList;

public class Trie implements Serializable {
    private Node root;
	
    public Trie() {
        root = new Node('@',true);
    }
	
    /**
     * call this for evalute every possible string
     * and returns the best one
     * @param segmentedStrings
     * @return
     */
    public String evaluteStrings(ArrayList<AlternativeString> segmentedStrings) {
        String bestString = segmentedStrings.get(0).string;
        double bestScore = -Double.MAX_VALUE;

        for(int i = 0; i < segmentedStrings.size(); i++){
            String [] currentLine = segmentedStrings.get(i).string.split(" ");
            double currentLineScore = 0;
			boolean discard=false;

            for (int j = 0; j < currentLine.length; j++) {
                int hits = getHits(root, currentLine[j]);
                if(!containWord(root,currentLine[j])) {
                	//System.err.println("Word not found: "+currentLine[j]);
                    hits = -Integer.MAX_VALUE;
					discard = true;
					break;
                }
                currentLineScore += hits;
            }
			if(discard)
				continue;
            currentLineScore = currentLineScore / currentLine.length;

            if (bestScore < currentLineScore) {
                bestScore = currentLineScore;
                bestString = segmentedStrings.get(i).string;
            }
        }
        return bestString;
    }
	
	
    /**
     * get the node of root
     * @return
     */
    public Node getRoot() {
        return root;
    }
	
    /**	
     * * counts the hits of a word
     * @param n
     * @param s
     * @return
     */
    public int getHits(Node n, String s) {
        if(s.length() == 0 && n.isRed()) {
            return n.getHits();
        } else if(s.length() == 0) {
            return 0;
        }

        ArrayList<Node> ar = n.getArrayList();

        for(int i = 0; i < ar.size(); i++){
            if(ar.get(i).getChar() == s.charAt(0)) {
                return getHits(ar.get(i),s.substring(1));
            }
        }
        return 0;
    }
	
    /**
     * se if the tree got this word in it
     * @param n
     * @param s
     * @return
     */
    public boolean containWord(Node n,String s) {
        if(s.length() == 0 && n.isRed()) {
            return true;
        } else if(s.length()==0) {
            return false;
        }

        ArrayList<Node> ar = n.getArrayList();

        for(int i = 0; i < ar.size(); i++) {
            if(ar.get(i).getChar() == s.charAt(0)) {
                return containWord(ar.get(i),s.substring(1));
            }
        }
        return false;
    }
	
    /**
     * just printing
     * @param n
     * @param level
     */
    public void printTrie(Node n, int level) {
        ArrayList<Node> ar = n.getArrayList();
        for(int i = 0; i < ar.size(); i++){
            System.out.println("level: " +  level + " ||" + ar.get(i).getChar());
            printTrie(ar.get(i),level+1);
        }
    }
	
    /**
     * adding word in the trie
     * @param n
     * @param s
     */
    public void addWord(Node n,String s) {
        if(s.length() == 0) {
            n.increaseHit();
            n.setRed(true);
            return;
        }
		
        char c = s.charAt(0);
        for(int i = 0; i < n.getArrayList().size(); i++) {
            Node children = n.getArrayList().get(i);
            char childChar = children.getChar();
            if(c == childChar){
                addWord(children,s.substring(1));
                return;
            }
        }
        //doesnt exist
        boolean isRed = false;
        if(s.length() == 1) {
            isRed = true;
        }

        Node child = new Node(c,isRed);
        n.getArrayList().add(child);
        child.setParent(n);
        addWord(child,s.substring(1));
    }
	
	public boolean existsInDic(){
		
		return true;
	}
	
	
    public void addWordWithoutHits(Node n,String s) {
        if(s.length() == 0) {
            n.setRed(true);
            return;
        }
		
        char c = s.charAt(0);
        for(int i = 0; i < n.getArrayList().size(); i++) {
            Node children = n.getArrayList().get(i);
            char childChar = children.getChar();
            if(c == childChar){
                addWord(children,s.substring(1));
                return;
            }
        }
        //doesnt exist
        boolean isRed = false;
        if(s.length() == 1) {
            isRed = true;
        }

        Node child = new Node(c,isRed);
        n.getArrayList().add(child);
        child.setParent(n);
        addWord(child,s.substring(1));
    }
	
	
	/**
	 * buildTrieformDic
	 * @param fileName
	 */
	public void buildTrieFromDic(String fileName){
		  ReadInput r = new ReadInput();
	        r.openReader(fileName);
	        String [] line = r.getLineFromDic();
	        while(!line[0].equals("-1jullebulle")) {
	            for(int i = 0; i < line.length; i++) {
	                addWord(root,line[i]);
	            }
	            line = r.getLineFromDic();
	        }
	        r.closeReader();
	}
	
    /**
     * builds our tree in
     * @param fileName
     */
    public void buildTrie(String fileName) {
        ReadInput r = new ReadInput();
        r.openReader(fileName);
        String [] line = r.getLine();
        while(!line[0].equals("-1jullebulle")) {
            for(int i = 0; i < line.length; i++) {
            	if(containWord(root, line[i]) && line[i].equals("th")) {
            		addWord(root,line[i]);
                	//System.err.println("We should never see this!");
            	}
            }
            line = r.getLine();
        }
        r.closeReader();
    }
}
