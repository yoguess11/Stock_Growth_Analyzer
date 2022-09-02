import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.util.ArrayList;

public class UI implements ActionListener {
    JRadioButton portfolio_radio;
    JRadioButton stock_radio;
    JButton addStocksButton;
    JButton calculateButton;
    JButton detailedInfoButton;
    JLabel portfolio_stocks_label;
    JLabel portfolio_consists_label;
    JLabel portfolioCalculationDetails;
    JLabel portfolio_buttom_label;
    JFrame frame;
    JLabel stock_label;
    JPanel vertPanel1;
    JButton doneBtn;
    JButton cancelBtn;
    JFrame stockFrame;
    Portfolio portfolio;
    Stock stock;
    ArrayList<JTextField> entryLocations;
    JEditorPane entryError;
    Savings savings;
    JLabel savingsCalculationDetails;
    int minHistoricalDate;
    double sumAmountInvested;
    JEditorPane detailedInfoLabel;

    public UI() {
/*
    One window is set up that, according to the radio button pressed, acts like a stock menu or a portfolio menu
    This is all set up by the radio_button and the details that make independent portions work together

    Constructor is responsible for setting up the window and adding the event listeners into various components of the window
 */
        //Setup the window and the radio buttons
        frame = new JFrame("Portfolio Analysis: Calculating Potential Value");
        //frame.setSize(1600,1600);
        portfolio_radio = new JRadioButton("Portfolio (Multiple Stocks)");
        stock_radio = new JRadioButton("Stock");
        ButtonGroup radio_Btn = new ButtonGroup();
        radio_Btn.add(portfolio_radio);
        radio_Btn.add(stock_radio);

        JPanel verticalPanel = setupAndGetContentTemplate();
        setupWindow(verticalPanel);

        //Add Event Listeners for all buttons/actions requiring it
        stock_radio.addActionListener(this);
        portfolio_radio.addActionListener(this);
        addStocksButton.addActionListener(this);
        calculateButton.addActionListener(this);
        detailedInfoButton.addActionListener(this);


        //customize JFrame settings
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public JPanel setupAndGetContentTemplate() {
        /*
        This method clears the jframe window; then adds two unselected radio buttons to the window...
        @returns the verticalPanel that goes down after each add method applied on it
         */
        //Adding items to a vertical going Container (contentContainer)
        Container contentContainer = frame.getContentPane();
        contentContainer.repaint();
        vertPanel1 = new JPanel();
        vertPanel1.setLayout(new BoxLayout(vertPanel1, BoxLayout.PAGE_AXIS));
        portfolio_radio.setAlignmentX(Component.LEFT_ALIGNMENT);
        stock_radio.setAlignmentX(Component.LEFT_ALIGNMENT);
        vertPanel1.add(portfolio_radio);
        vertPanel1.add(stock_radio);

        contentContainer.add(vertPanel1, BorderLayout.PAGE_START);
        return vertPanel1;
    }

    private void setupWindow(JPanel panel)
    {
        /*
        Sets up the content that occurs when you click the portfolio radio button:
        sets up everything after the (stock and portfolio radio buttons) in the window...
         */

        //Add the JLabel deciding the Portfolio consists of page & other purposes
        Container contentContainer = frame.getContentPane();
        portfolio_radio.setSelected(true);
        portfolio_consists_label = new JLabel("Portfolio Consists Of: ");
        portfolio_stocks_label = new JLabel("");
        stock_label = new JLabel("");

        //Adding the JButtons for different purposes
        addStocksButton = new JButton("Add Stock");
        addStocksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton = new JButton("Calculate Returns");
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        portfolio_consists_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        stock_label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(portfolio_consists_label);
        panel.add(portfolio_stocks_label);
        portfolio_consists_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(30));

        //Button--horizontal JPanel for "Add Stock" and "Calculate" Buttons
        JPanel horzPanel1 = new JPanel();
        horzPanel1.setLayout(new BoxLayout(horzPanel1, BoxLayout.LINE_AXIS));
        horzPanel1.add(addStocksButton);
        horzPanel1.add(calculateButton);

        contentContainer.add(panel, BorderLayout.PAGE_START);
        contentContainer.add(horzPanel1, BorderLayout.CENTER);

        //Last stretch: vertical panels at the buttom of the screen
        JPanel vertPanel2 = new JPanel();
        vertPanel2.setLayout(new BoxLayout(vertPanel2, BoxLayout.PAGE_AXIS));

        //Add Information/Directions after the center buttoms below:
        portfolio_buttom_label = new JLabel("Portfolio Performance:");
        portfolioCalculationDetails = new JLabel("");

        JLabel savings_buttom_label = new JLabel("What if you had a Savings Account?");
        savingsCalculationDetails = new JLabel("");
        detailedInfoButton = new JButton("Detailed Information");
        detailedInfoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailedInfoLabel = new JEditorPane();
        detailedInfoLabel.setContentType("text/html");
        detailedInfoLabel.setEditable(false);
        detailedInfoLabel.setText("");

        vertPanel2.add(portfolio_buttom_label);
        vertPanel2.add(portfolioCalculationDetails);
        vertPanel2.add(Box.createVerticalStrut(20));
        vertPanel2.add(savings_buttom_label);
        vertPanel2.add(savingsCalculationDetails);
        vertPanel2.add(Box.createVerticalStrut(20));
        vertPanel2.add(detailedInfoButton);
        vertPanel2.add(detailedInfoLabel);

        contentContainer.add(vertPanel2, BorderLayout.PAGE_END);
    }

    private void updateRadioButton()
    {
        /*
        updates the page according to the radio button pressed...
        This is important because only one label exists for both portfolio and stocks and only their content is changed according to the radio button
         */
        if (stock_radio.isSelected())
        {
            portfolio_consists_label.setText("STOCK:");
            portfolio_stocks_label.setText("");
            detailedInfoButton.setEnabled(false);
            portfolio_buttom_label.setText("Stock Performance: ");
            portfolioCalculationDetails.setText("");
            savingsCalculationDetails.setText("");
            detailedInfoLabel.setText("");
        }
        else
        {
            portfolio = null;
            portfolio_consists_label.setText("Portfolio Consists Of: ");
            portfolio_buttom_label.setText("Portfolio Performance: ");
            portfolio_stocks_label.setText("");
            detailedInfoButton.setEnabled(true);
            portfolioCalculationDetails.setText("");
            addStocksButton.setEnabled(true);
            savingsCalculationDetails.setText("");
            detailedInfoLabel.setText("");
        }
    }

    public void calculateButtonPressed() {

        //Only calculate if stocks are in the portfolio
        if (!portfolio_stocks_label.getText().equals(""))
        {
            if (stock_radio.isSelected()) {
                portfolioCalculationDetails.setText(stock.toString());
                savingsCalculationDetails.setText(savings.toString());
            } else {
                //Portfolio_radio is selected and we're trying to caluclate for it:
                portfolioCalculationDetails.setText(portfolio.getPortfolioInformation());
                savings = new Savings(sumAmountInvested, 0.08, minHistoricalDate+"");
                savingsCalculationDetails.setText(savings.toString());


            }
        }
        else
        {
            portfolioCalculationDetails.setText("Please Click on 'Add Stocks' and Add Stocks to Calculate Change in Value!");
        }
    }

    public void addStockWindow()
    {
        /*
        Window that pops of when you click of addStockButton: should be two-sided with instructions on left and entrybox on right

        symbol ----
        historical Date ----
        amount Invested ----
        done and cancel buttons
        entryError JEditorPane

         */

        stockFrame = new JFrame("Add Stock");
        stockFrame.setVisible(true);
        stockFrame.setSize(600,400);
        //Didn't set up defaultCloseOperation since b/c it will close the whole program...if you close the stock pop-up
        Container main = stockFrame.getContentPane();

        ArrayList<String> dialogues = new ArrayList<String>();
        //Symbol dialogue
        dialogues.add("<html> "
                + "<h1> SYMBOL: </h1>"
                + "<em> Type the symbol of the company here </em>"
                + "</html>");
        //Date Dialogue
        dialogues.add("<html> "
                + "<h1> HISTORICAL DATE: </h1>"
                + "<em> Enter the date from which you want to compare <br> the price to today (FORMAT: YYYYMMDD) </em>"
                + "</html>");
        //Total invested Dialogue
        dialogues.add("<html> "
                + "<h1> Total Invested: $</h1>"
                + "<em> Type the total money you invested on historical date </em>"
                + "</html>");
        //Stores the entryField text contents in order (3 total currently)
        entryLocations = new ArrayList<>();
        JPanel verticalPane = new JPanel();
        verticalPane.setLayout(new BoxLayout(verticalPane, BoxLayout.PAGE_AXIS));

        //To avoid bugs in repetitive code, I introduced a nice loop that could keep track of everything!
        for (String htmlText: dialogues) {
            JPanel horzPane = new JPanel();
            horzPane.setLayout(new BoxLayout(horzPane, BoxLayout.LINE_AXIS));

            JEditorPane textPane = new JEditorPane();
            textPane.setContentType("text/html");
            textPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            textPane.setText(htmlText);
            textPane.setEditable(false);
            JTextField entryField = new JTextField();
            entryField.setBackground(Color.lightGray);
            entryField.setAlignmentX(Component.RIGHT_ALIGNMENT);
            entryLocations.add(entryField);
            horzPane.add(textPane);
            horzPane.add(entryField);

            verticalPane.add(horzPane);
        }
        JPanel horzPane = new JPanel();
        horzPane.setLayout(new BoxLayout(horzPane, BoxLayout.LINE_AXIS));
        doneBtn = new JButton("DONE");
        cancelBtn = new JButton("CANCEL");
        doneBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        cancelBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        horzPane.add(doneBtn);
        horzPane.add(cancelBtn);
        verticalPane.add(horzPane);

        entryError = new JEditorPane();
        entryError.setContentType("text/html");
        entryError.setEditable(false);
        verticalPane.add(entryError);

        main.add(verticalPane);

        //Add event listeners
        doneBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

    }

    public void handleAddingStockEntry()
    {
        /*
        Given some entries (not verified) in the text fields, handle what should be done when you click the done button
         */

        //Verifying entries
        String symbol = entryLocations.get(0).getText();
        String historicalDate = entryLocations.get(1).getText();
        String amountInvested = entryLocations.get(2).getText();
        double investmentAmount;
        int dateInt;

        //Check symbol
        for (int i = 0; i < symbol.length(); i++)
        {
            if (Character.isDigit(symbol.charAt(i)))
            {
                //There should be no number in a symbol
                entryError.setText("Entry Error. Symbol cannot contain any numbers!");
                return;
            }
        }

        //Check the date and investmentAmount
        try
        {
            dateInt = Integer.parseInt(historicalDate);
            investmentAmount = Double.parseDouble(amountInvested);
            if (historicalDate.length() != 8)
            {
                entryError.setText("Ensure you've formatted date correctly. It should be YYYYMMDD.");
                return;
            }
        } catch (NumberFormatException e)
        {
            entryError.setText("Error parsing historical Date/Investment Amount into integer/double. Ensure there are no letters.");
            return;
        }

        //Now depending on the type of entry, check for entry validity with server
        //If valid, calculate information (stock), or add stock to portfolio
        //If invalid, display a entryError in the current addStock window and do not close the frame (return;)
        if (stock_radio.isSelected())
        {
            try {
                stock = new Stock(symbol, historicalDate, investmentAmount);
                portfolio_stocks_label.setText(symbol.toUpperCase());
                savings = new Savings(investmentAmount, 0.08, historicalDate);
                //since you can only add one stock in stock
                addStocksButton.setEnabled(false);
                stockFrame.setVisible(false);
                stockFrame.dispose();
            } catch (Exception e)
            {
                entryError.setText("Problem with stock: \'"+symbol
                        + "\'. Some error occurred while gathering stock data. Ensure the stock market is open on given date. Ensure the date is in valid format (YYYYMMDD). Verify your input follows the directions given and is of the correct type."
                        + " Ensure the stock is released on this date. Anticipate potential errors on this date and time for the particular stock. API could also be down; please also try again later.");
                return;
            }

        }
        else // ie we're dealing with a portfolio
        {
            //First stock in the portfolio--initialize portfolio and relevant variables
            if (portfolio == null) {
                try {
                    portfolio = new Portfolio(new Stock(symbol, historicalDate, investmentAmount));
                    portfolio_stocks_label.setText(symbol.toUpperCase());
                    minHistoricalDate = dateInt;
                    sumAmountInvested = investmentAmount;

                } catch (Exception e)
                {
                    entryError.setText("Problem with stock: \'"+symbol
                            + "\'. Some error occurred while gathering stock data. Ensure the stock market is open on given date. Ensure the date is in valid format (YYYYMMDD). Verify your input follows the directions given and is of the correct type."
                            + " Ensure the stock is released on this date. Anticipate potential errors on this date and time for the particular stock. API could also be down; please also try again later.");
                    return;
                }
            }
            else //portfolio already exists and we're adding more stock information to the portfolio
            {
                try {
                    portfolio.addStockToPortfolio(new Stock(symbol, historicalDate, investmentAmount));
                    if (dateInt < minHistoricalDate) //update min date if smaller historical date is found
                        minHistoricalDate = dateInt;


                    sumAmountInvested += investmentAmount;
                    portfolio_stocks_label.setText(portfolio_stocks_label.getText() + ", " + symbol.toUpperCase());
                } catch (Exception e)
                {
                    entryError.setText("Problem with stock: \'"+symbol
                            + "\'. Some error occurred while gathering stock data. Ensure the stock market is open on given date. Ensure the date is in valid format (YYYYMMDD). Verify your input follows the directions given and is of the correct type."
                            + " Ensure the stock is released on this date. Anticipate potential errors on this date and time for the particular stock. API could also be down; please also try again later.");
                    return;
                }

            }
            //x out of the current addStock window
            detailedInfoLabel.setText("");
            stockFrame.setVisible(false);
            stockFrame.dispose();



        }
    }

    private void showDetailedInfo() {
        /*
        Handles what clicking 'detailed Information' button does
         */

        //First check whether you have a portfolio or not to show Detailed Information for
        if (portfolio != null)
        {
            detailedInfoLabel.setText("<html><h3>"+portfolio.getDetailedInformation()+"</h3></html>");
        }
        else
        {
            detailedInfoLabel.setText("<html><h3>You must have stocks in your portfolio to see detailed information. <br> Add Stocks to Continue.</h3><html>");

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        Handles the functionality of each of the buttons
         */

        if (e.getSource() == calculateButton) {
            calculateButtonPressed();
        }
        else if (e.getSource() == stock_radio || e.getSource() == portfolio_radio)
        {
            updateRadioButton();
        }
        else if (e.getSource() == addStocksButton)
        {
            addStockWindow();
        }
        else if (e.getSource() == doneBtn)
        {
            handleAddingStockEntry();
        }
        else if (e.getSource() == cancelBtn)
        {
            stockFrame.setVisible(false);
            stockFrame.dispose();
        }
        else if (e.getSource() == detailedInfoButton)
        {
            showDetailedInfo();
        }
    }
}
