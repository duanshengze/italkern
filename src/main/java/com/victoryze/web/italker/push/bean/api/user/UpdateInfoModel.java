package com.victoryze.web.italker.push.bean.api.user;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.victoryze.web.italker.push.bean.db.User;


/**
 * Created by dsz on 2018/2/25.
 */
public class UpdateInfoModel {

    @Expose
    private String name;
    @Expose
    private String portrait;
    @Expose
    private String desc;
    @Expose
    private int sex;

    public static boolean check(UpdateInfoModel model) {

        //model不允许为kong
        //并且只需要具有一个及其以上的参数即可
        return model != null && (!Strings.isNullOrEmpty(model.name)
                || !Strings.isNullOrEmpty(model.desc) ||
                !Strings.isNullOrEmpty(model.portrait) ||
                model.sex != 0);
    }


    /**
     * 把当前的信息填充到用户Model中
     *
     * @param user
     * @return
     */
    public User updateToUser(User user) {
        if (!Strings.isNullOrEmpty(name)) {
            user.setName(name);
        }

        if (!Strings.isNullOrEmpty(portrait)) {
            user.setPortrait(portrait);
        }

        if (!Strings.isNullOrEmpty(desc)) {
            user.setDescription(desc);
        }

        if (sex != 0) {
            user.setSex(sex);
        }
        return user;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
