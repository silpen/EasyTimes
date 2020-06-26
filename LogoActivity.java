package com.silvia_penacoba.easytimes.Vista;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.silvia_penacoba.easytimes.R;

import static com.silvia_penacoba.easytimes.Controlador.Controlador.checkPermissions;
import static com.silvia_penacoba.easytimes.Controlador.Controlador.requestPermissionsResult;


public class LogoActivity extends AppCompatActivity {
    private static final String TAG = LogoActivity.class.getSimpleName();
    private doCharge doCharge = null;





    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);

        if (checkPermissions(this)) {
            doCharge = new doCharge(this);
            doCharge.execute();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class doCharge extends AsyncTask<Void, Void, Boolean> {
        private Context context;

        doCharge(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isOnlineNet() || isNetDisponible();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                startActivity(new Intent(context, PasswordActivity.class));
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                finish();
            } else {
                cargarDialog();
            }
            doCharge.cancel(true);
            doCharge = null;

        }
    }
    @Override
    public void onBackPressed() {
        if (doCharge != null) {
            doCharge.cancel(true);
            doCharge = null;
        }

        /**if (Controlador.isMyServiceRunning(AlarmService.class, context))
            Controlador.closeMyService(AlarmService.class, context);*/
        finish();
    }
    /**
     * Dialogo para saber que no tiene conexion a internet
     */
    private void cargarDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No tiene conexión a internet")
                .setIcon(R.drawable.connection_icon)
                .setMessage("Debe activar la conexión a internet y volver a iniciar la aplicación")
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, which) -> {
                            finishAffinity();
                            System.exit(0);
                            System.runFinalization();
                        });
        builder.show();
    }
    private Boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val = p.waitFor();
            return (val == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    /**public static Boolean checkPermissions(Activity a) {
        final String[] requiredPermissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(a.getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                a.requestPermissions(neededPermissions.toArray(new String[]{}),
                        MY_PERMISSIONS_REQUEST_ACCESS_CODE);
            }
            return false;
        } else {
            return true;
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        requestPermissionsResult(requestCode, permissions, grantResults, this);
        doCharge = new doCharge(this);
        doCharge.execute();
    }
}
