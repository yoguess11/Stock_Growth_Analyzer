import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Savings {
    private double amountInvested;
    private double interestRate;
    private String investmentDate;

    public Savings(double amountInvested, double interestRate, String investmentDate) {
        /*
        amountInvested is a double representing the amountInvested on investmentDate
        interest rate is the APY, the rate at which accumulated savings are compounded annually (compound interest)
        investmentDate must be in YYYYMMDD format, where Y=year (4 digits), M=month (2 digits), D=Day (2 digits); this is to stay
            consistent with the format required for the IEXCloud API that deals with the historical price of stocks.

         */
        this.amountInvested = amountInvested;
        this.interestRate = interestRate;
        this.investmentDate = investmentDate;
    }

    public double getCurrentValue() {
        /*
        Current value is calculated by calculating the compounded interest since yearOfInvestment
         */
        //Get first 4 digits of investmentDate to get the year we invested in
        int year = Integer.parseInt(investmentDate.substring(0,4));
        int month = Integer.parseInt(investmentDate.substring(4,6));
        int day = Integer.parseInt(investmentDate.substring(6,8));

        Calendar current = new GregorianCalendar();
        //Note that we subtract one from the month since Gregorian Calendary measures months starting from 0, so...
        Calendar historical = new GregorianCalendar(year, month-1, day);

        //Calculate the difference in years
        int yearsDiff = current.get(GregorianCalendar.YEAR) - historical.get(GregorianCalendar.YEAR);
        //Check if month is valid for yearsDiff:

        if ((historical.get(GregorianCalendar.MONTH) > current.get(GregorianCalendar.MONTH)) ||
        ((historical.get(GregorianCalendar.MONTH) == current.get(GregorianCalendar.MONTH)) && (historical.get(Calendar.DATE) > current.get(Calendar.DATE))))
        {
            yearsDiff += -1; //b/c you've not completed a year yet...
        }

        //Use the compound interest amount formula to get the final amount after yearsDiff
        double amount = amountInvested * Math.pow(1+interestRate, yearsDiff);
        return amount;

    }
    @Override
    public String toString() {
        /*
        When you print this object, change what shows to this toString from the default uninformative memory location of the object
         */
        return "Investing "+amountInvested+" since "+investmentDate+" at "+interestRate*100+"% interest (typical savings interest) will yield $"+String.format("%.2f",getCurrentValue())+" today.";
    }
}
