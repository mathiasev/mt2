import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.io.*;
import java.awt.Desktop;
import java.util.Date;
import java.text.*;

/**
 * Write a description of class WebQuote here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class WebQuote extends QuoteView
{
    // instance variables - replace the example below with your own
    private Quote quote;
        private List<String> lsInvoiceLines = new ArrayList();
        private String html = "<html><table><tr>", preHTML = "<th>Quantity</th><th>Item</th><th>Unit Cost</th><th>Total Cost</th></tr>";
        private String endHTML = "</table></html>";
        private BufferedWriter bw;
        private File file; 
        private String creditCheck;

    /**
     *  WebQuote constructor
     */
   public WebQuote(Quote qQuote) {
       super(qQuote);
       this.quote = qQuote;
    }
    
    public String getHTML() {
        
        html += "<tr><td colspan=\"4\"><h1>Quote for " + quote.client().getName() +"</h1></td></tr>";
        html += preHTML;
        html += tableHTML();
        
         if((quote.client().getCreditLimit()-quote.getInvoiceCost()) > 0) {  
                
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                 String creditCheck = String.format("<tr><td colspan=\"4\">CREDIT CHECK DONE %s</td></td>", dateFormat.format(date));
                 html += creditCheck;
            } else { 
                html += "<tr><td colspan=\"4\">WARNING QUOTATION IS NOT WITHIN CUSTOMERS CREDIT LIMIT </td></tr>";}

            if(quote.service().isManned() && !quote.getOrbit().equals("LEO")) {
                html +="<tr><td colspan=\"4\">Congratulations you receive a free inflatable space suit souvenir pack.</td></tr>";
            }
            
        html +=endHTML;
        System.out.println(html);
        return html;
        
    }
    
    private String tableHTML() {
        String tableHTML = "";
        lsInvoiceLines = new ArrayList();
         try {  
            String sLaunches = String.format("<tr class=\"item\"><td>%,16.2f</td>" +
                                             "<td>%s launches</td><td>$%,16.2f</td>" +
                                             "<td>$%,16.2f</td></tr>",
                                              quote.getLaunches(),
                                              quote.service().getName(),
                                              quote.service().getCost(),
                                              quote.getServiceCost());
            lsInvoiceLines.add(sLaunches);

            if(quote.isNitrogen()) {
                String sNitrogen = String.format("<tr class=\"item\"><td>%,16.2f</td><td>Nitrogen Flushes</td><td>$%,16.2f</td><td>$%,16.2f</td></tr>", quote.getLaunches(), quote.getNitrogenUnitCost() ,quote.getNitrogenCost());
                lsInvoiceLines.add(sNitrogen);
            }

             if(quote.isDiscount()){
                String sDiscount = String.format("<tr class=\"item\"><td></td><td>2 percent discount for more than 5 launches</td><td>-$%,16.2f</td><td></td>", -1*quote.getDiscount());
                lsInvoiceLines.add(sDiscount);
            }
          
            String sGrossCost = String.format("<tr class=\"item\"><td><td></td><td><strong style=\"text-align:right\">Gross Launch Cost</strong></td><td>$%,16.2f</td></tr>", quote.getGrossCost());
              lsInvoiceLines.add(sGrossCost);
                        
            if(quote.isTax()){  
               String sTax = String.format("<tr class=\"item\"><td><td></td><td><strong style=\"text-align:right\">Tax</strong></td><td>$%,16.2f</td></tr>", quote.getTax());
              lsInvoiceLines.add(sTax);
            }

               String sNettLaunch = String.format("<tr class=\"item\"><td><td></td><td><strong style=\"text-align:right\">Nett Launch Cost</strong></td><td>$%,16.2f</td></tr>", quote.getNettCost());
              lsInvoiceLines.add(sNettLaunch);
              
            
           
            
            if(quote.isInsurance()) {
                String sInsurance = String.format("<tr class=\"item\"><td>1</td><td>Insurance for payload</td><td></td><td>$%,16.2f</td></tr>", quote.getInsuranceCost());
                lsInvoiceLines.add(sInsurance);
            }
           

            if (quote.isNESA()){  
                String sNESA = String.format("<tr class=\"item\"><td></td><td></td><td><strong style=\"text-align:right\">NESA tax for GTO</strong></td><td>$%,16.2f</td></tr>",quote.getNESACost());
                lsInvoiceLines.add(sNESA);
            }
            

               String sTotal = String.format("<tr class=\"item last\"><td colspan=\"4\"></td></tr><tr><td></td><td></td><td><strong style=\"text-align:right\">Total Amount Due</strong></td><td>$%,16.2f</td></tr>",quote.getInvoiceCost());
               lsInvoiceLines.add(sTotal);
            }
        catch (Exception e) {
              e.printStackTrace();
                        //canvas.drawString("Error printing quote. Please try again.", 30, 60);
        }
        

        
        for (String sLineItem : lsInvoiceLines) {
           tableHTML +=sLineItem;
        }
        
        return tableHTML;
    }
    
    public void saveHTML() {
        
            try {
                file = new File(String.format("%s-quote.html", quote.client().getName().replaceAll(" ","")));

	 /* This logic will make sure that the file 
	  * gets created if it is not present at the
	  * specified location*/
	  if (file.createNewFile())
	  {
	      System.out.println("file created");
	  }
	  else { System.out.println("file exists"); }

	  FileWriter fw = new FileWriter(file);
	  bw = new BufferedWriter(fw); 
	  
 if((quote.client().getCreditLimit()-quote.getInvoiceCost()) > 0) {  
                
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                  creditCheck = String.format("Credit check processed on %s", dateFormat.format(date));
                 //lsInvoiceLines.add(creditCheck);
            } else { 
                creditCheck = "WARNING: QUOTATION IS NOT WITHIN CUSTOMERS CREDIT LIMIT ";}

                
                  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                
        String html = String.format("<html><style>.invoice-box{max-width: 800px; margin: auto; padding: 30px; border: 1px solid #eee; box-shadow: 0 0 10px rgba(0, 0, 0, .15); font-size: 16px; line-height: 24px; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; color: #555;}.invoice-box table{width: 100%%; line-height: inherit; text-align: left;}.invoice-box table td{padding: 5px; vertical-align: top;}.invoice-box table tr td:nth-child(2){text-align: right;}.invoice-box table tr.top table td{padding-bottom: 20px;}.invoice-box table tr.top table td.title{font-size: 45px; line-height: 45px; color: #333;}.invoice-box table tr.information table td{padding-bottom: 40px;}.invoice-box table tr.heading td{background: #eee; border-bottom: 1px solid #ddd; font-weight: bold;}.invoice-box table tr.details td{padding-bottom: 20px;}.invoice-box table tr.item td{border-bottom: 1px solid #eee;}.invoice-box table tr.item.last td{border-bottom: none;}.invoice-box table tr.total td:nth-child(2){border-top: 2px solid #eee; font-weight: bold;}@media only screen and (max-width: 600px){.invoice-box table tr.top table td{width: 100%%; display: block; text-align: center;}.invoice-box table tr.information table td{width: 100%%; display: block; text-align: center</style><div class=\"invoice-box\"> <table cellpadding=\"0\" cellspacing=\"0\"> <tr class=\"top\"> <td colspan=\"4\"> <table> <tr> <td class=\"title\"> <img src=\"https://i.imgur.com/8RYFYsS.gif\" style=\"width:100%%; max-width:300px;\"> </td><td> Invoice #: 123<br>Created: %s</td></tr></table> </td></tr><tr class=\"information\"> <td colspan=\"4\"> <table> <tr> <td><strong>Space Y</strong></td><td>%s<br>%s</td></tr></table> </td></tr><tr class=\"heading\"> <td colspan=\"4\"> Credit Check </td></tr><tr class=\"details\"> <td colspan=\"4\"> %s </td></tr><tr class=\"heading\"> <td> Qty </td><td> Item </td><td>Price</td><td>Total</td></tr>%s</table> </div></html>",
            dateFormat.format(date),
            quote.client().getName(),
            quote.client().getTitle(),
            creditCheck,
            tableHTML());	  
	  bw.write(html);
     bw.close();
     JOptionPane.showMessageDialog(null, "File Saved");
     Desktop.getDesktop().browse(file.toURI());
    }
    catch (IOException e) {
       e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving file.");
        }
        }
}
