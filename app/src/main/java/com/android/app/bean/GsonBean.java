package com.android.app.bean;

import java.io.Serializable;

/**
 * @author : 流星
 * @CreateDate: 2021/12/11-21:43
 * @Description:
 */
public class GsonBean implements Serializable {

    private String pc;
    private int isDefalut;

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public int getIsDefalut() {
        return isDefalut;
    }

    public void setIsDefalut(int isDefalut) {
        this.isDefalut = isDefalut;
    }

    @Override
    public String toString() {
        return "GsonBean{" +
                "pc='" + pc + '\'' +
                ", isDefalut=" + isDefalut +
                '}';
    }
}
