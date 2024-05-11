package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Account.TinkoffPayCardAccount;
import ru.geekstar.Bank.Tinkoff;
import ru.geekstar.ClientProfile.TinkoffPhysicalPersonProfile;
import ru.geekstar.Transaction.PayMilesTransaction;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TinkoffAirlinesMir extends CardMir implements IMulticurrencyCard, IAirlinesCard {

    private ArrayList<PayCardAccount> multicurrencyAccounts = new ArrayList<>();

    @Override
    public ArrayList<PayCardAccount> getMulticurrencyAccounts() {
        return multicurrencyAccounts;
    }

    @Override
    public void setMulticurrencyAccounts(ArrayList<PayCardAccount> multicurrencyAccounts) {
        this.multicurrencyAccounts = multicurrencyAccounts;
    }

    @Override
    public void addAccount(String currencyCodeAccount) {
        // открываем новый счет
        PayCardAccount payCardAccount = (PayCardAccount) ((Tinkoff) this.getBank()).openAccount(this.getCardHolder(), new TinkoffPayCardAccount(), currencyCodeAccount);
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

    @Override
    public void payByCard(float sumPay, String buyProductOrService, String pinCode) {
        super.payByCard(sumPay, buyProductOrService, pinCode);
        accumulateMiles(sumPay);
    }

    @Override
    public void accumulateMiles(float sumPay) {
        TinkoffPhysicalPersonProfile cardHolder = (TinkoffPhysicalPersonProfile) getCardHolder();
        int miles = 0;
        if(getPayCardAccount().getCurrencyCode().equals("RUB")) miles = (int)(sumPay/cardHolder.getCostMilesInRUB());
        if(getPayCardAccount().getCurrencyCode().equals("USD")) miles = (int)(sumPay/cardHolder.getCostMilesInUSD());
        if(getPayCardAccount().getCurrencyCode().equals("EUR")) miles = (int)(sumPay/cardHolder.getCostMilesInEUR());

        cardHolder.setMiles(cardHolder.getMiles() + miles);
    }

    @Override
    public void payByCardMiles(float sumPay, int milesPay, String buyProductOrService, String pinCode) {
        PayMilesTransaction payMilesTransaction = new PayMilesTransaction();
        payMilesTransaction.setLocalDateTime(LocalDateTime.now());
        payMilesTransaction.setFromCard(this);
        payMilesTransaction.setTypeOperation("оплата милями");
        payMilesTransaction.setBuyProductOrService(buyProductOrService);

        //определяем владельца карты, так как бонусы привязаны к клиенту, а не к конкретной карте
        TinkoffPhysicalPersonProfile cardHolder = (TinkoffPhysicalPersonProfile) getCardHolder();

       if(cardHolder.getMiles() >= milesPay) {
        if(milesPay > sumPay) milesPay = (int) sumPay;
        cardHolder.setMiles(cardHolder.getMiles() - milesPay);
        sumPay -= milesPay;
        payMilesTransaction.setStatusOperation("Оплата милями прошла успешно");
       } else payMilesTransaction.setStatusOperation("Недостаточно миль");

       payMilesTransaction.setPayMiles(milesPay);
       payMilesTransaction.setBalanceMiles(cardHolder.getMiles());
        this.getPayCardAccount().getPayTransactions().add(payMilesTransaction);

        payByCard(sumPay, buyProductOrService, pinCode);


    }
}
