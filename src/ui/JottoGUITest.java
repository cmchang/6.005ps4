package ui;

public class JottoGUITest {

    /**
     * My manual testing strategy: 
     * (I successfully did all of the following)
     * 
     * (A) Creating a new puzzle
     *      (A1) By clicking the new puzzle button when the new newPuzzleNumber text field was
     *          (A1.1) empty -> random number was produced
     *          (A1.2) contained a valid number (i.e. 1, 56099 ,99999999999) -> the puzzle number was changed to the indicated number
     *          (A1.3) contained an invalid number (i.e. 0, and negative numbers) -> random number was produced
     *          (A1.4) contained non-number string -> random number was produced
     *      (A2) By hitting "enter" when typing into the newPuzzleNumber text field when the text field was
     *          (A2.1) empty -> random number was produced
     *          (A2.2) contained a valid number -> the puzzle number was changed to the indicated number
     *          (A3.3) contained an invalid number (i.e. 0, and negative numbers) -> random number was produced
     *          (A4.4) contained non-number string -> random number was produced
     *      (A3) When a guess (request to server) isn't completed yet 
     *           -> waits for the server response to complete before updating the puzzle number
     * (B) Creating a guess
     *      (B1) Creating a correct guess -> all of the following displayed "You win" in the JTable
     *          (B1.1) Using puzzle number 16952, guessing "cargo"
     *          (B1.2) Using puzzle number 2015, guessing "rucks"
     *          (B1.3) Using puzzle number 5555, guessing "vapid"
     *          (B1.4) Using puzzle number 8888, guessing "rased"
     *      (B2) Creating an incorrect guess (tried all of the following with multiple random puzzle numbers)
     *          (B2.1) all lower case/all upper case/combination of upper and lower case
     *                 5-letter word IN dictionary-> JTable displayed correct guess response ("guess x y")
     *          (B2.2) all lower case/all upper case/combination of upper and lower case
     *                 5-letter word NOT in dictionary
     *                  -> JTable displayed displayed "Invalid guess"
     *                  (Console displayed "error 2: Invalid guess. Length of guess != 5 or guess is not a dictionary word.")
     *          (B2.3) an ill-formatted guess (i.e. "&*%") -> JTable displayed displayed "Invalid guess"
     *                  (Console displayed "error 0: Ill-formatted request.")
     *      (B3) Slow request guess
     *          (B3.1) One single valid request using "*" (i.e. "hell*")
     *                 -> JTable eventually displayed correct guess response ("guess x y")
     *          (B3.2) Many consecutive valid requests using "*" (i.e. "hell*")
     *                 ->JTable eventually displayed correct corresponding guess responses ("guess x y")
     *          (B3.3) One single valid request using "*" followed by a valid request without using "*"
     *                 ->JTable eventually displayed correct corresponding guess responses ("guess x y")
     *                   It filled out the information for the faster request first and then the slower request later
     */
    
}
