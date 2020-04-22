package im.weshine.plugin.cm

import android.app.Activity
import com.bytedance.sdk.openadsdk.*
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/**
 * author:dai
 * des: 穿山甲插件
 */
class CmPlugin(activity: Activity) : MethodCallHandler {
    private val mActivity = activity

    private var newAd: TTRewardVideoAd? = null
    private var newFullAd: TTFullScreenVideoAd? = null

    companion object {
        private lateinit var channel: MethodChannel
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            channel = MethodChannel(registrar.messenger(), "im.weshine.plugin/cm")
            channel.setMethodCallHandler(CmPlugin(registrar.activity()))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "initcm" -> {
                val appId = call.argument<String>("appId")
                val appName = call.argument<String>("appName")
                val debug = call.argument<Boolean>("debug")
                initCm(appId!!, appName!!, debug!!)
                result.success("CM_Plugin: Cm init successed")
            }

            "rewardad" -> {
                val adId = call.argument<String>("adId")
                if (adId == null){
                    result.success("CM_Plugin: adid can not be null")
                    return
                }
                loadRewardAD(adId, result)
            }
            "showreward" -> {
                showAd(result)
            }
            "fullad" -> {
                val adId = call.argument<String>("adId")
                if (adId == null){
                    result.success("CM_Plugin: adid can not be null")
                    return
                }
                loadFullAD(adId, result)
            }
            "showfull" -> {
                showFullAd(result)
            }

            else -> {
                result.notImplemented()
            }
        }
    }

    /**
     * 初始化穿山甲
     */
    private fun initCm(appId: String, appName: String, debug: Boolean) {
        TTAdSdk.init(mActivity,
                TTAdConfig.Builder().appId(appId)
                        .useTextureView(false)
                        .appName(appName)
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                        .debug(debug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                        .supportMultiProcess(false) //是否支持多进程，true支持
                        .build())
        TTAdSdk.getAdManager().requestPermissionIfNecessary(mActivity)
    }

    /**
     * 加载激励视频
     */
    private fun loadRewardAD(adId: String, result: Result) {
        val adSlot = AdSlot.Builder()
                .setCodeId(adId)
                .setSupportDeepLink(true)
                .setAdCount(1)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("") //奖励的名称
                .setRewardAmount(0) //奖励的数量
                .setUserID("")
                .setOrientation(TTAdConstant.VERTICAL) //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
                .build()

        val manager = TTAdSdk.getAdManager()
        val native = manager.createAdNative(mActivity)
        native.loadRewardVideoAd(adSlot, object : TTAdNative.RewardVideoAdListener {
            override fun onRewardVideoAdLoad(rewoardAD: TTRewardVideoAd?) {
                newAd = rewoardAD
                if (rewoardAD != null) {
                    result.success("CM_Plugin:An Ad is ready to show")
                } else {
                    result.success("CM_Plugin: Cannot load reward ad from cm")
                }
            }

            override fun onRewardVideoCached() {

            }

            override fun onError(errId: Int, errMsg: String?) {
                result.success("CM_Plugin: Reward ad failed,ErrId: $errId,ErrMsg: $errMsg")
            }

        })
    }

    /**
     * 加载全屏视频
     */
    private fun loadFullAD(adId: String, result: Result) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
                .setCodeId(adId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL)
                .build()

        val manager = TTAdSdk.getAdManager()
        val native = manager.createAdNative(mActivity)
        native.loadFullScreenVideoAd(adSlot, object : TTAdNative.FullScreenVideoAdListener {
            override fun onFullScreenVideoAdLoad(ad: TTFullScreenVideoAd?) {
                newFullAd = ad
                if (newFullAd != null) {
                    result.success("CM_Plugin:An Ad is ready to show")
                } else {
                    result.success("CM_Plugin: Cannot load full ad from cm")
                }
            }

            override fun onFullScreenVideoCached() {

            }

            override fun onError(errId: Int, errMsg: String?) {
                result.success("CM_Plugin: full ad failed,ErrId: $errId,ErrMsg: $errMsg")
            }

        })

    }

    /**
     * 展示激励视频
     */
    private fun showAd(result: Result) {
        if (newAd != null) {
            newAd!!.setRewardAdInteractionListener(object : TTRewardVideoAd.RewardAdInteractionListener {
                override fun onRewardVerify(p0: Boolean, p1: Int, p2: String?) {

                }

                override fun onSkippedVideo() {
                    channel.invokeMethod("adSkip", "CM_Plugin: Ad Skip ")
                }

                override fun onAdShow() {
                    channel.invokeMethod("adShow", "CM_Plugin: Ad Show ")
                }

                override fun onAdVideoBarClick() {

                }

                override fun onVideoComplete() {
                    channel.invokeMethod("videoComplete", "CM_Plugin: Video Complete ")
                }

                override fun onAdClose() {
                    channel.invokeMethod("adClose", "CM_Plugin: Ad Close ")
                }

                override fun onVideoError() {
                    channel.invokeMethod("videoError", "CM_Plugin: Video Error ")
                }

            })
            newAd!!.showRewardVideoAd(mActivity)
            result.success("CM_Plugin:Ad showing")
        } else {
            result.error("103", "No ad fill", null)
        }
    }


    /**
     * 展示激励视频
     */
    private fun showFullAd(result: Result) {
        if (newFullAd != null) {

            newFullAd!!.setFullScreenVideoAdInteractionListener(object : TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                override fun onSkippedVideo() {
                    channel.invokeMethod("adSkip", "CM_Plugin: Ad Skip ")
                }

                override fun onAdShow() {
                    channel.invokeMethod("adShow", "CM_Plugin: Ad Show ")
                }

                override fun onAdVideoBarClick() {

                }

                override fun onVideoComplete() {
                    channel.invokeMethod("videoComplete", "CM_Plugin: Video Complete ")
                }

                override fun onAdClose() {
                    channel.invokeMethod("adClose", "CM_Plugin: Ad Close ")
                }


            })


            newFullAd!!.showFullScreenVideoAd(mActivity)
            result.success("CM_Plugin: full ad showing")
        } else {
            result.success("CM_Plugin:No ad fill")
        }
    }

}
