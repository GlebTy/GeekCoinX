package ru.geekstar.PhysicalPerson;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Bank.IBankServicePhysicalPerson;
import ru.geekstar.Card.Card;
import ru.geekstar.Card.IAirlinesCard;
import ru.geekstar.Card.IBonusCard;
import ru.geekstar.Card.IMulticurrencyCard;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import java.util.ArrayList;

public class PhysicalPerson {

    public static int personCount;

    private String firstName;

    private String lastName;

    private String telephone;

    private byte age;

    private char gender;

    private ArrayList<PhysicalPersonProfile> physicalPersonProfiles = new ArrayList<>();


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public ArrayList<PhysicalPersonProfile> getPhysicalPersonProfiles() {
        return physicalPersonProfiles;
    }

    public void setPhysicalPersonProfiles(ArrayList<PhysicalPersonProfile> physicalPersonProfiles) {
        this.physicalPersonProfiles = physicalPersonProfiles;
    }

    public PhysicalPersonProfile getPhysicalPersonProfile (IBankServicePhysicalPerson bank) {

        for (int idProfile = 0; idProfile < physicalPersonProfiles.size(); idProfile++) {
            PhysicalPersonProfile profile = physicalPersonProfiles.get(idProfile);
            if (profile.getBank().equals(bank)) return profile;
        }
       return null;
    }

    public PhysicalPerson(String firstName, String lastName, String telephone) {
        personCount++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
    }

    public PhysicalPerson(String firstName, String lastName, String telephone, byte age, char gender) {
        this(firstName, lastName, telephone);
        this.age = age;
        this.gender = gender;
    }

    public void registerPhysicalPersonToBank(IBankServicePhysicalPerson bank) {
        physicalPersonProfiles.add(bank.registerPhysicalPersonProfile(this));
    }

    public Card openCard(IBankServicePhysicalPerson bank, Class<? extends Card> classCard, Class<? extends PayCardAccount> classPayCardAccount, String currencyCode, String pinCode) {
        return bank.openCard(getPhysicalPersonProfile(bank), classCard, classPayCardAccount, currencyCode, pinCode);
    }

    public Account openAccount(IBankServicePhysicalPerson bank, Class<? extends Account> classAccount, String currencyCode) {
        return bank.openAccount(getPhysicalPersonProfile(bank), classAccount, currencyCode);
    }

    public void depositingCash2Card(Card toCard, float sumDepositing) {
        toCard.depositingCash2Card(sumDepositing);
    }

    public void payByCard(Card card, float sumPay, String buyProductOrService, String pinCode) {
        card.payByCard(sumPay, buyProductOrService, pinCode);
    }

    public void payByCard(Card card, float sumPay, String buyProductOrService, String country, String pinCode) {
        card.payByCard(sumPay, buyProductOrService, country, pinCode);
    }

    public void transferCard2Card(Card fromCard, Card toCard, float sumTransfer) {
        fromCard.transferCard2Card(toCard, sumTransfer);
    }

    public void transferCard2Account(Card fromCard, Account toAccount, float sumTransfer) {
        fromCard.transferCard2Account(toAccount, sumTransfer);
    }

    public void transferAccount2Card(Account fromAccount, Card toCard, float sumTransfer) {
        fromAccount.transferAccount2Card(toCard, sumTransfer);
    }

    public void transferAccount2Account(Account fromAccount, SberSavingsAccount toAccount, float sumTransfer) {
        fromAccount.transferAccount2Account(toAccount, sumTransfer);
    }

    public void depositingCardFromCard(Card toCard, Card fromCard, float sumDepositing) {
        toCard.depositingCardFromCard(fromCard, sumDepositing);
    }

    public void depositingCardFromAccount(Card toCard, Account fromAccount, float sumDepositing) {
        toCard.depositingCardFromAccount(fromAccount, sumDepositing);
    }

    public void depositingAccountFromAccount(SberSavingsAccount toAccount, SberSavingsAccount fromAccount, float sumDepositing) {
        toAccount.depositingAccountFromAccount(fromAccount, sumDepositing);
    }

    public void depositingAccountFromCard(SberSavingsAccount toAccount, Card fromCard, float sumDepositing) {
        toAccount.depositingAccountFromCard(fromCard, sumDepositing);
    }

    public void displayCardTransactions(Card card) {
        card.displayCardTransactions();
    }

    public void displayAccountTransactions(SberSavingsAccount account) {
        account.displayAccountTransactions();
    }

    public void displayProfileTransactions(IBankServicePhysicalPerson bank) {
        for (int idProfile = 0; idProfile < physicalPersonProfiles.size(); idProfile++) {
            PhysicalPersonProfile profile = physicalPersonProfiles.get(idProfile);
            if (profile.getBank().equals(bank)) profile.displayProfileTransactions();
        }
    }

    public void displayAllProfileTransactions() {
        for(int idProfile = 0; idProfile < physicalPersonProfiles.size(); idProfile++) {
            physicalPersonProfiles.get(idProfile).displayProfileTransactions();
        }
    }

    public void addAccountToMulticurrencyCard(IMulticurrencyCard multicurrencyCard, String currencyCodeAccount) {
        multicurrencyCard.addAccount(currencyCodeAccount);
    }

    public void switchAccountOfMulticurrencyCard(IMulticurrencyCard multicurrencyCard, String currencyCodeAccount) {
        multicurrencyCard.switchAccount(currencyCodeAccount);
    }

    public void displayMulticurrencyCardTransactions(IMulticurrencyCard multicurrencyCard) {
        multicurrencyCard.displayMulticurrencyCardTransactions();
    }

    public void payByCardBonuses(IBonusCard bonusCard, float sumPay, int bonusesPay, String buyProductOrService, String pinCode) {
        bonusCard.payByCardBonuses(sumPay, bonusesPay, buyProductOrService, pinCode);
    }

    public void payByCardMiles(IAirlinesCard airlinesCard, float sumPay, int milesPay, String buyProductOrService, String pinCode) {
        airlinesCard.payByCardMiles(sumPay, milesPay, buyProductOrService, pinCode);
    }
}