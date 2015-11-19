import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class SaveObject implements Serializable {
    /**
     * save the trie in an object file
     * @param trie
     */
    public void saveTrie(Trie trie){
		
        try {
            FileOutputStream fout = new FileOutputStream("triemodel");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            System.err.println("Trie saved!");
            oos.writeObject(trie);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * load the trie
     * @return
     */
    public Trie loadTrie() {
        FileInputStream saveFiled;
        Trie trie=null;
        try {
            saveFiled = new FileInputStream("triemodel");
            ObjectInputStream restore;
            restore = new ObjectInputStream(saveFiled);
            trie = (Trie) restore.readObject();
            System.err.println("Trie is loaded!");
            saveFiled.close();
            return trie;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return trie;
    }
	
    public Bigram loadBiGram(){
        FileInputStream saveFiled;
        Bigram bigram=null;
        try {
            saveFiled = new FileInputStream("bigrammodel");
            ObjectInputStream restore;
            restore = new ObjectInputStream(saveFiled);
            bigram = (Bigram) restore.readObject();
            System.err.println("bigram loaded!");
            saveFiled.close();
            return bigram;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bigram;
    }
    /**
     * save the bigram
     * @param bigram
     */
    public void saveBigram(Bigram bigram){
        try {
            FileOutputStream fout = new FileOutputStream("bigrammodel");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            System.err.println("Bigram saved!");
            oos.writeObject(bigram);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
