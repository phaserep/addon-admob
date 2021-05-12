package kr.co.paep.paepsdk.addon_admob

import android.content.Context
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.os.Handler
import android.widget.LinearLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class AdmobAdView {

    private var adPaepHandler: Handler? = null

    companion object {
        const val AD_LOAD: Int = 101
        const val AD_CLICK: Int = 201
        const val AD_FAILED301: Int = 301  //광고 요청 실패
        const val AD_FAILED302: Int = 302  //광고 소재 없음
        const val AD_FAILED303: Int = 303  //광고 패스백
        const val AD_FAILED401: Int = 401  //광고 로딩 실패
        const val AD_FAILED501: Int = 501  //SDK 내부 오류
    }

    fun makeAdView(c:Context, h:Handler, zoneid: String): LinearLayout {

        this.adPaepHandler = h

        MobileAds.initialize(c)

        val mAdView = AdView(c)
        mAdView.adSize = com.google.android.gms.ads.AdSize.BANNER
        mAdView.adUnitId = zoneid

        val admobLayout = LinearLayout(c)
        admobLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        admobLayout.orientation = LinearLayout.HORIZONTAL
        admobLayout.addView(mAdView)

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                notifyPaepAd(AD_LOAD)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                notifyPaepAd(AD_FAILED303)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that covers the screen.
            }

            override fun onAdClicked() {
                notifyPaepAd(AD_CLICK)
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return to the app after tapping on an ad.
            }
        }

        return admobLayout
    }

    private fun notifyPaepAd(status: Int) {
        if (this.adPaepHandler != null) {
            this.adPaepHandler!!.sendMessage(Message.obtain(this.adPaepHandler, status, ""));
        }
    }
}