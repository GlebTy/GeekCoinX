package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;

import java.util.ArrayList;

public interface IMulticurrencyCard {

    PayCardAccount getPayCardAccount();

    void setPayCardAccount(PayCardAccount payCardAccount);

    ArrayList<PayCardAccount> getMulticurrencyAccounts();

    void setMulticurrencyAccounts(ArrayList<PayCardAccount> multicurrencyAccounts);

    void addAccount(String currencyCodeAccount);

    void switchAccount(String currencyCodeAccount);

    default void displayMulticurrencyCardTransactions() {
        getPayCardAccount().displayAccountTransactions();
        for (int i = 0; i < getMulticurrencyAccounts().size(); i++) {
            getMulticurrencyAccounts().get(i).displayAccountTransactions();
        }
    }

}
