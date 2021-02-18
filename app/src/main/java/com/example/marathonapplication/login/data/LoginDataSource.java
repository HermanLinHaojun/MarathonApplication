package com.example.marathonapplication.login.data;

import com.example.marathonapplication.MyApplication;
import com.example.marathonapplication.db.user.LoginUser;
import com.example.marathonapplication.db.user.LoginUserDao;
import com.example.marathonapplication.login.data.model.LoggedInUser;
import com.example.marathonapplication.model.Config;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoginUserDao loginUserDao = LoginUserDao.getInstance(MyApplication.getAppContext());
            LoginUser loginUser = loginUserDao.login(username,password);
            if(loginUser == null){
//                return new Result.Error(new IOException("Error not exist this user", new IllegalArgumentException()));
                return new Result.Error(new IOException("Error logging in", new IllegalArgumentException()));
            }else {
                Config.local_user_id = loginUser.getId();
                Config.local_user_name = loginUser.getUserName();
                return new Result.Success<>(new LoggedInUser(loginUser.getUserId(),loginUser.getUserName()));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}