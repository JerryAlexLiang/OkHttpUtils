package com.example.yangliang.okhttputils.bean;

/**
 * 创建日期：2017/4/19 on 上午10:18
 * 描述:
 * 作者:yangliang
 */
public class StartFloatAllBean {

    /**
     * type : 1
     * data : {"userId":"20170106","account":"技术中心","type":"2","unitNo":"科大讯飞股份有限公司"}
     */

    private int type;
    private DataBean data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 20170106
         * account : 技术中心
         * type : 2
         * unitNo : 科大讯飞股份有限公司
         */

        private String userId;
        private String account;
        private String type;
        private String unitNo;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUnitNo() {
            return unitNo;
        }

        public void setUnitNo(String unitNo) {
            this.unitNo = unitNo;
        }
    }
}
