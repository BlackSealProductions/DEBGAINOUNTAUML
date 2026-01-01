package App.Model.Entities.OperationEntities;

import java.time.LocalDate;
import java.util.Date;

import App.Model.Entities.UserEntities.Account;

public class Statement {
    private String StatementId;
    private Account account;
    private LocalDate date;
    private Transaction transaction;



    public Statement(String statementId, Account account, LocalDate date, Transaction transaction) {
        StatementId = statementId;
        this.account = account;
        this.date = date;
        this.transaction = transaction;
    }


    public String getStatementId() {
        return StatementId;
    }


    public void setStatementId(String statementId) {
        StatementId = statementId;
    }


    public Account getAccount() {
        return account;
    }


    public void setAccount(Account account) {
        this.account = account;
    }


    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }


    public Transaction getTransaction() {
        return transaction;
    }


    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    



}
