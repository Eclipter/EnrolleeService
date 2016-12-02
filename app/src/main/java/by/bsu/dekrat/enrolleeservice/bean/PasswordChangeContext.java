package by.bsu.dekrat.enrolleeservice.bean;

import java.io.Serializable;

/**
 * Created by USER on 02.12.2016.
 */

public class PasswordChangeContext implements Serializable {

    private String oldPassword;
    private String newPassword;

    public PasswordChangeContext() {
    }

    public PasswordChangeContext(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
