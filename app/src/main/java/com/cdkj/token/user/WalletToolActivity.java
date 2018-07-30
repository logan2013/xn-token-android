package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserWalletBinding;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.WalletPasswordModifyActivity;
import com.cdkj.token.wallet.backup_guide.BackupWalletStartActivity;
import com.cdkj.token.wallet.backup_guide.BackupWalletWordsCheckActivity;
import com.cdkj.token.wallet.delete_guide.WallteDeleteStartActivity;
import com.cdkj.token.wallet.export.WalletExportPasswordCheckActivity;
import com.kenai.jffi.Main;

import org.greenrobot.eventbus.EventBus;

/**
 * 钱包工具
 * Created by cdkj on 2018/5/26.
 */

public class WalletToolActivity extends AbsLoadActivity {

    private ActivityUserWalletBinding mBinding;
    private InputDialog inputDialog;

    private final int DIALOG_DELETE = 0;//dialog 类型 删除
    private final int DIALOG_BACKUP = 1; //dialog类型备份


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, WalletToolActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_wallet, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.wallet_tool));

        setStatusBarBlue();
        setTitleBgBlue();
        initClickListener();

        mBinding.tvWalletName.setText(WalletHelper.getWalletNameByUserId(SPUtilHelper.getUserId()));

    }


    private void initClickListener() {

        mBinding.llBackUp.setOnClickListener(view -> {
            showPasswordInputDialog(DIALOG_BACKUP);
        });

        mBinding.llModify.setOnClickListener(view -> WalletPasswordModifyActivity.open(this));
        mBinding.btnDelete.setOnClickListener(view -> WallteDeleteStartActivity.open(this));
        mBinding.llOut.setOnClickListener(view -> WalletExportPasswordCheckActivity.open(this));

        mBinding.btnDelete.setOnClickListener(view -> {
            showDoubleWarnListen(getString(R.string.backup_introtation_dialog_title), getString(R.string.backup_introtation_dialog_content), view1 -> {
                showPasswordInputDialog(DIALOG_DELETE);
            });

        });
    }

    /**
     * 显示密码输入框
     *
     * @param type 调起dialog类型
     */
    private void showPasswordInputDialog(int type) {
        if (inputDialog == null) {
            inputDialog = new InputDialog(this).builder().setTitle(getString(R.string.please_input_transaction_pwd))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {
                        String tradePwd = inputDialog.getContentView().getText().toString().trim();
                        if (TextUtils.isEmpty(tradePwd)) {
                            UITipDialog.showInfoNoIcon(WalletToolActivity.this, getString(R.string.please_input_transaction_pwd));
                            return;
                        }
                        switch (type) {
                            case DIALOG_BACKUP:
                                BackupWalletStartActivity.open(this, true);
                                break;
                            case DIALOG_DELETE:
                                checkPassword(tradePwd);
                                break;
                        }

                    })
                    .setNegativeBtn(getStrRes(R.string.cancel), null)
                    .setContentMsg("");
        }
        inputDialog.getContentView().setText("");
        inputDialog.getContentView().setHint(getStrRes(R.string.please_input_transaction_pwd));
        inputDialog.getContentView().setText("");
        inputDialog.show();
    }

    /**
     * 用户密码是否输入正确 （钱包密码）
     *
     * @param tradePwd
     */
    private void checkPassword(String tradePwd) {
        if (WalletHelper.checkPasswordByUserId(tradePwd, SPUtilHelper.getUserId())) {  //用户密码输入正确
            WalletHelper.deleteUserWallet(SPUtilHelper.getUserId());
            UITipDialog.showSuccess(this, getString(R.string.wallet_delete_success), dialogInterface -> {
                EventBus.getDefault().post(new AllFinishEvent());
                MainActivity.open(this);
                finish();
            });

        } else {
            UITipDialog.showInfoNoIcon(this, getString(R.string.transaction_password_error));
        }

    }
}