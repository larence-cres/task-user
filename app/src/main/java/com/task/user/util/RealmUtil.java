package com.task.user.util;

import java.util.Objects;

import io.realm.Realm;

public class RealmUtil {

    public static Integer nextId(Realm realm, @SuppressWarnings("rawtypes") Class realmObject) {
        int id;
        try {
            id = Objects.requireNonNull(realm.where(realmObject).max("id")).intValue() + 1;
        } catch (Exception e) {
            id = 1;
        }
        return id;
    }

}
