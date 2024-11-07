package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Card.IPaySystem.IMastercard;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import java.util.ArrayList;

public abstract class CardMastercard extends Card implements IMastercard {

    public CardMastercard(PhysicalPersonProfile cardHolder, PayCardAccount payCardAccount, String pinCode) {
        super(cardHolder, payCardAccount, pinCode);
    }

    @Override
    // Запросить код валюты платёжной системы
    public String getCurrencyCodePaySystem(String country) {
        // по умолчанию null, потому что не во всех странах может использоваться данная платёжная система
        String billingCurrencyCode = null;
        // если покупка в Турции, то валюта биллинга в $
        if (country.equalsIgnoreCase("Турция")) billingCurrencyCode = CURRENCY_CODE_PAY_SYSTEM_USD;
        // если покупка во Франции, то есть в Еврозоне, то валюта биллинга в €
        if (country.equalsIgnoreCase("Франция")) billingCurrencyCode = CURRENCY_CODE_PAY_SYSTEM_EUROZONE;

        return billingCurrencyCode;
    }


    // Запросить обменный курс валют платёжной системы
    public ArrayList<Float> getExchangeRatePaySystem(String currency, String currencyExchangeRate) {
        // TODO: Запрос к API Mastercard
        ArrayList<Float> exchangeRatePaySystem = new ArrayList<>();

        // курс лиры в долларах
        if (currency.equals("TRY") && currencyExchangeRate.equals("USD")) {
            exchangeRatePaySystem.add(0.029f); //курс покупки
            exchangeRatePaySystem.add(0.032f); //курс продажи
        }

        // курс лиры в евро
        if (currency.equals("TRY") && currencyExchangeRate.equals("EUR")) {
            exchangeRatePaySystem.add(0.027f); //курс покупки
            exchangeRatePaySystem.add(0.030f); //курс продажи
        }

        if (currency.equals("KZT") && currencyExchangeRate.equals("USD")) {
            exchangeRatePaySystem.add(0.0022f); //курс покупки
            exchangeRatePaySystem.add(0.0024f); //курс продаж
        }

        return exchangeRatePaySystem;
    }


}
