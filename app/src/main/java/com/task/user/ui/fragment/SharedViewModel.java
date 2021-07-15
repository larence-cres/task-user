package com.task.user.ui.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.task.user.model.OnTransactionCallback;
import com.task.user.model.User;
import com.task.user.util.RealmUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<List<User>> users;
    private final Realm realm = Realm.getDefaultInstance();

    public void saveUser(User user, OnTransactionCallback onTransactionCall) {
        realm.executeTransactionAsync(realm -> {
            if (user.getId() == null) {
                user.setId(RealmUtil.nextId(realm, user.getClass()));
            }
            realm.insertOrUpdate(user);
        }, () -> {
            if (onTransactionCall != null) {
                onTransactionCall.onRealmSuccess();
            }
        }, error -> {
            if (onTransactionCall != null) {
                onTransactionCall.onRealmError();
            }
        });
    }

    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        users.postValue(realm.where(User.class).sort("createdAt", Sort.DESCENDING).findAllAsync());
    }

}
