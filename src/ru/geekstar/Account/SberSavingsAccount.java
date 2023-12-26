package ru.geekstar.Account;

import ru.geekstar.Card.SberVisaGold;

public class SberSavingsAccount extends SavingsAccount {

    @Override
    public void transferAccount2Card(SberVisaGold toCard, float sumTransfer) {
        // вызываем родительскую версию метода
        super.transferAccount2Card(toCard, sumTransfer);

        // и дополняем метод уникальным поведением
        // прибавим сумму перевода к общей сумме всех переводов клиентам Сбера без комиссии за месяц для контроля лимита
        getAccountHolder().updateTotalTransfersToClientSberWithoutCommissionMonthInRUB(toCard, sumTransfer);
    }

}
