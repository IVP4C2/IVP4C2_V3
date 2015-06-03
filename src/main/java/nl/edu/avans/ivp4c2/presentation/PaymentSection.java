package nl.edu.avans.ivp4c2.presentation;

import nl.edu.avans.ivp4c2.domain.Payment;
import nl.edu.avans.ivp4c2.domain.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterIOException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all the operations regarding payments
 * @author IVP4C2
 */
public class PaymentSection {
    private JPanel paymentPanel;
    private JButton printBill;
    private JPanel panelSouth;
    private JPanel southMid;
    private JPanel southRight;
    private Payment payment;
    private static final String EURO = "\u0080";

    public PaymentSection() {
        printBill = new JButton("Print Bon");
        printBill.setBackground(Color.decode("#DFDFDF"));
        printBill.addActionListener(new PrintBillHandler());
        panelSouth = new JPanel(new GridLayout(1, 3));
        southMid = new JPanel(new BorderLayout());
        southRight = new JPanel(new GridLayout(2, 1));
        panelSouth.setBackground(Color.decode("#DFDFDF"));
        southMid.setBackground(Color.decode("#DFDFDF"));
        southRight.setBackground(Color.decode("#DFDFDF"));
    }


    /**
     * Creates a JPanel with information extracted from a given Payment object
     * @param payment
     * @return JPanel with Payment information
     */
    public JPanel getPaymentPanel(Payment payment) {
        this.paymentPanel = new JPanel();
        this.payment = payment;
        paymentPanel.setLayout(new BorderLayout());
        paymentPanel.add(new JScrollPane(new JTable(buildTableModel(payment))), BorderLayout.CENTER);
        southRight.removeAll();
        southMid.add(new JLabel("Totaal Prijs:         "), BorderLayout.EAST);
        southRight.add(new JLabel("Incl btw: " + round(payment.getTotalPrice(), 2)));
        southRight.add(new JLabel("Excl btw: " + round(payment.getTotalPriceExcl(), 2)));

        panelSouth.add(printBill);
        panelSouth.add(southMid);
        panelSouth.add(southRight);
        paymentPanel.add(panelSouth, BorderLayout.SOUTH);
        return paymentPanel;
    }

    // Method to create JTable
    public DefaultTableModel buildTableModel(Payment payment) {

        // Gets column names from Table
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("ProductNr");
        columnNames.add("ProductNaam");
        columnNames.add("Prijs Exlc btw");
        columnNames.add("Prijs Incl btw");
        columnNames.add("Aantal");
        columnNames.add("Totaal");

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();

        for (Product p : payment.getProductList()) {
            Vector<Object> vector = new Vector<Object>();
            vector.add(p.getProductNumber());
            vector.add(p.getProductName());
            vector.add(round(p.getPrice(), 2));
            vector.add(round((p.getPrice() * (p.getBtw() + 100) / 100), 2)+ " ("+p.getBtw()+"%)");
            vector.add(p.getAmount());
            vector.add(round(p.getAmount() * (p.getPrice() * (p.getBtw() + 100) / 100), 2));
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    /**
     * Called when printBill is clicked.
     */
    class PrintBillHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                GenerateBill(payment);
            } catch (Exception ex) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.SEVERE, "an exception was thrown in the PaymentSection", ex);
            }
        }
    }


    /**
     * Creates a PDF file using a given Payment object.
     * Gives to option to print the PDF.
     * @param p
     * @throws Exception
     */
    public void GenerateBill(Payment p) throws Exception {
        //Create a new document
        PDDocument document = new PDDocument();
        //Add a new page
        PDPage page = new PDPage();
        document.addPage(page);

        //Create a font
        PDFont font1 = PDType1Font.HELVETICA;
        PDFont font2 = PDType1Font.HELVETICA_BOLD;


        InputStream inputStream = new FileInputStream(new File("src/main/resources/logo_bill.jpg"));

        //Create logo
        PDJpeg logo = new PDJpeg(document, inputStream);
        //Create a new stream which will hold the content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        //Set logo
        contentStream.drawImage(logo, 480, 620);

        //Set header
        contentStream.beginText();
        contentStream.setFont(font2, 20);
        contentStream.moveTextPositionByAmount(100, 700);
        contentStream.drawString("Hartige Hap");
        contentStream.endText();

        //Set title
        contentStream.beginText();
        contentStream.setFont(font2, 12);
        contentStream.moveTextPositionByAmount(100, 620);
        contentStream.drawString("Rekening");
        contentStream.endText();

        //Set payment id
        contentStream.beginText();
        contentStream.setFont(font2, 12);
        contentStream.moveTextPositionByAmount(100, 600);
        contentStream.drawString("ID " + p.getPaymentNumber());
        contentStream.endText();

        //Set product information column titles

        contentStream.beginText();
        contentStream.setFont(font2, 10);
        contentStream.moveTextPositionByAmount(100, 550);
        contentStream.drawString("Product");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font2, 10);
        contentStream.moveTextPositionByAmount(100+120, 550);
        contentStream.drawString("Prijs Exlc btw");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font2, 10);
        contentStream.moveTextPositionByAmount(100+220, 550);
        contentStream.drawString("Prijs Incl btw");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font2, 10);
        contentStream.moveTextPositionByAmount(100+320, 550);
        contentStream.drawString("Aantal");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font2, 10);
        contentStream.moveTextPositionByAmount(100+420, 550);
        contentStream.drawString("Totaal prijs");
        contentStream.endText();

        //Set line under column titles (x, y, x, y)
        contentStream.drawLine(100, 540, 580, 540);

        //Set product information
        int i = 0;
        int endY = 0;
        for(Product product : p.getProductList()) {
            i++;
            //Set product name
            contentStream.beginText();
            contentStream.setFont(font1, 10);
            contentStream.moveTextPositionByAmount(100, 540-(20*i));
            contentStream.drawString(product.getProductName());
            contentStream.endText();

            //Set product price excl
            contentStream.beginText();
            contentStream.setFont(font1, 10);
            contentStream.moveTextPositionByAmount(100+120, 540-(20*i));
            contentStream.drawString(EURO+" "+round(product.getPrice(), 2));
            contentStream.endText();

            //Set product price incl
            contentStream.beginText();
            contentStream.setFont(font1, 10);
            contentStream.moveTextPositionByAmount(100+220, 540-(20*i));
            contentStream.drawString(EURO+" "+round((product.getPrice() * (product.getBtw() + 100) / 100), 2) + " ("+product.getBtw()+"%)");
            contentStream.endText();
            System.out.println(i);

            //Set product amount
            contentStream.beginText();
            contentStream.setFont(font1, 10);
            contentStream.moveTextPositionByAmount(100+320, 540-(20*i));
            contentStream.drawString(String.valueOf(product.getAmount()));
            contentStream.endText();

            //Set total product price
            contentStream.beginText();
            contentStream.setFont(font1, 10);
            contentStream.moveTextPositionByAmount(100+420, 540-(20*i));
            contentStream.drawString(EURO+" "+round(product.getAmount() * (product.getPrice() * (product.getBtw() + 100) / 100), 2));
            contentStream.endText();

            endY = 500-(20*i);
        }

        //Set total price excl and incl vat
        contentStream.beginText();
        contentStream.setFont(font2, 11);
        contentStream.moveTextPositionByAmount(100, endY-40);
        contentStream.drawString("Totaal: ");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font2, 10);
        contentStream.moveTextPositionByAmount(100, endY-55);
        contentStream.drawString("Excl. btw: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.setFont(font1, 10);
        contentStream.moveTextPositionByAmount(100+120, endY-55);
        contentStream.drawString(EURO+" "+round(payment.getTotalPriceExcl(), 2));
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font2, 10);
        contentStream.moveTextPositionByAmount(100, endY-65);
        contentStream.drawString("Incl. btw: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.setFont(font1, 10);
        contentStream.moveTextPositionByAmount(100+120, endY-65);
        contentStream.drawString(EURO+" "+round(payment.getTotalPrice(), 2));
        contentStream.endText();

        //Set footer
        contentStream.beginText();
        contentStream.setFont(font2, 8);
        contentStream.moveTextPositionByAmount(100, 50);
        contentStream.drawString("Datum: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(p.getPaymentDate()));
        contentStream.endText();

        //Close the content stream
        contentStream.close();
        inputStream.close();

        //Save document

        /*Create a new folder*/
        File dir = new File("C:\\hhbills\\");
        /*Check if the directory exists*/
        if(!dir.exists()) {
            /*Directory doesn't exist, so try to create a new directory and catch possible exception*/
            boolean result = false;
            try {
                dir.mkdir(); //Create folder
                result = true;
            } catch (SecurityException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.SEVERE, "unable to create directory: " + dir.getName(), e);
            }

            /*result is set to 'true' if the folder was created succesfully.
            * Since the directory didn't exist yet, the file we want to create can't possibly exist in this location*/
            if(result) {
                if(!new File(dir+"\\bill"+payment.getPaymentNumber()+".pdf").isFile()) {
                    document.save(dir + "\\bill" + payment.getPaymentNumber() + ".pdf"); //Save document as bill<id>.pdf
                }
                /*if for some reason the directory wasn't created successfully, save the document in the default location and show the location in a popup*/
            } else {
                document.save("bill"+payment.getPaymentNumber()+".pdf");
                JOptionPane.showMessageDialog(paymentPanel, "Rekening op andere locatie opgeslagen: "
                        +document.getDocumentInformation().getDictionary().toString(), "Fout", JOptionPane.ERROR_MESSAGE);
            }
            /*If the directory already exists, save the document in this directory*/
        } else if (dir.exists()) {
            /*Check is a file with the same name is present*/
            if(!new File(dir+"\\bill"+payment.getPaymentNumber()+".pdf").isFile()) {
                /*No file present, save the document*/
                document.save(dir + "\\bill" + payment.getPaymentNumber() + ".pdf");
            } else {
                /*File existed already, create a new file and get the billid*/
                File fileName = new File(dir+"\\bill"+payment.getPaymentNumber()+".pdf");
                System.out.println(fileName);
                /*Since the last character before '.pdf' is always the billid or version number, we can easily extract this number from the file name */
                int billVersion = Character.getNumericValue(fileName.getName().charAt(fileName.getName().length() - 5));
                System.out.println(billVersion);
                int newVersion = 1;
                /*Repeat this loop until bill<id>_<version> is not a file in the directory*/
                while(fileName.exists()) {
                    newVersion++;
                    fileName = new File(dir + "\\bill" + payment.getPaymentNumber() + "_" + newVersion + ".pdf");
                    System.out.println(fileName.getName());
                }
                /*Save the document with the new version number*/
                document.save(dir + "\\bill" + payment.getPaymentNumber() + "_" + newVersion + ".pdf");
            }
        }


        //Print document
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPageable(document);
        printerJob.setJobName("Bon Hartige Hap id: "+payment.getPaymentNumber());
        try {
            printerJob.print();
        }catch (PrinterException pe) {
            JOptionPane.showMessageDialog(paymentPanel, "Rekening kon niet worden geprint: "+pe.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown in the PaymentSection", pe);
        }

        //Close document
        document.close();
    }


    /**
     * Rounds a double to 2 decimal places. Returns the result as a String
     * @param value
     * @param places
     * @return String value of rounded Double
     */
    private static String round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }
}

