package ru.geekstar.Form;

import ru.geekstar.ClientProfile.PhysicalPersonProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;

public class FormTransactions {
    private JPanel panelTransactions;
    private JButton buttonCancel;
    private JPanel panelTransactionsList;
    private JScrollPane scrollPaneTransactionList;

    public JPanel getPanelTransactions(){
        return panelTransactions;
    }

    public FormTransactions() {
        // убираем рамку
        scrollPaneTransactionList.setBorder(null);

        // менеджера компоновки BoxLayout
        // константа Y_AXIS указывает, что компоненты должны располагаться вертикально сверху донизу
        panelTransactionsList.setLayout(new BoxLayout(panelTransactionsList, BoxLayout.Y_AXIS));

        panelTransactions.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                // обновляем панель с транзакциями
                updatePanelTransactions();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormMain.formMain.displayPanelMain();
            }
        });
    }

    public void updatePanelTransactions(){
        // удаляем все компоненты JLabel с транзакциями с панели
        panelTransactionsList.removeAll();

        // запрашиваем все профили пользователя
        ArrayList<PhysicalPersonProfile> profiles = FormMain.physicalPerson.getPhysicalPersonProfiles();
        // перебираем профили
        for (PhysicalPersonProfile profile : profiles) {
            // получаем массив строк транзакций путём их деления двойным переносом строки
            String[] transactions = profile.displayProfileTransactions().split("\n\n");

            // перебираем строки с транзакциями
            for (String transaction : transactions) {
                ImageIcon iconTransaction = null;
                // если строка содержит символ номера "#" и одно из ключевых слов, то определяем соответствующую иконку для отображения транзакции
                if (transaction.contains("#")) {
                    if ((transaction.contains("Покупка") || transaction.contains("Оплата"))) {
                        iconTransaction = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "Payment.png"));
                    }
                    if ((transaction.contains("Перевод"))) {
                        iconTransaction = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "Transfer.png"));
                    }
                    if ((transaction.contains("Пополнение") || transaction.contains("Внесение"))) {
                        iconTransaction = new ImageIcon(getClass().getResource(File.separator + "resources" + File.separator + "TopUp.png"));
                    }
                }
                // создаём компонент JLabel для визуального отображения транзакции
                JLabel labelIconTransaction = new JLabel();
                labelIconTransaction.setIcon(iconTransaction);
                labelIconTransaction.setText("<html>" + transaction.replace("\n", "<br>") + "</html>");
                // устанавливаем отступ для текста от иконки транзакции
                labelIconTransaction.setIconTextGap(10);
                // устанавливаем выравнивание текста относительно иконки транзакции
                labelIconTransaction.setHorizontalTextPosition(JLabel.RIGHT);
                labelIconTransaction.setVerticalTextPosition(JLabel.CENTER);

                // добавляем созданный labelIconTransaction с иконкой и описанием транзакции на панель
                panelTransactionsList.add(labelIconTransaction);

                // создаём горизонтальный разделитель между транзакциями
                JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                // задаём размеры разделителя
                separator.setMaximumSize(new Dimension(1450, 20));
                // добавляем разделитель на панель
                panelTransactionsList.add(separator);

            }
        }

        // применить все изменения
        panelTransactionsList.revalidate();
        // перерисовать компоненты
        panelTransactionsList.repaint();
    }
}
