import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class ReadInput {
    private BufferedReader sc;
    String [] a = {"-1jullebulle"};
    public ReadInput(){}
	
    public String[] getLine() {
        try {
            for(String line; (line=sc.readLine())!=null;) {
                String[] dirtyArr = line.trim().toLowerCase().split(" ");
                for (int i = 0; i < dirtyArr.length; i++) {
                    dirtyArr[i] = dirtyArr[i].replaceAll("[^a-z\\-]","");
                }
                return dirtyArr;
            }
        } catch(IOException e) {
			
        }
        return a;
    }
	
	
    public String[] getLineFromDic() {
        try {
            String line = null;
            while((line=sc.readLine()) != null) {
                String[] toReturn = {line.toLowerCase()};
                if(!(toReturn[0].matches("[a-z]+"))) {
                    toReturn[0] = "";
                }
				
                return toReturn;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return a;
    }
	
    public void openReader(String s) {
        try {
            sc = new BufferedReader(new InputStreamReader(new FileInputStream(s), "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		

    }
    public void closeReader() {
        try {
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
	

