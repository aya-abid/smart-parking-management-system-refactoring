package com.parking.client.model;

/** The login and change password screens are the same for admins and employees, only the texts change. */
public enum AccountType {

    ADMIN("Admin Login", "Username", "Admin Change Password"),
    EMPLOYEE("Employee Login", "Employee Id", "Employee Change Password");

    private final String loginTitle;
    private final String userLabel;
    private final String changePasswordTitle;

    AccountType(String loginTitle, String userLabel, String changePasswordTitle) {
        this.loginTitle = loginTitle;
        this.userLabel = userLabel;
        this.changePasswordTitle = changePasswordTitle;
    }

    public String getLoginTitle() {
        return loginTitle;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public String getChangePasswordTitle() {
        return changePasswordTitle;
    }
}
