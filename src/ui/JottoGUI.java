package ui;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.table.DefaultTableModel;

import model.JottoModel;

/**
 * TODO Write the specification for JottoGUI
 */
public class JottoGUI extends JFrame {
    private final Object lock = new Object();
    private ArrayList<Thread> threads = new ArrayList<Thread>();

    private static final long serialVersionUID = 1L; // required by Serializable

    // components to use in the GUI
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
     * TODO Write the specification for this constructor
     */
    public JottoGUI() {
        currentPuzzleNum = CreateRandomPuzzleNum();
        
        // components must be named with "setName" as specified in the problem set
        // remember to use these objects in your GUI!
        newPuzzleButton = new JButton();
        newPuzzleButton.setName("newPuzzleButton");
        newPuzzleButton.setText("New Puzzle");
        newPuzzleButton.addMouseListener(new NewPuzzleButtonListener());
        
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        newPuzzleNumber.setSize(200, 40);
        
        puzzleNumber = new JLabel();
        puzzleNumber.setName("puzzleNumber");
        puzzleNumber.setText("Puzzle #" + currentPuzzleNum);
        
        newGuess = new JLabel();
        newGuess.setText("Type your guess here: ");
        
        guess = new JTextField();
        guess.setName("guess");
        guess.addKeyListener(new GuessListener());
        
        guessTable = new JTable(new DefaultTableModel(new Object[] {"Guess","Common Letters","Correct Position"},0));
        guessTable.setName("guessTable");
        tableModel = (DefaultTableModel) guessTable.getModel();
        // TODO Problems 2, 3, 4, and 5
        
        this.setSize(300,300);
        
        /**
         * Using the following as reference
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
    }
    
    //Creates a random puzzle number from 1 to 10,000
    private String CreateRandomPuzzleNum(){
        //note: nextInt generates num from 0 to x (exclusive)
        int randNum = (new Random()).nextInt(10000)+1;
        return ""+randNum;
    }

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
        boolean inputIsValidNum = newPuzzleNumber.getText().matches("\\d+");
        if (newPuzzleNumber.getText().equals("") | !inputIsValidNum) {
            currentPuzzleNum = CreateRandomPuzzleNum();
        }else{
            String text = newPuzzleNumber.getText();
            if((text.length() < 5 && !text.equals("0")) || text.equals("10000")){
                currentPuzzleNum = text;
            }else{//the int is not within the valid range 0 to 10,000
                currentPuzzleNum = CreateRandomPuzzleNum();
            }
        }
        puzzleNumber.setText("Puzzle #" + currentPuzzleNum);
        
        //clear the guess table
        tableModel.setRowCount(0);
    }
    
    private void guessWord(){
        String input = guess.getText();
        guess.setText(""); // placed early on so that the field clears as quickly as possible

        JottoModel JottoModel = new JottoModel(currentPuzzleNum);
        tableModel.addRow(new Object[] {input,"",""});
        int curRow = tableModel.getRowCount()-1;
        try {
            String guessResponse = JottoModel.makeGuess(input);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class NewPuzzleButtonListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            updatePuzzleNumber();
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    
    private class GuessListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER){
                Thread thread;
                synchronized (lock) {
                    thread = new Thread() {
                        public void run() { guessWord(); }
                    };
                    threads.add(thread);
                }
                thread.start();
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
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
