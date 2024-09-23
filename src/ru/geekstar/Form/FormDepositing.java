package ru.geekstar.Form;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.SavingsAccount;
import ru.geekstar.Card.Card;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FormDepositing {
 private JPanel panelDepositing;
 private JComboBox comboBoxFrom;
 private JComboBox comboBoxTo;
 private JTextField textFieldSum;
 private JButton buttonDepositing;
 private JButton buttonCancel;
 private JLabel labelCurrency;

 public JPanel getPanelDepositing() { return panelDepositing; }

 public FormDepositing() {
  buttonDepositing.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
    // проверяем заполнены ли все обязательные поля
    // если хоть одно поле не заполнено, то происходит return (выход из метода)
    if (!checkFillFields()) return;

    // извлекаем данные, которые указал пользователь
    Object objFrom = comboBoxFrom.getSelectedItem();
    Object objTo = comboBoxTo.getSelectedItem();
    float sumDepositing = Float.valueOf(textFieldSum.getText());

    // если пополнение через банкомат
    if (objFrom.equals("Банкомат")) {
     // если объектом зачисления является карта, то вызываем метод пополнения depositingCash2Card()
     if (objTo instanceof Card) FormMain.physicalPerson.depositingCash2Card((Card) objTo, sumDepositing);
      // иначе если объектом зачисления является счёт, то сообщаем, что пополнить в банкомате можно только карту
     else if (objTo instanceof SavingsAccount) {
      JOptionPane.showMessageDialog(panelDepositing, "Пополнить в банкомате можно только карту");
      return;
     }
    }

    // если объектом списания является карта
    if (objFrom instanceof Card) {
     // явно приводим Object к типу Card
     Card cardFrom = (Card) objFrom;
     // проверяем баланс карты
     if (!checkBalance(cardFrom, sumDepositing)) return;

     // если объектом зачисления является карта
     if (objTo instanceof Card) {
      Card cardTo = (Card) objTo;
      // то вызываем метод пополнения depositingCardFromCard()
      FormMain.physicalPerson.depositingCardFromCard(cardTo, cardFrom, sumDepositing);
     }

     // если объектом зачисления является счёт
     if (objTo instanceof Account) {
      Account accountTo = (Account) objTo;
      // то вызываем метод пополнения depositingAccountFromCard()
      FormMain.physicalPerson.depositingAccountFromCard(accountTo, cardFrom, sumDepositing);
     }
    }

    // если объектом списания является счёт
    if (objFrom instanceof Account) {
     Account accountFrom = (Account) objFrom;
     // проверяем баланс счёта
     if (!checkBalance(accountFrom, sumDepositing)) return;

     // если объектом зачисления является карта
     if (objTo instanceof Card) {
      Card cardTo = (Card) objTo;
      // то вызываем метод пополнения depositingCardFromAccount()
      FormMain.physicalPerson.depositingCardFromAccount(cardTo, accountFrom, sumDepositing);
     }

     // если объектом зачисления является счёт
     if (objTo instanceof Account) {
      Account accountTo = (Account) objTo;
      // то вызываем метод пополнения depositingAccountFromAccount()
      FormMain.physicalPerson.depositingAccountFromAccount(accountTo, accountFrom, sumDepositing);
     }
    }

    // отображаем панель со статусом операции
    FormMain.formMain.displayPanelStatus("Пополнение обрабатывается");
   }
  });

  // слушатель срабатывает в момент показа панели panelDepositing
  panelDepositing.addComponentListener(new ComponentAdapter() {
   @Override
   public void componentShown(ComponentEvent e) {
    super.componentShown(e);
    updatePanelDepositing();
   }
  });

  comboBoxFrom.addItemListener(new ItemListener() {
   @Override
   public void itemStateChanged(ItemEvent e) {
    updateCurrencySymbolSum();
   }
  });

  comboBoxTo.addItemListener(new ItemListener() {
   @Override
   public void itemStateChanged(ItemEvent e) {
    updateCurrencySymbolSum();
   }
  });

  buttonCancel.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
    FormMain.formMain.displayPanelMain();
   }
  });
 }

 public void updateCurrencySymbolSum() {
  Object objFrom = comboBoxFrom.getSelectedItem();

  if (objFrom != null && objFrom.equals("Банкомат")) {
   Object objTo = comboBoxTo.getSelectedItem();
   if (objTo instanceof Card) {
    Card cardTo = (Card) objTo;
    labelCurrency.setText(cardTo.getPayCardAccount().getCurrencySymbol());
   } else labelCurrency.setText("");
  }

  if (objFrom instanceof Card) {
   Card cardFrom = (Card) objFrom;
   labelCurrency.setText(cardFrom.getPayCardAccount().getCurrencySymbol());
  }

  if (objFrom instanceof Account) {
   Account accountFrom = (Account) objFrom;
   labelCurrency.setText(accountFrom.getCurrencySymbol());
  }
 }

 public void updatePanelDepositing() {
  // очищаем список с картами и счетами списания
  comboBoxFrom.removeAllItems();
  // очищаем список с картами и счетами зачисления
  comboBoxTo.removeAllItems();
  // очищаем поле с суммой
  textFieldSum.setText("");

  comboBoxFrom.addItem("Банкомат");
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
    // добавляем в список только сберегательные счёта для списания и зачисления
    if (account instanceof SavingsAccount) {
     comboBoxFrom.addItem(account);
     comboBoxTo.addItem(account);
    }
   }
  }
 }

 public boolean checkFillFields() {
  if (comboBoxFrom.getSelectedItem() == null) {
   JOptionPane.showMessageDialog(panelDepositing, "Выберите откуда пополнить");
   return false;
  }
  if (comboBoxTo.getSelectedItem() == null) {
   JOptionPane.showMessageDialog(panelDepositing, "Выберите куда пополнить");
   return false;
  }

  if (comboBoxFrom.getSelectedItem().equals(comboBoxTo.getSelectedItem())) {
   JOptionPane.showMessageDialog(panelDepositing, "Выберите разные карты или счета");
   return false;
  }

  if (textFieldSum.getText().isBlank()) {
   JOptionPane.showMessageDialog(panelDepositing, "Введите сумму пополнения");
   return false;
  }

  // регулярное выражение для проверки суммы на её корректность
  // целая цасть может быть от 1 до 7 символов {1,7}, то есть сумма не может быть больше 9999999
  // дробная часть не обязательна (?), должна содержать символ точка "." и не может превышать 2 символов {2}, то есть не может быть больше 99
  String regexSum = "[0-9]{1,7}(\\.[0-9]{2})?";
  if (!textFieldSum.getText().matches(regexSum)) {
   JOptionPane.showMessageDialog(panelDepositing, "Сумма может состоять из цифр и точки.\nСумма не может быть больше или равна 10 млн.");
   return false;
  }

  if (Float.valueOf(textFieldSum.getText()) == 0) {
   JOptionPane.showMessageDialog(panelDepositing, "Введите сумму больше 0");
   return false;
  }

  return true;
 }

 public boolean checkBalance(Card cardFrom, float sumDepositing) {
  if (cardFrom.getPayCardAccount().getBalance() < sumDepositing) {
   JOptionPane.showMessageDialog(panelDepositing, "Недостаточно средств на карте");
   return false;
  }
  return true;
 }

 public boolean checkBalance(Account accountFrom, float sumDepositing) {
  if (accountFrom.getBalance() < sumDepositing) {
   JOptionPane.showMessageDialog(panelDepositing, "Недостаточно средств на счёте");
   return false;
  }
  return true;
 }

}

