package ru.geekstar.Card;

public interface IBonusCard {

    int getBonuses();

    // накапливаем бонусы
    void accumulateBonuses(float sumPay);

    // оплачиваем бонусами
    void payByCardBonuses(float sumPay, int bonusesPay, String buyProductOrService, String pinCode);

}
