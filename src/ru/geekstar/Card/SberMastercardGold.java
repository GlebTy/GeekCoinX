package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Bank.Bank;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

public class SberMastercardGold extends CardMastercard {

    public SberMastercardGold(Bank bank, PhysicalPersonProfile cardHolder, PayCardAccount payCardAccount, String pinCode) {
        super( bank, cardHolder, payCardAccount, pinCode);
    }
}
