package com.pan.cookbookproject.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pan.cookbookproject.bean.CookBookBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pan on 16/7/14.
 */
public class CookBookDatabase {
    private final DatabaseHelper databaseHelper;

    public CookBookDatabase(Context context) {
        this.databaseHelper = new DatabaseHelper(context,DatabaseHelper.DATABASE_NAME,null,1);
    }

    /************************************************************
     * 增删改查
     ************************************************************/

    /**
     * 添加
     *
     * @param cookBookBean
     */
    public void insert(CookBookBean cookBookBean) {
        String sql = "insert into " + DatabaseHelper.COOK_BOOK_TABLE_NAME;
        sql += "(name, seasoning, method, remark) values(?, ?, ?, ?)";
        SQLiteDatabase sqlite = databaseHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[]{
                cookBookBean.getName(), cookBookBean.getSeasoning(), cookBookBean.getMethod(),
                cookBookBean.getRemark()});
        sqlite.close();
    }

    /**
     * 删除
     *
     * @param id 菜谱id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = databaseHelper.getWritableDatabase();
        String sql = ("delete from " + DatabaseHelper.COOK_BOOK_TABLE_NAME + " where _id=?");
        sqlite.execSQL(sql, new Integer[]{id});
        sqlite.close();
    }

    /**
     * 删除
     *
     * @param cookBookBean
     */
    public void delete(CookBookBean cookBookBean) {
        if (cookBookBean != null) {
            delete(cookBookBean.getId());
        }
    }

    /**
     * 修改
     *
     * @param cookBookBean
     */
    public void update(CookBookBean cookBookBean) {
        SQLiteDatabase sqlite = databaseHelper.getWritableDatabase();
        String sql = ("update "
                + DatabaseHelper.COOK_BOOK_TABLE_NAME
                + " set name=?, "
                + "seasoning=?, "
                + "method=?, "
                + "remark=? "
                + "where _id=?");
        sqlite.execSQL(sql,
                new String[]{cookBookBean.getName(), cookBookBean.getSeasoning(), cookBookBean.getMethod(),
                        cookBookBean.getRemark(),
                        cookBookBean.getId() + ""});
        sqlite.close();
    }

    /**
     * 条件查询
     *
     * @param where
     * @return 查询的结果集合
     */
    public List<CookBookBean> query(String where) {
        SQLiteDatabase sqlite = databaseHelper.getReadableDatabase();
        ArrayList<CookBookBean> data = null;
        data = new ArrayList<CookBookBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.COOK_BOOK_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            CookBookBean cookBookBean = new CookBookBean();
            cookBookBean.setId(cursor.getInt(0));
            cookBookBean.setName(cursor.getString(1));
            cookBookBean.setSeasoning(cursor.getString(2));
            cookBookBean.setMethod(cursor.getString(3));
            cookBookBean.setRemark(cursor.getString(4));
            data.add(cookBookBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    public List<CookBookBean> query() {
        return query(" ");
    }

    /**
     * 条件查询
     *
     * @param word
     * @param type 0:按名称查询 1:按原料查询 2:按做法查询 3:按备注查询
     * @return 查询的结果集合
     */
    public List<CookBookBean> fuzzyQuery(String word, int type) {
        SQLiteDatabase sqlite = databaseHelper.getReadableDatabase();
        ArrayList<CookBookBean> data = null;
        data = new ArrayList<CookBookBean>();
        Cursor cursor;
        switch (type) {
            case 0:
                cursor = sqlite.rawQuery("select * from "
                        + DatabaseHelper.COOK_BOOK_TABLE_NAME + " where name like ?", new String[]{"%" + word + "%"});
            default:
                cursor = sqlite.rawQuery("select * from "
                        + DatabaseHelper.COOK_BOOK_TABLE_NAME + " where name like ?", new String[]{"%" + word + "%"});
                break;
            case 1:
                cursor = sqlite.rawQuery("select * from "
                        + DatabaseHelper.COOK_BOOK_TABLE_NAME + " where seasoning like ?", new String[]{"%" + word + "%"});
                break;
            case 2:
                cursor = sqlite.rawQuery("select * from "
                        + DatabaseHelper.COOK_BOOK_TABLE_NAME + " where method like ?", new String[]{"%" + word + "%"});
                break;
            case 3:
                cursor = sqlite.rawQuery("select * from "
                        + DatabaseHelper.COOK_BOOK_TABLE_NAME + " where remark like ?", new String[]{"%" + word + "%"});
                break;
        }
//        Cursor cursor = sqlite.rawQuery("select * from "
//                + DatabaseHelper.COOK_BOOK_TABLE_NAME + " where name like ?", new String[]{"%" + word + "%"});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            CookBookBean cookBookBean = new CookBookBean();
            cookBookBean.setId(cursor.getInt(0));
            cookBookBean.setName(cursor.getString(1));
            cookBookBean.setSeasoning(cursor.getString(2));
            cookBookBean.setMethod(cursor.getString(3));
            cookBookBean.setRemark(cursor.getString(4));
            data.add(cookBookBean);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }


    /**
     * 保存一条数据，存在的就更新
     *
     * @param cookBookBean
     */
    public void save(CookBookBean cookBookBean) {
        List<CookBookBean> datas = query(" where _id=" + cookBookBean.getId());
        if (datas != null && !datas.isEmpty()) {
            update(cookBookBean);
        } else {
            insert(cookBookBean);
        }
    }

    /**
     * 重置表数据
     *
     * @param datas 新的数据
     */
    public void reset(List<CookBookBean> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = databaseHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + DatabaseHelper.COOK_BOOK_TABLE_NAME);
            // 重新添加
            for (CookBookBean data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }

    /**
     * 销毁数据库
     */
    public void destroy() {
        databaseHelper.close();
    }
}
