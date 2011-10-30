package org.wintirsstech.memorygame;

/**********************************************************************
 * Vic's version 12.2
 * August 18, 2011
 **********************************************************************/
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MemoryGame implements ActionListener, Runnable
{
    private Random randomNumberGenerator;
    private JFrame mainWindow;
    private JButton[] buttons;
    private ImageIcon coverIcon;
    private ArrayList<Integer> twentyNumberList;
    private ArrayList<Integer> fiftyTwoNumberList;
    private URL cardCoverImageAddress;
    private URL cardImageAddress;
    private int numberOfPairsExamined = 0;
    private int numberOfCorrectCardPairMatches = 0;
    private int turnedOverCardCount = 0;
    private int buttonNumber;
    private ImageIcon selectedIcon;
    private int firstCardNumber;
    private int secondCardNumber;
    private JButton selectedButton;
    private JButton firstButtonSelected;
    private JButton secondButtonSelected;

    public static void main(String[] args)
    {
        new MemoryGame().getGoing();
    }

    private void getGoing()
    {
        /**************************************************************
         * Set up window with buttons
         **************************************************************/
        mainWindow = new JFrame("Simple Memory Game");
        buttons = new JButton[20];
        twentyNumberList = new ArrayList<Integer>();
        fiftyTwoNumberList = new ArrayList<Integer>();
        randomNumberGenerator = new Random();
        cardCoverImageAddress = getClass().getResource("images/CardCover.jpg");
        coverIcon = new ImageIcon(cardCoverImageAddress);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(800, 800);
        mainWindow.setBackground(Color.DARK_GRAY);
        mainWindow.setLayout(new GridLayout(4, 5, 5, 5)); // Create a layout with rows = 4, 5 pixel gaps.
        /**************************************************************
         * Fill fiftyTwoList with the integers 0 - 51
         **************************************************************/
        for (int i = 0; i < 52; i++)
        {
            fiftyTwoNumberList.add(i);
        }

        /**********************************************************************************
         * Fill the twentyNumberList array with 10 pairs of random numbers
         **********************************************************************************/
        for (int i = 0; i < 10; i++)
        {
            int randomNumber = randomNumberGenerator.nextInt(fiftyTwoNumberList.size());//Because the list is getting smaller
            twentyNumberList.add(randomNumber);
            twentyNumberList.add(randomNumber);
            fiftyTwoNumberList.remove(randomNumber);//Removes the number from the list, if it exists.  Finds the number...magic!
        }
        knuthShuffleMethod();

        /**********************************************************************************
         * Make an array of buttons and add them to the JFrame; add button listeners
         **********************************************************************************/
        for (int i = 0; i < 20; i++)  // Create the array of 20 buttons; add to JFrame.
        {
            buttons[i] = new JButton(coverIcon);  // Fill array with buttons
            buttons[i].setMnemonic(twentyNumberList.get(i)); //Add ident number to button in array
            buttons[i].addActionListener(this); // Add The Action Listener To The Buttons in array
            mainWindow.add(buttons[i]);//Add buttons to JFrame
        }
        mainWindow.setVisible(true);
    }

    public void knuthShuffleMethod()
    {
        for (int i = 0; i < 20; i++)
        {
            int topNumber = twentyNumberList.get(i);//top number
            int tempRandomNumber = i + randomNumberGenerator.nextInt(20 - i);//random number
            int ranStore = twentyNumberList.get(tempRandomNumber);
            twentyNumberList.set(tempRandomNumber, topNumber);
            twentyNumberList.set(i, ranStore);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        turnedOverCardCount++;
        selectedButton = (JButton)e.getSource();
        buttonNumber = selectedButton.getMnemonic(); //Get button number
        cardImageAddress = getClass().getResource("images/Card" + buttonNumber + ".jpg");
        selectedIcon = new ImageIcon(cardImageAddress);
        selectedButton.setIcon(selectedIcon);
        if (turnedOverCardCount == 1) // First card turned over
        {
            firstButtonSelected = selectedButton;
            firstCardNumber = buttonNumber;
        }
        if (turnedOverCardCount == 2)//Second card turned over
        {
            numberOfPairsExamined++;
            turnedOverCardCount = 0;
            secondButtonSelected = selectedButton;
            secondCardNumber = buttonNumber;
            if (firstCardNumber == secondCardNumber)
            {
                numberOfCorrectCardPairMatches++;
                if (numberOfCorrectCardPairMatches == 10) // All pairs are found then game is over
                {
                    mainWindow.setTitle("Congratulations you won in " + numberOfPairsExamined + " tries");
                }
            }
            if (firstCardNumber != secondCardNumber)
            {
                mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                SwingUtilities.invokeLater(this); // Causes call to run() to let game logic run first and hold off graphics stuff until after game logic is finished.
            }
        }
    }

    public void run() // Runs after all painting done because of SwingUtilities.invokeLater(this).
    {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException ex)
        {
            //Do nothing
        }
        firstButtonSelected.setIcon(coverIcon);
        secondButtonSelected.setIcon(coverIcon);
        mainWindow.setCursor(Cursor.getDefaultCursor());
    }
}
