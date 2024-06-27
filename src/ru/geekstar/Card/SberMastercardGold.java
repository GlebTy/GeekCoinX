package ru.geekstar.Card;

import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

public final class SberMastercardGold extends CardMastercard {

    public SberMastercardGold(PhysicalPersonProfile cardHolder, PayCardAccount payCardAccount, String pinCode) {
        super(cardHolder, payCardAccount, pinCode);
    }
}
