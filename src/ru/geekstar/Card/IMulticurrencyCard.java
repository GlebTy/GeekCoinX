package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Bank.IBankServicePhysicalPerson;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import java.util.ArrayList;

public interface IMulticurrencyCard {

    ArrayList<PayCardAccount> getMulticurrencyAccounts();

    void setMulticurrencyAccounts(ArrayList<PayCardAccount> multicurrencyAccounts);

    void addAccount(IBankServicePhysicalPerson bank, PhysicalPersonProfile physicalPersonProfile, String currencyCodeAccount);

    void switchAccount(String currencyCodeAccount);

}
