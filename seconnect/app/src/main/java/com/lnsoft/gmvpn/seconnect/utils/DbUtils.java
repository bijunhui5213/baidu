package com.lnsoft.gmvpn.seconnect.utils;

import android.util.Log;

import com.lnsoft.gmvpn.seconnect.base.SeconApplicaton;
import com.lnsoft.gmvpn.seconnect.bean.DbBean;
import com.lnsoft.gmvpn.seconnect.dao.DaoMaster;
import com.lnsoft.gmvpn.seconnect.dao.DaoSession;
import com.lnsoft.gmvpn.seconnect.dao.DbBeanDao;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    private static DbUtils dbUtils;
    private final DbBeanDao dbBeanDao;

    public static DbUtils getUtils() {
        if (dbUtils == null) {
            synchronized (DbUtils.class) {
                if (dbUtils == null) {
                    dbUtils = new DbUtils();
                }
            }
        }
        return dbUtils;
    }

        public DbUtils() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(SeconApplicaton.getApplicaton(), "sjxc.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        dbBeanDao = daoSession.getDbBeanDao();
    }

    //插入集合
    public void insertAll(ArrayList<DbBean> list) {
        if (!has())
            dbBeanDao.insertOrReplaceInTx(list);
    }

    //插入对象
    public void insert(DbBean dbBean) {
        if (isHas(dbBean)) {
            return;
        }
        dbBeanDao.insertOrReplace(dbBean);
    }

    //删除
    public void delete(DbBean dbBean) {
        if (isHas(dbBean)) {
            dbBeanDao.delete(dbBean);
        }

    }

    //修改
    public void update(DbBean dbBean) {
        dbBeanDao.update(dbBean);
    }

    //查询所有
    public List<DbBean> queryAll() {
        List<DbBean> list = dbBeanDao.queryBuilder().list();
        Log.e("TAG", list.toString());
        return dbBeanDao.queryBuilder().list();
    }

    public boolean has() {
        List<DbBean> list = dbBeanDao.queryBuilder().list();
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    //判断是否存在
    public boolean isHas(DbBean dbBean) {
        List<DbBean> list = dbBeanDao.queryBuilder().where(DbBeanDao.Properties.PackageName.eq(dbBean
                .getPackageName())).list();

        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
