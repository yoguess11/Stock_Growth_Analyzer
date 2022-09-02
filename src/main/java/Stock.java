import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Stock {
    private double historicalPrice; //price for the historicalDate
    private String symbol; // stock symbol
    private double currentPrice;
    private String historicalDate; //historicalDate for which price is being requested Formatted as YYYYMMDD
    private double shares;
    private double amountInvested;
    private static String beforeSymbol = "https://cloud.iexapis.com/stable/stock/";
    private static String afterSymbolB4Date = "/chart/date/";
    private static String afterDate = "?chartByDay=true&token=pk_30cbc842f3374c48b8023536eb732042";


    public Stock(String symbol, String historicalDate, double amountInvested) {
        /*
        Sets up a a parameter constructor that gives value to each of the instance variables
         */
        this.symbol = symbol;
        this.historicalDate = historicalDate;
        this.amountInvested = amountInvested;
        this.historicalPrice = getHistoricalPrice();
        this.currentPrice = getCurrentPrice();
        this.shares = getShares(this.amountInvested);
    }

    public JSONObject parseToJSONObject(String url)
    {
        /*
        Given a file of format [ {"key": "value", "key":"value",... } ], parse it so that the inner JSONObject is returned as JSONObject
         */
        try {
            // Set up 'GET' to get the information from the server using HTTP Connection
            URL link = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.connect();
            StringBuilder json_text = new StringBuilder();

            //Error if we do not get response code 200
            if (conn.getResponseCode() != 200)
            {
                throw new RuntimeException("HTTP Response Code "+conn.getResponseCode());
            }
            else
            {
                //read json from the website URL (API's json file)
                Scanner scanner = new Scanner(link.openStream());
                while (scanner.hasNext())
                {
                    json_text.append(scanner.nextLine());
                }
                scanner.close();
            }
            // Now the file is in [{"key": "value"}] format for our particular case since we're focused on one specific info: hist.Prices
            //So, we parse accordingly:
            JSONArray arr = new JSONArray(String.valueOf(json_text));
            return arr.getJSONObject(0); //this is the json object at index 0 since we only have 1 object

        } catch (Exception e) {
            System.out.println("PROBLEMATIC STOCK: "+this.symbol);
            System.out.println("SOME ERROR OCCURED. ENSURE THE STOCK MARKET IS OPEN ON GIVEN DATE. ENSURE THE DATE IS IN VALID FORMAT (YYYYMMDD). Verify your Input.");
            System.out.println("Ensure the stock is released on this date. Anticipate potential errors on this date and time for the particular stock");
            e.printStackTrace();

        }
        return null;
    }

    public JSONObject parseTextToJSONObject(String url) {
        /*
        Given a file of format {"key": {"key": "value", "key":"value",... } }, parse it so that the inner JSONObject is returned as JSONObject
         */
            try {
                // Set up 'GET' to get the information from the server using HTTP Connection
                URL link = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) link.openConnection();
                conn.connect();
                StringBuilder json_text = new StringBuilder();

                //Error if we do not get response code 200
                if (conn.getResponseCode() != 200)
                {
                    throw new RuntimeException("HTTP Response Code "+conn.getResponseCode());
                }
                else
                {
                    //read json from the website URL (API's json file)
                    Scanner scanner = new Scanner(link.openStream());
                    while (scanner.hasNext())
                    {
                        json_text.append(scanner.nextLine());
                    }
                    scanner.close();
                }
                // Now the file is in {"key": {"key": "value"}, "key2": {"key3": "value"}} format for our particular case since we're focused on one specific info: hist.Prices

                //Return the dictionary of key to JSONObject mappings
                JSONObject obj = new JSONObject(String.valueOf(json_text));
                return obj;

            } catch (Exception e) {
                System.out.println("PROBLEMATIC STOCK: "+this.symbol);
                System.out.println("SOME ERROR OCCURED. ENSURE THE STOCK MARKET IS OPEN ON GIVEN DATE. ENSURE THE DATE IS IN VALID FORMAT (YYYYMMDD). Verify your Input.");
                System.out.println("Ensure the stock is released on this date. Anticipate potential errors on this date and time for the particular stock");
                e.printStackTrace();
            }
            return null;
    }

    public double getHistoricalPrice()
    {
        /*
        The historical price is the average of the high and low of the day for that particular date
         */

        //Below is also the url for historical price
        JSONObject obj = parseToJSONObject(beforeSymbol+this.symbol+afterSymbolB4Date+this.historicalDate+afterDate);
        double high = obj.getDouble("high");
        double low = obj.getDouble("low");
        return ((high + low) / 2);
    }

    public double getShares(double amountInvested) {
        /*
        Get the number of shares you own (double) given the money you put into it during the historical date
         */
        return amountInvested / this.historicalPrice;
    }

    public double getCurrentPrice() {
        /*
        Get the current price of the stock from the API
         */
        JSONObject obj = parseTextToJSONObject(beforeSymbol+this.symbol+"/book?token=pk_30cbc842f3374c48b8023536eb732042");
        return obj.getJSONObject("quote").getDouble("latestPrice");
    }

    public double getCurrentValue() {
        /*
        Get the value of the investment at the current time
         */
        return this.currentPrice * this.shares;
    }

    public double getNetValue() {
        /*
        Calculate the change in investment value from historical Date to today
         */
        return getCurrentValue() - this.amountInvested;
    }

    public int getPercentChange() {
        /*
        Calculate the percent change from investment date to today on the investment value
         */
        return (int)((getNetValue() / this.amountInvested) * 100);
    }

    public double getAmountInvested() {
        /*
        Getter for amountInvested (private double)
         */
        return amountInvested;
    }

    @Override
    public String toString() {
        /*
        Change to your own toString method for sharing the all the above information neatly about the given stock
         */
        return "You own " + String.format("%.2f",shares) + " shares of "+symbol.toUpperCase()
                + ", which you bought for $" + String.format("%.2f",historicalPrice)
                + " in " + historicalDate + ". Today, your value is $" + String.format("%.2f",getCurrentValue())
                + ". Net Change: $"+String.format("%.2f",getNetValue())+ " ("+getPercentChange()+"%).";
    }
}
