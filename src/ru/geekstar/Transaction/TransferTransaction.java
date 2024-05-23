package ru.geekstar.Transaction;

public class TransferTransaction extends Transaction {

    @Override
    public String getStringTransaction() {

        String transaction = getLocalDateTime() + " " + getSender() + " " + getTypeOperation() + " " + getRecipient() + ": -" + getSum() + getCurrencySymbol() +
                " Статус: " +  getStatusOperation() + " Баланс: " + getBalance() + getCurrencySymbol() + " Комиссия составила: " + getCommission() +
                getCurrencySymbol() + (getAuthorizationCode() != null ? " Код авторизации: " + getAuthorizationCode() : "");

        return transaction;

    }

}
