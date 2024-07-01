package ru.geekstar.Account;

import ru.geekstar.ClientProfile.PhysicalPersonProfile;

public abstract class SavingsAccount extends Account {

    private float percentOfBalance = 5.0f;

    private float percentOfBalanceDay;


    public float getPercentOfBalanceDay() {
        return percentOfBalanceDay;
    }

    public void setPercentOfBalanceDay(float percentOfBalanceDay) {
        this.percentOfBalanceDay = percentOfBalanceDay;
    }

    public SavingsAccount(PhysicalPersonProfile accountHolder, String currencyCode) {
        super(accountHolder,currencyCode);
    }

    // Рассчитать проценты на остаток в конце дня
    public void chargePercentOnBalanceEndDay() {
        // TODO: если 00:00 ежедневно, то
            percentOfBalanceDay += (getBalance() / 100) * percentOfBalance * 1 / 365;
    }

    // Начислить проценты на остаток в конце месяца
    public void chargePercentOnBalanceEndMonth() {
        // TODO: если 00:00 1-ого числа месяца, то
            setBalance(getBalance() + percentOfBalanceDay);
            percentOfBalance = 0;
    }

}
