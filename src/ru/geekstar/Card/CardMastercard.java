package ru.geekstar.Card;

public abstract class CardMastercard extends Card {

    private String currencyCodePaySystemUSD = "USD";

    private String currencyCodePaySystemEurozone = "EUR";


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

    @Override
    // Конвертировать в валюту по курсу платёжной системы
    public float convertToCurrencyExchangeRatePaySystem(float sum, String fromCurrencyCode, String toBillingCurrencyCode) {
        // запросить курс валюты покупки к курсу валюты биллинга (к $ или €) по курсу платёжной системы Mastercard
        float exchangeRateCurrencyToBillingCurrency = getExchangeRatePaySystem(fromCurrencyCode, toBillingCurrencyCode);
        // получаем сумму покупки в валюте биллинга умножив сумму покупки на обменный курс валюты биллинга ($ или €)
        float sumInBillingCurrency = sum * exchangeRateCurrencyToBillingCurrency;

        // возможна двойная конвертация через $ и затем ещё €

        return sumInBillingCurrency;
    }

    // Запросить обменный курс валют платёжной системы
    private float getExchangeRatePaySystem(String currency, String currencyExchangeRate) {
        // TODO: Запрос к API Mastercard
        float exchangeRate = 0;
        // курс лиры к доллару
        if (currency.equals("TRY") && currencyExchangeRate.equals("USD")) exchangeRate = 0.057f;
        // курс лиры к евро
        if (currency.equals("TRY") && currencyExchangeRate.equals("EUR")) exchangeRate = 0.059f;
        return exchangeRate;
    }


}
