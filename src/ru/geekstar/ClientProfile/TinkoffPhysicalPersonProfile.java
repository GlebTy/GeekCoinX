package ru.geekstar.ClientProfile;

import ru.geekstar.Bank.Bank;
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
    public void displayProfileTransactions() {
        // дополним метод уникальной информацией, присуще только тинькову
        System.out.println("Все операции по картам и счетам клиента " + getPhysicalPerson().getFirstName() + " " + getPhysicalPerson().getLastName() +
                " в " + getBank().getBankName() + " Банке");

        System.out.println("Накопленный кэшбэк: " + getCashback());
        System.out.println("Накопленные мили: " + getMiles());

        // и вызываем родительскую версию метода
        super.displayProfileTransactions();

        }
    }
