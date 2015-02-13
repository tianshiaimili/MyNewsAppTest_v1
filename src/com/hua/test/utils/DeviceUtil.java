package com.hua.test.utils;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DeviceUtil {

	private static final String TAG = DeviceUtil.class.getSimpleName();
	private static PackageInfo self;

	public static void hideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * judge whether is a tabletDevice
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTabletDevice(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
			// test screen size, use reflection because isLayoutSizeAtLeast is
			// only available since 11
			Configuration con = context.getResources().getConfiguration();
			try {
				Method mIsLayoutSizeAtLeast = con.getClass().getMethod(
						"isLayoutSizeAtLeast", int.class);
				Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con,
						0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
				return r;
			} catch (Exception x) {
				x.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * get the PackageInfo to get app information
	 * 
	 * @param context
	 * @return
	 */
	private static PackageInfo getSelfPackageInfo(Context context) {
		if (self == null) {
			PackageManager pm = context.getPackageManager();
			try {
				self = pm.getPackageInfo(context.getPackageName(),
						PackageManager.GET_ACTIVITIES);
			} catch (NameNotFoundException e) {
				Log.e(TAG, "getSelfPackageInfo failed.", e);
			}
		}
		return self;
	}

	/**
	 * Get app version name. If failed to get it, return "1.0".
	 * 
	 * @param context
	 * @return app version name.
	 */
	public static String getAppVersion(Context context) {
		PackageInfo pInfo = getSelfPackageInfo(context);
		if (pInfo == null)
			return "1.0";
		return pInfo.versionName;
	}

	/**
	 * Get app name.
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		final PackageManager pm = context.getPackageManager();
		return getSelfPackageInfo(context).applicationInfo.loadLabel(pm)
				.toString();
	}

	/**
	 * get the device info
	 */

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);
			LogUtils2.d("the Device Info = " + json.toString());
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
