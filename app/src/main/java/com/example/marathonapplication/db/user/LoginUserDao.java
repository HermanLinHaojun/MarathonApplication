package com.example.marathonapplication.db.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.marathonapplication.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.marathonapplication.db.TableContract.UserInfoEntry.COLUMN_EMAIL;
import static com.example.marathonapplication.db.TableContract.UserInfoEntry.COLUMN_PASSWORD;
import static com.example.marathonapplication.db.TableContract.UserInfoEntry.COLUMN_USER_ADDRESS;
import static com.example.marathonapplication.db.TableContract.UserInfoEntry.COLUMN_USER_ID;
import static com.example.marathonapplication.db.TableContract.UserInfoEntry.COLUMN_USER_NAME;
import static com.example.marathonapplication.db.TableContract.UserInfoEntry.TABLE_NAME;
import static com.example.marathonapplication.db.TableContract.UserInfoEntry._ID;



public class LoginUserDao {
    private DbHelper mDbHelper;
    private LoginUserDao(Context ctx){
        mDbHelper = new DbHelper(ctx);
    }

    private static LoginUserDao instance;
    public static synchronized LoginUserDao getInstance(Context ctx){
        if(instance == null){
            instance = new LoginUserDao(ctx);
        }
        return  instance;
    }

    public void addLoginUser(String userId, String userName, String email, String userAddress, String password){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_USER_ADDRESS, userAddress);
        values.put(COLUMN_PASSWORD, password);


        db.insert(TABLE_NAME, null, values);
    }
//    public void updateBlackNumMode(String costId, String costCustomerId, int state){
//        SQLiteDatabase db = mLoginUserDbHelper.getWritableDatabase();
//        ContentValues values= new ContentValues();
//        values.put(COLUMN_COST_STATE,state);
//        db.update(TABLE_NAME, values," cost_id = ? and customer_id = ? ", new String[]{costId,costCustomerId});
//    }


    public LoginUser login(String email, String password){
        List<LoginUser> loginUsers = new ArrayList<LoginUser>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ COLUMN_EMAIL + " = ? and "+ COLUMN_PASSWORD +" like ? order by _id desc;", new String[]{email,password});
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String mUserId = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
            String mEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String mUserName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            String mUserAddress = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS));
            String mPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            //将number mode 封装到bean中
            LoginUser bean = new LoginUser(mId, mUserId, mEmail, mUserName, mUserAddress, mPassword);
            //封装的对象添加到集合中
            loginUsers.add(bean);

        }
        cursor.close();
        if(loginUsers.size()>0){
            return loginUsers.get(0);
        }else {
            return null;
        }
    }

    public List<LoginUser> getLoginUsers(){
        List<LoginUser> loginUsers = new ArrayList<LoginUser>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by _id desc;", new String[]{});
        while(cursor.moveToNext()){
            String mId = cursor.getString(cursor.getColumnIndex(_ID));
            String mUserId = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
            String mEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String mUserName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            String mUserAddress = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS));
            String mPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            //将number mode 封装到bean中
            LoginUser bean = new LoginUser(mId, mUserId, mEmail, mUserName, mUserAddress, mPassword);
            //封装的对象添加到集合中
            loginUsers.add(bean);

        }

        cursor.close();
        return loginUsers;
    }


}
