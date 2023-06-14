package com.example.musicbackend.Utils;

import com.example.musicbackend.entity.User;
import com.example.musicbackend.entity.base.BaseEntity;

import java.util.Date;

public class DBLogicUtil {

    public static void setupUpdate(BaseEntity baseEntity, User user){
        baseEntity.setUpdateAt(DateUtil.getNow());
        baseEntity.setIdUpdateBy(user.getId());
    }

    public static void setupDelete(BaseEntity baseEntity, User user){
        baseEntity.setUpdateAt(DateUtil.getNow());
        baseEntity.setIdUpdateBy(user.getId());
        baseEntity.setDeleteFlag(true);
    }

    public static void setupCreate(BaseEntity baseEntity, User user){
        Date now = DateUtil.getNow();
        baseEntity.setCreateAt(now);
        baseEntity.setIdCreateBy(user.getId());
        baseEntity.setUpdateAt(now);
        baseEntity.setIdUpdateBy(user.getId());
        baseEntity.setDeleteFlag(false);
        baseEntity.setId(null);
    }
}
