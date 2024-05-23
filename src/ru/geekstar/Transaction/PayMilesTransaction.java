package ru.geekstar.Transaction;

public class PayMilesTransaction extends PayTransaction {

    private int payMiles;

    private int balanceMiles;

    public int getPayMiles() {
        return payMiles;
    }

    public void setPayMiles(int payMiles) {
        this.payMiles = payMiles;
    }

    public int getBalanceMiles() {
        return balanceMiles;
    }

    public void setBalanceMiles(int balanceMiles) {
        this.balanceMiles = balanceMiles;
    }

    @Override
    public String getStringTransaction() {
        String consumer = getNameCard(getFromCard());

        String transaction = getLocalDateTime() + " " + consumer + " " + getTypeOperation() + " " + getBuyProductOrService() + ": -" + payMiles +
                " миль Статус: " + getStatusOperation() + " Доступно миль: " + balanceMiles;

        return transaction;

    }
}
