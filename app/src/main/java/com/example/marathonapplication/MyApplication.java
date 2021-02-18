package com.example.marathonapplication;

import android.app.Application;
import android.content.Context;

import com.example.marathonapplication.db.DbHelper;
import com.example.marathonapplication.db.user.LoginUserDao;

public class MyApplication extends Application {
    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LoginUserDao loginUserDao = LoginUserDao.getInstance(context);
        if(loginUserDao.getLoginUsers().size() == 0){
            loginUserDao.addLoginUser("stu1","hermanlimxrays","hermanlimxrays@gmail.com","street 1","123456");
            loginUserDao.addLoginUser("stu2","hermanlimxrays2","hermanlimxrays2@gmail.com","street 2","12345678");
            loginUserDao.addLoginUser("stu3","hermanlimxrays3","hermanlimxrays3@gmail.com","street 3","12345678");
            loginUserDao.addLoginUser("stu4","hermanlimxrays4","hermanlimxrays4@gmail.com","street 4","12345678");
            loginUserDao.addLoginUser("stu5","hermanlimxrays5","hermanlimxrays5@gmail.com","street 5","12345678");
            loginUserDao.addLoginUser("stu6","hermanlimxrays6","hermanlimxrays6@gmail.com","street 6","12345678");
        }

    }
}
