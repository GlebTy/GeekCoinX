package ru.geekstar.Bank;

import ru.geekstar.Account.Account;
import ru.geekstar.Card.Card;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public interface IServicePhysicalPerson {

    PhysicalPersonProfile registerPhysicalPersonProfile (PhysicalPerson physicalPerson);

    Card openCard(PhysicalPersonProfile physicalPersonProfile, Card card, String currencyCode, String pinCode);

    Account openAccount(PhysicalPersonProfile physicalPersonProfile, Account account, String currencyCode);
}
