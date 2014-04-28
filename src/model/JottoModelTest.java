package model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
public class JottoModelTest {
    private final JottoModel JottoModel = new JottoModel("6005");
    
    /**
     * I will be testing JottoModel with the following partition space:
     * (A) A correct request to the server
     *      (A1) Guess does not contain asterix (*) character
     *      (A2) Guess contains asterix (*) character
     * (B) An incorrect request to the server
     *      (B1) Ill-formatted request
     *      (B2) Non-number puzzle ID
     *      (B3) Invalid guess - length of guess != 5
     *      (B4) Invalid guess - guess is not in the dictionary
     * @throws IOException 
     *      
     */
    
    @Test // A1
    public void testCorrectRequestNoAsterix() throws IOException {
        String expectedAns = "guess 0 0";
        String response = JottoModel.makeGuess("hello");
        assertEquals(response, expectedAns);
    }
    
    @Test // A2
    public void testCorrectRequestAsterix() throws IOException {
        String expectedAns = "guess 0 0";
        String response = JottoModel.makeGuess("h*llo");
        assertEquals(response, expectedAns);
    }
    
    @Test // B1
    public void testIllFormatedRequest() throws IOException {
        String expectedAns = "error 0: Ill-formatted request.";
        String response = JottoModel.makeGuess("###");
        assertEquals(response, expectedAns);
    }
    @Test // B2
    public void testNonNumberPuzzleId() throws IOException {
        String expectedAns = "error 1: Non-number puzzle ID.";
        JottoModel JottoModel2 = new JottoModel("0");
        String response = JottoModel2.makeGuess("hello");
        assertEquals(response, expectedAns);
    }
    
    @Test // B3
    public void testInvalidLengthGuess() throws IOException {
        String expectedAns = "error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.";
        String response = JottoModel.makeGuess("helloo");
        assertEquals(response, expectedAns);
    }
    
    @Test // B4
    public void testWordNotInDictionary() throws IOException {
        String expectedAns = "error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.";
        String response = JottoModel.makeGuess("asdfg");
        assertEquals(response, expectedAns);
    }
}
