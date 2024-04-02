package ru.geekstar.Card;

public interface IBonusCard {

    // накапливаем бонусы
    void accumulateBonuses(float sumPay);

    // оплачиваем бонусами
    void payByCardBonuses(float sumPay, int bonusesPay, String buyProductOrService, String pinCode);

}
