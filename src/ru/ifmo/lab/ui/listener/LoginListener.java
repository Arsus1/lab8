package ru.ifmo.lab.ui.listener;

import ru.ifmo.lab.database.CurrentUser;

public interface LoginListener {
    void onLoginSuccessful(CurrentUser user);
    void onLoginError(String message);
}
