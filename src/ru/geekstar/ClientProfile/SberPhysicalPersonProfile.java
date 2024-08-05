package ru.geekstar.ClientProfile;

import ru.geekstar.Account.Account;
import ru.geekstar.Bank.Bank;
import ru.geekstar.Card.Card;
import ru.geekstar.IOFile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public class SberPhysicalPersonProfile extends PhysicalPersonProfile {
    private int bonuses;

    private float percentBonusOfSumpay;

    private float limitTransfersToClientSberWithoutCommissionMonthInRUB;

    private float totalTransfersToClientSberWithoutCommissionMonthInRUB;

    public float getPercentBonusOfSumpay() {
        return percentBonusOfSumpay;
    }

    public int getBonuses() {
        return bonuses;
    }

    public void setBonuses(int bonuses) {
        this.bonuses = bonuses;
    }

    public void setPercentBonusOfSumpay(float percentBonusOfSumpay) {
        this.percentBonusOfSumpay = percentBonusOfSumpay;
    }

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

    public SberPhysicalPersonProfile(Bank bank, PhysicalPerson physicalPerson) {
        super(bank,physicalPerson);

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
    public void updateTotalTransfersToClientSberWithoutCommissionMonthInRUB(Card toCard, float sumTransfer) {
        boolean isMyCard = isClientCard(toCard);
        boolean isCardMyBank = getBank().isCardBank(toCard);
        // если карта не моя, но моего банка, то есть клиент Сбера, то суммируем
        if (!isMyCard && isCardMyBank) totalTransfersToClientSberWithoutCommissionMonthInRUB += sumTransfer;
    }

    // Прибавить сумму перевода на счёт к общей сумме всех переводов на счета клиентам Сбера без комиссии за месяц, чтобы контролировать лимит
    public void updateTotalTransfersToClientSberWithoutCommissionMonthInRUB(Account toAccount, float sumTransfer) {
        boolean isMyAccount = isClientAccount(toAccount);
        boolean isAccountMyBank = getBank().isAccountBank(toAccount);
        // если счёт не мой, но моего банка, то есть клиент Сбера, то суммируем
        if (!isMyAccount && isAccountMyBank) totalTransfersToClientSberWithoutCommissionMonthInRUB += sumTransfer;
    }

    @Override
    public float getTotalPaymentsTransfersDayInUSD() {
        return super.getTotalPaymentsTransfersDayInUSD();
    }

    @Override
    public void setPercentOfCommissionForTransferInUsdOrOtherCurrency(float percentOfCommissionForTransferInUsdOrOtherCurrency) {
        super.setPercentOfCommissionForTransferInUsdOrOtherCurrency(percentOfCommissionForTransferInUsdOrOtherCurrency);
    }

    @Override
    // Вывод всех операций по всем картам и счетам профиля физического лица в Сбере
    public void displayProfileTransactions() {
        // дополним метод уникальной информацией, присуще только Сберу
        String allTransactionsPhysicalPerson = ("Все операции по картам и счетам клиента " + getPhysicalPerson().getFirstName() + " " + getPhysicalPerson().getLastName() +
                " в " + getBank().getBankName() + "Банке");

        String transfersToClientSberWithoutCommissionMonthInRub = ("Переводы клиентам " + getBank().getBankName() +
                " без комиссии за текущий месяц: " + getTotalTransfersToClientSberWithoutCommissionMonthInRUB() + "₽ Доступный лимит: " +
                (getLimitTransfersToClientSberWithoutCommissionMonthInRUB() - getTotalTransfersToClientSberWithoutCommissionMonthInRUB()) + "₽ из " +
                getLimitTransfersToClientSberWithoutCommissionMonthInRUB() + "₽");

        String balanceBonuses = (getBank().getBankName() + "Бонусов: " + getBonuses());

        String headerProfileTransactions = allTransactionsPhysicalPerson + "\n" + transfersToClientSberWithoutCommissionMonthInRub + "\n" + balanceBonuses;
        System.out.println(headerProfileTransactions);
        IOFile.write(getPathToTransactionHistoryFile(), headerProfileTransactions,true);

        // и вызываем родительскую версию метода
        super.displayProfileTransactions();
    }

}
