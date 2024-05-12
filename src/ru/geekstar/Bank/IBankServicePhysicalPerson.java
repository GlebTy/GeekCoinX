package ru.geekstar.Bank;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Card.Card;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public interface IBankServicePhysicalPerson {

    PhysicalPersonProfile registerPhysicalPersonProfile (PhysicalPerson physicalPerson);

    default Card openCard(PhysicalPersonProfile physicalPersonProfile, Class<? extends Card> classCard, PayCardAccount payCardAccount, String currencyCode, String pinCode) {

        //открыть платежный счет
        PayCardAccount bankPayCardAccount = (PayCardAccount) openAccount(physicalPersonProfile, payCardAccount, currencyCode);

        Card card = null;
        try {
            card = classCard.getConstructor(PhysicalPersonProfile.class, PayCardAccount.class, String.class)
                    .newInstance(physicalPersonProfile, bankPayCardAccount, pinCode);
        } catch (Exception e) {
            System.out.println(e);
        }

        //привязать карту к платежному счету
        bankPayCardAccount.getCards().add(card);

        //привязать карту к профилю клиента
        physicalPersonProfile.getCards().add(card);

            return card;
        }


    default Account openAccount(PhysicalPersonProfile physicalPersonProfile, Account account, String currencyCode) {
        //становить свойства платежного счета
        account.setBank(physicalPersonProfile.getBank());
        account.setNumberAccount(physicalPersonProfile.getBank().generateNumberAccount());
        account.setAccountHolder(physicalPersonProfile);
        account.setCurrencyCode(currencyCode);
        account.setCurrencySymbol(currencyCode);

        //привязать платежный счет к профилю клиента
        physicalPersonProfile.getAccounts().add(account);

        return account;
    }
}
