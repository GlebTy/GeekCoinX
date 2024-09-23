package ru.geekstar.Form;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.PayCardAccount;
import ru.geekstar.Account.SavingsAccount;
import ru.geekstar.Card.*;
import ru.geekstar.ClientProfile.PhysicalPersonProfile;
import ru.geekstar.PhysicalPerson.PhysicalPerson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FormMain extends JDialog {
    private JPanel contentPanel;
    private JPanel panelMain;
    private JLabel labelNameClient;
    private JButton buttonPay;
    private JButton buttonTransfer;
    private JButton buttonDepositing;
    private JButton buttonOpenAccount;
    private JButton buttonOpenCard;
    private JButton buttonRegistration;
    private JButton buttonTransactions;
    private JLabel labelSwitchAccountForCard;
    private JComboBox comboBoxAccountsCard;
    private JLabel labelAddAccountForCard;
    private JComboBox comboBoxAddAccountForCard;
    private JButton buttonAddAccountForCard;
    private JLabel labelDescription;
    private JScrollPane scrollPaneCardsAccounts;
    private JPanel panelCardsAccounts;

    private Card selectedCard;
    private Account selectedAccount;
    private FormStatus formStatus;
    private CardLayout cardLayout;
    public static FormMain formMain;
    public static PhysicalPerson physicalPerson;

    public FormMain() {
        setContentPane(contentPanel);
        setModal(true);

        // создаём менеджер расположения для переключения между панелями форм
        cardLayout = new CardLayout();
        // устанавливаем созданный менеджер для нашкй основной панели
        contentPanel.setLayout(cardLayout);

        // создаём объекты всех форм
        FormRegistration formRegistration = new FormRegistration();
        FormOpenCard formOpenCard = new FormOpenCard();
        FormOpenAccount formOpenAccount = new FormOpenAccount();
        FormPay formPay = new FormPay();
        FormTransfer formTransfer = new FormTransfer();
        FormDepositing formDepositing = new FormDepositing();
        FormTransactions formTransactions = new FormTransactions();
        formStatus = new FormStatus();

        // получаем объекты панелей всех форм
        JPanel panelRegistration = formRegistration.getPanelRegistration();
        JPanel panelOpenCard = formOpenCard.getPanelOpenCard();
        JPanel panelOpenAccount = formOpenAccount.getPanelOpenAccount();
        JPanel panelPay = formPay.getPanelPay();
        JPanel panelTransfer = formTransfer.getPanelTransfer();
        JPanel panelDepositing = formDepositing.getPanelDepositing();
        JPanel panelTransactions = formTransactions.getPanelTransactions();
        JPanel panelStatus = formStatus.getPanelStatus();

        // добавляем панели и основную панель главной формы
        // и назначаем имена для каждой панели для переключения между ними
        contentPanel.add("PANEL_MAIN", panelMain);
        contentPanel.add("PANEL_REGISTRATION", panelRegistration);
        contentPanel.add("PANEL_OPEN_CARD", panelOpenCard);
        contentPanel.add("PANEL_OPEN_ACCOUNT", panelOpenAccount);
        contentPanel.add("PANEL_PAY", panelPay);
        contentPanel.add("PANEL_TRANSFER", panelTransfer);
        contentPanel.add("PANEL_DEPOSITING", panelDepositing);
        contentPanel.add("PANEL_TRANSACTIONS", panelTransactions);
        contentPanel.add("PANEL_STATUS", panelStatus);

        // убрать рамку
        scrollPaneCardsAccounts.setBorder(null);

        // менеджера компоновки BoxLayout
        // константа Y_AXIS указывает, что компоненты должны рапспологаться вертикально сверху донизу
        panelCardsAccounts.setLayout(new BoxLayout(panelCardsAccounts, BoxLayout.Y_AXIS));

        // устанавливаем иконки для кнопок
        ImageIcon iconOpenCard = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "OpenCard.png"));
        buttonOpenCard.setIcon(iconOpenCard);

        ImageIcon iconOpenAccount = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "OpenAccount.png"));
        buttonOpenAccount.setIcon(iconOpenAccount);

        ImageIcon iconPayment = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "Payment.png"));
        buttonPay.setIcon(iconPayment);

        ImageIcon iconTransfer = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "Transfer.png"));
        buttonTransfer.setIcon(iconTransfer);

        ImageIcon iconTopup = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "TopUp.png"));
        buttonDepositing.setIcon(iconTopup);

        // делаем иконки недоступными
        buttonTransactions.setEnabled(false);
        buttonOpenCard.setEnabled(false);
        buttonOpenAccount.setEnabled(false);
        buttonPay.setEnabled(false);
        buttonTransactions.setEnabled(false);
        buttonDepositing.setEnabled(false);

        // делаем кнопки невидимыми
        labelNameClient.setVisible(false);
        labelSwitchAccountForCard.setVisible(false);
        comboBoxAccountsCard.setVisible(false);
        labelAddAccountForCard.setVisible(false);
        comboBoxAddAccountForCard.setVisible(false);
        buttonAddAccountForCard.setVisible(false);

        // заглушка для пропуска формы регистрации
        // PhysicalPerson I = new PhysicalPerson("Глеб", "Тюрин", "+79277394841", LocalDate.of(2005, 1, 9), 'M');
        // установить и отобразить на главной форме зарегистрированного пользователя
        // initPhysicalPerson(I);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel,"PANEL_REGISTRATION");
            }
        });
        buttonOpenCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "PANEL_OPEN_CARD");
            }
        });
        buttonOpenAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "PANEL_OPEN_ACCOUNT");
            }
        });
        buttonPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "PANEL_PAY");
            }
        });
        buttonTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "PANEL_TRANSFER");
            }
        });
        buttonDepositing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "PANEL_DEPOSITING");
            }
        });
        buttonTransactions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "PANEL_TRANSACTIONS");
            }
        });
        comboBoxAccountsCard.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // если выбранная пользователем карта является мультивалютной
                if (selectedCard instanceof IMulticurrencyCard) {
                    // получаем текущий (выбранный пользователем) элемент из списка
                    Object selectedItem = comboBoxAccountsCard.getSelectedItem();
                    // если текующий
                    if (selectedItem instanceof PayCardAccount) {
                        // явно приводим Object к PayCardAccount
                        PayCardAccount payCardAccount = (PayCardAccount) selectedItem;
                        String currencyCode = payCardAccount.getCurrencyCode();
                        // переключаем счет карты на текущий (выбранный пользавателем) счет из списка
                        physicalPerson.switchAccountOfMulticurrencyCard((IMulticurrencyCard) selectedCard, currencyCode);
                        // обновляем панель с картами
                        updatePanelCardsAccounts();
                    }
                }
            }
        });
        buttonAddAccountForCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // если выбранная карта является мультивалютной
                if (selectedCard instanceof IMulticurrencyCard) {
                    // явно приводим Card к интерфейсу IMulticurrencyCard
                    IMulticurrencyCard multicurrencyCard = (IMulticurrencyCard) selectedCard;
                    // создаем и привязываем новый счет к карте
                    physicalPerson.addAccountToMulticurrencyCard(multicurrencyCard, comboBoxAddAccountForCard.getSelectedItem().toString());
                    // Обновляем список со счетами новым созданным счетом для переключения между ними
                }
            }
        });

        //слушатель срабатывает в момент показа компонента panelMain
        panelMain.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                // обновляем главную панель с картами и счетами
                updatePanelCardsAccounts();
            }
        });
    }

    public void initPhysicalPerson(PhysicalPerson physicalPerson) {
        this.physicalPerson = physicalPerson;

        // устанавливаем иконку и отображаем имя и фамилию зарегесрированного пользователя
        ImageIcon iconUser = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "User.png"));
        labelNameClient.setIcon(iconUser);
        labelNameClient.setText(physicalPerson.getFirstName() + " " + physicalPerson.getLastName());
        labelNameClient.setVisible(true);

        // делаем кнопку регистрации невидимой
        buttonRegistration.setVisible(false);

        // делаем кнопки доступными
        buttonTransactions.setEnabled(true);
        buttonOpenCard.setEnabled(true);
        buttonOpenAccount.setEnabled(true);
        buttonPay.setEnabled(true);
        buttonTransfer.setEnabled(true);
        buttonDepositing.setEnabled(true);
    }

    public void updatePanelCardsAccounts() {
        // удаляем все компоненты JLabel с картами и счетами с панели
        panelCardsAccounts.removeAll();

        // коллекция для хранения объектов в виде пар : объекта JLabel и объекта Card
        HashMap<JLabel,Card> cardsHashMap = new HashMap<>();
        // коллекция для хранения объектов в виде пар: объекта JLabel и объекта Account
        HashMap<JLabel, Account> accountsHashMap = new HashMap<>();

        // получаем все профили физ лица
        ArrayList<PhysicalPersonProfile> profiles = physicalPerson.getPhysicalPersonProfiles();

        // перебираем профили
        for (PhysicalPersonProfile profile : profiles) {
            // получаем массив карт профиля
            ArrayList<Card> cards = profile.getCards();
            // перебираем все карты профиля
            for (Card card : cards) {
                // создаем помпонент JLabel для визуального отображения иконки и описания карты
                JLabel labelIconCard = new JLabel();
                // станавливаем иконку для отображения карты
                ImageIcon iconCard = new ImageIcon(getClass().getResource(File.separator + "resourses" + File.separator + card.getClass().getSimpleName() + ".png"));
                labelIconCard.setIcon(iconCard);
                labelIconCard.setText("<html>" + "Карта " + card.getClass().getSimpleName() + " ⦁⦁" + card.getNumberCard().split(" ")[3]  + "<br>" +
                        "Платёжный счёт ⦁⦁" + card.getPayCardAccount().getNumberAccount().substring(20) + "<br>" +
                        ((card instanceof IBonusCard) ? "Бонусов " + ((IBonusCard) card).getBonuses() + "<br>" : "") +
                        ((card instanceof IAirlinesCard) ? "Миль " + ((IAirlinesCard) card).getMiles() + "<br>" : "") +
                        ((card instanceof ICashbackCard) ? "Кэшбэк " + ((ICashbackCard) card).getCashback() + "<br>" : "") +
                        "<big>" + card.getPayCardAccount().getBalance() + " " + card.getPayCardAccount().getCurrencySymbol() +
                        "</big></html>");
                // устанавливаем отступ для текста от иконки карты
                labelIconCard.setIconTextGap(15);
                // устанавливаем выравнивание текста относительно иконки карты
                labelIconCard.setHorizontalTextPosition(JLabel.RIGHT);
                labelIconCard.setVerticalTextPosition(JLabel.CENTER);

                // добавляем в коллекцию пару: объект labelIconCard и объект card
                cardsHashMap.put(labelIconCard, card);

                labelIconCard.addMouseListener(new MouseAdapter() {
                    @Override
                    // слушатель срабатывает в случае клика по labelIconCard
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        // определяем объект выбранной пользователем карты по labelIconCard, по которому кликнул пользователь
                        selectedCard = cardsHashMap.get(labelIconCard);

                        String pinCode = selectedCard.getPinCode();

                        // выводим информацию о карте
                        labelDescription.setText("<html><b>О карте</b><br>" + selectedCard.getClass().getSimpleName() +
                                "<br><br>Номер карты<br>" + selectedCard.getNumberCard() +
                                "<br><br>Владелец карты<br>" + selectedCard.getCardHolder().getPhysicalPerson() +
                                "<br><br>Статус карты<br>" + selectedCard.getStatusCard() +
                                "<br><br>PIN-код " + pinCode + "</html>");

                        // если карта мультивалютная, то отображаем компоненты для переключения и добавления счетов
                        if(selectedCard instanceof IMulticurrencyCard) {
                            updateDisplaySwitchAccountForMulticurrencyCard();
                        } else {
                            // если не мультивалютная, то делаем компоненты невидимыми
                            labelSwitchAccountForCard.setVisible(false);
                            comboBoxAccountsCard.setVisible(false);
                            labelAddAccountForCard.setVisible(false);
                            comboBoxAddAccountForCard.setVisible(false);
                            buttonAddAccountForCard.setVisible(false);
                        }
                    }
                });

                // добавляем созданный labelIconCard с иконкой и описанием карты на панель
                panelCardsAccounts.add(labelIconCard);
                // создаём невидимый компонент фиксированной высоты между двумя компонентами для отступа
                panelCardsAccounts.add(Box.createVerticalStrut(15));
            }

            // аналогично для счетов
            ArrayList<Account> accounts = profile.getAccounts();
            for (Account account : accounts) {
                // отображаем только сберегательные счета
                if (account instanceof SavingsAccount) {
                    JLabel labelIconAccount = new JLabel();
                    ImageIcon iconAccount = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + account.getClass().getSimpleName() + ".png"));
                    labelIconAccount.setIcon(iconAccount);
                    labelIconAccount.setText("<html>" + account + "<br>" + "<big>" + account.getBalance() + " " + account.getCurrencySymbol() + "</big></html>");
                    labelIconAccount.setIconTextGap(15);
                    labelIconAccount.setHorizontalTextPosition(JLabel.RIGHT);
                    labelIconAccount.setVerticalTextPosition(JLabel.CENTER);

                    accountsHashMap.put(labelIconAccount, account);

                    labelIconAccount.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            // делаем компоненты невидимыми, так как они предназначены только для мультивалютных карт
                            labelSwitchAccountForCard.setVisible(false);
                            comboBoxAccountsCard.setVisible(false);
                            labelAddAccountForCard.setVisible(false);
                            comboBoxAddAccountForCard.setVisible(false);
                            buttonAddAccountForCard.setVisible(false);

                            // определяем объект выбранного пользователем счёта по labelIconAccount, по которому кликнул пользователь
                            selectedAccount = accountsHashMap.get(labelIconAccount);

                            // выводим информацию
                            labelDescription.setText("<html><b>О счёте</b><br>" + selectedAccount.getClass().getSimpleName() +
                                    "<br><br>Номер счёта<br>" + selectedAccount.getNumberAccount() +
                                    "<br><br>Владелец счёта<br>" + selectedAccount.getAccountHolder().getPhysicalPerson() +
                                    "<br><br>Валюта счёта<br>" + selectedAccount.getCurrencyCode() + "</html>");
                        }
                    });

                    // добавляем созданный labelIconAccount с иконкой и описанием счёта на панель
                    panelCardsAccounts.add(labelIconAccount);
                    panelCardsAccounts.add(Box.createVerticalStrut(15));
                }
            }
        }

        // применить все изменения
        panelCardsAccounts.revalidate();
        // перерисовать компоненты
        panelCardsAccounts.repaint();
    }

    public void updateDisplaySwitchAccountForMulticurrencyCard() {
        // очищаем выпадающий список со счетами карты для переключения между ними
        comboBoxAccountsCard.removeAllItems();

        // выбранный пользователем объект карты явно приводим к типу интерфейса мультивалютной карты
        IMulticurrencyCard multicurrencyCard = (IMulticurrencyCard) selectedCard;

        // запрашиваем текущий основной счёт карты
        PayCardAccount payCardAccount = multicurrencyCard.getPayCardAccount();
        //// добавляем в список текущий основной счёт карты для переключения между счетами
        comboBoxAccountsCard.addItem(payCardAccount);

        // запрашиваем дополнительные мульти счета карты
        ArrayList<PayCardAccount> multicurrencyAccounts = multicurrencyCard.getMulticurrencyAccounts();
        // добавляем в список дополнительные мульти счета карты для переключения между счетами
        for (PayCardAccount account : multicurrencyAccounts) comboBoxAccountsCard.addItem(account);

        // очищаем выпадающий список с кодами валют для создания нового счёта мультивалютной карты
        comboBoxAddAccountForCard.removeAllItems();

        // создаём список с кодами валют для выбора пользователем и создания нового счёта в валюте для мультивалютной карты
        ArrayList<String> currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("EUR");

        // если список с кодами валют уже содержит код валюты текущего счёта карты, то удаляем код валюты уже существующего счёта карты из списка
        if (currencies.contains(payCardAccount.getCurrencyCode())) currencies.remove(payCardAccount.getCurrencyCode());

        // если список с кодами валют уже содержит коды валют мульти счётов карты, то удаляем коды валют уже существующих счётов карты из списка
        for (PayCardAccount account : multicurrencyAccounts) {
            if (currencies.contains(account.getCurrencyCode())) currencies.remove(account.getCurrencyCode());
        }

        // полученный список с кодами валют, счета в которых ещё не существуют, устанавливаем в выпадающий список
        comboBoxAddAccountForCard.setModel(new DefaultComboBoxModel(currencies.toArray()));

        // делаем компоненты видимыми
        labelSwitchAccountForCard.setVisible(true);
        comboBoxAccountsCard.setVisible(true);
        labelAddAccountForCard.setVisible(true);
        comboBoxAddAccountForCard.setVisible(true);
        buttonAddAccountForCard.setVisible(true);

        // если список с кодами валют не пустой, то делаем компоненты доступными
        if (!currencies.isEmpty()) {
            labelAddAccountForCard.setEnabled(true);
            comboBoxAddAccountForCard.setEnabled(true);
            buttonAddAccountForCard.setEnabled(true);
        } else {
            // иначе недоступными
            labelAddAccountForCard.setEnabled(false);
            comboBoxAddAccountForCard.setEnabled(false);
            buttonAddAccountForCard.setEnabled(false);
        }
    }
    public void displayPanelMain() {
        cardLayout.show(contentPanel, "PANEL_MAIN");
    }

    public void displayPanelStatus(String status) {
        formStatus.getLabelStatus().setText(status);
        // переключаемся на панель panelStatus для ее обработки
        cardLayout.show(contentPanel, "PANEL_STATUS");
    }

    public static void main(String[] args) {
        formMain = new FormMain();
        formMain.setTitle("GeekCoinX");
        // устанавливаем размер формы
        formMain.setSize(1200,700);
        // для отображения формы по центру экрана
        formMain.setLocationRelativeTo(null);
        // делаем форму видимой для пользователя
        formMain.setVisible(true);
    }

}
