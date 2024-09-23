package ru.geekstar.ClientProfile;

import ru.geekstar.Bank.Bank;
import ru.geekstar.IOFile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public class TinkoffPhysicalPersonProfile extends PhysicalPersonProfile{

    private int miles;

    private float costMilesInRUB;

    private float costMilesInUSD;

    private float costMilesInEUR;

    private float cashback;

    private float percentCashbackOfSumPay;

    public float getCashback() {
        return cashback;
    }

    public void setCashback(float cashback) {
        this.cashback = cashback;
    }

    public float getPercentCashbackOfSumPay() {
        return percentCashbackOfSumPay;
    }

    public void setPercentCashbackOfSumPay(float percentCashbackOfSumPay) {
        this.percentCashbackOfSumPay = percentCashbackOfSumPay;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public float getCostMilesInRUB() {
        return costMilesInRUB;
    }

    public void setCostMilesInRUB(float costMilesInRUB) {
        this.costMilesInRUB = costMilesInRUB;
    }

    public float getCostMilesInUSD() {
        return costMilesInUSD;
    }

    public void setCostMilesInUSD(float costMilesInUSD) {
        this.costMilesInUSD = costMilesInUSD;
    }

    public float getCostMilesInEUR() {
        return costMilesInEUR;
    }

    public void setCostMilesInEUR(float costMilesInEUR) {
        this.costMilesInEUR = costMilesInEUR;
    }

    public TinkoffPhysicalPersonProfile(Bank bank, PhysicalPerson physicalPerson) {
        super(bank, physicalPerson);
    }

    @Override
    // Вывод всех операций по всем картам и счетам профиля физического лица в тинькове
    public String displayProfileTransactions() {
        // дополним метод уникальной информацией, присуще только тинькову
        String allTransactionsPhysicalPerson = ("Все операции по картам и счетам клиента " + getPhysicalPerson().getFirstName() + " " + getPhysicalPerson().getLastName() +
                " в " + getBank().getBankName() + " Банке");

        String cashback = ("Накопленный кэшбэк: " + getCashback());
        String balanceMiles = ("Накопленные мили: " + getMiles());

        String headerProfileTransactions = allTransactionsPhysicalPerson + "\n" + cashback + "\n" + balanceMiles;

        String profileTransactions = headerProfileTransactions + "\n" + super.displayProfileTransactions();

        System.out.println(headerProfileTransactions);
        IOFile.write(getPathToTransactionHistoryFile(), headerProfileTransactions, true);

        return profileTransactions;

        }
    }
