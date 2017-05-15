package unlockscreen.nvt.com.unlockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Duc Nguyen on 4/5/2017.
 */

public class BootRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if(pref.getBoolean("STATUS", false)){
            Intent i = new Intent(context, EnglockService.class);
            context.startService(i);

        }
    }
}
