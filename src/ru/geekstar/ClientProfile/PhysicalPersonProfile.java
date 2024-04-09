package ru.geekstar.ClientProfile;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.SberPayCardAccount;
import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Card.Card;
import ru.geekstar.Card.SberVisaGold;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

import java.util.Arrays;

public abstract class PhysicalPersonProfile extends ClientProfile {

    private PhysicalPerson physicalPerson;

    private Card[] cards = new Card[50];

    private Account[] accounts = new Account[50];

    private byte countCards;

    private byte countAccounts;



    public PhysicalPerson getPhysicalPerson() {
        return physicalPerson;
    }

    public void setPhysicalPerson(PhysicalPerson physicalPerson) {
        this.physicalPerson = physicalPerson;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

    public byte getCountCards() {
        return countCards;
    }

    public void setCountCards(byte countCards) {
        this.countCards = countCards;
    }

    public byte getCountAccounts() {
        return countAccounts;
    }

    public void setCountAccounts(byte countAccounts) {
        this.countAccounts = countAccounts;
    }


    // Привязать  счёт к профилю клиента
    public void addAccount(Account account) {
        accounts[countAccounts++] = account;
    }

    // Привязать карту к профилю клиента
    public void addCard(Card card) {
        cards[countCards++] = card;
    }

    // проверить привязана ли карта к профилю клиента
    public boolean isClientCard(Card card) {
        for (int idCard = 0; idCard < countCards; idCard++) {
            if (cards[idCard].equals(card)) return true;
        }
        return false;
    }

    // проверить привязан ли счёт к профилю клиента
    public boolean isClientAccount(Account account) {
        for (int idAccount = 0; idAccount < countAccounts; idAccount++) {
            if (accounts[idAccount].equals(account)) return true;
        }
        return false;
    }

    // Прибавить сумму перевода на карту к общей сумме совершённых оплат и переводов в сутки, чтобы контролировать лимиты
    public void updateTotalPaymentsTransfersDay(float sum, String fromCurrencyCode, Card toCard) {
        // моя ли карта, на которую выполняем перевод
        boolean isMyCard = isClientCard(toCard);
        // если не моя карта, то обновляем общую сумму
        if (!isMyCard) updateTotalPaymentsTransfersDay(sum, fromCurrencyCode);
    }

    // Прибавить сумму перевода на счёт к общей сумме совершённых оплат и переводов в сутки, чтобы контролировать лимиты
    public void updateTotalPaymentsTransfersDay(float sum, String fromCurrencyCode, Account toAccount) {
        // мой ли счёт, на который выполняем перевод
        boolean isMyAccount = isClientAccount(toAccount);
        // если не мой счёт, то обновляем общую сумму
        if (!isMyAccount) updateTotalPaymentsTransfersDay(sum, fromCurrencyCode);
    }

    @Override
    public void setLimitCommissionTransferInRUB(float limitCommissionTransferInRUB) {
        super.setLimitCommissionTransferInRUB(limitCommissionTransferInRUB);
    }

    @Override
    // Вывод всех операций по всем картам и счетам профиля физического лица
    public void displayProfileTransactions() {
        System.out.println("Платежей и переводов за текущие сутки выполнено на сумму: " + getTotalPaymentsTransfersDayInRUB() +
                "₽ Доступный лимит: " + (getLimitPaymentsTransfersDayInRUB() - getTotalPaymentsTransfersDayInRUB()) + "₽ из " +
                getLimitPaymentsTransfersDayInRUB() + "₽");

        // для подсчёта всех транзакций по всем счетам и картам клиента
        int countAllTransactions = 0;

        // подсчитать общее количество всех транзакций по всем счетам
        for (int idAccount = 0; idAccount < countAccounts; idAccount++) {
            countAllTransactions += accounts[idAccount].getAllAccountTransactions().length;
        }


        // и объявить массив всех транзакций профиля клиента длиной равной количеству всех транзакций
        String[] allTransactions = new String[countAllTransactions];

        // теперь нужно перебрать все счета
        int destPos = 0;
        for (int idAccount = 0; idAccount < countAccounts; idAccount++) {
            String[] allAccountTransactions = accounts[idAccount].getAllAccountTransactions();
            System.arraycopy(allAccountTransactions, 0, allTransactions, destPos, allAccountTransactions.length);
            destPos += allAccountTransactions.length;
        }


        // далее нужно отсортировать все транзакции по дате и времени
        Arrays.sort(allTransactions);

        // и осталось вывести все транзакции
        for (int idTransaction = 0; idTransaction < countAllTransactions; idTransaction++) {
            System.out.println("#" + (idTransaction + 1) + " " + allTransactions[idTransaction]);
        }

        System.out.println();

    }

}
