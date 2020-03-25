package com.meedbankingclub.cordova.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.ContentResolver;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class echoes a string called from JavaScript.
 */
public class Contacts extends CordovaPlugin {

    private ContactSdk contactSdk;
    private CallbackContext callbackContext;        // The callback context from which we were invoked.
    private JSONArray executeArgs;


    // Actions
    private static final String ACTION_ALL_CONTACTS = "all";
    private static final int    ALL_CONTACTS_CODE = 100;

    private static final String ACTION_SEARCH_CONTACT = "search";
    private static final int    SEARCH_CONTACT_CODE = 200;

    private static final String READ = Manifest.permission.READ_CONTACTS;

    private static final String LOG_TAG = "MEED_CONTACTS_PLUGIN";




    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.executeArgs = args;
        this.callbackContext = callbackContext;
        if (this.contactSdk == null) {
            this.contactSdk = new ContactSdk(this.cordova);
        }

        if(action.equals(ACTION_ALL_CONTACTS)) {
            if (this.cordova.hasPermission(READ)) {
                this.all(executeArgs);
            } else {
                getReadPermission(ALL_CONTACTS_CODE);
            }

        } else if(action.equals(ACTION_SEARCH_CONTACT)){
            if (this.cordova.hasPermission(READ)) {
                this.search(executeArgs);
            } else {
                getReadPermission(SEARCH_CONTACT_CODE);
            }
        } else {
            return false;
        }
        return true;
    }


    private void getReadPermission(int requestCode) {
        PermissionHelper.requestPermission(this, requestCode, READ);
    }



    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                LOG.d(LOG_TAG, "Read Contact Permission Denied");
                this.callbackContext.sendPluginResult(
                    new PluginResult(PluginResult.Status.ERROR,
                    new JSONObject()
                    .put("ERROR_MESSAGE", "READ_CONTACTS_PERMISSION_DENIED")
                    .put("ERROR_CODE", "400"))
                );
                return;
            }
        }
        switch(requestCode) {
            case ALL_CONTACTS_CODE:
                this.all(executeArgs);
                break;
            case SEARCH_CONTACT_CODE:
                this.search(executeArgs);
                break;
        }
    }


    /// ACTION METHODS


    private void all(JSONArray executeArgs) throws JSONException {
        if (executeArgs != null && executeArgs.length() > 0) {
            JSONObject options = executeArgs.getJSONObject(0);
            this.cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray contacts = contactSdk.all(options);
                    callbackContext.success(contacts);
                }
            });
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }


    private void search(JSONArray executeArgs) throws JSONException {
        if (executeArgs != null && executeArgs.length() > 0) {
            String searchString = executeArgs.getString(0);
            JSONObject options = executeArgs.getJSONObject(1);
            this.cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    JSONArray contacts = contactSdk.search(searchString, options);
                    callbackContext.success(contacts);
                }
            });
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

}
