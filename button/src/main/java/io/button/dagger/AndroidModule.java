package io.button.dagger;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.button.dagger.annotation.Application;
import io.button.dagger.annotation.VersionCode;
import io.button.dagger.annotation.VersionName;

@Module(complete = true, library = true)
public class AndroidModule {
    private final Context appContext;

    public AndroidModule(Context context) {
        appContext = context;
    }

    @Provides
    @Application
    Context provideContext() {
        return appContext;
    }

    @Provides
    @VersionCode
    Integer provideVersionCode(PackageInfo p) {
        return p.versionCode;
    }

    @Provides
    @VersionName
    String provideVersionName(PackageInfo p) {
        return p.versionName;
    }

    @Provides
    PackageInfo providePackageInfo(@Application Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager(@Application Context context) {
        return AccountManager.get(context);
    }

    @Provides
    @Singleton
    @Application
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(appContext);
    }
}
