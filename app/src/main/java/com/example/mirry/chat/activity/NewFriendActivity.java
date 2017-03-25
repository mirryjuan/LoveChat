package com.example.mirry.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mirry.chat.R;
import com.example.mirry.chat.adapter.NewFriendAdapter;
import com.example.mirry.chat.utils.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewFriendActivity extends Activity {

    @InjectView(R.id.friend_list)
    ListView friendList;
    private List<Map<String,Object>> list = new ArrayList<>();
    private List<Map<String,Object>> listAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_friend);

        Intent intent = getIntent();
        list = (List<Map<String, Object>>) intent.getSerializableExtra("newFriend");

        ButterKnife.inject(this);

        final NewFriendAdapter adapter = new NewFriendAdapter(NewFriendActivity.this,listAll,true);
        friendList.setAdapter(adapter);

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewFriendActivity.this, FriendInfoActivity.class);
                intent.putExtra("info", (Serializable) listAll.get(position));
                intent.putExtra("isRequest",false);
                startActivity(intent);
            }
        });

        for (final Map<String, Object> info :
                list) {
            final String content = info.get("content") == null ? "" : info.get("content").toString();
            String account = info.get("account").toString();
            NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
                @Override
                public void onSuccess(NimUserInfo param) {
                    if (param == null) {
                        Toast.makeText(NewFriendActivity.this, "没有用户信息", Toast.LENGTH_SHORT).show();
                    } else {
//                    Map<String, Object> extensionMap = param.getExtensionMap();
//                    Log.e("info",extensionMap.toString());
                        Map<String, Object> info = new HashMap<>();
                        info.put("account", param.getAccount());
                        info.put("content",content);
                        info.put("head",param.getAvatar());
                        info.put("nickname",param.getName());
                        info.put("sex",param.getGenderEnum().getValue());
                        info.put("birthday", param.getBirthday());
                        info.put("mobile", param.getMobile());
                        listAll.add(info);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailed(int code) {
                    Log.e("code", "错误码：" + code);
                    switch (code) {
                        case 408:
                            Toast.makeText(NewFriendActivity.this, "请求超时，请稍候重试", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }

    }
}
