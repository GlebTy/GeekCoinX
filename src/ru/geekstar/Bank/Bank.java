package ru.geekstar.Bank;

import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Card.Card;
import ru.geekstar.Card.SberVisaGold;
import ru.geekstar.ClientProfile.SberPhysicalPersonProfile;

public class Bank {

    private String bankName;

    private SberPhysicalPersonProfile[] clientProfiles = new SberPhysicalPersonProfile[5];

    private int countClientProfiles;


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public SberPhysicalPersonProfile[] getClientProfiles() {
        return clientProfiles;
    }

    public void setClientProfiles(SberPhysicalPersonProfile[] clientProfiles) {
        this.clientProfiles = clientProfiles;
    }

    public int getCountClientProfiles() {
        return countClientProfiles;
    }

    public void setCountClientProfiles(int countClientProfiles) {
        this.countClientProfiles = countClientProfiles;
    }

    // Добавить профиль клиента
    public void addClientProfile(SberPhysicalPersonProfile clientProfile) {
        clientProfiles[countClientProfiles++] = clientProfile;
    }

    // Сгенерировать номер карты 3546 0957 9843 7845
    public String generateNumberCard() {
        byte lengthNumberCard = 20;
        StringBuffer numberCardBuffer = new StringBuffer();
        for (byte i = 1; i < lengthNumberCard; i++) {   // i = 1, чтобы не было пробела в начале, потому что при делении 0 на 5 будет 0
            if (i % 5 != 0) numberCardBuffer.append((byte) (Math.random() * 10));
            else numberCardBuffer.append(' ');
        }
        return numberCardBuffer.toString();
    }

    // Сгенерировать номер счёта 35467095799843178451
    public String generateNumberAccount() {
        byte lengthNumberAccount = 20;
        StringBuffer numberAccountBuffer = new StringBuffer();
        for (byte i = 0; i < lengthNumberAccount; i++) {
            numberAccountBuffer.append((byte) (Math.random() * 10));
        }
        return numberAccountBuffer.toString();
    }

    // Провести авторизацию и выдать разрешение на проведение операции
    public String authorization(SberVisaGold card, String typeOperation, float sum, float commission) {
        // сгенерировать код авторизации
        String authorizationCode = generateAuthorizationCode();

        String authorizationMessage;
        // проверить статус карты
        boolean statusCard = card.getStatusCard().equalsIgnoreCase("Активна") ? true : false;
        if (statusCard) {
            authorizationMessage = "Success: Карта активна";

            // если тип операции покупка или перевод, то проверяем баланс и блокируем сумму покупки или перевода с комиссией
            if (typeOperation.contains("Покупка") || typeOperation.contains("Перевод")) {
                // проверяем баланс и хватит ли нам денег с учётом комиссии
                boolean checkBalance = card.getPayCardAccount().checkBalance(sum + commission);
                if (checkBalance) {
                    // проверяем не превышен ли лимит по оплатам и переводам в сутки
                    boolean exceededLimitPaymentsTransfersDay = card.getCardHolder().exceededLimitPaymentsTransfersDay(sum, card.getPayCardAccount().getCurrencyCode());
                    if (!exceededLimitPaymentsTransfersDay) {
                        // блокируем сумму операции и комиссию на балансе счёта карты
                        boolean reserveAmountStatus = card.getPayCardAccount().blockSum(sum + commission);
                        authorizationMessage = reserveAmountStatus ? "Success: Авторизация прошла успешно" : "Failed: Сбой авторизации";
                    } else authorizationMessage = "Failed: Превышен лимит по оплатам и переводам в сутки";
                } else authorizationMessage = "Failed: Недостаточно средств, пополните карту";
            }
        } else authorizationMessage = "Failed: Карта заблокирована";

        // вернуть код и сообщение о статусе авторизации
        return authorizationCode + "@" + authorizationMessage;
    }

    private String generateAuthorizationCode() {
        byte lengthAuthorizationCode = 6;
        StringBuffer authorizationCodeBuffer = new StringBuffer();
        for (byte i = 0; i < lengthAuthorizationCode; i++) {
            authorizationCodeBuffer.append((byte) (Math.random() * 10));
        }
        return authorizationCodeBuffer.toString();
    }

    // Рассчитать комиссию при оплате
    public float getCommission(SberPhysicalPersonProfile cardHolder, float sumPay, String buyProductOrService) {
        float commission = buyProductOrService.equalsIgnoreCase("ЖКХ") ? (sumPay/100) * cardHolder.getPercentOfCommissionForPayHousingCommunalServices() : 0;
        return commission;
    }

    // Рассчитать комиссию за перевод на свою или чужую карту моего или другого банка
    public float getCommission(SberPhysicalPersonProfile clientProfile, String fromCurrencyCode, float sum, SberVisaGold toCard) {
        // запросить моя ли карта, на которую выполняем перевод
        boolean isMyCard = clientProfile.isClientCard(toCard);
        // запросить моего ли банка карта, на которую выполняем перевод
        boolean isCardMyBank = isCardBank(toCard);

        float commission = 0;
        // если карта зачисления не моя, но моего банка, то вычисляем комиссию за перевод клиенту моего банка
        if (!isMyCard && isCardMyBank) commission = getCommissionOfTransferToClientBank(clientProfile, sum, fromCurrencyCode);
        // если карта зачисления не моя и не моего банка, то вычисляем комиссию за перевод клиенту другого банка
        if (!isMyCard && !isCardMyBank) commission = getCommissionOfTransferToClientAnotherBank(clientProfile, sum, fromCurrencyCode);

        // проверить превышен ли лимит на сумму комиссии. Если да, то ограничим сумму комиссии заданным лимитом
        commission = exceededLimitCommission(clientProfile, fromCurrencyCode, commission);

        return commission;
    }

    // Рассчитать комиссию за перевод на свою или чужую карту моего или другого банка
    public float getCommission(SberPhysicalPersonProfile clientProfile, String fromCurrencyCode, float sum, SberSavingsAccount toAccount) {
        // запросить мой ли счёт, на который выполняем перевод
        boolean isMyAccount = clientProfile.isClientAccount(toAccount);
        // запросить моего ли банка счёт, на который выполняем перевод
        boolean isAccountMyBank = isAccountBank(toAccount);

        float commission = 0;
        // если счёт зачисления не мой, но моего банка, то вычисляем комиссию за перевод клиенту моего банка
        if (!isMyAccount && isAccountMyBank) commission = getCommissionOfTransferToClientBank(clientProfile, sum, fromCurrencyCode);
        // если счёт зачисления не мой и не моего банка, то вычисляем комиссию за перевод клиенту другого банка
        if (!isMyAccount && !isAccountMyBank) commission = getCommissionOfTransferToClientAnotherBank(clientProfile, sum, fromCurrencyCode);

        // проверить превышен ли лимит на сумму комиссии. Если да, то ограничим сумму комиссии заданным лимитом
        commission = exceededLimitCommission(clientProfile, fromCurrencyCode, commission);

        return commission;
    }

    // Проверить превышен ли лимит на сумму комиссии? Если да, то ограничим сумму комиссии лимитом
    private float exceededLimitCommission(SberPhysicalPersonProfile clientProfile, String fromCurrencyCode, float commission) {
        // если комиссия превышает лимит за перевод в рублях, то ограничим комиссию лимитом в рублях, то есть максимально возможной суммой комиссии установленной банком
        if (fromCurrencyCode.equals("RUB") && commission > clientProfile.getLimitCommissionTransferInRUB()) commission = clientProfile.getLimitCommissionTransferInRUB();
        // иначе если комиссия превышает лимит за перевод в $, то ограничим комиссию лимитом в $
        else if (fromCurrencyCode.equals("USD") && commission > clientProfile.getLimitCommissionTransferInUsdOrEquivalentInOtherCurrency())
            commission = clientProfile.getLimitCommissionTransferInUsdOrEquivalentInOtherCurrency();
        // иначе если другая валюта, то по аналогии
        else {
            // рассчитать лимит комиссии в другой валюте путём конвертации лимита в $ в эквивалентную сумму в другой валюте
            float limitCommissionTransferInCurrency = convertToCurrencyExchangeRateBank(
                    clientProfile.getLimitCommissionTransferInUsdOrEquivalentInOtherCurrency(),
                    "USD",
                    fromCurrencyCode
            );
            // если комиссия превышает лимит за перевод в другой валюте, то ограничим комиссию лимитом в этой валюте
            if (commission > limitCommissionTransferInCurrency) commission = limitCommissionTransferInCurrency;
        }

        return commission;
    }

    // Рассчитать комиссию за перевод клиенту моего банка. Переопределим метод в дочерних классах конкретных банков
    public float getCommissionOfTransferToClientBank(SberPhysicalPersonProfile clientProfile, float sum, String fromCurrencyCode) {
        return 0;
    }

    // Рассчитать комиссию за перевод клиенту другого банка
    private float getCommissionOfTransferToClientAnotherBank(SberPhysicalPersonProfile clientProfile, float sum, String fromCurrencyCode) {
        // можно не инициализировать, так как в любом случае будет результат благодаря ветке else
        float commission;
        // рассчитаем комиссию за перевод в рублях
        if (fromCurrencyCode.equals("RUB")) commission = (sum / 100) * clientProfile.getPercentOfCommissionForTransferInRUB();
        // рассчитаем комиссию за перевод в $ или другой валюте
        else commission = (sum / 100) * clientProfile.getPercentOfCommissionForTransferInUsdOrOtherCurrency();

        return commission;

    }

    // Проверить карта моего ли банка
    public boolean isCardBank(SberVisaGold card) {
        if (card.getBank().getBankName().equals(getBankName())) return true;
        return false;
    }

    // Проверить счёт моего ли банка
    public boolean isAccountBank(SberSavingsAccount account) {
        if (account.getBank().getBankName().equals(getBankName())) return true;
        return false;
    }

    // Запросить код валюты по названию страны
    public String getCurrencyCode(String country) {
        String currencyPayCode = null;
        if (country.equalsIgnoreCase("Турция")) currencyPayCode = "TRY";
        if (country.equalsIgnoreCase("Франция")) currencyPayCode = "EUR";
        return currencyPayCode;
    }

    // Конвертировать в валюту по курсу банка
    public float convertToCurrencyExchangeRateBank(float sum, String fromCurrencyCode, String toCurrencyCode) {
        float exchangeRateToCardCurrency = getExchangeRateBank(fromCurrencyCode, toCurrencyCode);
        float sumInCardCurrency = sum * exchangeRateToCardCurrency;

        return sumInCardCurrency;
    }

    // Предоставить обменный курс валют банка. Переопределим в каждом банке, потому что у каждого банка свой курс
    public float getExchangeRateBank(String currency, String currencyExchangeRate) {
        return 0;
    }

    public float round(float sum) {
        return Math.round(sum * 100) / 100;
    }


}
