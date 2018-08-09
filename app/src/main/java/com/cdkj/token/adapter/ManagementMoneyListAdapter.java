package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * 理财列表
 * Created by cdkj on 2018/5/25.
 */

public class ManagementMoneyListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ManagementMoneyListAdapter(@Nullable List<String> data) {
        super(R.layout.item_manager_money, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }


}
