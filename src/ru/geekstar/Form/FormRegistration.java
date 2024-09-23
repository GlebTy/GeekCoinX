package ru.geekstar.Form;

import ru.geekstar.PhysicalPerson.PhysicalPerson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FormRegistration {
    private JPanel panelRegistration;
    private JTextField textFieldSurname;
    private JTextField textFieldName;
    private JTextField textFieldTelephone;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxYear;
    private JRadioButton radioButtonMan;
    private JRadioButton radioButtonWoman;
    private JTextField textFieldLogin;
    private JCheckBox checkBoxConfirmation;
    private JButton buttonRegistration;
    private JButton buttonCancel;
    private JPasswordField passwordField;
    private JLabel labelLinkOffer;

    private static final String SURNAME = "Фамилия";
    private static final String NAME = "Имя";
    private static final String LOGIN = "Логин";
    private static final String PASS = "*****************";
    private static final String TELEPHONE = "+79999999999";

    public JPanel getPanelRegistration() {
        return panelRegistration;
    }

    public FormRegistration() {
        // отображаем подсказки для ввода пользователя
        hintDisplay(textFieldSurname, SURNAME);
        hintDisplay(textFieldName, NAME);
        hintDisplay(textFieldTelephone, TELEPHONE);
        hintDisplay(textFieldLogin, LOGIN);
        hintDisplay(passwordField, PASS);

        // объединяем в группу иконки для выбора пола
        ButtonGroup buttonGroupGender = new ButtonGroup();
        buttonGroupGender.add(radioButtonMan);
        buttonGroupGender.add(radioButtonWoman);

        // инициализируем список с месяцами года
        initItemsComboBoxMonth();

        // ининциализируем список с годами
        initItemsComboBoxYear();

        // инициализируем список с днями
        updateItemsComboBoxDay();

        // делаем кнопку "Зарегистрироваться" неактивной
        buttonRegistration.setEnabled(false);

        // focusGained — фокус достигнут, когда пользователь кликнет по полю
        // focusLost — фокус утрачен, когда пользователь кликнет по другому полю и фокус перейдёт на другое поле
        textFieldSurname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hintClear(textFieldSurname, SURNAME);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                hintDisplay(textFieldSurname, SURNAME);
            }
        });
        textFieldName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hintDisplay(textFieldName, NAME);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                hintClear(textFieldName, NAME);
            }
        });
        textFieldTelephone.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hintDisplay(textFieldTelephone, TELEPHONE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                hintDisplay(textFieldTelephone, TELEPHONE);
            }
        });
        textFieldLogin.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hintClear(textFieldLogin, LOGIN);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                hintDisplay(textFieldLogin, LOGIN);
            }
        });
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                hintClear(passwordField, PASS);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                hintDisplay(passwordField, PASS);
            }
        });

        // слушатель срабатывает, когда изменено состояние элемента, то есть пользователь кликнул и выбрал элемент из списка
        checkBoxConfirmation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (checkBoxConfirmation.isSelected()) buttonRegistration.setEnabled(true);
                else buttonRegistration.setEnabled(false);
            }
        });

        // слушатель срабатывает в случае клика по labelLinkOffer
        labelLinkOffer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Desktop.getDesktop().browse(new URI("https://ru.wikipedia.org/wiki/One_Piece._%D0%91%D0%BE%D0%BB%D1%8C%D1%88%D0%BE%D0%B9_%D0%BA%D1%83%D1%88"));
                } catch (URISyntaxException uriSyntaxEx) {
                    System.out.println("Проверьте на корректность URL-адрес " + uriSyntaxEx.getMessage());
                } catch (IOException ioEx) {
                    System.out.println("Браузер по умолчанию не найден " + ioEx.getMessage());
                }
            }
        });

        // если пользователь выбирает другой месяц, то пересчитываем и обновляем выпадающий список с количеством дней
        comboBoxMonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateItemsComboBoxDay();
            }
        });

        // если пользователь выбирает другой год, то пересчитываем и обновляем выпадающий список с количеством дней
        comboBoxYear.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateItemsComboBoxDay();
            }
        });
        buttonRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // проверяем заполнены ли поля
                boolean checkFields = checkFillFields();
                // если checkfields() возвращает false то выходим из метода
                if (!checkFields) return;

                // извлекаем данные, которые указал пользователь
                String surname = textFieldSurname.getText();
                String name = textFieldName.getText();
                String telephone = textFieldTelephone.getText();

                int year = (int) comboBoxYear.getSelectedItem();
                int month = comboBoxMonth.getSelectedIndex() + 1;
                int day = (int) comboBoxDay.getSelectedItem();
                LocalDate dateOfBirth = LocalDate.of(year, month, day);

                char gender = (radioButtonMan.isSelected()) ? 'М' : 'Ж';

                PhysicalPerson physicalPerson = new PhysicalPerson(name, surname, telephone, dateOfBirth, gender);

                // выводим на лавной форме имя фамилию пользователя
                FormMain.formMain.initPhysicalPerson(physicalPerson);
                // отображаем панель со статусом операции
                FormMain.formMain.displayPanelStatus("Регистрация завершена");
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormMain.formMain.displayPanelMain();
            }
        });
    }

    public boolean checkFillFields() {
        // если в поле отображаеться подсказка, то это значит что пользователь ничего не ввел
        if (textFieldSurname.getText().equals(SURNAME)) {
            JOptionPane.showMessageDialog(panelRegistration,"Введите фамилию");
            return false;
        }

        if (textFieldName.getText().equals(NAME)) {
            JOptionPane.showMessageDialog(panelRegistration, "Введите имя");
            return false;
        }

        if (textFieldTelephone.getText().equals(TELEPHONE)) {
            JOptionPane.showMessageDialog(panelRegistration, "Введите телефон");
            return false;
        }

        if (!radioButtonMan.isSelected() && radioButtonWoman.isSelected()) {
            JOptionPane.showMessageDialog(panelRegistration, "Выберите пол");
            return false;
        }

        if (textFieldLogin.getText().equals(LOGIN)) {
            JOptionPane.showMessageDialog(panelRegistration, "Введите логин");
            return false;
        }

        String pass = String.valueOf(passwordField.getPassword());
        if (pass.equals(PASS)) {
            JOptionPane.showMessageDialog(panelRegistration, "Придумайте пароль");
            return false;
        }

        return true;

    }

    public void initItemsComboBoxMonth() {
        String[] month = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        // оздаем модель для полей поиска
        DefaultComboBoxModel<String> modelMonth = new DefaultComboBoxModel<>(month);
        // Устанавливаем созданную модель для списка
        comboBoxMonth.setModel(modelMonth);
        // устанавливаем текущий месяц в качестве выбранного по умолчанию
        comboBoxMonth.setSelectedIndex(LocalDateTime.now().getMonthValue() - 1);
    }

    public void initItemsComboBoxYear() {
        for (int year = 1924; year <= LocalDateTime.now().getYear(); year++) {
            comboBoxYear.addItem(year);
        }
        // устанавливаем текущий год в качестве выбранного по умолчанию
        comboBoxYear.setSelectedIndex(comboBoxYear.getItemCount() - 1);
    }

    public void updateItemsComboBoxDay() {
        // в зависимосости от года и месяца определяем колво дней в месяце
        int month = comboBoxMonth.getSelectedIndex() + 1;
        int year = (int) comboBoxYear.getSelectedItem();
        int daysMonth = LocalDate.of(year, month, 1).lengthOfMonth();

        // очищаем выпадающий список с дняими месяца
        comboBoxDay.removeAllItems();

        // добавляем обновленное колво дней
        for (int i = 1; i <= daysMonth; i++) {
            comboBoxDay.addItem(i);
        }
    }

    // удалить подсказку
    public void hintClear(JTextField textField, String hint) {
        if (textField.getText().equals(hint)) {
            textField.setText("");
            textField.setForeground(Color.BLACK);
        }
    }

    // отобразить подсказку
    public void hintDisplay(JTextField textField, String hint) {
        // если поле пустое, то отображаем в поле подсказку и устанавливаем для нее серый цвет
        if (textField.getText().equals("")) {
            textField.setText(hint);
            textField.setForeground(Color.GRAY);
        }
    }
}
