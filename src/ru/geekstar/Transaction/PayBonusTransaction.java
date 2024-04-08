package ru.geekstar.Transaction;

import ru.geekstar.Card.Card;

public class PayBonusTransaction extends PayTransaction {

    private int payBonuses;

    private int balanceBonuses;

    public int getPayBonuses() {
        return payBonuses;
    }

    public void setPayBonuses(int payBonuses) {
        this.payBonuses = payBonuses;
    }

    public int getBalanceBonuses() {
        return balanceBonuses;
    }

    public void setBalanceBonuses(int balanceBonuses) {
        this.balanceBonuses = balanceBonuses;
    }

    @Override
    public String getStringTransaction() {
        String consumer = getNameCard(getFromCard());

        String transaction = getLocalDateTime() + " " + consumer + " " + getTypeOperation() + " " + getBuyProductOrService() + ": " + payBonuses +
                " бонусов Статус: " + getStatusOperation() + " Доступно бонусов: " + balanceBonuses;

        return transaction;

    }
}
