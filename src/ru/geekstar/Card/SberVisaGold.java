package ru.geekstar.Card;

import ru.geekstar.Account.Account;
import ru.geekstar.Account.SberSavingsAccount;
import ru.geekstar.ClientProfile.SberPhysicalPersonProfile;
import ru.geekstar.Transaction.PayBonusTransaction;

import java.time.LocalDateTime;

public class SberVisaGold extends CardVisa implements IBonusCard {

    @Override
    public void accumulateBonuses(float sumPay) {
        SberPhysicalPersonProfile cardHolder = (SberPhysicalPersonProfile) getCardHolder();
        int bonuses = Math.round((sumPay/100) * cardHolder.getPercentBonusOfSumpay());
        cardHolder.setBonuses(cardHolder.getBonuses() + bonuses);

    }

    @Override
    public void payByCardBonuses(float sumPay, int bonusesPay, String buyProductOrService, String pinCode) {
        // создаем и вносим данные в транзакцию
        PayBonusTransaction payBonusTransaction = new PayBonusTransaction();
        payBonusTransaction.setLocalDateTime(LocalDateTime.now());
        payBonusTransaction.setFromCard(this);
        payBonusTransaction.setTypeOperation("Оплата бонусами");
        payBonusTransaction.setBuyProductOrService(buyProductOrService);

        //определяем владельца карты, так как бонусы привязаны к клиенту, а не к конкретной карте
        SberPhysicalPersonProfile cardHolder = (SberPhysicalPersonProfile) getCardHolder();

        int balanceBonuses = cardHolder.getBonuses();

        //для начала проверяем емть ли у нас столько бонусов сколько мы хотим списать
        //если накопленных бонусов >= тех бонусов, которыми хоти оплатить покупку
        if(balanceBonuses >= bonusesPay) {
            //то бонусов достаточно и мы ими точно сможем оплатить
            //мы знаем что можем оплатить до 99% от стоимости покупки sumPay, а остаток оплатить картой
            //тогда вопрос - какой именно процент от суммы покупки sumPay мы сможем оплатить этими бонусами sumPay
            // а это зависит от кол-во бонусов bonusesPay, которые мы хотим списать, поэтому нужено опнять сколько бонусов мы передали для списания
            // и есть 3 варинта:
            // 1 вариант - передали бонусов bonusesPay > чем 99% от суммы покупки sumPay
            // поэтому раситаем максимально возможную сумму равную 99% от стоимости покупки, которую можно оплатить бонусами
            int sumPay99 = (int) ((sumPay / 100) * 99);

            // Этим условием мы автоматически решаем 2-й и 3-й варинты, потомучто иначе либо бонусы bonusesPay == 99% либо < 99%
            // и в обоих случаях мы просто списываем bonusesPay

            //списываем бонусы, которыми оплачиваем до 99% от стоимости покупки
            cardHolder.setBonuses(balanceBonuses - bonusesPay);
            // рассчитываем остаток от суммы покупки, которую уже нужно будет оплатить картой
            sumPay -= bonusesPay;
            payBonusTransaction.setStatusOperation("Оплата бонусами прошло успешно");
        }
        else payBonusTransaction.setStatusOperation("Недостаточно болнусов");

        // вносим данные в транзакцию
        payBonusTransaction.setPayBonuses(bonusesPay);
        payBonusTransaction.setBalanceBonuses(balanceBonuses);
        this.getPayCardAccount().getPayTransactions().add(payBonusTransaction);
        // остаток оплачиваем картой
        payByCard(sumPay, buyProductOrService, pinCode);


    }

    @Override
    public void payByCard(float sumPay, String buyProductOrService, String pinCode) {
        // вызовем родительскую версию метода
        super.payByCard(sumPay, buyProductOrService, pinCode);

        // и дополним метод уникальным поведением: начислим сбербонусы, которые присуще только картам Сбера
        accumulateBonuses(sumPay);
    }

    @Override
    public void transferCard2Card(Card toCard, float sumTransfer) {
        // вызовем родительскую версию метода
        super.transferCard2Card(toCard, sumTransfer);

        // и дополним метод уникальным поведением:
        // прибавим сумму перевода к общей сумме всех переводов клиентам Сбера без комиссии за месяц, чтобы контролировать лимит
        ((SberPhysicalPersonProfile) getCardHolder()).updateTotalTransfersToClientSberWithoutCommissionMonthInRUB(toCard, sumTransfer);
    }

    @Override
    public void transferCard2Account(Account toAccount, float sumTransfer) {
        // вызовем родительскую версию метода
        super.transferCard2Account(toAccount, sumTransfer);

        // и дополним метод уникальным поведением:
        // прибавим сумму перевода к общей сумме всех переводов клиентам Сбера без комиссии за месяц, чтобы контролировать лимит
        ((SberPhysicalPersonProfile) getCardHolder()).updateTotalTransfersToClientSberWithoutCommissionMonthInRUB(toAccount, sumTransfer);
    }
}
