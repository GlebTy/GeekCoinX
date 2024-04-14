package ru.geekstar.Bank;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Account.TinkoffPayCardAccount;
import ru.geekstar.Card.Card;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.ClientProfile.TinkoffPhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public class Tinkoff extends Bank implements IBankServicePhysicalPerson {

    @Override
    public PhysicalPersonProfile registerPhysicalPersonProfile(PhysicalPerson physicalPerson) {
        //создать профиль клиента
        TinkoffPhysicalPersonProfile tinkoffPhysicalPersonProfile = new TinkoffPhysicalPersonProfile();
        tinkoffPhysicalPersonProfile.setBank(this);
        tinkoffPhysicalPersonProfile.setPhysicalPerson(physicalPerson);

        //установить лимиты
        tinkoffPhysicalPersonProfile.setLimitPaymentsTransfersDayInRUB(1000000.00f);
        tinkoffPhysicalPersonProfile.setLimitPaymentsTransfersDayInUSD(50000.00f);
        tinkoffPhysicalPersonProfile.setLimitPaymentsTransfersDayInEUR(3800.00f);

        //установить проценты коммисий
        tinkoffPhysicalPersonProfile.setPercentOfCommissionForPayHousingCommunalServices(2.0f);
        tinkoffPhysicalPersonProfile.setPercentOfCommissionForTransferInRUB(1.0f);
        tinkoffPhysicalPersonProfile.setPercentOfCommissionForTransferInUsdOrOtherCurrency(1.25f);

        //становить лимиты на суммы комиссий
        tinkoffPhysicalPersonProfile.setLimitCommissionTransferInRUB(3000.00f);
        tinkoffPhysicalPersonProfile.setLimitCommissionTransferInUsdOrEquivalentInOtherCurrency(100.00f);

        //и привязать профиль клиента к банку
        getClientProfiles().add(tinkoffPhysicalPersonProfile);

        return tinkoffPhysicalPersonProfile;
    }

    @Override
    public Card openCard(PhysicalPersonProfile physicalPersonProfile, Card card, String currencyCode, String pinCode) {
        //установить свойства карты
        card.setBank(this);
        card.setNumberCard(generateNumberCard());
        card.setCardHolder(physicalPersonProfile);

        //открыть платежный счет
        PayCardAccount payCardAccount = (PayCardAccount) openAccount(physicalPersonProfile, new TinkoffPayCardAccount(), currencyCode);

        //привязать карту к платежному счету
        payCardAccount.getCards().add(card);

        //привязать платежный счет к карте
        card.setPayCardAccount(payCardAccount);
        card.setStatusCard("Активна");
        card.setPinCode(pinCode);

        //привязать карту к профилю клиента
        physicalPersonProfile.getCards().add(card);

        return card;
    }

    @Override
    public Account openAccount(PhysicalPersonProfile physicalPersonProfile, Account account, String currencyCode) {
        //становить свойства платежного счета
        account.setBank(this);
        account.setNumberAccount(generateNumberAccount());
        account.setAccountHolder(physicalPersonProfile);
        account.setCurrencyCode(currencyCode);
        account.setCurrencySymbol(currencyCode);

        //привязать платежный счет к профилю клиента
        physicalPersonProfile.getAccounts().add(account);

        return account;
    }

    @Override
    public float getCommissionOfTransferToClientBank(PhysicalPersonProfile clientProfile, float sum, String fromCurrencyCode) {
        return 0;
    }

    @Override
    public float getExchangeRateBank(String currency, String currencyExchangeRate) {
        float exchangeRateBank = 0f;

        //курс доллара к рублю
        if(currency.equals("USD") && currencyExchangeRate.equals("RUB")) exchangeRateBank = 84.0f;

        //курс доллара к рублю
        if(currency.equals("EUR") && currencyExchangeRate.equals("RUB")) exchangeRateBank = 89.65f;


    }
}
