package org.monakhov.sber;

import java.util.*;

enum ExecutionResult {
    Adjust, RemoveAdjustAdd, Remove, Add
}

class OrderBook {
    /* Collection to keep bid orders by orderId */
    private HashMap<Integer, Order> bidByOrderId = new HashMap<>();
    /* Collection to keep ask orders by orderId */
    private HashMap<Integer, Order> askByOrderId = new HashMap<>();

    /* Collection to keep bid orders ordered by price */
    private TreeSet<Order> sortedBid = new TreeSet<>();
    /* Collection to keep ask orders ordered by price */
    private TreeSet<Order> sortedAsk = new TreeSet<>();

    /* Delete order from appropriate collection by orderId */
    public void deleteOrder(int orderId) {
        Order order = null;
        if (bidByOrderId.containsKey(orderId)) {
            order = bidByOrderId.remove(orderId);
            sortedBid.remove(order);
        } else if (askByOrderId.containsKey(orderId)) {
            order = askByOrderId.remove(orderId);
            sortedAsk.remove(order);
        }
    }

    /* add order to appropriate collections */
    public void addOrder(int orderId, Order order) {
        switch (order.getOperation()) {
            case "BUY":
                execute(orderId, order, sortedAsk, sortedBid,
                        bidByOrderId, askByOrderId); break;
            case "SELL":
                execute(orderId, order, sortedBid, sortedAsk,
                        askByOrderId, bidByOrderId); break;
        }
    }

    /* process incoming order */
    private void execute(int orderId, Order order,
                         TreeSet<Order> counterSet,
                         TreeSet<Order> setToAdd,
                         HashMap<Integer, Order> mapToAdd,
                         HashMap<Integer, Order> mapToRemove) {
        ExecutionResult result = match(order, counterSet, mapToRemove);
        if (result == ExecutionResult.RemoveAdjustAdd || result == ExecutionResult.Add) {
            mapToAdd.put(orderId, order);
            setToAdd.add(order);
        }
    }

    /* match incoming order with orders with higher price from counter collection */
    private ExecutionResult match(Order order, TreeSet<Order> counterSet, HashMap<Integer, Order> counterMap) {
        for (Iterator<Order> iterator = counterSet.iterator(); iterator.hasNext(); ) {
            Order counterOrder = iterator.next();
            if (order.match(counterOrder)) {
                ExecutionResult result = order.execute(counterOrder);
                switch (result) {
                    case RemoveAdjustAdd:
                    case Remove: iterator.remove();
                                 counterMap.remove(counterOrder.getOrderId());
                                 return result;
                    case Adjust: return result;
                }
            } else break;
        }
        return ExecutionResult.Add;
    }

    /* smart print order book to console. it prints consequently by one element from both collections */
    public void print() {
        Iterator<Order> bidIterator = sortedBid.iterator();
        Iterator<Order> askIterator = sortedAsk.iterator();
        boolean bidHasElements = true;
        boolean askHasElements = true;
        boolean printBid = true;
        float bidPrice = 0;
        float askPrice = 0;

        while (bidHasElements || askHasElements) {
            if (printBid) {
                if (bidIterator.hasNext()) {
                    Order order = bidIterator.next();
                    float currentPrice = order.getPrice();
                    if (currentPrice != bidPrice) {
                        order.print();
                        bidPrice = currentPrice;
                        printBid = false;
                    } else {
                        continue;
                    }
                } else {
                    printBid = false;
                    bidHasElements = false;
                    System.out.print("-------\t\t");
                }
            } else {
                if (askIterator.hasNext()) {
                    Order order = askIterator.next();
                    float currentPrice = order.getPrice();
                    if (currentPrice != askPrice) {
                        order.print();
                        System.out.println();
                        askPrice = currentPrice;
                        printBid = true;
                    }
                } else {
                    System.out.println("-------\t\t");
                    printBid = true;
                    askHasElements = false;
                }
            }
        }
    }
}
