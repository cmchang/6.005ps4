package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import model.JottoModel;

/**
 * JottoGUI creates a GUI for the Jotto game.  
 * The client can choose a specific or random puzzle and can make guesses of the 5 letter word.
 * 
 * Threading:
 * 
 * To separate the GUI interactions from the back-end (sending/receiving messages from the server),
 * threading is used.  This is implemented by creating a new thread with a lock for each new guess that is made.
 * A new row is created in the JTable with the corresponding guess that was made.
 * SwingUtilities.invokeLater is then used within the corresponding thread when the JTable needs to be updated 
 * after the server response is received. 
 * 
 * In this implementation, if the client wants a new puzzle, we make sure all the threads have finished running
 * before the puzzle changes.
 * 
 * Thread Safety Argument:
 * 
 * This is thread safe because there is a lock shared for each thread.  For each thread, a unique new JottoModel is 
 * created and is the variables within the thread are not manipulated from outside the thread so there is no rep 
 * exposure.
 * 
 */
public class JottoGUI extends JFrame {
    private final Object lock = new Object();
    private ArrayList<Thread> threads = new ArrayList<Thread>();

    private static final long serialVersionUID = 1L; // required by Serializable

    // components  in the GUI
    private final JButton newPuzzleButton;
    private final JTextField newPuzzleNumber;
    private final JLabel puzzleNumber;
    private final JLabel newGuess;
    private final JTextField guess;
    private final JTable guessTable;
    private final DefaultTableModel tableModel;
    private String currentPuzzleNum;
    
    private final GroupLayout groupLayout;

    /**
     * No arguments necessary for this constructor.  
     * 
     * A GUI is produced containing a randomized puzzle number (0 to 10,000).  The client can
     * choose to play a new game (random or input a new number) or make a guess at what the 
     * word is (by entering the word in the text field and hitting 'enter').
     */
    public JottoGUI() {
        currentPuzzleNum = CreateRandomPuzzleNum();
        
        // components must be named with "setName" as specified in the problem set
        // remember to use these objects in your GUI!
        newPuzzleButton = new JButton();
        newPuzzleButton.setName("newPuzzleButton");
        newPuzzleButton.setText("New Puzzle");
        newPuzzleButton.addActionListener(new NewPuzzleButtonListener());
        
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        newPuzzleNumber.setSize(200, 40);
        newPuzzleNumber.addActionListener(new NewPuzzleListener());
        
        puzzleNumber = new JLabel();
        puzzleNumber.setName("puzzleNumber");
        puzzleNumber.setText("Puzzle #" + currentPuzzleNum);
        
        newGuess = new JLabel();
        newGuess.setText("Type your guess here: ");
        
        guess = new JTextField();
        guess.setName("guess");
        guess.addActionListener(new GuessListener());
        
        guessTable = new JTable(new DefaultTableModel(new Object[] {"Guess","Common Letters","Correct Position"},0));
        guessTable.setName("guessTable");
        tableModel = (DefaultTableModel) guessTable.getModel();
        
        this.setSize(300,300);
        
        /**
         * Using the following as reference for the groupLayout
         * http://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
         */
        
        Container container = getContentPane();
        groupLayout = new GroupLayout(container);
        container.setLayout(groupLayout);
        
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                    .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(puzzleNumber)
                            .addComponent(newPuzzleButton)
                            .addComponent(newPuzzleNumber))
                    .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(newGuess)
                            .addComponent(guess))
                    .addComponent(guessTable)
            );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(puzzleNumber)
                            .addComponent(newPuzzleButton)
                            .addComponent(newPuzzleNumber))
                    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(newGuess)
                            .addComponent(guess))
                    .addComponent(guessTable)
            );
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    //Creates a random puzzle number from 1 to 10,000
    private String CreateRandomPuzzleNum(){
        //note: nextInt generates num from 0 to x (exclusive)
        int randNum = (new Random()).nextInt(10000)+1;
        return ""+randNum;
    }

    /**
     * This method is called when either the newPuzzleButton is clicked
     * or by the keyboard command "enter" in the newPuzzleNumber text field.
     * 
     * This method creates a randomized puzzle number if no number is given or if the
     * give number is not valid (i.e. in correct range, not a number).
     * This also updates all the GUI components with the new puzzle number and clears the JTable/Guess text field.
     * 
     */
    private void updatePuzzleNumber(){
        //Make sure all the threads for guessWord finished running
        for(Thread thread: threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        guess.setText("");
        //checks if all digits in the string
        boolean inputIsValidNum = newPuzzleNumber.getText().matches("\\d+"); 
        if (newPuzzleNumber.getText().equals("") | !inputIsValidNum) { 
            //if no number is specified or if the string contains non-digit characters, randomize new puzzle num
            currentPuzzleNum = CreateRandomPuzzleNum();
        }else{
            String text = newPuzzleNumber.getText();
            if((text.length() < 5 && !text.equals("0")) || text.equals("10000")){
                //Checks that the given number is in the correct range 0 to 10,000
                currentPuzzleNum = text;
            }else{//the num is not within the valid range
                currentPuzzleNum = CreateRandomPuzzleNum();
            }
        }
        puzzleNumber.setText("Puzzle #" + currentPuzzleNum);
        
        //clear the guess table
        tableModel.setRowCount(0);
    }
    
    /**
     * This method is called when the client is guessing a word in the game.
     * Returns void.
     * 
     * A new JottoModel is created and interacts with the server.  The JTable
     * is updated with the server response and is also displayed in the console.
     */
    private void guessWord(){
        final String input = guess.getText();
        guess.setText(""); // placed early on so that the field clears as quickly as possible
        
        tableModel.addRow(new Object[] {input,"",""});
        final int curRow = tableModel.getRowCount()-1;

        JottoModel JottoModel = new JottoModel(currentPuzzleNum);
        try {
            final String guessResponse = JottoModel.makeGuess(input);
            
            SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                    if(guessResponse.equals("guess 5 5")){
                        tableModel.setValueAt("You win!", curRow, 0);
                        System.out.println("You win!");
                    }else{
                        if(guessResponse.charAt(0) == 'g'){ //message will be "guess x y"
                            String[] responseWords = guessResponse.split(" ");
                            tableModel.setValueAt(responseWords[1], curRow, 1);
                            tableModel.setValueAt(responseWords[2], curRow, 2);
                        }else{
                            tableModel.setValueAt("Invalid guess.", curRow, 1);
                        }
                        System.out.println(guessResponse);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Adds a listener for the newPuzzleButton.
     * When the button is clicked, it puzzle number is updated.
     */
    private class NewPuzzleButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            updatePuzzleNumber();
        }
    }
    
    /**
     * Adds a listener for the newPuzzleNumber text field.
     * If "enter" is clicked by the keyboard while interacting with the text field, the puzzle number is updated. 
     */
    private class NewPuzzleListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            updatePuzzleNumber();
        }
    }

    /**
     * Adds a listener for the guess text field.
     * If "enter" is clicked by the keyboard while interacting with the text field, the typed in guess if made. 
     *
     */
    private class GuessListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            Thread thread;
            synchronized (lock) {
                thread = new Thread() {
                    public void run() { 
                        guessWord(); 
                        }
                };
                threads.add(thread);
            }
            thread.start();
        }
    }
    
    /**
     * Start the GUI Jotto client.
     * @param args unused
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JottoGUI main = new JottoGUI();

                main.setVisible(true);
            }
        });
    }
}
