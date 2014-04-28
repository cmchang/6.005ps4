package ui;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * TODO Write the specification for JottoGUI
 */
public class JottoGUI extends JFrame {

    private static final long serialVersionUID = 1L; // required by Serializable

    // components to use in the GUI
    private final JButton newPuzzleButton;
    private final JTextField newPuzzleNumber;
    private final JLabel puzzleNumber;
    private final JTextField guess;
    private final JTable guessTable;
    private int currentPuzzleNum;
    
    private final GroupLayout groupLayout;
    private final JLabel newGuess;

    /**
     * TODO Write the specification for this constructor
     */
    public JottoGUI() {
        currentPuzzleNum = 1;
        
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
        
        guessTable = new JTable();
        guessTable.setName("guessTable");

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
        System.out.println("click");
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
