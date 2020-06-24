package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

public class UserBuilder {

    private String _firstName;
    private String _lastName;
    private String _email;
    private AccountStatus _accountStatus;

    public static UserBuilder builder(){
        return new UserBuilder();
    }

    public UserBuilder firstName(String firstName){
        this._firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName){
        this._lastName = lastName;
        return this;
    }

    public UserBuilder email(String email){
        this._email = email;
        return this;
    }

    public  UserBuilder accountStatus(AccountStatus accountStatus){
        this._accountStatus = accountStatus;
        return this;
    }

    public User build(){
        User user = new User();
        user.setAccountStatus(_accountStatus);
        user.setLastName(_lastName);
        user.setFirstName(_firstName);
        user.setEmail(_email);
        return user;
    }
}
