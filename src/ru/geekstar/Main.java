package ru.geekstar;

import ru.geekstar.Account.SberPayCardAccount;
import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Account.TinkoffPayCardAccount;
import ru.geekstar.Bank.Sberbank;
import ru.geekstar.Bank.Tinkoff;
import ru.geekstar.Card.*;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

public class Main {

    public static void main(String[] args) {

        PhysicalPerson I = new PhysicalPerson("Игорь", "Коннов", "+79277394841", (byte)32, 'M');
        PhysicalPerson friend = new PhysicalPerson("Герман", "Греф", "+79273547845", (byte)58, 'M');

        System.out.println("Физ. лиц в системе " + PhysicalPerson.personCount);

        Sberbank sberbank = new Sberbank();
        Tinkoff tinkoff = new Tinkoff();

        I.registerPhysicalPersonToBank(tinkoff);
        I.registerPhysicalPersonToBank(sberbank);
        friend.registerPhysicalPersonToBank(sberbank);

        TinkoffAirlinesMir myTinkoffAirlinesMir1 = (TinkoffAirlinesMir) I.openCard(tinkoff, TinkoffAirlinesMir.class, TinkoffPayCardAccount.class, "RUB", "3131");
        TinkoffBlackMir myTinkoffBlackMir1 = (TinkoffBlackMir) I.openCard(tinkoff, TinkoffBlackMir.class, TinkoffPayCardAccount.class, "RUB", "9292");

        SberVisaGold mySberVisaGold1 = (SberVisaGold) I.openCard(sberbank, SberVisaGold.class, SberPayCardAccount.class, "RUB", "2864");
        SberVisaGold mySberVisaGold2 = (SberVisaGold) I.openCard(sberbank, SberVisaGold.class, SberPayCardAccount.class, "RUB", "1234");

        SberMastercardGold sberMastercardGold = (SberMastercardGold) I.openCard(sberbank, SberMastercardGold.class, SberPayCardAccount.class, "RUB", "9672");
        SberMastercardTravel sberMastercardtravel = (SberMastercardTravel) I.openCard(sberbank, SberMastercardTravel.class, SberPayCardAccount.class, "RUB", "7621");

        I.switchAccountOfMulticurrencyCard(sberMastercardtravel, "USD");

        SberSavingsAccount mySberSavingsAccount1 = (SberSavingsAccount) I.openAccount(sberbank, SberSavingsAccount.class, "RUB");
        SberSavingsAccount mySberSavingsAccount2 = (SberSavingsAccount) I.openAccount(sberbank, SberSavingsAccount.class, "RUB");

        SberVisaGold friendSberVisaGold1 = (SberVisaGold) friend.openCard(sberbank, SberVisaGold.class, SberPayCardAccount.class, "RUB", "9078");


        I.depositingCash2Card(sberMastercardtravel, 10000.00f);
        I.depositingCash2Card(mySberVisaGold1, 7600.50f);
        I.depositingCash2Card(sberMastercardGold, 2000.00f);
        I.depositingCash2Card(myTinkoffBlackMir1, 1000.0f);
        I.depositingCash2Card(myTinkoffAirlinesMir1, 5000.0f);

        I.payByCard(sberMastercardtravel, 3700.00f, "Bike", "Турция","7621");
        I.payByCard(mySberVisaGold1, 100.50f, "ЖКХ", "2864");
        I.payByCard(sberMastercardGold, 700.00f,"Пятерочка", "9672");
        I.payByCard(mySberVisaGold1, 110.00f, "Excursion", "Турция", "2864");
        I.payByCard(sberMastercardGold, 200.00f, "Attraction", "Турция","9672");
        I.payByCardBonuses(mySberVisaGold1, 150.00f, 1, "Starbucks", "2864");
        I.payByCard(myTinkoffBlackMir1, 500.0f, "Магнит","Казахстан","9292");
        I.payByCard(myTinkoffAirlinesMir1, 1000.0f, "Подписка Netflix","3131");
        I.payByCardMiles(myTinkoffAirlinesMir1, 3000.0f, 10, "Авиабилеты", "3131");

        I.transferCard2Card(mySberVisaGold1, mySberVisaGold2, 250.00f);
        I.transferCard2Card(mySberVisaGold1, friendSberVisaGold1, 55.00f);
        I.transferCard2Card(sberMastercardGold, mySberVisaGold1, 100.00f);
        I.transferCard2Card(mySberVisaGold1, sberMastercardGold, 50.00f);

        I.transferCard2Account(mySberVisaGold1, mySberSavingsAccount1, 95.00f);
        I.transferCard2Account(sberMastercardGold, mySberSavingsAccount1, 70.00f);


        I.transferAccount2Card(mySberSavingsAccount1, mySberVisaGold1, 15.00f);
        I.transferAccount2Card(mySberSavingsAccount1, sberMastercardGold, 33.00f);
        I.transferAccount2Account(mySberSavingsAccount1, mySberSavingsAccount2, 30.00f);

        I.depositingCardFromCard(mySberVisaGold1, mySberVisaGold2, 145.00f);
        I.depositingCardFromCard(sberMastercardGold, mySberVisaGold1, 80.00f);
        I.depositingCardFromAccount(sberMastercardGold, mySberSavingsAccount1, 111.00f);
        I.depositingCardFromAccount(mySberVisaGold1, mySberSavingsAccount1, 75.00f);

        I.depositingAccountFromCard(mySberSavingsAccount1, mySberVisaGold1, 350.00f);
        I.depositingAccountFromCard(mySberSavingsAccount1, sberMastercardGold, 390.00f);
        I.depositingAccountFromAccount(mySberSavingsAccount1, mySberSavingsAccount2, 50.00f);

        System.out.println("\nКоличество карт в системе: ");
        System.out.println(SberMastercardGold.class.getSimpleName() + ": " + SberMastercardGold.countCards);
        System.out.println(SberMastercardTravel.class.getSimpleName() + ": " + SberMastercardTravel.countCards);
        System.out.println(SberVisaGold.class.getSimpleName() + ": " + SberVisaGold.countCards);
        System.out.println(TinkoffAirlinesMir.class.getSimpleName() + ": " + TinkoffAirlinesMir.countCards);
        System.out.println(TinkoffBlackMir.class.getSimpleName() + ": " + TinkoffBlackMir.countCards);

        // Вывод всех операций по всем счетам мультивалютной карты
        I.displayMulticurrencyCardTransactions(sberMastercardtravel);

/*
        System.out.println("Вывод операций по карте " + sberMastercardGold.getNumberCard());
        I.displayCardTransactions(sberMastercardGold);

        System.out.println("\nВывод операций по карте друга " + friendSberVisaGold1.getNumberCard());
        I.displayCardTransactions(friendSberVisaGold1);

        System.out.println("\nВывод операций по карте " + mySberVisaGold2.getNumberCard());
        I.displayCardTransactions(mySberVisaGold2);

        System.out.println("\nВывод операций по счету " + mySberSavingsAccount1.getNumberAccount());
        I.displayAccountTransactions(mySberSavingsAccount1);
*/

        // Вывод всех операций по всем картам и счетам профиля клиента с сортировкой по дате и времени
        // I.displayProfileTransactions(sberbank);
        // friend.displayProfileTransactions(sberbank);

        I.displayAllProfileTransactions();
        friend.displayAllProfileTransactions();
    }
}
