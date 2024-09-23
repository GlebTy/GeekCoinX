package ru.geekstar.Form;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.SavingsAccount;
import ru.geekstar.Card.Card;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FormTransfer {
    private JPanel panelTransfer;
    private JComboBox comboBoxFrom;
    private JComboBox comboBoxTo;
    private JButton ButtonTransfer;
    private JButton ButtonCancel;
    private JTextField textFieldSum;
    private JLabel labelCurrency;

    public JPanel getPanelTransfer() {
        return panelTransfer;
    }

    public FormTransfer() {

        ButtonTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверяем заполнены ли все обязательные поля
                // если хоть одно поле не заполнено, то происходит return выход из метода
                if (!checkFillFields()) return;

                // извлекаем данные, которые ввел пользователь
                Object objFrom = comboBoxFrom.getSelectedItem();
                Object objTo = comboBoxTo.getSelectedItem();
                float sumTransfer = Float.valueOf(textFieldSum.getText());

                if (objFrom instanceof Card) {
                    Card cardFrom = (Card) objFrom;
                    if (!checkBalance(cardFrom, sumTransfer)) return;

                    if (objTo instanceof Card) {
                        Card cardTo = (Card) objTo;
                        FormMain.physicalPerson.transferCard2Card(cardFrom, cardTo, sumTransfer);
                    }
                    if (objTo instanceof Account) {
                        Account accountTo = (Account) objTo;
                        FormMain.physicalPerson.transferCard2Account(cardFrom, accountTo, sumTransfer);
                    }
                }

                if (objFrom instanceof Account) {
                    Account accountFrom = (Account) objFrom;
                    if (!checkBalance(accountFrom, sumTransfer)) return;

                    if (objTo instanceof Card) {
                        Card cardTo = (Card) objTo;
                        FormMain.physicalPerson.transferAccount2Card(accountFrom, cardTo, sumTransfer);
                    }
                    if (objTo instanceof Account) {
                        Account accountTo = (Account) objTo;
                        FormMain.physicalPerson.transferAccount2Account(accountFrom, accountTo, sumTransfer);
                    }
                }

                // отображаем панель со статусом операции
                FormMain.formMain.displayPanelStatus("Перевод обрабатывается");
            }
        });

        panelTransfer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                updatePanelTransfer();
            }
        });

        ButtonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormMain.formMain.displayPanelMain();
            }
        });

        comboBoxFrom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateCurrencySymbolSum();
            }
        });
    }

    public void updateCurrencySymbolSum() {
        Object objFrom = comboBoxFrom.getSelectedItem();
        if (objFrom instanceof Card) {
            Card cardFrom = (Card) objFrom;
            labelCurrency.setText(cardFrom.getPayCardAccount().getCurrencySymbol());
        }
        if (objFrom instanceof Account) {
            Account accountFrom = (Account) objFrom;
            labelCurrency.setText(accountFrom.getCurrencySymbol());
        }
    }

    public void updatePanelTransfer() {
        // очищаем список с картами и счетами списания
        comboBoxFrom.removeAllItems();
        // очищаем список с картами и счетами зачисления
        comboBoxTo.removeAllItems();
        // очищаем поле с суммой
        textFieldSum.setText("");

        // запрашиваем профили пользователя
        ArrayList<PhysicalPersonProfile> profiles = FormMain.physicalPerson.getPhysicalPersonProfiles();
        // перебираем профили пользователя
        for (PhysicalPersonProfile profile : profiles) {
            // запрашиваем карты профиля пользователя
            ArrayList<Card> cards = profile.getCards();
            // перебираем карты
            for (Card card : cards) {
                // добавляем в список карты для списания и зачисления
                comboBoxFrom.addItem(card);
                comboBoxTo.addItem(card);
            }
            // запрашиваем счета профиля пользователя
            ArrayList<Account> accounts = profile.getAccounts();
            // перебираем счета
            for (Account account : accounts) {
                //  добавляем в список только сберегательные счета для списания и зачисления
                if (account instanceof SavingsAccount) {
                    comboBoxFrom.addItem(account);
                    comboBoxTo.addItem(account);
                }
            }
        }
    }

    public boolean checkFillFields() {
        if (comboBoxFrom.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(panelTransfer, "Выберите откуда бы вы хотели осуществить перевод");
            return false;
        }

        if (comboBoxTo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(panelTransfer, "ВЫберите куда бы вы хотели осуществить перевод");
            return false;
        }

        if (comboBoxTo.getSelectedItem().equals(comboBoxFrom.getSelectedItem())) {
            JOptionPane.showMessageDialog(panelTransfer, "Выберите разные карты или счета");
        }

        if (textFieldSum.getText().isBlank()) {
            JOptionPane.showMessageDialog(panelTransfer, "Введите сумму перевода");
        }

        // регулярное выражения для проверки суммы на ее коректность
        String regexSum = "[0-9]{1,7}(\\.[0-9]{2})?";
        if (!textFieldSum.getText().matches(regexSum)) {
            JOptionPane.showMessageDialog(panelTransfer, "Сумма может состоять только из цифр и точки.\nСумма не может быть больше и равна 10 млн.");
            return false;
        }

        if (Float.valueOf(textFieldSum.getText()) == 0) {
            JOptionPane.showMessageDialog(panelTransfer, "Введите сумму больше 0");
            return false;
        }

        return true;
    }

    public boolean checkBalance(Card cardFrom, float sumTransfer) {
        if (cardFrom.getPayCardAccount().getBalance() < sumTransfer) {
            JOptionPane.showMessageDialog(panelTransfer, "Недостаточно средств на карте");
            return false;
        }
        return true;
    }

    public boolean checkBalance(Account accountFrom, float sumTransfer) {
        if (accountFrom.getBalance() < sumTransfer) {
            JOptionPane.showMessageDialog(panelTransfer, "Недостаточно средств на счете");
            return false;
        }
        return true;
    }
}
