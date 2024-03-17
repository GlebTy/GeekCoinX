package ru.geekstar.Card;

public abstract class CardVisa extends Card {

    private String currencyCodePaySystemUSD = "USD";

    @Override
    // Запросить код валюты платёжной системы
    public String getCurrencyCodePaySystem(String country) {
        // по умолчанию null, потому что не во всех странах может использоваться данная платёжная система
        String billingCurrencyCode = null;
        // если покупка в Турции, то валюта биллинга в $
        if (country.equalsIgnoreCase("Турция")) billingCurrencyCode = currencyCodePaySystemUSD;

        return billingCurrencyCode;
    }

    @Override
    // Конвертировать в валюту по курсу платёжной системы
    public float convertToCurrencyExchangeRatePaySystem(float sum, String fromCurrencyCode, String toBillingCurrencyCode) {
        // запросить курс валюты покупки к курсу валюты биллинга ($) по курсу платёжной системы VISA
        float exchangeRateCurrencyToBillingCurrency = getExchangeRatePaySystem(fromCurrencyCode, toBillingCurrencyCode);
        // получаем сумму покупки в валюте биллинга умножив сумму покупки на обменный курс валюты биллинга ($)
        float sumInBillingCurrency = sum * exchangeRateCurrencyToBillingCurrency;

        return sumInBillingCurrency;
    }

    // Запросить обменный курс валют платёжной системы
    private float getExchangeRatePaySystem(String currency, String currencyExchangeRate) {
        // TODO: Запрос к API Visa
        float exchangeRate = 0;
        // курс лиры к доллару
        if (currency.equals("TRY") && currencyExchangeRate.equals("USD")) exchangeRate = 0.056f;
        return exchangeRate;
    }

}
