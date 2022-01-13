package com.example.scrolly.utils

fun Long.getTimeAgo(): String {
    var time = this
    if (time < 1000000000000L) {
        time *= 1000;
    }

    var now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        ""
    }


    val diff = now - time;
    if (diff < Constants.MINUTE_MILLIS) {
        return "just now";
    } else if (diff < 2 * Constants.MINUTE_MILLIS) {
        return "a minute ago";
    } else if (diff < 50 * Constants.MINUTE_MILLIS) {
        return "${diff / Constants.MINUTE_MILLIS}m ago"
    } else if (diff < 90 * Constants.MINUTE_MILLIS) {
        return "an hour ago";
    } else if (diff < 24 * Constants.HOUR_MILLIS) {
        return "${diff / Constants.HOUR_MILLIS}h ago";
    } else if (diff < 48 * Constants.HOUR_MILLIS) {
        return "yesterday";
    } else {
        return "${diff / Constants.DAY_MILLIS}d ago";
    }
}

