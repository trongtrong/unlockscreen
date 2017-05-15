package unlockscreen.nvt.com.unlockscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private Switch swtEnglockService;
    public SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initViews();
        initListeners();
    }

    private void requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(!Settings.canDrawOverlays(this)){

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intent, 200);

            }
        }else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (!Settings.canDrawOverlays(this)) {
                    finish();
                }
            }
        }
    }
    private void initViews() {

        swtEnglockService = (Switch) findViewById(R.id.swt_englock_service);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if(pref.getBoolean("STATUS", false == true)){
            swtEnglockService.setChecked(true);
        }else {
            swtEnglockService.setChecked(false);
        }
    }

    private void initListeners() {
        // Đăng ký lắng nghe sự kiện
        swtEnglockService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(getBaseContext(), EnglockService.class);
                    startService(intent);
                    //lưu trạng thái on vào share preferences
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("STATUS", true);
                    editor.apply();
                } else {
                    Intent intent = new Intent(getBaseContext(), EnglockService.class);
                    stopService(intent);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("STATUS", false);
                    editor.apply();
                }
            }
        });
    }

}
