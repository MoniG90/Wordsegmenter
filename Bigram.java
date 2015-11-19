import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;


public class Bigram implements Serializable{
    private int[][][] lookupTable = new int[256][256][4];
    double THRESHOLD;
    
    public Bigram(double THRESHOLD) {
        this.THRESHOLD = THRESHOLD;
    }
	
    /*
     * where = Const.[BEFORE|AFTER|BETWEEN|NOSPACE]
     */
    public void add(char[] twoLetters, int where) {
        if(twoLetters.length > 2 || twoLetters.length == 0) {
            System.err.println("twoLetters in add() was not 2 letters!");
            System.exit(-1);
        }
		
        lookupTable[(int) twoLetters[0]][(int) twoLetters[1]][where]++;
    }
	
    public int[] getEntries(char[] twoLetters) {		
        if(twoLetters.length > 2 || twoLetters.length == 0) {
            System.err.println("twoLetters in getEntries() was not 2 letters!");
            System.exit(-1);
        }
        return lookupTable[(int) twoLetters[0]][(int) twoLetters[1]];
    }
	
    public void printTable(){
        for(int i = 0; i < lookupTable.length; i++) {
            for(int j = 0; j < lookupTable[0].length; j++) {
                int[] probs = lookupTable[i][j];
                if(probs[0] != 0 || probs[1] != 0 || probs[2] != 0 || probs[3] != 0) {
                    System.err.println("place: "+ (char) i + (char) j + " Before: "+probs[0]+ " After: "+probs[1]+ " Between: "+probs[2]+ " NoSpace: "+probs[3]);
                }
            }
        }
    }
	
    public void trainFromFile(String filepathAndName) {
        String line;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepathAndName), "UTF-8"));
            while((line = br.readLine()) != null) {
                line = line.toLowerCase();
                line = line.replaceAll("[^a-z -]", "");
                line = line.replaceAll("\\s+", " ");
                char[] twoLetters = new char[2];
                int length = line.length();
                for(int i = 0; i < length-2; i++) {
                    char before = (char) 0;
                    if( i != 0) {
                        before = line.charAt(i-1);
                    }
                    char first = line.charAt(i);
                    char second = line.charAt(i+1);
                    char after = line.charAt(i+2);
                    if(i != 0 && before == ' ') {
                        twoLetters[0] = first;
                        twoLetters[1] = second;
                        if(second == ' ') {
                            if(i != length-2) {
                                twoLetters[1] = after; // can't be space!
                                add(twoLetters, Const.BEFORE);
                            } else {
                                continue; // source of errors?
                            }
                        } else {
                            add(twoLetters, Const.BEFORE);
                        }
						
                    } 
					
                    if(second == ' ') {
                        twoLetters[0] = first; // can't be space!
                        twoLetters[1] = after; // can't be space!
                        add(twoLetters, Const.BETWEEN);
                    } 
					
                    if(after == ' ') {
                        twoLetters[0] = first;
                        twoLetters[1] = second;
                        if(first == ' ') {
                            if(i != 0) {
                                twoLetters[0] = before; // can't be space!
                                add(twoLetters, Const.AFTER);
                            } else {
                                continue; // source of errors?
                            }
                        } else {
                            add(twoLetters, Const.AFTER);
                        }
                    } 
                    if(before != ' ' && after != ' ' && second != ' '){ // first,second is surrounded with chars
                        twoLetters[0] = first;
                        twoLetters[1] = second;
                        add(twoLetters, Const.NOSPACE);
                        continue;
                    }
					
                    // if last word in string:
                    if(i == length-1) {
                        twoLetters[0] = first;
                        twoLetters[1] = second;
                        add(twoLetters, Const.AFTER);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Something went wrong when reading input");
            e.printStackTrace();
        }
    }

    public ArrayList<AlternativeString> parseString(Trie trie) {
        String line = readFromStdIn();
		
        char[] charsInLine = line.toCharArray();
        ArrayList<AlternativeString> possibleStrings = new ArrayList<AlternativeString>();
        possibleStrings.add(new AlternativeString(line, 0));
        for(int i = 0; i < charsInLine.length-1; i++) {
            char[] twoLetters = {charsInLine[i], charsInLine[i+1]}; 
            int[] results = getEntries(twoLetters);
            double probOfSpace = results[Const.BETWEEN];
            double sum = results[Const.AFTER] + results[Const.BEFORE] + results[Const.BETWEEN] + results[Const.NOSPACE];
            probOfSpace /= sum;
			
            if(probOfSpace > 0) { // "normalize" with divide with NOSPACE
                char[] before = new char[2];
                int[] resultsBefore = null;
                if(i != 0) {
                    before[0] = charsInLine[i-1];
                    before[1] = charsInLine[i];
                    resultsBefore = getEntries(before);
                }
                char[] after = new char[2];
                int[] resultsAfter = null;
                if(i != charsInLine.length-2) {
                    after[0] = charsInLine[i+1];
                    after[1] = charsInLine[i+2];
                    resultsAfter = getEntries(after);
                }
                if(resultsBefore != null) {
                    double sumBefore = resultsBefore[Const.AFTER] + resultsBefore[Const.BEFORE] + resultsBefore[Const.BETWEEN] + resultsBefore[Const.NOSPACE];
                    probOfSpace *= (resultsBefore[Const.AFTER]/sumBefore);
                }
                if(resultsAfter != null) {
                    double sumAfter = resultsAfter[Const.AFTER] + resultsAfter[Const.BEFORE] + resultsAfter[Const.BETWEEN] + resultsAfter[Const.NOSPACE];
                    probOfSpace *= (resultsAfter[Const.BEFORE]/sumAfter);
                }
				
                if(probOfSpace > THRESHOLD) {
                    ArrayList<AlternativeString> toAdd = new ArrayList<AlternativeString>();
                    for(int j = 0; j < possibleStrings.size(); j++) {
                        AlternativeString as = possibleStrings.get(j);
                        StringBuilder sb = new StringBuilder();
                        sb.append(as.string);
                        sb.insert((i+1)+as.nrOfSpacesInString, ' ');
                        String[] words = sb.toString().split(" ");
                        String toCheck = words[words.length-2];
                        if(trie.containWord(trie.getRoot(),toCheck))  
                            toAdd.add(new AlternativeString(sb.toString(), (as.nrOfSpacesInString+1)));
						
                        // do nuttin
                    }
                    for(int j = 0; j < toAdd.size(); j++) {
                        possibleStrings.add(toAdd.get(j));
                    }

                }
            }
        }
        return possibleStrings;
//		for(int i = 0; i < possibleStrings.size(); i++) {
//			System.out.println(""+(i+1)+" "+possibleStrings.get(i).string);
//		}
    }

    private String readFromStdIn() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            while((line = stdin.readLine()) != null) {
                return line;
            }
        } catch (IOException e) {
            System.err.println("Something went wrong when reading input");
            e.printStackTrace();
        }
        return null;
    }
	
	
    public ArrayList<AlternativeString> parseString(String line,Trie trie) {
		
		
        char[] charsInLine = line.toCharArray();
        ArrayList<AlternativeString> possibleStrings = new ArrayList<AlternativeString>();
        possibleStrings.add(new AlternativeString(line, 0));
        for(int i = 0; i < charsInLine.length-1; i++) {
            char[] twoLetters = {charsInLine[i], charsInLine[i+1]}; 
            int[] results = getEntries(twoLetters);
            double probOfSpace = results[Const.BETWEEN];
            double sum = results[Const.AFTER] + results[Const.BEFORE] + results[Const.BETWEEN] + results[Const.NOSPACE];
            probOfSpace /= sum;
			
            if(probOfSpace > 0) { // "normalize" with divide with NOSPACE
                char[] before = new char[2];
                int[] resultsBefore = null;
                if(i != 0) {
                    before[0] = charsInLine[i-1];
                    before[1] = charsInLine[i];
                    resultsBefore = getEntries(before);
                }
                char[] after = new char[2];
                int[] resultsAfter = null;
                if(i != charsInLine.length-2) {
                    after[0] = charsInLine[i+1];
                    after[1] = charsInLine[i+2];
                    resultsAfter = getEntries(after);
                }
                if(resultsBefore != null) {
                    double sumBefore = resultsBefore[Const.AFTER] + resultsBefore[Const.BEFORE] + resultsBefore[Const.BETWEEN] + resultsBefore[Const.NOSPACE];
                    probOfSpace *= (resultsBefore[Const.AFTER]/sumBefore);
                }
                if(resultsAfter != null) {
                    double sumAfter = resultsAfter[Const.AFTER] + resultsAfter[Const.BEFORE] + resultsAfter[Const.BETWEEN] + resultsAfter[Const.NOSPACE];
                    probOfSpace *= (resultsAfter[Const.BEFORE]/sumAfter);
                }
				
                if(probOfSpace > THRESHOLD) {
                    ArrayList<AlternativeString> toAdd = new ArrayList<AlternativeString>();
                    for(int j = 0; j < possibleStrings.size(); j++) {
                        AlternativeString as = possibleStrings.get(j);
                        StringBuilder sb = new StringBuilder();
                        sb.append(as.string);
                        sb.insert((i+1)+as.nrOfSpacesInString, ' ');
                        String[] words = sb.toString().split(" ");
                        String toCheck = words[words.length-2];
                        if(trie.containWord(trie.getRoot(),toCheck))  
                            toAdd.add(new AlternativeString(sb.toString(), (as.nrOfSpacesInString+1)));
						
                        // do nuttin
                        //toAdd.add(new AlternativeString(sb.toString(), (as.nrOfSpacesInString+1)));
						
                    }
                    for(int j = 0; j < toAdd.size(); j++) {
                        possibleStrings.add(toAdd.get(j));
                    }

                }
            }
        }
        return possibleStrings;
//		for(int i = 0; i < possibleStrings.size(); i++) {
//			System.out.println(""+(i+1)+" "+possibleStrings.get(i).string);
//		}
    }
	
	
}
