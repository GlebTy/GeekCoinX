package ru.geekstar.Bank;

import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.ClientProfile.TinkoffPhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public class Tinkoff extends Bank implements IBankServicePhysicalPerson {

    public Tinkoff () {
        this("Тинькофф");
    }

    public Tinkoff (String bankName) {
        super(bankName);
    }

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

        //установить цены на милю в разных валютах
        tinkoffPhysicalPersonProfile.setCostMilesInRUB(60.0f);
        tinkoffPhysicalPersonProfile.setCostMilesInUSD(1.0f);
        tinkoffPhysicalPersonProfile.setCostMilesInEUR(1.0f);

        //установить проценты коммисий
        tinkoffPhysicalPersonProfile.setPercentOfCommissionForPayHousingCommunalServices(2.0f);
        tinkoffPhysicalPersonProfile.setPercentOfCommissionForTransferInRUB(1.0f);
        tinkoffPhysicalPersonProfile.setPercentOfCommissionForTransferInUsdOrOtherCurrency(1.25f);

        //становить лимиты на суммы комиссий
        tinkoffPhysicalPersonProfile.setLimitCommissionTransferInRUB(3000.00f);
        tinkoffPhysicalPersonProfile.setLimitCommissionTransferInUsdOrEquivalentInOtherCurrency(100.00f);

        //установить процент кэшбэка
        tinkoffPhysicalPersonProfile.setPercentCashbackOfSumPay(1.0f);

        //и привязать профиль клиента к банку
        getClientProfiles().add(tinkoffPhysicalPersonProfile);

        return tinkoffPhysicalPersonProfile;
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
        return exchangeRateBank;


    }
}
