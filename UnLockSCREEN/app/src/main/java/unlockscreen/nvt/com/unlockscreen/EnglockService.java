package unlockscreen.nvt.com.unlockscreen;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;

public class EnglockService extends Service {
    private ScreenStateReceiver screenStateReceiver;
    private WindowManager windowManager;
    private LockScreenView lockScreenView;
    private boolean isLockScreenAdded;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        registerScreenStateReceiver();
        disableKeyguard();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1000){
                    hideLockScreen();

                }
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Tùy chọn START_STICKY cho phép
        // Service tự động sống lại và chạy tiếp
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterScreenStateReceiver();
        enableKeyguard();
        super.onDestroy();
    }

    private void showLockScreenView() {
        if(isLockScreenAdded){
            lockScreenView.bindData();
            return;
        }

        lockScreenView = new LockScreenView(this, handler);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.TRANSPARENT;

        windowManager.addView(lockScreenView, params);
        isLockScreenAdded = true;
    }
    //mở khóa
    private void hideLockScreen(){
        if(isLockScreenAdded){
            windowManager.removeView(lockScreenView);;
            isLockScreenAdded = false;
        }
    }

    // Hủy bảo vệ màn hình
    // Thì mới có thể hiển thị được Custom View
    private void disableKeyguard() {
        KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("IN");
        lock.disableKeyguard();
    }

    // Kích hoạt lại bảo vệ màn hình
    private void enableKeyguard() {
        KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("IN");
        lock.reenableKeyguard();
    }


    //lắng nghe sự kiện "SCREEN_OFF"
    private class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            showLockScreenView();
        }
    }


    private void registerScreenStateReceiver() {
        screenStateReceiver = new ScreenStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(screenStateReceiver, filter);
    }


    private void unregisterScreenStateReceiver() {
        unregisterReceiver(screenStateReceiver);
    }

}