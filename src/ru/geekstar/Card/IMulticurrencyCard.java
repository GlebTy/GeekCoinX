package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;

import java.util.ArrayList;

public interface IMulticurrencyCard {

    ArrayList<PayCardAccount> getMulticurrencyAccounts();

    void setMulticurrencyAccounts(ArrayList<PayCardAccount> multicurrencyAccounts);

    void addAccount(String currencyCodeAccount);

    void switchAccount(String currencyCodeAccount);

}
