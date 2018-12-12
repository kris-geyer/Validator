package geyer.sensorlab.validator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> installedApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        installedApps = new ArrayList<>();
        generateAppList();
        openRandomApp();
    }

    private void generateAppList() {

        PackageManager pm = getApplicationContext().getPackageManager();
        final List<PackageInfo> appInstall= pm.getInstalledPackages(PackageManager.GET_PERMISSIONS|PackageManager.GET_RECEIVERS|
                PackageManager.GET_SERVICES| PackageManager.GET_PROVIDERS);

        for(PackageInfo pInfo:appInstall) {
            installedApps.add(pInfo.applicationInfo.packageName);
        }

        Log.i("apps", String.valueOf(installedApps));
    }

    private void openRandomApp() {
        Random rand = new Random();
        int randomInt = rand.nextInt(installedApps.size())+1;
        String app = installedApps.get(randomInt);
        Log.i("new app", app);

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app);
        if(isAppEnabled(this, installedApps.get(randomInt))){
            this.startActivity(this.getPackageManager().getLaunchIntentForPackage(app));
        }else{
            openRandomApp();
        }
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    private static boolean isAppEnabled(Context context, String packageName) {
        boolean appStatus = false;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (ai != null) {
                appStatus = ai.enabled;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appStatus;
    }
}
