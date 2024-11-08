package ru.geekstar.Bank;

import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.ClientProfile.TinkoffPhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

import java.util.ArrayList;

public class Tinkoff extends Bank implements IBankServicePhysicalPerson {

    public static final String TINKOFF;

    static {
        TINKOFF = "Тинькофф";
        System.out.println(TINKOFF + " для физическиз лиц");
    }
    
    public Tinkoff () {
        this(TINKOFF);
    }

    public Tinkoff (String bankName) {
        super(bankName);
    }

    @Override
    public PhysicalPersonProfile registerPhysicalPersonProfile(PhysicalPerson physicalPerson) {
        //создать профиль клиента
        TinkoffPhysicalPersonProfile tinkoffPhysicalPersonProfile = new TinkoffPhysicalPersonProfile(this,physicalPerson);

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
    public ArrayList<Float> getExchangeRateBank(String currency, String currencyExchangeRate) {
        // TODO: Запрос к API банка.
        ArrayList<Float> exchangeRateBank = new ArrayList<>();

        // курс доллара
        if (currency.equals("USD")) {
            // в рублях
            if (currency.equals("RUB")) {
                exchangeRateBank.add(99.30f);
                exchangeRateBank.add(90.10f);
            }
            // в евро
            if (currency.equals("EUR")) {
                exchangeRateBank.add(0.93f);
                exchangeRateBank.add(0.89f);
            }
        }

        // курс евро
        if (currency.equals("EUR")) {
            // в рублях
            if (currency.equals("RUB")) {
                exchangeRateBank.add(108.80f);
                exchangeRateBank.add(100.20f);
            }
            // в долларах
            if (currency.equals("USD")) {
                exchangeRateBank.add(1.07f);
                exchangeRateBank.add(1.01f);
            }
        }

        // курс рубля
        if (currency.equals("RUB")) {
            // в долларах
            if (currency.equals("USD")) {
                exchangeRateBank.add(0.010f);
                exchangeRateBank.add(0.006f);
            }
            // в евро
            if (currency.equals("EUR")) {
                exchangeRateBank.add(0.0096f);
                exchangeRateBank.add(0.0083f);
            }
        }
        return exchangeRateBank;
    }
}
