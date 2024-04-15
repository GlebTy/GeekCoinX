package ru.geekstar.Card;

import ru.geekstar.Card.IPaySystem.IMastercard;

public abstract class CardMastercard extends Card implements IMastercard {


    @Override
    // Запросить код валюты платёжной системы
    public String getCurrencyCodePaySystem(String country) {
        // по умолчанию null, потому что не во всех странах может использоваться данная платёжная система
        String billingCurrencyCode = null;
        // если покупка в Турции, то валюта биллинга в $
        if (country.equalsIgnoreCase("Турция")) billingCurrencyCode = currencyCodePaySystemUSD;
        // если покупка во Франции, то есть в Еврозоне, то валюта биллинга в €
        if (country.equalsIgnoreCase("Франция")) billingCurrencyCode = currencyCodePaySystemEurozone;

        return billingCurrencyCode;
    }


    // Запросить обменный курс валют платёжной системы
    public float getExchangeRatePaySystem(String currency, String currencyExchangeRate) {
        // TODO: Запрос к API Mastercard
        float exchangeRate = 0;
        // курс лиры к доллару
        if (currency.equals("TRY") && currencyExchangeRate.equals("USD")) exchangeRate = 0.057f;
        // курс лиры к евро
        if (currency.equals("TRY") && currencyExchangeRate.equals("EUR")) exchangeRate = 0.059f;
        return exchangeRate;
    }


}
