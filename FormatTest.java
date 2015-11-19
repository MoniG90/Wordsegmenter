import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;


public class FormatTest {
    

	Trie trie;
	SaveObject saveOb = new SaveObject();
	/**
	 * read a textfile, and prints to "printTestText.txt" the words wihtout segmentation
	 * @param fileName
	 */
	public void formatTest(String fileName, int ROWS,int WORDS_EACH_ROW, boolean isTreated){	
		trie = saveOb.loadTrie();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			PrintWriter pwtest = new PrintWriter("testText.txt","UTF-8");
			PrintWriter pwfacit = new PrintWriter("testTextFacit.txt","UTF-8");
			String line;
			int rowcount=0;
            Random rand = new Random(System.currentTimeMillis());
			int randNr = rand.nextInt(300000-ROWS); //300000 since korpus is that big
			for(int i=0;i<randNr;i++)
				bf.readLine();
			while((line=bf.readLine())!=null && rowcount<ROWS){
                StringBuilder testBuilder = new StringBuilder();
                StringBuilder facitBuilder = new StringBuilder();
                int amountOfAppendedWords = 0;
				line = line.toLowerCase();
				String[] spaceSplittedLine = line.replaceAll("[,\\.!\\?]", "").split(" ");
				for(int i=0;i<spaceSplittedLine.length;i++){
					//trie.containWord(trie.getRoot(),a[i])
                    if(spaceSplittedLine[i].matches("[a-z]+") ){
                        if(isTreated){
                            if(trie.containWord(trie.getRoot(),spaceSplittedLine[i])){
                                amountOfAppendedWords++;
                                testBuilder.append(spaceSplittedLine[i]);
                                facitBuilder.append(spaceSplittedLine[i] + " ");
                            }
                        } else {
                            amountOfAppendedWords++;
                            testBuilder.append(spaceSplittedLine[i]);
                            facitBuilder.append(spaceSplittedLine[i] + " ");
                        }
                    }	
				}
				
                if(amountOfAppendedWords==WORDS_EACH_ROW){
                    pwtest.print(testBuilder.toString());
                    pwfacit.print(facitBuilder.toString());
                    pwtest.println();
                    pwfacit.println();
                    rowcount++;
                }
			}
			pwtest.close();
			pwfacit.close();
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void main(String[] args){
		FormatTest ft = new FormatTest();
		if(args.length==4){
            int ROWS = Integer.parseInt(args[1]);
            int WORDS_EACH_ROW = Integer.parseInt(args[2]);
            if(args[3].equals("untreated")){
                ft.formatTest(args[0],ROWS,WORDS_EACH_ROW,false);
            } else if(args[3].equals("treated")){
                System.out.println("treated");
                ft.formatTest(args[0],ROWS,WORDS_EACH_ROW,true);
            }     
        }
	
	}
	
}
