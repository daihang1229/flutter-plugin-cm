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
                result.success("CM_Plugin: Cm init success")
            }

            "loadad" -> {
                val adId = call.argument<String>("adId")
                loadRewardAD(adId!!, result)
            }
            "showad" -> {
                showAd(result)
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
                .setAdCount(2)
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
                    result.error("102", "CM_Plugin: Cannot load ad from cm", null)
                }

            }

            override fun onRewardVideoCached() {

            }

            override fun onError(errId: Int, errMsg: String?) {
                result.error("$errId", "CM_Plugin: Ad load error -> $errMsg", null)
            }

        })
    }

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

}
