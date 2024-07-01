package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Account.SberPayCardAccount;
import ru.geekstar.Bank.Sberbank;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import java.util.ArrayList;

public final class SberMastercardTravel extends CardMastercard implements IMulticurrencyCard {

    public static int countCards;

    private ArrayList<PayCardAccount> multiCurrencyAccounts = new ArrayList<>();

    @Override
    public ArrayList<PayCardAccount> getMulticurrencyAccounts() {
        return multiCurrencyAccounts;
    }

    @Override
    public void setMulticurrencyAccounts(ArrayList<PayCardAccount> multicurrencyAccounts) {
        this.multiCurrencyAccounts = multicurrencyAccounts;
    }

    public SberMastercardTravel(PhysicalPersonProfile cardHolder, PayCardAccount payCardAccount, String pinCode) {
        super(cardHolder, payCardAccount, pinCode);
        addAccount("USD");
        addAccount("EUR");
        countCards++;
    }

    @Override
    public void addAccount(String currencyCodeAccount) {
        // открываем новый счет
        PayCardAccount payCardAccount = (PayCardAccount) ((Sberbank) this.getBank()).openAccount(this.getCardHolder(), SberPayCardAccount.class, currencyCodeAccount);
        // связывем созданный счет с картой
        payCardAccount.getCards().add(this);
        // добавляем созданный счет в массив мултивалютных карт
        getMulticurrencyAccounts().add(payCardAccount);

    }

    @Override
     public void switchAccount(String currencyCodeAccount) {
        for(int idPayCardAccount = 0; idPayCardAccount < getMulticurrencyAccounts().size(); idPayCardAccount++) {
            PayCardAccount payCardMultiAccount = getMulticurrencyAccounts().get(idPayCardAccount);
            String currencyMultiCode = payCardMultiAccount.getCurrencyCode();

            if(currencyCodeAccount.equals(currencyMultiCode)) {
                getMulticurrencyAccounts().remove(payCardMultiAccount);
                getMulticurrencyAccounts().add(getPayCardAccount());
                setPayCardAccount(payCardMultiAccount);
            }

        }
    }
}
