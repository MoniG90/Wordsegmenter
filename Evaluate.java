import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Evaluate {
    Bigram bigram;
    Trie trie;
    SaveObject sb = new SaveObject();
    ArrayList<String> input_without_spaces = new ArrayList<String>();
    ArrayList<String> facit_with_spaces = new ArrayList<String>();

    int truePositives = 0;
    int falsePositives = 0;
    int falseNegatives = 0;
	
    private void read_input_without_spaces(String fileName) {
        String line;
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            while((line=bf.readLine())!=null) {
                input_without_spaces.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void read_facit_with_spaces(String fileName){
        String line;
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            while((line=bf.readLine())!=null){
                facit_with_spaces.add(line);
            }
		
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readInput(String inputFile,String facitFile) {
        read_input_without_spaces(inputFile);
        read_facit_with_spaces(facitFile);
    }
	
    public void evaluate(int numberOfSentences) {
        int counter = 0;
        trie = sb.loadTrie();
        bigram = sb.loadBiGram();
        for(int i=0;i<numberOfSentences;i++) {
            ArrayList<AlternativeString> as = bigram.parseString(input_without_spaces.get(i),trie);
            String bestAlternative = trie.evaluteStrings(as);
            String facit = facit_with_spaces.get(i);
            facit = facit.substring(0,facit.length()-1);
            if(bestAlternative.equals(facit)) {
                counter++;
            }
            int TP = countTruePositives(facit, bestAlternative);
            int FP = countFalsePositives(facit, bestAlternative);
            int FN = countFalseNegatives(facit, bestAlternative);
            double P = calcPrecision(TP, FP);
            double R = calcRecall(TP, FN);
            double F = calcFscore(P,R);
            System.out.println("TP sentence: " + TP);
            System.out.println("FP sentence: " + FP);
            System.out.println("FN sentence: " + FN);
            System.out.println("P sentence: " + P);
            System.out.println("R sentence: " + R);
            System.out.println("F sentence: " + F);
            truePositives += TP;
            falsePositives += FP;
            falseNegatives += FN;
            System.out.println("FACIT: " + facit);
            System.out.println("SEGMENTED: " + bestAlternative);
            System.out.println("-----------------------------");
        }

        System.out.println("--------------DONE, data for the whole file:--------------");
        System.out.println("True positives: " + truePositives);
        System.out.println("False negatives: " + falseNegatives);
        System.out.println("False positives: " + falsePositives);
        double precision = calcPrecision(truePositives, falsePositives);
        double recall = calcRecall(truePositives, falseNegatives);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F score: " + calcFscore(precision,recall));
        System.out.println(counter);
    }

        public double calcFscore(double P, double R) {
            return (2*P*R) / (P+R);
        }
    
        public int countTruePositives(String facit, String output) {        
        String[] facitArray = facit.split(" ");
        String[] outputArray = output.split(" ");

        StringBuilder tmpFacit = new StringBuilder();
        StringBuilder tmpOutput = new StringBuilder();

        int truePositives = 0;

        int facitIndex = 0;
        int outputIndex = 0;

        while(facitIndex < facitArray.length || outputIndex < outputArray.length) {
            tmpFacit.append(facitArray[facitIndex]);
            tmpOutput.append(outputArray[outputIndex]);

            while(tmpFacit.length() != tmpOutput.length()) {
                if(tmpFacit.length() < tmpOutput.length()) {
                    facitIndex++;
                    tmpFacit.append(facitArray[facitIndex]);
                } else {
                    outputIndex++;
                    tmpOutput.append(outputArray[outputIndex]);
                }
            }
            truePositives++;
            facitIndex++;
            outputIndex++;
        }

        return (truePositives);
    }

    public int countFalsePositives(String facit, String output) {        
        String[] facitArray = facit.split(" ");
        String[] outputArray = output.split(" ");

        StringBuilder tmpFacit = new StringBuilder();
        StringBuilder tmpOutput = new StringBuilder();

        int falsePositives = 0;

        int facitIndex = 0;
        int outputIndex = 0;

        while(facitIndex < facitArray.length || outputIndex < outputArray.length) {
            tmpFacit.append(facitArray[facitIndex]);
            tmpOutput.append(outputArray[outputIndex]);
            while(tmpFacit.length() != tmpOutput.length()) {
                if(tmpFacit.length() < tmpOutput.length()) {
                    facitIndex++;
                    tmpFacit.append(facitArray[facitIndex]);
                } else {
                    outputIndex++;
                    falsePositives++;
                    tmpOutput.append(outputArray[outputIndex]);
                }
            }
    
            facitIndex++;
            outputIndex++;
        }

        return falsePositives;
    }

    public int countFalseNegatives(String facit, String output) {        
        String[] facitArray = facit.split(" ");
        String[] outputArray = output.split(" ");

        StringBuilder tmpFacit = new StringBuilder();
        StringBuilder tmpOutput = new StringBuilder();

        int falseNegatives = 0;

        int facitIndex = 0;
        int outputIndex = 0;

        while(facitIndex < facitArray.length || outputIndex < outputArray.length) {
            tmpFacit.append(facitArray[facitIndex]);
            tmpOutput.append(outputArray[outputIndex]);
            while(tmpFacit.length() != tmpOutput.length()) {
                if(tmpFacit.length() < tmpOutput.length()) {
                    facitIndex++;
                    falseNegatives++;
                    tmpFacit.append(facitArray[facitIndex]);
                } else {
                    outputIndex++;
                    tmpOutput.append(outputArray[outputIndex]);
                }
            }
            facitIndex++;
            outputIndex++;
        }

        return falseNegatives;
    }

    public double calcPrecision(int truePositives, int falsePositives) {
        return (double)truePositives/((double)truePositives+(double)falsePositives);
    }

    public double calcRecall(int truePositives, int falseNegatives) {
        return (double)truePositives/((double)truePositives+(double)falseNegatives);
    }

	
    public static void main(String[] args) {
        Evaluate eval = new Evaluate();
        if(args.length==1){
            eval.readInput("testText.txt","testTextFacit.txt");
            eval.evaluate(Integer.parseInt(args[0]));
        }
    }
}
