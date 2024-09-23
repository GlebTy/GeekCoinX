package ru.geekstar.Account;

import ru.geekstar.ClientProfile.PhysicalPersonProfile;

public class TinkoffSavingsAccount extends SavingsAccount {

    public TinkoffSavingsAccount(PhysicalPersonProfile accountHolder, String currencyCode) {
        super(accountHolder,currencyCode);
    }

    @Override
    public String toString() {
        return "Накопительный счёт ⦁⦁" + this.getNumberAccount().substring(20) + " " + getBalance() + " " + getCurrencySymbol();
    }
}
