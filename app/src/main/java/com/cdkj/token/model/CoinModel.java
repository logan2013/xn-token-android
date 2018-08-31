package com.cdkj.token.model;

import com.cdkj.baselibrary.interfaces.AmountShowTypeInterface;
import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lei on 2017/10/25.
 */

public class CoinModel implements Serializable, AmountShowTypeInterface {


    /**
     * accountList : [{"accountNumber":"A201711212030187096126","userId":"U201711212030187015235","realName":"18984955240","type":"C","status":"0","currency":"ETH","amountString":"0","frozenAmountString":"0","md5":"4122cb13c7a474c1976c9706ae36521d","addAmountString":"0","inAmountString":"0","outAmountString":"0","createDatetime":"Nov 21, 2017 8:30:18 PM","systemCode":"CD-COIN000017","companyCode":"CD-COIN000017","coinAddress":"0x2fa7a62e954995b6b96ecc9f7b622a7ad784f6e5"}]
     * totalAmountCNY : 0.0000
     * totalAmountUSD : 0.00
     * totalAmountHKD : 0.00
     */

    private String totalAmountCNY;
    private String totalAmountUSD;
    private String totalAmountHKD;
    private String totalAmountKRW;
    private List<AccountListBean> accountList;

    public String getTotalAmountKRW() {
        return totalAmountKRW;
    }

    public void setTotalAmountKRW(String totalAmountKRW) {
        this.totalAmountKRW = totalAmountKRW;
    }

    public String getTotalAmountCNY() {
        return totalAmountCNY;
    }

    public void setTotalAmountCNY(String totalAmountCNY) {
        this.totalAmountCNY = totalAmountCNY;
    }

    public String getTotalAmountUSD() {
        return totalAmountUSD;
    }

    public void setTotalAmountUSD(String totalAmountUSD) {
        this.totalAmountUSD = totalAmountUSD;
    }

    public String getTotalAmountHKD() {
        return totalAmountHKD;
    }

    public void setTotalAmountHKD(String totalAmountHKD) {
        this.totalAmountHKD = totalAmountHKD;
    }

    public List<AccountListBean> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountListBean> accountList) {
        this.accountList = accountList;
    }

    @Override
    public String _getAmountStringUSD() {
        return totalAmountUSD;
    }

    @Override
    public String _getAmountStringCNY() {
        return totalAmountCNY;
    }

    @Override
    public String _getAmountStringKRW() {
        return totalAmountKRW;
    }

    public static class AccountListBean implements Serializable, IPickerViewData {
        /**
         * accountNumber : A201711212030187096126
         * userId : U201711212030187015235
         * realName : 18984955240
         * type : C
         * status : 0
         * currency : ETH
         * amountString : 0
         * frozenAmountString : 0
         * md5 : 4122cb13c7a474c1976c9706ae36521d
         * addAmountString : 0
         * inAmountString : 0
         * outAmountString : 0
         * createDatetime : Nov 21, 2017 8:30:18 PM
         * systemCode : CD-COIN000017
         * companyCode : CD-COIN000017
         * coinAddress : 0x2fa7a62e954995b6b96ecc9f7b622a7ad784f6e5
         */

        private String accountNumber;
        private String userId;
        private String realName;
        private String type;
        private String status;
        private String currency;
        private String amountString;  //总余额
        private BigDecimal amount;  //总余额
        private String frozenAmountString;//冻结余额
        private BigDecimal frozenAmount;//冻结余额
        private String md5;
        private String addAmountString;
        private String inAmountString;
        private String outAmountString;
        private String createDatetime;
        private String systemCode;
        private String companyCode;
        private String coinAddress;
        private String coinBalance;

        private String amountCNY;
        private String amountUSD;
        private String amountHKD;
        private String priceCNY;
        private String priceUSD;
        private String priceHKD;

        private String localCoinType;//本地类型
        private boolean isChoose;//本地是否配置选中

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getFrozenAmount() {
            return frozenAmount;
        }

        public void setFrozenAmount(BigDecimal frozenAmount) {
            this.frozenAmount = frozenAmount;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public String getLocalCoinType() {
            return localCoinType;
        }

        public void setLocalCoinType(String localCoinType) {
            this.localCoinType = localCoinType;
        }

        public String getAmountCNY() {
            return amountCNY;
        }

        public String getAmountUSD() {
            return amountUSD;
        }

        public void setAmountUSD(String amountUSD) {
            this.amountUSD = amountUSD;
        }

        public String getAmountHKD() {
            return amountHKD;
        }

        public void setAmountHKD(String amountHKD) {
            this.amountHKD = amountHKD;
        }

        public String getPriceCNY() {
            return priceCNY;
        }

        public void setPriceCNY(String priceCNY) {
            this.priceCNY = priceCNY;
        }

        public String getPriceUSD() {
            return priceUSD;
        }

        public void setPriceUSD(String priceUSD) {
            this.priceUSD = priceUSD;
        }

        public String getPriceHKD() {
            return priceHKD;
        }

        public void setPriceHKD(String priceHKD) {
            this.priceHKD = priceHKD;
        }

        public void setAmountCNY(String amountCNY) {
            this.amountCNY = amountCNY;
        }

        public String getCoinBalance() {
            return coinBalance;
        }

        public void setCoinBalance(String coinBalance) {
            this.coinBalance = coinBalance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAmountString() {
            return amountString;
        }

        public void setAmountString(String amountString) {
            this.amountString = amountString;
        }

        public String getFrozenAmountString() {
            return frozenAmountString;
        }

        public void setFrozenAmountString(String frozenAmountString) {
            this.frozenAmountString = frozenAmountString;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getAddAmountString() {
            return addAmountString;
        }

        public void setAddAmountString(String addAmountString) {
            this.addAmountString = addAmountString;
        }

        public String getInAmountString() {
            return inAmountString;
        }

        public void setInAmountString(String inAmountString) {
            this.inAmountString = inAmountString;
        }

        public String getOutAmountString() {
            return outAmountString;
        }

        public void setOutAmountString(String outAmountString) {
            this.outAmountString = outAmountString;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getCoinAddress() {
            return coinAddress;
        }

        public void setCoinAddress(String coinAddress) {
            this.coinAddress = coinAddress;
        }

        @Override
        public String getPickerViewText() {
            return currency;
        }
    }
}
