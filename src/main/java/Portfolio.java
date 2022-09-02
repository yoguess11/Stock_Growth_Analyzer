import java.util.ArrayList;

public class Portfolio {
    /* What's a portfolio: it's a collection of stocks
        This has its own net return and percent change method that depends on the sum of the stocks...
        That's what I'm going to build here as well:
     */
    private ArrayList<Stock> stocks;

    public Portfolio(Stock stock) {
        /*
        There's a reason I chose to add a stock as opposed to an arraylist of stocks
        This arraylist could lead to some kind of an aliasing problem if the arraylist is changed/modified after giving inputs here
        So, I believe just giving it a stock is a very functionable and a great approach!
         */
        stocks = new ArrayList<>();
        stocks.add(stock);
    }

    public void addStockToPortfolio(Stock s) {
        /*
        Add Stock s to the portfolio
         */
        this.stocks.add(s);
    }

    public String getPortfolioInformation() {
        /*
        Get the total amount of Investment made in the portfolio (sum of all the stocks in the portfolio)
        Also get the current value (the current vaue of all the stocks in the portfolio added together
         */
        double amountInvested = 0;
        double currentValue = 0;
        for (Stock s: stocks)
        {
            amountInvested += s.getAmountInvested();
            currentValue += s.getCurrentValue();
        }

        String s = "Your total amount invested in the given date is $"+String.format("%.2f", amountInvested)
                    + ". Currently, the value of that investment is $"+String.format("%.2f", currentValue)
                    + ".\nThe net change is $"+String.format("%.2f", currentValue - amountInvested) + " ("+(int)((currentValue-amountInvested)*100/amountInvested)+"%).";
        return s;
    }

    public String getDetailedInformation() {
        /*
        Show some more detailed information than getPortfolioInformation method.
        Mainly get the investment amount, net change and percent change for individual stocks in the portfolio as well!
         */
        String s = "";

        for (Stock stock: stocks)
        {
            s += "\n";
            s += stock.toString();
        }
        s += "\n";
        s += getPortfolioInformation();
        return s;
    }

}
