package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.StringUtil;
import com.cdkj.token.model.WithdrawOrderModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lei on 2017/10/31.
 */

public class WithdrawOrderAdapter extends BaseQuickAdapter<WithdrawOrderModel.ListBean, BaseViewHolder> {

    public WithdrawOrderAdapter(@Nullable List<WithdrawOrderModel.ListBean> data) {
        super(R.layout.item_withdraw_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithdrawOrderModel.ListBean item) {

        helper.setText(R.id.tv_price, AmountUtil.transformFormatToString(new BigDecimal(item.getAmountString()), item.getCurrency(), 8)+" "+item.getCurrency());
        helper.setText(R.id.tv_fee, AmountUtil.transformFormatToString(new BigDecimal(item.getFeeString()), item.getCurrency(), 8)+" "+item.getCurrency());
        helper.setText(R.id.tv_status, getStatus(item.getStatus()));
        helper.setText(R.id.tv_date, DateUtil.formatStringData(item.getApplyDatetime(), DateUtil.DEFAULT_DATE_FMT));
    }

    private String getStatus(String status){

        switch (status){

            case "1":
                return StringUtil.getString(R.string.order_status_daishenpi);

            case "2":
                return StringUtil.getString(R.string.order_status_butongguo);

            case "3":
                return StringUtil.getString(R.string.order_status_daiguangbo);

            case "4":
                return StringUtil.getString(R.string.order_status_guangbozhong);

            case "5":
                return StringUtil.getString(R.string.order_status_shibai);

            case "6":
                return StringUtil.getString(R.string.order_status_chenggong);

            default:
                return "";
        }

    }
}
