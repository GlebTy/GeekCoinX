package ru.geekstar.Card;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Account.SberPayCardAccount;
import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Bank.Bank;
import ru.geekstar.Bank.Sberbank;
import ru.geekstar.Card.IPaySystem.IPaySystem;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.Transaction.DepositingTransaction;
import ru.geekstar.Transaction.PayTransaction;
import ru.geekstar.Transaction.TransferTransaction;

import java.time.LocalDateTime;

public abstract class Card implements IPaySystem {

    private Bank bank;

    private PhysicalPersonProfile cardHolder;

    private PayCardAccount payCardAccount;

    private String numberCard;

    private String statusCard;

    private String pinCode;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public PhysicalPersonProfile getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(PhysicalPersonProfile cardHolder) {
        this.cardHolder = cardHolder;
    }

    public PayCardAccount getPayCardAccount() {
        return payCardAccount;
    }

    // Привязываем платёжный счёт к карте
    public void setPayCardAccount(PayCardAccount payCardAccount) {
        this.payCardAccount = payCardAccount;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        String number = numberCard.replace(" ", "");
        String regex = "[0-9]{16}";

        if (number.matches(regex)) this.numberCard = numberCard;
        else System.out.println("Недопустимый номер карты");
    }

    public String getStatusCard() {
        return statusCard;
    }

    public void setStatusCard(String statusCard) {
        this.statusCard = statusCard;
    }

    // Оплатить картой
    public void payByCard(float sumPay, String buyProductOrService, String pinCode) {
        // инициализировать транзакцию оплаты
        PayTransaction payTransaction = new PayTransaction();
        payTransaction.setLocalDateTime(LocalDateTime.now());
        payTransaction.setFromCard(this);
        payTransaction.setSum(sumPay);
        payTransaction.setCurrencySymbol(payCardAccount.getCurrencySymbol());
        payTransaction.setTypeOperation("Покупка");
        payTransaction.setBuyProductOrService(buyProductOrService);

        // рассчитать комиссию при оплате
        float commission = bank.getCommission(cardHolder, sumPay, buyProductOrService);

        // внести в транзакцию данные о комиссии
        payTransaction.setCommission(commission);

        // запросить разрешение банка на проведение операции с блокированием суммы оплаты и комиссии
        String authorization = bank.authorization(this, payTransaction.getTypeOperation(), sumPay, commission, pinCode);
        // извлекаем массив строк разделяя их символом @
        String[] authorizationData = authorization.split("@");
        // извлекаем код авторизации
        String authorizationCode = authorizationData[0];
        payTransaction.setAuthorizationCode(authorizationCode);
        // извлекаем сообщение авторизации
        String authorizationMessage = authorizationData[1];  // "Success: Авторизация прошла успешно"
        // извлекаем статус из сообщения авторизации
        String authorizationStatus = authorizationMessage.substring(0, authorizationMessage.indexOf(":"));
        // если разрешение получено, то выполняем списание зарезервированной суммы и комиссии со счёта карты
        if (authorizationStatus.equalsIgnoreCase("Success")) {
            boolean writeOffBlockedSum = payCardAccount.writeOffBlockedSum(sumPay + commission);
            if (writeOffBlockedSum) {
                // внести в транзакцию статус оплаты
                payTransaction.setStatusOperation("Оплата прошла успешно");

                // TODO: перевести сумму на счёт магазина, а комиссию на счёт банка

                // прибавить сумму оплаты к общей сумме совершенных оплат и переводов за сутки, чтобы контролировать лимиты
                getCardHolder().updateTotalPaymentsTransfersDay(sumPay, payCardAccount.getCurrencyCode());
            } else payTransaction.setStatusOperation("Оплата не прошла");
        } else {
            // иначе выводим сообщение о статусе авторизации, чтобы понимать что пошло не так
            String authorizationStatusMessage = authorizationMessage.substring(authorizationMessage.indexOf(":") + 2);
            payTransaction.setStatusOperation(authorizationStatusMessage);
        }

        // внести в транзакцию баланс счёта карты после оплаты
        payTransaction.setBalance(getPayCardAccount().getBalance());

        // добавить и привязать транзакцию оплаты к счёту карты
        payCardAccount.getPayTransactions().add(payTransaction);
    }

    // Оплатить картой за рубежом
    public void payByCard(float sumPay, String buyProductOrService, String country, String pinCode) {
        // по названию страны определяем валюту покупки
        String currencyPayCode = bank.getCurrencyCode(country);
        // по названию страны определяем валюту биллинга - это валюта платёжной системы
        String billingCurrencyCode = getCurrencyCodePaySystem(country);

        // если валюта покупки и валюта биллинга НЕ совпадают, то конвертируем сумму покупки в валюту платёжной системы по курсу платёжной системы
        // если валюты совпадают, то конвертация не выполняется
        float sumPayInBillingCurrency = convertToCurrencyExchangeRatePaySystem(sumPay, currencyPayCode, billingCurrencyCode);

        // если валюта биллинга и валюта счёта карты не совпадают, то конвертируем сумму покупки в валюте биллинга в валюту карты по курсу нашего банка
        // если валюты совпадают, то конвертация не выполняется
        String cardCurrencyCode = getPayCardAccount().getCurrencyCode();
        float sumPayInCardCurrency = bank.convertToCurrencyExchangeRateBank(sumPayInBillingCurrency, billingCurrencyCode, cardCurrencyCode);

        // округлим дробную часть до двух знаков после запятой
        sumPayInCardCurrency = bank.round(sumPayInCardCurrency);

        // приведя сумму покупки к валюте карты вызываем метод оплаты по умолчанию
        payByCard(sumPayInCardCurrency, buyProductOrService, pinCode);

    }


    // Перевести с карты на карту
    public void transferCard2Card(Card toCard, float sumTransfer) {
        // инициализировать транзакцию перевода
        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setLocalDateTime(LocalDateTime.now());
        transferTransaction.setFromCard(this);
        transferTransaction.setToCard(toCard);
        transferTransaction.setSum(sumTransfer);
        transferTransaction.setCurrencySymbol(payCardAccount.getCurrencySymbol());
        transferTransaction.setTypeOperation("Перевод на карту");

        String fromCurrencyCode = payCardAccount.getCurrencyCode();
        // рассчитать комиссию за перевод на свою или чужую карту моего или другого банка
        float commission = bank.getCommission(cardHolder, fromCurrencyCode, sumTransfer, toCard);
        // внести в транзакцию перевода данные о комиссии
        transferTransaction.setCommission(commission);

        // запросить разрешение банка на проведение операции с блокированием суммы перевода и комиссии
        String authorization = bank.authorization(this, transferTransaction.getTypeOperation(), sumTransfer, commission, null);
        String[] authorizationData = authorization.split("@");
        String authorizationCode = authorizationData[0];
        transferTransaction.setAuthorizationCode(authorizationCode);
        String authorizationMessage = authorizationData[1];
        String authorizationStatus = authorizationMessage.substring(0, authorizationMessage.indexOf(":"));

        // если разрешение получено, то выполняем списание зарезервированной суммы перевода и комиссии со счёта карты
        if (authorizationStatus.equalsIgnoreCase("Success")) {
            boolean writeOffReservedAmountStatus = payCardAccount.writeOffBlockedSum(sumTransfer + commission);
            if (writeOffReservedAmountStatus) {
                // внести в транзакцию перевода статус списания
                transferTransaction.setStatusOperation("Списание прошло успешно");

                // инициализировать транзакцию пополнения
                DepositingTransaction depositingTransaction = new DepositingTransaction();
                depositingTransaction.setLocalDateTime(LocalDateTime.now());
                depositingTransaction.setFromCard(this);
                depositingTransaction.setToCard(toCard);
                depositingTransaction.setSum(sumTransfer);
                depositingTransaction.setCurrencySymbol(toCard.getPayCardAccount().getCurrencySymbol());
                depositingTransaction.setTypeOperation("Пополнение с карты");
                depositingTransaction.setAuthorizationCode(authorizationCode);

                String toCurrencyCode = toCard.getPayCardAccount().getCurrencyCode();
                sumTransfer = bank.convertToCurrencyExchangeRateBank(sumTransfer, fromCurrencyCode, toCurrencyCode);

                // зачислить на карту
                boolean topUpStatus = toCard.getPayCardAccount().topUp(sumTransfer);
                if (topUpStatus) {
                    // внести в транзакцию пополнения статус пополнения
                    depositingTransaction.setStatusOperation("Пополнение прошло успешно");
                    // внести в транзакцию пополнения баланс карты после пополнения
                    depositingTransaction.setBalance(toCard.getPayCardAccount().getBalance());
                    // добавить и привязать транзакцию пополнения к счёту карты зачисления
                    toCard.getPayCardAccount().getDepositingTransactions().add(depositingTransaction);

                    // внести в транзакцию перевода статус перевода
                    transferTransaction.setStatusOperation("Перевод прошёл успешно");

                    // прибавить сумму перевода к общей сумме совершенных оплат и переводов за сутки, чтобы контролировать лимиты
                    getCardHolder().updateTotalPaymentsTransfersDay(sumTransfer, fromCurrencyCode, toCard);

                    // TODO: и перевести комиссию на счёт банка

                } else transferTransaction.setStatusOperation("Перевод не прошёл");
            } else transferTransaction.setStatusOperation("Списание не прошло");
        } else {
            // иначе выводим сообщение о статусе авторизации, чтобы понимать что пошло не так
            String authorizationStatusMessage = authorizationMessage.substring(authorizationMessage.indexOf(":") + 1);
            transferTransaction.setStatusOperation(authorizationStatusMessage);
        }

        // внести в транзакцию перевода баланс карты после списания
        transferTransaction.setBalance(getPayCardAccount().getBalance());

        // добавить и привязать транзакцию перевода к счёту карты списания
        payCardAccount.getTransferTransactions().add(transferTransaction);

    }

    // Перевести с карты на счёт
    public void transferCard2Account(Account toAccount, float sumTransfer) {
        // инициализировать транзакцию перевода
        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setLocalDateTime(LocalDateTime.now());
        transferTransaction.setFromCard(this);
        transferTransaction.setToAccount(toAccount);
        transferTransaction.setSum(sumTransfer);
        transferTransaction.setCurrencySymbol(payCardAccount.getCurrencySymbol());
        transferTransaction.setTypeOperation("Перевод на счёт");

        String fromCurrencyCode = payCardAccount.getCurrencyCode();
        // рассчитать комиссию за перевод на свой или чужой счёт моего или другого банка
        float commission = bank.getCommission(cardHolder, fromCurrencyCode, sumTransfer, toAccount);

        // внести в транзакцию данные о комиссии
        transferTransaction.setCommission(commission);

        // проверить баланс карты и достаточно ли денег
        boolean checkBalance = payCardAccount.checkBalance(sumTransfer + commission);
        if (checkBalance) {
            // проверить не превышен ли лимит по оплатам и переводам в сутки
            boolean exceededLimitPaymentsTransfersDay = cardHolder.exceededLimitPaymentsTransfersDay(sumTransfer, fromCurrencyCode);
            if (!exceededLimitPaymentsTransfersDay) {
                // если не превышен, то выполняем списание суммы перевода и комиссии со счёта карты
                boolean withdrawalStatus = payCardAccount.withdrawal(sumTransfer + commission);
                if (withdrawalStatus) {
                    // внести в транзакцию статус списания
                    transferTransaction.setStatusOperation("Списание прошло успешно");
                    // инициализировать транзакцию пополнения
                    DepositingTransaction depositingTransaction = new DepositingTransaction();
                    depositingTransaction.setLocalDateTime(LocalDateTime.now());
                    depositingTransaction.setFromCard(this);
                    depositingTransaction.setToAccount(toAccount);
                    depositingTransaction.setTypeOperation("Пополнение с карты");
                    depositingTransaction.setSum(sumTransfer);
                    depositingTransaction.setCurrencySymbol(toAccount.getCurrencySymbol());

                    String toCurrencyCode = toAccount.getCurrencyCode();
                    if(!fromCurrencyCode.equalsIgnoreCase(toCurrencyCode)) sumTransfer = bank.convertToCurrencyExchangeRateBank(sumTransfer, fromCurrencyCode, toCurrencyCode);


                    // и зачислить на счёт
                    boolean topUpStatus = toAccount.topUp(sumTransfer);
                    if (topUpStatus) {
                        // внести в транзакцию пополнения статус зачисления
                        depositingTransaction.setStatusOperation("Пополнение прошло успешно");
                        // внести в транзакцию пополнения баланс счёта после зачисления
                        depositingTransaction.setBalance(toAccount.getBalance());
                        // добавить и привязать транзакцию пополнения к счёту зачисления
                        toAccount.getDepositingTransactions().add(depositingTransaction);

                        // внести в транзакцию перевода статус перевода
                        transferTransaction.setStatusOperation("Перевод прошёл успешно");
                        // прибавить сумму перевода к общей сумме совершённых оплат и переводов за сутки, чтобы контролировать лимиты
                        getCardHolder().updateTotalPaymentsTransfersDay(sumTransfer, fromCurrencyCode, toAccount);

                        // TODO: и перевести комиссию на счёт банка

                    } else transferTransaction.setStatusOperation("Перевод не прошёл");
                } else transferTransaction.setStatusOperation("Списание не прошло");
            } else transferTransaction.setStatusOperation("Лимит по сумме операций в день превышен");
        } else transferTransaction.setStatusOperation("Недостаточно средств");

        // внести в транзакцию баланс карты после списания
        transferTransaction.setBalance(getPayCardAccount().getBalance());
        // добавить и привязать транзакцию перевода к счёту карты списания
        payCardAccount.getTransferTransactions().add(transferTransaction);
    }

    // Внести наличные на карту
    public void depositingCash2Card(float sumDepositing) {
        // инициализировать транзакцию пополнения
        DepositingTransaction depositingTransaction = new DepositingTransaction();
        depositingTransaction.setLocalDateTime(LocalDateTime.now());
        depositingTransaction.setToCard(this);
        depositingTransaction.setSum(sumDepositing);
        depositingTransaction.setCurrencySymbol(payCardAccount.getCurrencySymbol());
        depositingTransaction.setTypeOperation("Внесение наличных");

        // запросить разрешение банка на проведение операции с проверкой статуса карты
        String authorization = bank.authorizationStatusCard(this);
        // извлекаем массив строк разделяя их символом @
        String[] authorizationData = authorization.split("@");
        // извлекаем код авторизации
        String authorizationCode = authorizationData[0];
        // вносим в транзакцию код авторизации
        depositingTransaction.setAuthorizationCode(authorizationCode);
        // извлекаем сообщение из авторизации
        String authorizationMessage = authorizationData[1];
        // извлекаем статус из сообщения авторизации
        String authorizationStatus = authorizationMessage.substring(0, authorizationMessage.indexOf(":"));

        // если разрешение получено, то выполняем пополнение
        if (authorizationStatus.equalsIgnoreCase("Success")) {
            boolean topUpStatus = payCardAccount.topUp(sumDepositing);
            if (topUpStatus) {
                // внести в транзакцию статус пополнения
                depositingTransaction.setStatusOperation("Внесение наличных прошло успешно");
            } else depositingTransaction.setStatusOperation("Внесение наличных не прошло");
        } else {
            // иначе выводим сообщение о статусе авторизации, чтобы понимать что пошло не так
            String authorizationStatusMessage = authorizationMessage.substring(authorizationMessage.indexOf(":"));
            depositingTransaction.setStatusOperation(authorizationStatusMessage);
        }

        // внести в транзакцию баланс карты после пополнения
        depositingTransaction.setBalance(getPayCardAccount().getBalance());

        // добавить и привязать транзакцию пополнения к счёту карты зачисления
        payCardAccount.getDepositingTransactions().add(depositingTransaction);
    }

    // Пополнить карту с карты
    public void depositingCardFromCard(Card fromCard, float sumDepositing) {
        // то есть перевести с карты на карту
        fromCard.transferCard2Card(this, sumDepositing);
    }

    // Пополнить карту со счёта
    public void depositingCardFromAccount(Account fromAccount, float sumDepositing) {
        // то есть перевести со счёта на карту
        fromAccount.transferAccount2Card(this, sumDepositing);
    }

    // Вывести транзакции по счёту карты
    public void displayCardTransactions() {
        payCardAccount.displayAccountTransactions();
    }

}
