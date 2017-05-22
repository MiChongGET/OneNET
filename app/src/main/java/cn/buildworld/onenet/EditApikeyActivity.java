package cn.buildworld.onenet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;

import cn.buildworld.onenet.util.Preferences;


public class EditApikeyActivity extends AppCompatActivity {

    private EditText edit_apikey ;
    private EditText edit_device_num;
    private String apikey;
    private String device_num;
    private Preferences preferences;
    private String TAG = "开发者设置";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_apikey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        toolbar.setTitle("开发者设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_apikey = (EditText) findViewById(R.id.edit_apikey);
        edit_device_num = (EditText) findViewById(R.id.edit_device_num);

        preferences = Preferences.getInstance(this);

        apikey = preferences.getString(Preferences.API_KEY,null);
        device_num = preferences.getString(Preferences.Device_Num,null);

        if (TextUtils.isEmpty(apikey)){
        }else {
            edit_apikey.setText(apikey);
        }
    }

    public void saveApi(View view){

        String key = edit_apikey.getText().toString().trim();
        String device_num = edit_device_num.getText().toString().trim();

        Log.i(TAG, "saveApi: "+key);
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(device_num)) {
            Toast.makeText(this, "设置为空！！！", Toast.LENGTH_SHORT).show();
        }else {
            preferences.putString(Preferences.API_KEY,key);
            preferences.putString(Preferences.Device_Num,device_num);

            OneNetApi.setAppKey(key);
            Toast.makeText(this, "设备号："+device_num+"  api_key"+key, Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}
