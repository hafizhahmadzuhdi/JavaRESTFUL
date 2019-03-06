package sot.rest.model;

import java.util.Objects;

public class Account {
    private int user_id;
    private String user_mail;
    private String name;
    private String password;

    public Account() {
    }

    public Account(int user_id, String user_mail, String name, String password) {
        this.user_id = user_id;
        this.user_mail = user_mail;
        this.name = name;
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "user_id=" + user_id +
                ", user_mail='" + user_mail + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return user_id == account.user_id &&
                Objects.equals(user_mail, account.user_mail) &&
                Objects.equals(name, account.name) &&
                Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, password);
    }
}
