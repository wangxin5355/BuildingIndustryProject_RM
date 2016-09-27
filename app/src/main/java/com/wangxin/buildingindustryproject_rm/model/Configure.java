package com.wangxin.buildingindustryproject_rm.model;

/**
 * Created by wangxin on 2016/7/10.
 */
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * 保存在本地文件中的用户信息
 */
public class Configure implements Serializable {
    private static final long serialVersionUID = 6949210739149936927L;
    /**
     * 邮箱
     */
    @SerializedName("Email")
    private String email;
    /**
     * 用户名
     */
    @SerializedName("UserName")
    private String userName;
    /**
     * 用户订阅的模型名称
     */
    @SerializedName("ModelNam")
    private String modelName;
    /**
     * qq
     */
    @SerializedName("QQ")
    private String qq;
    /**
     * 真实姓名
     */
    @SerializedName("RealName")
    private String realName;
    /**
     * 手机号
     */
    @SerializedName("Telphone")
    private String telphone;
    /**
     * 验证字符串
     */
    @SerializedName("TokenID")
    private String tokenID;
    /**
     * app版本号
     */
    private String appVersion;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}

