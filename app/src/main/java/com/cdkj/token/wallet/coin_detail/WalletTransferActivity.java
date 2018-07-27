package com.cdkj.token.wallet.coin_detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityTransferBinding;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.user.WalletToolActivity;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.web3j.crypto.WalletUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cdkj.token.utils.AccountUtil.ETHSCALE;

/**
 * 钱包转账
 * Created by cdkj on 2018/6/8.
 */

public class WalletTransferActivity extends AbsLoadActivity {

    private ActivityTransferBinding mBinding;

    private final int CODEPERSE = 101;


    private BigInteger mGas;//燃料单位费用
    private BigInteger gasPriceLimit;//矿工费用
    private BigInteger transferGasPrice;//计算后转账矿工费用

    private WalletBalanceModel accountListBean;

    private PermissionHelper mPermissionHelper;

    private InputDialog passWordInputDialog;


    //需要的权限
    private String[] needLocationPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static void open(Context context, WalletBalanceModel accountListBean) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletTransferActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, accountListBean);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transfer, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        accountListBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        mPermissionHelper = new PermissionHelper(this);
        if (accountListBean != null && !TextUtils.isEmpty(accountListBean.getCoinBalance())) {
            mBinding.tvCurrency.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(accountListBean.getCoinBalance()), ETHSCALE) + " " + accountListBean.getCoinName());
            mBaseBinding.titleView.setMidTitle(accountListBean.getCoinName());
        }

//        mBinding.tvCurrencySing.setText(getString(R.string.wallet_withdraw_balance) + WalletHelper.getShowLocalCoinType());

        transferGasPrice = WalletHelper.getGasLimit();
        mGas = WalletHelper.getGasLimit();
        gasPriceLimit = WalletHelper.getGasLimit();

        mBaseBinding.titleView.setMidTitle(R.string.transfer);
        mBinding.edtAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtAmount, 15, 8));

        initClickListener();

        getGasPriceValue();

        mBinding.btnNext.setOnClickListener(view -> {

            if (transferInputCheck()) return;
            showPasswordInputDialog();

        });
    }

    /**
     * 转账输入状态检测
     *
     * @return
     */
    boolean transferInputCheck() {
        if (TextUtils.isEmpty(mBinding.editToAddress.getText().toString().trim())) {
            UITipDialog.showInfo(this, getString(R.string.please_to_address));
            return true;
        }

        if (!WalletUtils.isValidAddress(mBinding.editToAddress.getText().toString().trim())) {
            UITipDialog.showInfo(this, getStrRes(R.string.error_wallet_address));
            return true;
        }

        if (TextUtils.isEmpty(mBinding.edtAmount.getText().toString().trim())) {
            UITipDialog.showInfo(this, getString(R.string.please_input_transaction_number));
            return true;
        }

        try {

            if (accountListBean == null || TextUtils.isEmpty(accountListBean.getCoinBalance())) {
                UITipDialog.showInfo(this, getStrRes(R.string.no_balance));
                return true;
            }

            BigInteger amountBigInteger = AccountUtil.bigIntegerFormat(new BigDecimal(mBinding.edtAmount.getText().toString().trim())); //转账数量

            if (amountBigInteger.compareTo(BigInteger.ZERO) == 0 || amountBigInteger.compareTo(BigInteger.ZERO) == -1) {
                UITipDialog.showInfo(this, getString(R.string.please_correct_transaction_number));
                return true;
            }

            BigInteger allBigInteger = transferGasPrice.add(amountBigInteger);//手续费+转账数量

            int checkInt = allBigInteger.compareTo(new BigDecimal(accountListBean.getCoinBalance()).toBigInteger()); //比较

            if (checkInt == 1 || checkInt == 0) {
                UITipDialog.showInfo(this, getString(R.string.no_balance));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            UITipDialog.showInfo(this, getString(R.string.please_correct_transaction_number));
            return true;
        }
        return false;
    }


    /**
     * 相机权限请求
     */
    private void permissionRequest() {
        mPermissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                Intent intent = new Intent(WalletTransferActivity.this, CaptureActivity.class);
                startActivityForResult(intent, CODEPERSE);
            }

            @Override
            public void doAfterDenied(String... permission) {
                showToast(getStrRes(R.string.no_camera_permission));
            }
        }, needLocationPermissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 转账操作
     */
    private void transfer() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(s -> {
                    WalletDBModel w = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());

                    if (TextUtils.equals(accountListBean.getCoinName(), WalletHelper.COIN_WAN)) {
                        return WalletHelper.transferWan(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), transferGasPrice);
                    }
                    return WalletHelper.transfer(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), transferGasPrice);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> disMissLoading())
                .subscribe(s -> {
                    if (s == null || s.getError() != null) {
                        LogUtil.E("has————" + s.getError().getMessage());
                        UITipDialog.showFail(WalletTransferActivity.this, getString(R.string.transfer_fail));
                        return;
                    }

                    if (!TextUtils.isEmpty(s.getTransactionHash())) {
                        UITipDialog.showSuccess(WalletTransferActivity.this, getString(R.string.transaction_success), dialogInterface -> finish());
                    }

                }, throwable -> {
                    UITipDialog.showFail(WalletTransferActivity.this, getString(R.string.transfer_fail));
                    LogUtil.E("has————" + throwable);
                }));
    }

    /**
     * 获取燃料费用
     */
    private void getGasPriceValue() {
        if (accountListBean == null) {
            return;
        }
        showLoadingDialog();

        mSubscription.add(
                Observable.just("")
                        .subscribeOn(Schedulers.newThread())
                        .map(s -> WalletHelper.getGasValue(accountListBean.getCoinName()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> disMissLoading())
                        .subscribe(gasPrice -> {
                            this.mGas = gasPrice;
                            setShowGasPrice();

                        }, throwable -> {

                        })
        );
    }


    /**
     * 显示密码输入框
     */
    private void showPasswordInputDialog() {
        if (passWordInputDialog == null) {
            passWordInputDialog = new InputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {

                        String tradePwd = passWordInputDialog.getContentView().getText().toString().trim();

                        if (TextUtils.isEmpty(tradePwd)) {
                            UITipDialog.showInfoNoIcon(WalletTransferActivity.this, getString(R.string.trade_code_hint));
                            return;
                        }

                        if (!WalletHelper.checkPasswordByUserId(tradePwd, SPUtilHelper.getUserId())) {
                            UITipDialog.showInfoNoIcon(this, getString(R.string.transaction_password_error));
                            return;
                        }

                        transfer();
                    })
                    .setNegativeBtn(getStrRes(R.string.cancel), null)
                    .setContentMsg("");
        }
        passWordInputDialog.getContentView().setText("");
        passWordInputDialog.getContentView().setHint(getStrRes(R.string.trade_code_hint));
        passWordInputDialog.getContentView().setText("");
        passWordInputDialog.show();
    }


    /**
     * 设置矿工费显示
     */
    private void setShowGasPrice() {

        mBinding.tvGas.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(transferGasPrice).multiply(new BigDecimal(this.mGas)), ETHSCALE) + " " + accountListBean.getCoinName());
    }

    private void initClickListener() {
        mBinding.fraLayoutQRcode.setOnClickListener(view -> {
            permissionRequest();
        });


        //矿工费设置
        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (i < 100) {
                    float lowerI = 100 - i;
                    BigDecimal bg = new BigDecimal(lowerI / 100);
                    transferGasPrice = gasPriceLimit.subtract(new BigDecimal(gasPriceLimit).multiply(bg).toBigInteger());
                    BigInteger minPrice = gasPriceLimit.divide(new BigDecimal(2).toBigInteger());//最小矿工费
                    if (transferGasPrice.compareTo(minPrice) < 0) {
                        transferGasPrice = minPrice;
                    }

                } else if (i > 100) {
                    float upperI = i - 100;
                    BigDecimal bg = new BigDecimal(upperI);
                    transferGasPrice = gasPriceLimit.add(new BigDecimal(gasPriceLimit).multiply(bg).toBigInteger());

                } else {
                    transferGasPrice = gasPriceLimit;
                }

                setShowGasPrice();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        mBinding.linLayoutGasChoose.setOnClickListener(view -> {
//            new GasTypeChoosePop(this, chooseGasType).setItemClickListener((chooseType, typeString) -> {
//                chooseGasType = chooseType;
//                mBinding.tvChooseType.setText(typeString);
//
//                switch (chooseType) {
//                    case GasTypeChoosePop.FIRST:
//                        transferGasPrice = WalletHelper.getGasLimit().multiply(new BigDecimal(2).toBigInteger());
//                        break;
//                    case GasTypeChoosePop.ORDINARY:
//                        transferGasPrice = WalletHelper.getGasLimit();
//                        break;
//
//                    case GasTypeChoosePop.ECONOMICS:
//                        transferGasPrice = WalletHelper.getGasLimit().divide(new BigDecimal(2).toBigInteger());
//                        break;
//                }
//                setShowGasPrice();
//
//            }).showPopupWindow();
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * 处理二维码扫描结果
         */
        if (requestCode == CODEPERSE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (WalletUtils.isValidAddress(result)) {
                        mBinding.editToAddress.setText(result);
                    } else {
                        Toast.makeText(WalletTransferActivity.this, R.string.error_wallet_address, Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WalletTransferActivity.this, R.string.resolve_wallet_address_fail, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
