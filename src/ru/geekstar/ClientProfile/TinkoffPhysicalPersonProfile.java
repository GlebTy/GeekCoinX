package ru.geekstar.ClientProfile;

public class TinkoffPhysicalPersonProfile extends PhysicalPersonProfile{

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

    @Override
    // Вывод всех операций по всем картам и счетам профиля физического лица в тинькове
    public void displayProfileTransactions() {
        // дополним метод уникальной информацией, присуще только тинькову
        System.out.println("Все операции по картам и счетам клиента " + getPhysicalPerson().getFirstName() + " " + getPhysicalPerson().getLastName() +
                " в " + getBank().getBankName() + " Банке");

        System.out.println("Накопленный кэшбэк: " + getCashback());

        // и вызываем родительскую версию метода
        super.displayProfileTransactions();

        }
    }
