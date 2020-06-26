package com.silvia_penacoba.easytimes.Controlador;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Controlador {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;
    private static final String TAG = Controlador.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    /**
     * Checkea los permisos que la app necesita
     *
     * @param a activity
     * @return True tiene permisos/False no tiene permisos
     */
   public static Boolean checkPermissions(Activity a) {
        final String[] requiredPermissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS
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
    }
    /**
     * Pide los permisos necesarios
     *
     * @param requestCode
     * @param permissions  permisos
     * @param grantResults resultado de permisos
     * @param a            activity
     */
    public static void requestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults, Activity a) {
        boolean showToast = false;
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    showRationale = a.shouldShowRequestPermissionRationale(permission);
                }
                if (!showRationale) {
                    if (!showToast) {
                        Toast.makeText(a.getApplicationContext(),
                                "La aplicación necesita de los permisos para funcionar correctamente",
                                Toast.LENGTH_SHORT).show();
                        showToast = true;
                    }
                } else {
                    checkPermissions(a);
                }
            }
        }
    }
    /**
     * Comprobar si el servicio esta ejecutandose o no
     *
     * @param serviceClass nombre de la clase del servicio
     * @param context      contexto
     * @return True esta ejecutando/False no esta
     */
    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already", "running");
                return true;
            }
        }
        Log.i("Service not", "running");
        return false;
    }

    /**
     * Cerrar un servicio
     *
     * @param serviceClass servicio a cerrar
     * @param context      contexto
     */
    public static void closeMyService(Class<?> serviceClass, Context context) {
        try {
            context.stopService(new Intent(context, serviceClass));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
