package com.melolchik.permission_manager;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements PermissionManager.PermissionResultListener {

    /**
     * The M permission manager.
     */
    PermissionManager mPermissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPermissionManager = new PermissionManager();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLocationPermissionGranted()) {
            checkLocationPermissionGranted();
        }else {
            showErrorToast("location permission granted ");
            if(isStoragePermissionGranted()){
                checkStoragePermissionGranted();
            }else {
                showErrorToast("storage permission granted ");
            }
        }
    }

    /**
     * Is location permission granted boolean.
     *
     * @return the boolean
     */
    public boolean isLocationPermissionGranted() {
        return mPermissionManager.isLocationPermissionGranted(this);
    }

    /**
     * Is storage permission granted boolean.
     *
     * @return the boolean
     */
    public boolean isStoragePermissionGranted() {
        return mPermissionManager.isStoragePermissionGranted(this);
    }

    /**
     * Check location permission granted boolean.
     *
     * @return the boolean
     */
    public boolean checkLocationPermissionGranted() {
        return mPermissionManager.checkPermissionGrantedByCode(this, this,
                PermissionManager.REQUEST_CODE_ACCESS_LOCATION);
    }

    /**
     * Check storage permission granted boolean.
     *
     * @return the boolean
     */
    public boolean checkStoragePermissionGranted() {
        return mPermissionManager.checkPermissionGrantedByCode(this, this,
                PermissionManager.REQUEST_CODE_ACCESS_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionManager != null) {
            mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> permissionsNotGrantedList) {
        //only first time

        switch (requestCode) {
            case PermissionManager.REQUEST_CODE_ACCESS_LOCATION:
                if (!isStoragePermissionGranted()) {
                    checkStoragePermissionGranted();
                }
                if (permissionsNotGrantedList.isEmpty()) {
                    //it's ok permission granted
                    showErrorToast("location permission granted ");

                } else {
                    //permission denied
                    showErrorToast("location permission denied ");
                }
                break;
            case PermissionManager.REQUEST_CODE_ACCESS_STORAGE: {
                if (permissionsNotGrantedList.isEmpty()) {
                    //it's ok permission granted
                    showErrorToast("storage permission granted ");

                } else {
                    //permission denied
                    showErrorToast("storage permission denied ");
                }
                break;
            }
        }
        //

    }

    /**
     * Permission denied in Past
     *
     * @param requestCode the request code
     */
    @Override
    public void onPermissionsDenied(int requestCode) {
        //super.onPermissionsDenied(requestCode);
        switch (requestCode) {
            case PermissionManager.REQUEST_CODE_ACCESS_LOCATION:
                if (!isStoragePermissionGranted()) {
                    checkStoragePermissionGranted();
                }
                showErrorToast("location permission denied ");
                break;
            case PermissionManager.REQUEST_CODE_ACCESS_STORAGE:
                showErrorToast("storage permission denied ");
                break;
        }
    }

    /**
     * Show error toast.
     *
     * @param messageResId the message res id
     */
    protected void showErrorToast(@StringRes int messageResId) {
        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Show error toast.
     *
     * @param message the message
     */
    protected void showErrorToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
        log(message);
    }

    /**
     * Log.
     *
     * @param message the message
     */
    protected void log(String message) {
         AppLogger.log(this.getClass().getSimpleName() + " " + message);
    }

}
