package ru.geekstar.ClientProfile;

import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Card.SberVisaGold;

public class SberPhysicalPersonProfile extends PhysicalPersonProfile {

    private float limitTransfersToClientSberWithoutCommissionMonthInRUB;

    private float totalTransfersToClientSberWithoutCommissionMonthInRUB;


    public float getLimitTransfersToClientSberWithoutCommissionMonthInRUB() {
        return limitTransfersToClientSberWithoutCommissionMonthInRUB;
    }

    public void setLimitTransfersToClientSberWithoutCommissionMonthInRUB(float limitTransfersToClientSberWithoutCommissionMonthInRUB) {
        this.limitTransfersToClientSberWithoutCommissionMonthInRUB = limitTransfersToClientSberWithoutCommissionMonthInRUB;
    }

    public float getTotalTransfersToClientSberWithoutCommissionMonthInRUB() {
        return totalTransfersToClientSberWithoutCommissionMonthInRUB;
    }

    public void setTotalTransfersToClientSberWithoutCommissionMonthInRUB(float totalTransfersToClientSberWithoutCommissionMonthInRUB) {
        this.totalTransfersToClientSberWithoutCommissionMonthInRUB = totalTransfersToClientSberWithoutCommissionMonthInRUB;
    }


    // Проверить не превышен ли лимит по переводам клиентам Сбера в месяц
    public boolean exceededLimitTransfersToClientSberWithoutCommissionMonthInRUB(float sumTransfer) {
        if (totalTransfersToClientSberWithoutCommissionMonthInRUB + sumTransfer > limitTransfersToClientSberWithoutCommissionMonthInRUB) return true;
        return false;
    }

    // Обнулять сумму переводов клиентам Сбера каждое первое число месяца
    public void zeroingTotalTransfersToClientSberWithoutCommissionMonthInRUB() {
        // TODO: если 00:00 1-е число месяца, то totalTransfersToClientSberWithoutCommissionMonthInRUB = 0;
    }

    // Прибавить сумму перевода на карту к общей сумме всех переводов на карты клиентам Сбера без комиссии за месяц, чтобы контролировать лимит
    public void updateTotalTransfersToClientSberWithoutCommissionMonthInRUB(SberVisaGold toCard, float sumTransfer) {
        boolean isMyCard = isClientCard(toCard);
        boolean isCardMyBank = getBank().isCardBank(toCard);
        // если карта не моя, но моего банка, то есть клиент Сбера, то суммируем
        if (!isMyCard && isCardMyBank) totalTransfersToClientSberWithoutCommissionMonthInRUB += sumTransfer;
    }

    // Прибавить сумму перевода на счёт к общей сумме всех переводов на счета клиентам Сбера без комиссии за месяц, чтобы контролировать лимит
    public void updateTotalTransfersToClientSberWithoutCommissionMonthInRUB(SberSavingsAccount toAccount, float sumTransfer) {
        boolean isMyAccount = isClientAccount(toAccount);
        boolean isAccountMyBank = getBank().isAccountBank(toAccount);
        // если счёт не мой, но моего банка, то есть клиент Сбера, то суммируем
        if (!isMyAccount && isAccountMyBank) totalTransfersToClientSberWithoutCommissionMonthInRUB += sumTransfer;
    }

    @Override
    // Вывод всех операций по всем картам и счетам профиля физического лица в Сбере
    public void displayProfileTransactions() {
        // дополним метод уникальной информацией, присуще только Сберу
        System.out.println("Все операции по картам и счетам клиента " + getPhysicalPerson().getFirstName() + " " + getPhysicalPerson().getLastName() +
                " в " + getBank().getBankName() + "Банке");

        System.out.println("Переводы клиентам " + getBank().getBankName() +
                " без комиссии за текущий месяц: " + getTotalTransfersToClientSberWithoutCommissionMonthInRUB() + "₽ Доступный лимит: " +
                (getLimitTransfersToClientSberWithoutCommissionMonthInRUB() - getTotalTransfersToClientSberWithoutCommissionMonthInRUB()) + "₽ из " +
                getLimitTransfersToClientSberWithoutCommissionMonthInRUB() + "₽");

        // и вызываем родительскую версию метода
        super.displayProfileTransactions();
    }

}
