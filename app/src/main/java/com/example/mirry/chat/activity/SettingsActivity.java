package com.example.mirry.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mirry.chat.R;
import com.example.mirry.chat.utils.HeadUtil;
import com.example.mirry.chat.utils.PreferencesUtil;
import com.example.mirry.chat.view.CircleImageView;
import com.example.mirry.chat.view.IconFontTextView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.head)
    CircleImageView head;
    @InjectView(R.id.nickname)
    TextView nickname;
    @InjectView(R.id.account)
    TextView account;
    @InjectView(R.id.editPwd)
    TextView editPwd;
    @InjectView(R.id.voice)
    Switch voice;
    @InjectView(R.id.vibration)
    Switch vibration;
    @InjectView(R.id.exit)
    Button exit;
    @InjectView(R.id.back)
    IconFontTextView back;
    private String mNickname;
    private String mAccount;
    private Boolean mVoice;
    private Boolean mVibration;
    private String mHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);

        initData();
        back.setOnClickListener(this);
        head.setOnClickListener(this);
        nickname.setOnClickListener(this);
        editPwd.setOnClickListener(this);
        exit.setOnClickListener(this);

        voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtil.setBoolean(SettingsActivity.this, "config", "voice", isChecked);
                voice.setChecked(isChecked);
            }
        });
        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtil.setBoolean(SettingsActivity.this, "config", "vibration", isChecked);
                vibration.setChecked(isChecked);
            }
        });
    }

    private void initData() {
        mAccount = PreferencesUtil.getString(SettingsActivity.this, "config", "account", "");
        NimUserInfo mInfo = NIMClient.getService(UserService.class).getUserInfo(mAccount);
        if (mInfo != null) {
            mNickname = mInfo.getName();
            mHead =   mInfo.getAvatar();

            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + mAccount +".jpg";
            File file = new File(path);
            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                head.setImageBitmap(bitmap);
            }else{
                if(mHead != null && !mHead.equals("")){
                    HeadUtil.setHead(head,mHead);
                }
            }

            if (mNickname == null || mNickname.equals("")) {
                nickname.setText(mAccount);
            } else {
                nickname.setText(mNickname);
            }
        }
        account.setText(mAccount);

        mVoice = PreferencesUtil.getBoolean(SettingsActivity.this, "config", "voice", true);
        mVibration = PreferencesUtil.getBoolean(SettingsActivity.this, "config", "vibration", true);
        voice.setChecked(mVoice);
        vibration.setChecked(mVibration);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.head:
                Toast.makeText(this, "更换头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editPwd:
                startActivity(new Intent(SettingsActivity.this, ResetPwdActivity.class));
                break;
            case R.id.exit:
                NIMClient.getService(AuthService.class).logout();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
