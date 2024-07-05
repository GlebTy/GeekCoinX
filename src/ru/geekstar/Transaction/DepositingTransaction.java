package ru.geekstar.Transaction;

import ru.geekstar.Account.Account;
import ru.geekstar.Card.Card;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public class DepositingTransaction extends Transaction {

    public DepositingTransaction(Card fromCard, Card toCard, String typeOperation, float sum, char currencySymbol) {
        super(fromCard, toCard, typeOperation, sum, currencySymbol);
    }

    public DepositingTransaction(Card fromCard, Account toAccount, String typeOperation, float sum, char currencySymbol) {
        super(fromCard, toAccount, typeOperation, sum, currencySymbol);
    }

    public DepositingTransaction(Account fromAccount, Account toAccount, String typeOperation, float sum, char currencySymbol) {
        super(fromAccount, toAccount, typeOperation, sum, currencySymbol);
    }

    public DepositingTransaction(Account fromAccount, Card toCard, String typeOperation, float sum, char currencySymbol) {
        super(fromAccount, toCard, typeOperation, sum, currencySymbol);
    }

    public DepositingTransaction(Card toCard, String typeOperation, float sum) {
        super(typeOperation, toCard, sum);
    }

    @Override
    public String getSender() {

        String sender = "";

        // если списание происходит с карты
        if(getFromCard() != null){

            //если мы пополняем чужую карту или счет, то добавляем имя отправителя
            if ((getToCard() != null && !getFromCard().getCardHolder().isClientCard(getToCard())) || (getToAccount() != null && !getFromCard().getCardHolder().isClientAccount(getToAccount()))) {

                PhysicalPerson cardHolder = getFromCard().getCardHolder().getPhysicalPerson();
                sender = cardHolder.getFirstName() + " " + cardHolder.getLastName().substring(0,1);
            }

        }
        // если списание происходит со счета
        if(getFromAccount() != null){

            //если мы пополняем чужую карту или счет, то добавляем имя отправителя
            if ((getToCard() != null && !getFromAccount().getAccountHolder().isClientCard(getToCard())) || (getToAccount() != null && !getFromAccount().getAccountHolder().isClientAccount(getToAccount()))) {

                PhysicalPerson AccountHolder = getFromAccount().getAccountHolder().getPhysicalPerson();
                sender = AccountHolder.getFirstName() + " " + AccountHolder.getLastName().substring(0,1);
            }

            if (!sender.isEmpty()) {
                sender = super.getSender() + " от " + sender + ".";
            }
        }
        return sender;
    }
    @Override
    public String getStringTransaction() {

        String transaction = getLocalDateTime() + " " + getRecipient() + " " + getTypeOperation() + (!getSender().isEmpty() ? " " + getSender() : "") + ": +" + getSum() + getCurrencySymbol() +
                " Статус: " +  getStatusOperation() + " Баланс: " + getBalance() + getCurrencySymbol() + " Комиссия составила: " + getCommission() +
                getCurrencySymbol() + (getAuthorizationCode() != null ? " Код авторизации: " + getAuthorizationCode() : "");

        return transaction;
    }


}
