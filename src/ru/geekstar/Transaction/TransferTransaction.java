package ru.geekstar.Transaction;

import ru.geekstar.Account.Account;
import ru.geekstar.Card.Card;

public class TransferTransaction extends Transaction {

    public TransferTransaction(Card fromCard, Card toCard, String typeOfOperation, float sum, String currencySymbol) {
        super(fromCard, toCard, typeOfOperation, sum, currencySymbol);
    }

    public TransferTransaction(Card fromCard, Account toAccount, String typeOfOperation, float sum, String currencySymbol) {
        super(fromCard, toAccount, typeOfOperation, sum, currencySymbol);
    }

    public TransferTransaction(Account fromAccount, Account toAccount, String typeOfOperation, float sum, String currencySymbol) {
        super(fromAccount, toAccount, typeOfOperation, sum, currencySymbol);
    }

    public TransferTransaction(Account fromAccount, Card toCard, String typeOfOperation, float sum, String currencySymbol) {
        super(fromAccount, toCard, typeOfOperation, sum, currencySymbol);
    }



    @Override
    public String getStringTransaction() {

        String transaction = getLocalDateTime() + " " + getSender() + " " + getTypeOperation() + " " + getRecipient() + ": -" + getSum() + getCurrencySymbol() +
                " Статус: " +  getStatusOperation() + " Баланс: " + getBalance() + getCurrencySymbol() + " Комиссия составила: " + getCommission() +
                getCurrencySymbol() + (getAuthorizationCode() != null ? " Код авторизации: " + getAuthorizationCode() : "");

        return transaction;

    }

}
