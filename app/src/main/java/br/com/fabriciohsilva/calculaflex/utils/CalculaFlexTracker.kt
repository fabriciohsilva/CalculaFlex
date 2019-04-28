package br.com.fabriciohsilva.calculaflex.utils

import android.app.Activity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object CalculaFlexTracker {

    fun trackScreen(activity: Activity, screenName: String) {
        if (screenName != ScreenMap.SCREEN_NOT_TRACKING) {
            //Log.i("ANALYTICS", screenName)
            val mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity)
            mFirebaseAnalytics.setCurrentScreen(activity, screenName, null)
        }//end if (screenName != ScreenMap.SCREEN_NOT_TRACKING)
    }//end fun trackScreen(activity: Activity, screenName: String)

    fun trackEvent(activity: Activity, bundle: Bundle) {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity)
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }//end fun trackEvent(activity: Activity, bundle: Bundle)

}//end object CalculaFlexTracker