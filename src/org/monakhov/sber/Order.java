package org.monakhov.sber;

class Order implements Comparable {

    /* intrinsic order fields  */
    private boolean type = false;       //   true if SELL (ASK), false - BID (BUY)
    private final String operation;
    private int volume;
    private final float price;
    private final int orderId;

    public Order(float price, int volume, String operation, int orderId) {
        this.price = price;
        this.volume = volume;
        this.operation = operation;
        this.orderId = orderId;
        this.type = operation.startsWith("S");
    }

    /* getters and setters */
    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getPrice() {
        return price;
    }

    public String getOperation() {
        return operation;
    }

    public int getOrderId() {
        return orderId;
    }

    /* order prints itself. it is used for big final printing */
    public void print() {
        System.out.print(volume + "@" + price + "   \t");
    }

    public boolean isMoreExpensive(Order order) {
        return price > order.getPrice();
    }

    /* matching depending on orders prices */
    public boolean match(Order counterOrder) {
        if (price == counterOrder.getPrice()) return true;
        else return type ^ price > counterOrder.getPrice();
    }

    public int getMinVolume(Order counterOrder) {
        return Math.min(volume, counterOrder.getVolume());
    }

    /* execution. three cases from the task */
    public ExecutionResult execute(Order counterOrder) {
        int minVolume = getMinVolume(counterOrder);
        int counterVolume = counterOrder.getVolume();
        if (volume > counterVolume) {
            setVolume(minVolume);
            return ExecutionResult.RemoveAdjustAdd;
        } else if (volume < counterVolume) {
            counterOrder.setVolume(minVolume);
            return ExecutionResult.Adjust;
        } else return ExecutionResult.Remove;
    }

    @Override
    public String toString() {
        return volume + "@" + price + " ";
    }

    /* smart compare. it sorts ask orders in incremental order and bid ones in decremental */
    @Override
    public int compareTo(Object o) {
        Order order = (Order) o;
        if (orderId == order.getOrderId()) return 0;
        else if (type) {
            if (isMoreExpensive(order)) return 1;
            else if (order.isMoreExpensive(this)) return -1;
        } else {
            if (isMoreExpensive(order)) return -1;
            else if (order.isMoreExpensive(this)) return 1;
        }
        return -1;
    }
}


