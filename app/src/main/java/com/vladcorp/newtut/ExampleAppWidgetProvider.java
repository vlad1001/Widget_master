package com.vladcorp.newtut;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static com.vladcorp.newtut.ExampleAppWidgetConfig.KEY_BUTTON_TEXT;
import static com.vladcorp.newtut.ExampleAppWidgetConfig.SHARED_PREFS;

public class ExampleAppWidgetProvider extends AppWidgetProvider{

    public static final String ACTION_TIMER = "action_timer";
    public static int numOfMinutes;
    static RemoteViews views;
    static String buttonText;
    int tmp;
    SharedPreferences prefs;
    HashMap<Integer, String> meMap=new HashMap<Integer, String>();

    public static final String SHARED_PREFS_MINUTE = "minute_pref";
    public static final int AMOUNT_OF_MINUTES = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
//        Toast.makeText(context, "onUpdate [0]" + appWidgetId).show();

        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
            //Toast.makeText(context, "onUpdate " + appWidgetId, Toast.LENGTH_LONG).show();


        }
    }

    @Override
    public void onEnabled(Context context) {
        //Toast.makeText(context, "onEnabled ", Toast.LENGTH_LONG).show();

        super.onEnabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
       // Toast.makeText(context, "onAppWidgetOptionsChanged " + appWidgetId, Toast.LENGTH_LONG).show();

        RemoteViews vies = new RemoteViews(context.getPackageName(), R.layout.example_widget);
        prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String tmpText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "Press me");
        numOfMinutes = Integer.parseInt(tmpText);

        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFS_MINUTE, context.MODE_PRIVATE).edit();
        editor.putInt("pref_min" , numOfMinutes);
        editor.apply();
        //Toast.makeText(context, "onAppWidgetOptionsChanged numOfMinutes:  " + numOfMinutes, Toast.LENGTH_LONG).show();

        appWidgetManager.updateAppWidget(appWidgetId, vies);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //Toast.makeText(context, "onRestored numOfMinutes:  " + numOfMinutes, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        //Toast.makeText(context, "onRestored  newWidgetIds: " + newWidgetIds, Toast.LENGTH_LONG).show();

    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        //Toast.makeText(context, "updateAppWidget " + appWidgetId, Toast.LENGTH_LONG).show();
        //Toast.makeText(context, "update: numOfMinutes: " + numOfMinutes + "appWidgetId: " + appWidgetId, Toast.LENGTH_LONG).show();
        //meMap.put(appWidgetId ,"15");

        prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "0");

        String tmpNum = prefs.getString(KEY_BUTTON_TEXT, "0");
       // Toast.makeText(context, "update: numOfMinutes: " + numOfMinutes + "appWidgetId: " + appWidgetId, Toast.LENGTH_LONG).show();

        numOfMinutes = Integer.parseInt(tmpNum);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("KEY_ID",appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//        Intent intent1 = new Intent(context,
//                ExampleAppWidgetProvider.class);
//
//        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
//        intent1.putExtra("KEY_ID", appWidgetId);

        views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
        views.setCharSequence(R.id.btn_txt, "setText", buttonText);
        //views.setOnClickPendingIntent(R.id.example_widget_imageview, pendingIntent);
        views.setOnClickPendingIntent(R.id.example_widget_imageview,
                getPendingSelfIntent(context, ACTION_TIMER, appWidgetId));
        //Toast.makeText(context, "vv: " + (R.id.btn_txt) + "numOfMinutes: " + numOfMinutes, Toast.LENGTH_LONG).show();


//        if(numOfMinutes == 0 ) {
//            numOfMinutes = numOfMinutes;

//        }
       // Toast.makeText(context, "100" + buttonText, Toast.LENGTH_LONG).show();

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId) {
        Intent intent = new Intent(context, getClass());
        intent.putExtra("KEY_ID",appWidgetId);
        Toast.makeText(context, "vlad: " + appWidgetId, Toast.LENGTH_LONG).show();

        intent.setAction(action);

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String a22 = intent.getStringExtra("ID");
        //Bundle extras = intent.getExtras();
//        Integer b = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        int a = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        String b = AppWidgetManager.EXTRA_APPWIDGET_ID;
        AppWidgetManager appWidgetManager2 = AppWidgetManager.getInstance(context);

        int widgetId = intent.getIntExtra("KEY_ID", -1);

        Toast.makeText(context, "onReceive1 b:" + widgetId, Toast.LENGTH_LONG).show();

        numOfMinutes= 0;
        int minutes = 0;
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_MINUTE, context.MODE_PRIVATE);
        int restoredText = prefs.getInt("pref_min", 0);
        if (restoredText != 0 || restoredText != 1)
        {
            //Toast.makeText(context, "inside if", Toast.LENGTH_LONG).show();
            minutes = prefs.getInt("pref_min", 0); //0 is the default value.
        }
        if(numOfMinutes == 1 || numOfMinutes == 0) {
            numOfMinutes = minutes;
        }
        //Toast.makeText(context, numOfMinutes + "numOfMin", Toast.LENGTH_LONG).show();
        meMap.put(15 ,"15");

        //Toast.makeText(context, "memap:" + meMap.get(15), Toast.LENGTH_LONG).show();


        try {

            if (ACTION_TIMER.equals(intent.getAction())) {
                //Toast.makeText(context, "A timer for " + numOfMinutes + "minutes has been activated", Toast.LENGTH_LONG).show();

                int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.example_widget_imageview);

                Intent setTimer = new Intent(AlarmClock.ACTION_SET_TIMER);
                setTimer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                setTimer.putExtra(AlarmClock.EXTRA_LENGTH, numOfMinutes*60);
                setTimer.putExtra(AlarmClock.EXTRA_MESSAGE, numOfMinutes + " Minutes");
                setTimer.putExtra(AlarmClock.EXTRA_SKIP_UI, false);
                context.startActivity(setTimer);

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_widget);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);

                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        }catch (Exception ex){
        }
    }

    @Override
    public void onDisabled(Context context) {
    }
}
