package ru.geekstar.Transaction;

public class PayTransaction extends Transaction {

    private String buyProductOrService;


    public String getBuyProductOrService() {
        return buyProductOrService;
    }

    public void setBuyProductOrService(String buyProductOrService) {
        this.buyProductOrService = buyProductOrService;
    }


    @Override
    public String getStringTransaction() {
        String consumer = getNameCard(getFromCard());

        String transaction = getLocalDateTime() + " " + consumer + " " + getTypeOperation() + " " + buyProductOrService + ": " + getSum() + getCurrencySymbol() +
                " Статус: " + getStatusOperation() + " Баланс: " + getBalance() + getCurrencySymbol() + " Комиссия составила: " + getCommission() +
                getCurrencySymbol() + " Код авторизации: " + getAuthorizationCode();

        return transaction;

    }


}
