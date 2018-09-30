package com.vladcorp.newtut;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ExampleAppWidgetConfig extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_BUTTON_TEXT = "keyButtonText";

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText example_widget_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_app_widget_config);
        Toast.makeText(getApplicationContext(), "Config onCreate " + appWidgetId, Toast.LENGTH_LONG).show();

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        example_widget_imageview = findViewById(R.id.edit_text_button);

    }

    public void confirmConfiguration(View v) {
        Toast.makeText(getApplicationContext(), "Config confirmConfiguration " + appWidgetId, Toast.LENGTH_LONG).show();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent ButtonIntent = new Intent(this, ExampleAppWidgetProvider.class);
        PendingIntent buttonPendingIntent =
                PendingIntent.getActivity(this, 0, ButtonIntent, 0);
        String buttonText = example_widget_imageview.getText().toString();

        Intent clickIntent = new Intent(this, ExampleAppWidgetProvider.class);
        clickIntent.setAction(ExampleAppWidgetProvider.ACTION_TIMER);
        PendingIntent clickPendingIntent =
                buttonPendingIntent.getBroadcast(this, 0 , clickIntent, 0);

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.example_widget);
        views.setOnClickPendingIntent(R.id.example_widget_imageview, clickPendingIntent);
        views.setCharSequence(R.id.btn_txt, "setText", buttonText);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BUTTON_TEXT + appWidgetId, buttonText);
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

        Intent intent2 = new Intent(getApplicationContext(), ExampleAppWidgetProvider.class);
        intent2.putExtra("KEY_ID", appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}
