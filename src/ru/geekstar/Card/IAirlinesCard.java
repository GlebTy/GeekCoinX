package ru.geekstar.Card;

public interface IAirlinesCard {

    int getMiles();

    void accumulateMiles (float sumPay);

    void payByCardMiles(float sumPay, int milesPay, String buyProductOrService, String pinCode);
}
