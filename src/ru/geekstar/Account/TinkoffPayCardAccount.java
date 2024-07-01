package ru.geekstar.Account;

import ru.geekstar.ClientProfile.PhysicalPersonProfile;

public class TinkoffPayCardAccount extends PayCardAccount{

    public TinkoffPayCardAccount(PhysicalPersonProfile accountHolder, String currencyCode) {
        super(accountHolder,currencyCode);
    }


}
