package com.cdkj.token.wallet.delete_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityWalletWordsCheckInputBinding;
import com.cdkj.token.utils.WalletHelper;
import com.cdkj.token.wallet.IntoWalletBeforeActivity;
import com.cdkj.token.wallet.import_guide.ImportCreatePassWordActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 钱包删除单词验证
 * Created by cdkj on 2018/6/6.
 */

public class WalletDeleteWordsInputActivity extends AbsBaseLoadActivity {

    private ActivityWalletWordsCheckInputBinding mBinding;
    private ArrayList<String> words;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletDeleteWordsInputActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_words_check_input, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.delete_wallet);
        initClickListener();
    }


    private void initClickListener() {

        mBinding.btnNowCheck.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mBinding.editWords.getText().toString())) {
                UITipDialog.showInfo(this, getString(R.string.please_input_help_words));
                return;
            }

            showLoadingDialog();
            mSubscription.add(
                    Observable.just(StringUtils.mergeSpace(mBinding.editWords.getText().toString().trim()))
                            .subscribeOn(Schedulers.newThread())
                            .map(s -> {
                                words = StringUtils.splitAsArrayList(s, " ");
                                return words;
                            })
                            .map(wds -> WalletHelper.checkMnenonic(wds))

                            .observeOn(AndroidSchedulers.mainThread())

                            .doOnComplete(() -> disMissLoading())

                            .subscribe(isPass -> {
                                if (isPass && WalletHelper.checkCacheWords(StringUtils.mergeSpace(mBinding.editWords.getText().toString().trim(), WalletHelper.HELPWORD_SIGN))) {
                                    showSureDialog();
                                } else {
                                    UITipDialog.showFail(WalletDeleteWordsInputActivity.this, getString(R.string.check_words_fail));
                                }

                            }, throwable -> {
                                UITipDialog.showFail(WalletDeleteWordsInputActivity.this, getString(R.string.check_words_fail));
                            })


            );

        });
    }


    public void showSureDialog() {
        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle("删除钱包").setContentMsg("钱包助记词验证通过，确认是否删除钱包？")
                .setPositiveBtn("确认删除", view -> {
                    WalletHelper.clearCache();
                    IntoWalletBeforeActivity.open(this);
                    EventBus.getDefault().post(EventTags.AllFINISH);
                    finish();
                })
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), null, false);

        commonDialog.show();
    }

}