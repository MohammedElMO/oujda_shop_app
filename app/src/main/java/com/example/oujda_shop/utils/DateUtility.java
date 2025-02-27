package com.example.oujda_shop.utils;

import android.text.format.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtility {


    public static String getTimeAgo(Date pastDate) {
        long pastTimeMillis = pastDate.getTime();
        long now = System.currentTimeMillis();
        long diff = now - pastTimeMillis;

        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return "Just now";
        } else if (diff < TimeUnit.HOURS.toMillis(1)) {
            return (diff / TimeUnit.MINUTES.toMillis(1)) + " minutes ago";
        } else if (diff < TimeUnit.DAYS.toMillis(1)) {
            return (diff / TimeUnit.HOURS.toMillis(1)) + " hours ago";
        } else if (diff < TimeUnit.DAYS.toMillis(7)) {
            return (diff / TimeUnit.DAYS.toMillis(1)) + " days ago";
        } else {
            return (diff / TimeUnit.DAYS.toMillis(7)) + " weeks ago";
        }
    }
}
