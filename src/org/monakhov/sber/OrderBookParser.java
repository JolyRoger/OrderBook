package org.monakhov.sber;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class OrderBookParser {
    /* good old Java Sax parser */
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    private SAXParser saxParser = null;
    /* file to read from */
    private File xmlFile = null;
    /* the main collection where all data is saved */
    private TreeMap<String, OrderBook> booksMap = new TreeMap<>();

    private DefaultHandler handler = new DefaultHandler() {
        @Override
        public void startElement(String uri,String localName,String qName, Attributes attributes) throws SAXException {
            if (qName.startsWith("O")) return;
            else if (qName.startsWith("D")) {
                String book = attributes.getValue(0);
                int orderId = Integer.parseInt(attributes.getValue(1));
                OrderBook orderBook = booksMap.get(book);
                orderBook.deleteOrder(orderId);
            } else {
                String book = attributes.getValue(0);
                String operation = attributes.getValue(1);
                float price = Float.parseFloat(attributes.getValue(2));
                int volume = Integer.parseInt(attributes.getValue(3));
                int orderId = Integer.parseInt(attributes.getValue(4));

                OrderBook orderBook = booksMap.get(book);
                if (orderBook == null) {
                    orderBook = new OrderBook();
                    booksMap.put(book, orderBook);
                }
                Order order = new Order(price, volume, operation, orderId);
                orderBook.addOrder(orderId, order);
            }
        }
    };

    public OrderBookParser(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    /* print header of resulting table and the table itself */
    private void print() {
        for (String book : booksMap.keySet()) {
            System.out.println("Order Book: " + book);
            System.out.println("BID\t\t\tASK");
            System.out.println("Volume@Price - Volume@Price");
            OrderBook orderBook = booksMap.get(book);
            orderBook.print();
            System.out.println();
        }
    }

    /* entry point */
    private void go() {
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(xmlFile, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            exit(e.getMessage());
        }
    }

    private static void exit(String message) {
        System.err.println(message);
        System.exit(1);
    }

    private static void process(String fileName) {
        if (!fileName.endsWith(".xml")) exit("This is not an XML file. Can't proceed. Please enter valid one");
        File f = new File(fileName);
        if (!f.exists()) exit("File does not exist. Please check entered path");

        OrderBookParser orderBookParser = new OrderBookParser(f);
        orderBookParser.go();
        orderBookParser.print();
    }

    private static boolean tryFileHere() {
        File f = new File("orders.xml");
        if (f.exists()) {
            process(f.getName());
            return true;
        } else return false;
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            if (tryFileHere()) return;
            System.out.println("Enter valid XML file name: ");
            Scanner in = new Scanner(System.in);
            String fileName = in.next();
            if (fileName == null || fileName.isEmpty()) {
                exit("Please enter your book XML file");
            } else process(fileName);
        } else process(args[0]);
    }
}



