package ru.mirea.android4;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Widget extends AppWidgetProvider {
    InfoRepository repository;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh");
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        repository = InfoRepository.createInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Date date = repository.getDate();
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        System.out.println(now);
        String result = "NO";
        if(now.getTime() < date.getTime()) {
            long differenceBetweenDates = date.getTime() - now.getTime();
            long differenceSeconds = (differenceBetweenDates / 1000) % 60;
            long differenceMinutes = (differenceBetweenDates / (1000*60)) %60;
            long differenceHours = (differenceBetweenDates / (1000*60*60)) % 24;
            long differenceDays = (differenceBetweenDates / (1000*60*60*24));
            result = differenceDays + " дней " + differenceHours + " часов " + differenceMinutes + " минут "
                    + differenceSeconds + "  секунд" ;
        }
        views.setTextViewText(R.id.date_text, result);
        ComponentName componentName = new ComponentName(context, Widget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, views);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

}
