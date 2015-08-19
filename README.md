# OrderBook

Task

«Order book» task

Before you start
Please follow these instructions super accurately.
Develop the solution in Java (you can use Maven as a build tool if you want)
The core of the solution should be written without any add-ons to the core of the programming language. Use
only the defined language standard (e.g. you can use all features that you find in a standard JDK). This means
that you can use external libraries, but your program should still produce the required output after removing
those parts. For example, if you would decide to use a logging library then your program would still work
correctly after removing all parts that use the logging library. You can also use JUnit.

Optimize for quality of development and execution speed.
- In general execution time on PC with HDD should be around 6 seconds.
- Be ready to explain and protect all performance optimizations (if there will be any).
Deliver your result in the best professional quality you can produce. Polish your solution. Make a master piece
out of it.  It is part of this task to compare what different people consider to be a professional quality solution.

Task Description

Program should create Order Books according to orders from XML file:
(drive.google.com/file/d/0B4FxtBlfiwJhbERraGJ2RXVtR00/).
Two actions supported: new order, delete exiting order. And after processing the whole file, print to standard output
all order books generated.

What is Order Book?
Order Book is basically a combination of bid ladder and ask ladder. Bid ladder consists from buy orders; ask ladder
consists from sell orders. See example order book and format of output for your program:
Order book: ${order_book_name}

BID          ASK
Volume@Price – Volume@Price
10@100.1  – 11@100.2
4@100.0    – 14@100.21
98@99.98  – 14@100.23
-----------      – 12@101.00

Bid ladder is sorted from the highest bid price at the top to lowest price at the bottom.
Ask ladder – visa versa, sorted from lowest at the top to highest price at the bottom.
Note: when printing out resulted order books - quantity for all orders with the same price should be aggregated. In
other words – on each side (bid / ask) of order book can have only one level with particular price.
Note: You don’t need to check XML file for correctness and implement any safety checks.
Note: There are several different order books in XML file (see ‘book’ attribute)
Matching logic:
When new bid/ask order is added into order book you should check opposite book side for matching. Checking always
starts from the top of opposite side. To get execution (matching) price of counter orders must overlap (bid>=ask). For
example:

* bid order at 4.4 matches ask order at 4.3
* ask order at 3.7 matches bid order at 3.8
* opposite side orders with same price matches as well.

Matching is a process of execution two counter orders. When orders are matched they current volume is decreased by
minimum current volumes of these orders:  volume_executed=min(ask_order,bid_order).  So when there is a match
you need to adjust orders quantities. There could be three cases:

* Order existing in order book fully filled by incoming order  remove existing, adjust quantity of incoming
order and add it
* Incoming order fully filled by existing order  adjust quantity of existing order
* Full quantity match  Remove existing order

Also
Describe do’s and don’ts of your solution, what could be done to speed it up further, improve, simplify?

OrderBook

This task has been done in Java 7, i.e. without new Java 8 features. To parse XML file I use built-in XML Java SAX parser. Parser works consequentially. It finds out an XML tag and process it. According to the result some structures have been changed.
All data collects to main TreeMap booksMap. In it the key is name of the book and value is set of collections OrderBook.

OrderBook in turn contains two Maps to keep list of Orders by orderId and two sets TreeSet to keep orders ordered by its price. Each of couples of collections contain BID orders and ASK orders accordingly.

Order is an object representing result of parsing an AddOrder node in XML. It contains all Order fields and some useful methods, e.g. compareTo to sort by price and print for output.

Before append an Order to collections incoming order matches with objects from opposite by operation TreeSet. Due to TreeSet is ordered by price matching is convenient and the algorithm can avoid unnecessary checking operations. According to result incoming order either append to appropriate collection or not. Also compared order either deleted or not. Besides volume changes accordingly the task condition.

HashMap needs to remove orders with O(1), that is very important considering huge XML document is to process.
TreeSet needs to keep incoming orders sorted by price that is very useful to print data speedily.

Printing happens immediately after parser has its work done. Program prints to console. Two iterators  consequently read from TreeSets and output to default stream in one line. In this way the print method generates two data columns sorted by price according the task. Orders with the same price prints only once (first met in the list).

You can transmit XML file name to program like this:
* as parameter: java -jar OrderBook.jar /valid/path/to/orders.xml
* to copy manually to program folder. Then "java -jar OrderBook.jar" will try to find orders.xml in folder where the jar file is.
* to type the path in console if orders.xml is not found in current folder.

To do the program better one may by:
* reduce redundant code, for example getters and setters from Order class. Instead of these one can add appropriate library to dependency (e.g. Lombok) injecting these methods through annotations.
* also it possible to use Java 8 — it also gives possibility to decrease some pieces of code.
I did not use this release because of speed development. I have much more practice with old releases.
* it would be very handy to use some structures from other sources. Not only from Java SDK library.
For instance, sorted set with duplications or bi-directional map from Guava.


