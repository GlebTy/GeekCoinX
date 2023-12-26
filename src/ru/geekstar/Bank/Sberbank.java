package ru.geekstar.Bank;

import ru.geekstar.Account.SberPayCardAccount;
import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Card.SberVisaGold;
import ru.geekstar.ClientProfile.SberPhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public class Sberbank extends Bank {

    // Зарегистрировать профиль физ лица
    public SberPhysicalPersonProfile registerClientProfile(PhysicalPerson physicalPerson) {
        // создать профиль клиента
        SberPhysicalPersonProfile sberPhysicalPersonProfile = new SberPhysicalPersonProfile();
        sberPhysicalPersonProfile.setBank(this);
        sberPhysicalPersonProfile.setPhysicalPerson(physicalPerson);

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
        addClientProfile(sberPhysicalPersonProfile);

        return sberPhysicalPersonProfile;
    }

    // Открыть карту
    public SberVisaGold openCard(SberPhysicalPersonProfile clientProfile, SberVisaGold card, String currencyCode) {
        // установить свойства карты
        card.setBank(this);
        card.setNumberCard(generateNumberCard());
        card.setCardHolder(clientProfile);

        //открыть платёжный счёт
        SberPayCardAccount payCardAccount = openAccount(clientProfile, new SberPayCardAccount(), currencyCode);

        // привязать карту к платёжному счёту
        payCardAccount.addCard(card);

        // привязать платёжный счёт к карте
        card.setPayCardAccount(payCardAccount);
        card.setStatusCard("Активна");

        // привязать карту к профилю клиента
        clientProfile.addCard(card);

        return card;
    }

    // Открыть платёжный счёт
    private SberPayCardAccount openAccount(SberPhysicalPersonProfile clientProfile, SberPayCardAccount account, String currencyCode) {
        // установить свойства платёжного счёта
        account.setBank(this);
        account.setNumberAccount(generateNumberAccount());
        account.setAccountHolder(clientProfile);
        account.setCurrencyCode(currencyCode);
        account.setCurrencySymbol(currencyCode);

        // привязать платёжный счёт к профилю клиента
        clientProfile.addAccount(account);

        return account;
    }

    // Открыть сберегательный счёт
    public SberSavingsAccount openAccount(SberPhysicalPersonProfile clientProfile, SberSavingsAccount account, String currencyCode) {
        // установить свойства сберегательного счёта
        account.setBank(this);
        account.setNumberAccount(generateNumberAccount());
        account.setAccountHolder(clientProfile);
        account.setCurrencyCode(currencyCode);
        account.setCurrencySymbol(currencyCode);

        // привязать сберегательный счёт к профилю клиента
        clientProfile.addAccount(account);

        return account;
    }

    @Override
    // Предоставить обменный курс валют Сбера
    public float getExchangeRateBank(String currency, String currencyExchangeRate) {
        // TODO: Запрос к API банка
        float exchangeRateBank = 0;
        // курс доллара к рублю
        if (currency.equals("USD") && currencyExchangeRate.equals("RUB")) exchangeRateBank = 60.48f;
        // курс евро к рублю
        if (currency.equals("EUR") && currencyExchangeRate.equals("RUB")) exchangeRateBank = 61.35f;
        return exchangeRateBank;
    }

    @Override
    // Рассчитать комиссию за перевод клиенту моего банка Сбер
    public float getCommissionOfTransferToClientBank(SberPhysicalPersonProfile clientProfile, float sum, String fromCurrencyCode) {
        // по умолчанию комиссия 0
        float commission = 0;
        // если сумма перевода в рублях
        if (fromCurrencyCode.equals("RUB")) {
            // и если превышен лимит по переводам клиентам Сбера в месяц, то рассчитываем комиссию за перевод
            boolean exceededLimitTransfersToClientSberWithoutCommissionMonthInRUB = clientProfile.exceededLimitTransfersToClientSberWithoutCommissionMonthInRUB(sum);
            if (exceededLimitTransfersToClientSberWithoutCommissionMonthInRUB)
                commission = (sum / 100) * clientProfile.getPercentOfCommissionForTransferInRUB();
        }
        return commission;
    }

}
