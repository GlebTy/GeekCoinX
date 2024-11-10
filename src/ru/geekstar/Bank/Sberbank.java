package ru.geekstar.Bank;

import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.ClientProfile.SberPhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

import java.util.ArrayList;

public class Sberbank extends Bank implements IBankServicePhysicalPerson {

    public static final String SBER;

    static {
        SBER = "Сбер";
        System.out.println(SBER + " для физических лиц");
    }

    public Sberbank () {
        this(SBER);
    }

    public Sberbank (String bankName) {
        super(bankName);
    }

    // Зарегистрировать профиль физ лица
    @Override
    public PhysicalPersonProfile registerPhysicalPersonProfile(PhysicalPerson physicalPerson) {
        // создать профиль клиента
        SberPhysicalPersonProfile sberPhysicalPersonProfile = new SberPhysicalPersonProfile(this, physicalPerson);
        sberPhysicalPersonProfile.setBank(this);
        sberPhysicalPersonProfile.setPhysicalPerson(physicalPerson);

        sberPhysicalPersonProfile.setPercentBonusOfSumpay(0.5f);

        // установить лимиты
        sberPhysicalPersonProfile.setLimitPaymentsTransfersDayInRUB(1000000.00f);
        sberPhysicalPersonProfile.setLimitPaymentsTransfersDayInUSD(50000.00f);
        sberPhysicalPersonProfile.setLimitPaymentsTransfersDayInEUR(3800.00f);
        sberPhysicalPersonProfile.setLimitTransfersToClientSberWithoutCommissionMonthInRUB(50000.00f);

        // установить проценты комиссий
        sberPhysicalPersonProfile.setPercentOfCommissionForPayHousingCommunalServices(2.0f);
        sberPhysicalPersonProfile.setPercentOfCommissionForTransferInRUB(1.0f);
        sberPhysicalPersonProfile.setPercentOfCommissionForTransferInUsdOrOtherCurrency(1.25f);

        // установить лимиты на суммы комиссий
        sberPhysicalPersonProfile.setLimitCommissionTransferInRUB(3000.00f);
        sberPhysicalPersonProfile.setLimitCommissionTransferInUsdOrEquivalentInOtherCurrency(100.00f);

        // и привязать профиль клиента к банку
        getClientProfiles().add(sberPhysicalPersonProfile);

        return sberPhysicalPersonProfile;
    }

    @Override
    // Предоставить обменный курс валют Сбера
    public ArrayList<Float> getExchangeRateBank(String currency, String currencyExchangeRate) {
        // TODO: Запрос к API банка.
        ArrayList<Float> exchangeRateBank = new ArrayList<>();
        // курс доллара
        if (currency.equals("USD")) {
            // в рублях
            if (currencyExchangeRate.equals("RUB")) {
                exchangeRateBank.add(99.30f);
                exchangeRateBank.add(90.10f);
            }
            // в евро
            if (currencyExchangeRate.equals("EUR")) {
                exchangeRateBank.add(0.93f);
                exchangeRateBank.add(0.89f);
            }
        }

        // курс евро
        if (currency.equals("EUR")) {
            // в рублях
            if (currencyExchangeRate.equals("RUB")) {
                exchangeRateBank.add(108.80f);
                exchangeRateBank.add(100.20f);
            }
            // в долларах
            if (currencyExchangeRate.equals("USD")) {
                exchangeRateBank.add(1.07f);
                exchangeRateBank.add(1.01f);
            }
        }

        // курс рубля
        if (currency.equals("RUB")) {
            // в долларах
            if (currencyExchangeRate.equals("USD")) {
                exchangeRateBank.add(0.010f);
                exchangeRateBank.add(0.006f);
            }
            // в евро
            if (currencyExchangeRate.equals("EUR")) {
                exchangeRateBank.add(0.0096f);
                exchangeRateBank.add(0.0083f);
            }
        }
        return exchangeRateBank;
    }

    @Override
    // Рассчитать комиссию за перевод клиенту моего банка Сбер
    public float getCommissionOfTransferToClientBank(PhysicalPersonProfile clientProfile, float sum, String fromCurrencyCode) {
        // по умолчанию комиссия 0
        float commission = 0;
        // если сумма перевода в рублях
        if (fromCurrencyCode.equals("RUB")) {
            // и если превышен лимит по переводам клиентам Сбера в месяц, то рассчитываем комиссию за перевод
            boolean exceededLimitTransfersToClientSberWithoutCommissionMonthInRUB = ((SberPhysicalPersonProfile)clientProfile).exceededLimitTransfersToClientSberWithoutCommissionMonthInRUB(sum);
            if (exceededLimitTransfersToClientSberWithoutCommissionMonthInRUB)
                commission = (sum / 100) * clientProfile.getPercentOfCommissionForTransferInRUB();
        }
        return commission;
    }

}
