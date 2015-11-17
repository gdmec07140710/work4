package com.example.administrator.work4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Vector;

/**
 * Created by Administrator on 2015/11/10.
 */
public class ContactsTable {
    private final static String TABLENAME="contactsTable";
    private MyDB db;

    public ContactsTable(Context context){
        db=new MyDB(context);
        if(!db.isTableExits(TABLENAME)){
            String createTableSql="CREATE TABLE IF NOT EXISTS "+
                    TABLENAME+"(id_DB integer primary key AUTOINCREMENT,"+
                    User.NAME +" VARCHAR,"+
                    User.MOBILE +" VARCHAR,"+
                    User.QQ +" VARCHAR,"+
                    User.DANWEI +" VARCHAR,"+
                    User.ADDRESS+ "VARCHAR)";
            db.creatTable(createTableSql);
        }
    }
    public boolean addData(User user){
        ContentValues values = new ContentValues();
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.QQ,user.getQq());
        values.put(User.ADDRESS,user.getAddress());
        return  db.save(TABLENAME,values);
    }

    public User[] getAllUser(){
        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try{
            cursor = db.find("select * from "+TABLENAME,null);
            while(cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                v.add(temp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
            db.closeConnection();
        }
        if(v.size()>0){
            return v.toArray(new User[]{});
        }else{
            User[] users = new User[1];
            User user = new User();
            user.setName("无结果");
            users[0] = user;
            return users;
        }
    }

    public User getUserByID(int id){
        Cursor cursor = null;
        try{
            cursor = db.find("select * from "+TABLENAME + "where id_DB=?",new String[]{id+""});
            User temp = new User();
            cursor.moveToNext();
            temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
            temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
            temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
            temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
            temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
            return temp;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.closeConnection();
        }
        return null;
    }

    public boolean updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.QQ,user.getQq());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.ADDRESS,user.getAddress());
        return db.update(TABLENAME,values,"id_ab=?",new String[]{user.getId_DB()+""});
    }

    public User[] findUserByKey(String key){
        Vector<User> v = new Vector();
        Cursor cursor = null;
        try{
            cursor = db.find(
                    "select * from "+ TABLENAME +" where "
                            +User.NAME+" like '%"+key+"%'"+
                            "or"+User.MOBILE+" like '%"+key+"%'"+
                            "or"+User.QQ+" like '%"+key+"%'"
                    ,null);
            while(cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(
                        cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(
                        cursor.getColumnIndex(User.NAME)));
                temp.setMobile(cursor.getString(
                        cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(
                        cursor.getColumnIndex(User.DANWEI)));
                temp.setQq(cursor.getString(
                        cursor.getColumnIndex(User.QQ)));
                temp.setAddress(cursor.getString(
                        cursor.getColumnIndex(User.ADDRESS)));
                v.add(temp);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
            db.closeConnection();
        }
        if(v.size()>0){
            return v.toArray(new User[]{});
        }else{
            User[] users = new User[1];
            User user = new User();
            user.setName("无结果");
            users[0] = user;
            return users;
        }
    }

    public boolean deleteByUser(User user){
        return db.delete(TABLENAME,
                " id+DB=?",new String[]{user.getId_DB()+""});
    }
}