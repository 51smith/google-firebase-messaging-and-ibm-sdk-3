/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.fcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.quickstart.fcm.ResourcesHelper;
import com.ibm.mce.sdk.api.MceApplication;
import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.plugin.inapp.ImageTemplate;
import com.ibm.mce.sdk.plugin.inapp.InAppTemplateRegistry;
import com.ibm.mce.sdk.plugin.inapp.VideoTemplate;
import com.ibm.mce.sdk.plugin.inbox.HtmlRichContent;
import com.ibm.mce.sdk.plugin.inbox.PostMessageTemplate;
import com.ibm.mce.sdk.plugin.inbox.RichContentTemplateRegistry;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResourcesHelper resourcesHelper = new ResourcesHelper(getResources(), getPackageName());

        MceSdk.getNotificationsClient().setCustomNotificationLayout(this,
                resourcesHelper.getString("expandable_layout_type"),
                resourcesHelper.getLayoutId("custom_notification"),
                resourcesHelper.getId("bigText"),
                resourcesHelper.getId("bigImage"), resourcesHelper.getId("action1"),
                resourcesHelper.getId("action2"),
                resourcesHelper.getId("action3"));

        MceSdk.getNotificationsClient().getNotificationsPreference().setSoundEnabled(getApplicationContext(), true);
        MceSdk.getNotificationsClient().getNotificationsPreference().setSound(getApplicationContext(), resourcesHelper.getRawId("notification_sound"));
        MceSdk.getNotificationsClient().getNotificationsPreference().setVibrateEnabled(getApplicationContext(), true);
        long[] vibrate = { 0, 100, 200, 300 };
        MceSdk.getNotificationsClient().getNotificationsPreference().setVibrationPattern(getApplicationContext(), vibrate);
        MceSdk.getNotificationsClient().getNotificationsPreference().setIcon(getApplicationContext(),resourcesHelper.getDrawableId("icon"));
        MceSdk.getNotificationsClient().getNotificationsPreference().setLightsEnabled(getApplicationContext(), true);
        int ledARGB = 0x00a2ff;
        int ledOnMS = 300;
        int ledOffMS = 1000;
        MceSdk.getNotificationsClient().getNotificationsPreference().setLights(getApplicationContext(), new int[]{ledARGB, ledOnMS, ledOffMS});

        
        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        Button subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                // [END subscribe_topics]

                // Log and toast
                String msg = getString(R.string.msg_subscribed);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
