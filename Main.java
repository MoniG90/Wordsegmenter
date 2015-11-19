import java.util.ArrayList;


public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String fileName = "korpus";
        SaveObject saveOb = new SaveObject();
        Trie trie;
        Bigram bigram;
        if(args.length==1 && args[0].equals("load")){
            trie = saveOb.loadTrie();
            bigram = saveOb.loadBiGram();
        }else{
                    bigram = new Bigram(Double.parseDouble(args[args.length-1]));
                    trie = new Trie();
            for(int i=0;i<args.length-1;i++){
                System.out.println(args[i]);
                fileName = args[i];

                if(fileName.equals("dictionary")){
                    trie.buildTrieFromDic(fileName);
                }
                else{
                    trie.buildTrie(fileName);
                    bigram.trainFromFile(fileName);
                }
            }
            saveOb.saveTrie(trie);
            saveOb.saveBigram(bigram);
        }
         while(true) {
             ArrayList<AlternativeString> as = bigram.parseString(trie);
             String bestAlternative = trie.evaluteStrings(as);
             System.out.println("Best alternative: ");
             System.out.println(bestAlternative);
         }
		
    }
	

}
