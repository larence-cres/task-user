package com.task.user.ui.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.task.user.model.User;
import com.task.user.util.RealmUtil;

import java.util.List;

import io.realm.Realm;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<List<User>> users;
    private final Realm realm = Realm.getDefaultInstance();

    public User saveUser(User user) {

        realm.executeTransactionAsync((realm) -> {
            if (user.getId() == null) {
                user.setId(RealmUtil.nextId(realm, user.getClass()));
            }

            realm.insertOrUpdate(user);
        });

        return user;
    }

    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        users.postValue(realm.where(User.class).findAll());
    }

}
