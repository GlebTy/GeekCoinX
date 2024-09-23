package ru.geekstar.Form;

import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.Account.TinkoffSavingsAccount;
import ru.geekstar.Bank.IBankServicePhysicalPerson;
import ru.geekstar.Bank.Sberbank;
import ru.geekstar.Bank.Tinkoff;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class FormOpenAccount {
    private JPanel panelOpenAccount;
    private JComboBox comboBoxBank;
    private JComboBox comboBoxCurrency;
    private JButton buttonOpenAccount;
    private JButton buttonCancel;

    public JPanel getPanelOpenAccount() {
        return panelOpenAccount;
    }

    public FormOpenAccount() {
        String[] classBanks = {Sberbank.class.getSimpleName(), Tinkoff.class.getSimpleName()};
        comboBoxBank.setModel(new DefaultComboBoxModel(classBanks));

        String[] currencies = {"RUB", "USD", "EUR"};
        comboBoxCurrency.setModel(new DefaultComboBoxModel(currencies));

        buttonOpenAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Class classBank = null;
                Class classSavingsAccount = null;

                if (comboBoxBank.getSelectedItem().toString().equals(Sberbank.class.getSimpleName())) {
                    classBank = Sberbank.class;
                    classSavingsAccount = SberSavingsAccount.class;
                }
                if (comboBoxBank.getSelectedItem().toString().equals(Tinkoff.class.getSimpleName())) {
                    classBank = Tinkoff.class;
                    classSavingsAccount = TinkoffSavingsAccount.class;
                }

                IBankServicePhysicalPerson bank = null;
                PhysicalPersonProfile profile = FormMain.physicalPerson.getPhysicalPersonProfile(classBank);
                if (profile == null) {
                    try {
                        bank = (IBankServicePhysicalPerson) classBank.getConstructor().newInstance();
                    } catch (NoSuchMethodException noSuchMethodEx) {
                        System.out.println("Конструктор не найден " + noSuchMethodEx.getMessage());
                    } catch (IllegalAccessException illegalAccessEx) {
                        System.out.println("Конструктор недоступен " + illegalAccessEx.getMessage());
                    } catch (InstantiationException instantiationEx) {
                        System.out.println("Невозможно создать объект абстрактного класса " + instantiationEx.getMessage());
                    } catch (InvocationTargetException invocationTargetEx) {
                        System.out.println("Вызываемый конструктор выбросил исключение " + invocationTargetEx.getMessage());
                    }
                    FormMain.physicalPerson.registerPhysicalPersonToBank(bank);
                } else bank = (IBankServicePhysicalPerson) profile.getBank();

                FormMain.physicalPerson.openAccount(bank, classSavingsAccount, comboBoxCurrency.getSelectedItem().toString());
                FormMain.formMain.displayPanelStatus("Счёт открыт");
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormMain.formMain.displayPanelMain();
            }
        });
    }
}
