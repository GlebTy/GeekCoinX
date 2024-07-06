package ru.geekstar.Card.IPaySystem;

import java.util.ArrayList;

public interface IPaySystem {

    // Конвертировать в валюту по курсу платёжной системы
    default float convertToCurrencyExchangeRatePaySystem(float sum, String fromCurrencyCode, String toBillingCurrencyCode) {
        if(!fromCurrencyCode.equalsIgnoreCase(toBillingCurrencyCode)) {
            // запросить курс валюты покупки к курсу валюты биллинга ($) по курсу платёжной системы VISA
           ArrayList<Float> exchangeRateCurrencyToBillingCurrency = getExchangeRatePaySystem(fromCurrencyCode, toBillingCurrencyCode);
            // получаем сумму покупки в валюте биллинга умножив сумму покупки на обменный курс валюты биллинга ($)
             sum *= exchangeRateCurrencyToBillingCurrency.get(0);

        }
        return sum;
    }

    // Запросить код валюты платёжной системы
    String getCurrencyCodePaySystem(String country);

    ArrayList<Float> getExchangeRatePaySystem(String currency, String currencyExchangeRate);
}
