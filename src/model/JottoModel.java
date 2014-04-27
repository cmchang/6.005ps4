package model;
import java.net.*;
import java.io.*;

/**
 * TODO Write the specification for JottoModel
 */
public class JottoModel {
    private final String puzzleNumber;
    
    public JottoModel(String puzzleNumber){
        this.puzzleNumber = puzzleNumber;
    }
    /**
     * This method receives a client's guess and returns the server's response.
     * 
     * @param guess, the user's guess of what the word is
     * @return the server's response
     * @throws IOException 
     */
    
    public String makeGuess(String guess) throws IOException {
        // Problem 1
        URL oracle = createURLrequest(guess);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream())
        );
        
        String output = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            output += inputLine;
        in.close();
        return output;
    }
    
    /**
     * This method creates the correct URL request using the client's puzzle Number and guess.
     * @param guess, the user's guess of what the word is
     * @return the URL request to send to the sever
     * @throws MalformedURLException
     */
    private URL createURLrequest(String guess) throws MalformedURLException{
        String URLrequest = "http://courses.csail.mit.edu/6.005/jotto.py?puzzle="
                + puzzleNumber 
                + "&guess=" 
                + guess;
        
        return new URL(URLrequest);
    }
}
