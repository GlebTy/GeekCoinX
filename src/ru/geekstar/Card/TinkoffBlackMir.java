package ru.geekstar.Card;

import ru.geekstar.ClientProfile.TinkoffPhysicalPersonProfile;
import ru.geekstar.Transaction.DepositingTransaction;

import java.time.LocalDateTime;

public class TinkoffBlackMir extends CardMir implements ICashbackCard {

    @Override
    public void payByCard(float sumPay, String buyProductOrService, String pinCode) {
        // вызовем родительскую версию метода
        super.payByCard(sumPay, buyProductOrService, pinCode);

        // и дополним метод уникальным поведением: начислим кэшбэк
        accumulateCashback(sumPay);
    }

    @Override
    public void accumulateCashback(float sumPay) {
        TinkoffPhysicalPersonProfile cardHolder = (TinkoffPhysicalPersonProfile) getCardHolder();
        int cashback = Math.round((sumPay/100) * cardHolder.getPercentCashbackOfSumPay());
        cardHolder.setCashback(cardHolder.getCashback() + cashback);

    }

    @Override
    public void depositingCashback2Card() {
        TinkoffPhysicalPersonProfile cardHolder = (TinkoffPhysicalPersonProfile) getCardHolder();
        // инициализировать транзакцию пополнения
        DepositingTransaction depositingTransaction = new DepositingTransaction();
        depositingTransaction.setLocalDateTime(LocalDateTime.now());
        depositingTransaction.setToCard(this);
        depositingTransaction.setSum(cardHolder.getCashback());
        depositingTransaction.setCurrencySymbol(getPayCardAccount().getCurrencySymbol());
        depositingTransaction.setTypeOperation("Зачисление кэшбэка");

        //выполняем пополнение
        boolean topUpStatus = getPayCardAccount().topUp(cardHolder.getCashback());

        //внести в транзакцию статус пополнения
        if(topUpStatus) {
            depositingTransaction.setStatusOperation("Зачисление кэшбэка прошло успешно");
        }
        else {
            depositingTransaction.setStatusOperation("Зачисление кэшбэка не прошло");
        }

        // внести в транзакцию баланс карты после пополнения
        depositingTransaction.setBalance(getPayCardAccount().getBalance());

        //добавить и привязать транзакцию пополнения к счету карты зачисления
        getPayCardAccount().getDepositingTransactions().add(depositingTransaction);

    }
}
