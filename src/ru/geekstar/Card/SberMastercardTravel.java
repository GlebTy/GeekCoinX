package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Account.SberPayCardAccount;
import ru.geekstar.Bank.IBankServicePhysicalPerson;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import java.util.ArrayList;

public class SberMastercardTravel extends CardMastercard implements IMulticurrencyCard {

    private ArrayList<PayCardAccount> multiCurrencyAccounts = new ArrayList<>();

    @Override
    public ArrayList<PayCardAccount> getMulticurrencyAccounts() {
        return multiCurrencyAccounts;
    }

    @Override
    public void setMulticurrencyAccounts(ArrayList<PayCardAccount> multicurrencyAccounts) {
        this.multiCurrencyAccounts = multicurrencyAccounts;
    }

    @Override
    public void addAccount(String currencyCodeAccount) {
        // открываем новый счет
        SberPayCardAccount sberPayCardAccount = (SberPayCardAccount) this.getBank().openAccount(this.getCardHolder(), new SberPayCardAccount(), currencyCodeAccount);
        // связывем созданный счет с картой
        sberPayCardAccount.getCards().add(this);
        // добавляем созданный счет в массив мултивалютных карт
        getMulticurrencyAccounts().add(sberPayCardAccount);

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
