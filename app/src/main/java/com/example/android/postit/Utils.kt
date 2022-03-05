package com.example.android.postit

class Utils {
    companion object{
        private  const val SECOND_MILLIS = 1000
        private  const val MINUTES_MILLIS = 60 * SECOND_MILLIS
        private  const val HOUR_MILLIS = 60 * MINUTES_MILLIS
        private  const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun getTimeAgo(time: Long): String? {
            val now: Long = System.currentTimeMillis()
            if(time > now || time <= 0){
                return null
            }

            val diff = now - time
            return if (diff < MINUTES_MILLIS) {
                "just now"
            } else if(diff < 2 * MINUTES_MILLIS){
                "a minute ago"
            } else if(diff < 50 * MINUTES_MILLIS){
                (diff / MINUTES_MILLIS).toString() + " minutes ago"
            } else if (diff < 24 * HOUR_MILLIS){
                (diff / HOUR_MILLIS).toString() + " hours ago"
            } else if (diff < 48 * HOUR_MILLIS){
                "yesterday"
            } else {
                (diff/ DAY_MILLIS).toString() + " days ago"
            }
        }
    }
}