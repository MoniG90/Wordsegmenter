import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

class CalculateTime{


	Trie trie;
	Bigram bigram;
	SaveObject sb = new SaveObject();
	//ArrayList<String> arry = new ArrayList<String>();
	String [] sentences;
	
	private void readFile(String fileName){
		String line;
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			while((line=bf.readLine())!=null){
				sentences = line.split(" ");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
    *counts time for a string of sentences
    *
    */
	public void printTime(){
		trie = sb.loadTrie();
		bigram = sb.loadBiGram();
        ArrayList<AlternativeString>  as;
        Stopwatch stopwatch = new Stopwatch();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sentences.length; i++) { 
            sb.append(sentences[i]);
            String packedString = sb.toString();
            stopwatch.start();
			as = bigram.parseString(packedString,trie);
            trie.evaluteStrings(as); // don't know if segmented correctly, doesn't matter.
            stopwatch.stop();
            double nanos = stopwatch.nanoseconds();
            stopwatch.reset();
            System.out.println(i+1 + " " + nanos/1000000000);
        }

	}
	
	public static void main(String[] args){
	
		CalculateTime ct = new CalculateTime();
		ct.readFile("sentences");
		ct.printTime();
		
	}
	
	}

