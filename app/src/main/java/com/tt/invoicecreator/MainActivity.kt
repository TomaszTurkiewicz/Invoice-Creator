package com.tt.invoicecreator

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.tt.invoicecreator.ui.theme.InvoiceCreatorTheme
import com.tt.invoicecreator.viewmodel.AppViewModel
import com.tt.invoicecreator.viewmodel.AppViewModelFactory
import java.util.concurrent.atomic.AtomicBoolean

const val TEST = true
class MainActivity : ComponentActivity() {

    private val viewModel:AppViewModel by viewModels {
        AppViewModelFactory(
            (this.application as InvoiceCreatorApplication).databaseV2.getItemDaoV2(),
            (this.application as InvoiceCreatorApplication).databaseV2.getClientDaoV2(),
            (this.application as InvoiceCreatorApplication).databaseV2.getInvoiceItemDaoV2(),
            (this.application as InvoiceCreatorApplication).databaseV2.getInvoiceDaoV2(),
            (this.application as InvoiceCreatorApplication).databaseV2.getPaidDaoV2()
        )
    }

    private var mRewardedAd: RewardedAd? = null
    private lateinit var consentInformation: ConsentInformation

    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvoiceCreatorTheme {
                InvoiceCreatorApp(
                    viewModel = viewModel,
                    activity = this
                )
            }
        }

        requestConsentForm()
    }

    private fun requestConsentForm() {
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation = UserMessagingPlatform.getConsentInformation(this)

        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@MainActivity
                ){
                    loadAndShowError ->
                    Log.w(
                        ContentValues.TAG, String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )

                    if(consentInformation.canRequestAds()){
                        initializeMobileAdsSdk()
                    }
                }
            },
            {
                requestConsentError ->
                Log.w(
                    ContentValues.TAG, String.format("%s, %s",
                        requestConsentError.errorCode,
                        requestConsentError.message))
            }
        )
        if(consentInformation.canRequestAds()){
            initializeMobileAdsSdk()
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.get()) {
            return
        }
        isMobileAdsInitializeCalled.set(true)

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)

    }

    fun loadRewardedAd(){
        val rewardedAdId: String = if(TEST) this.getString(R.string.test_rewarded_ad) else this.getString(R.string.admob_rewarded_ad)
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this,rewardedAdId,adRequest, object : RewardedAdLoadCallback(){
            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                mRewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                super.onAdLoaded(ad)
                mRewardedAd = ad
                viewModel.rewardedApLoaded()
            }
        })
    }

    fun showRewardedAd(){
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback(){

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                mRewardedAd = null
            }
        }

        mRewardedAd?.let {
            it.show(this){
                mRewardedAd = null
                viewModel.rewardedAdWatched()
            }
        }

    }
}



/**
 * todo add VAT calculation
 * todo add changing color schema if PRO
* todo alert dialog showing ad change to better explanation what PRO gives, add 2nd button to update to PRO
 * todo add settings screen
 * **/