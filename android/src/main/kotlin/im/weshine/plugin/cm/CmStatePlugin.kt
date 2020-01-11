package im.weshine.plugin.cm

import android.app.Activity
import android.util.Log
import com.bytedance.sdk.openadsdk.*
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/**
 * author:dai
 * des: 穿山甲广告状态(未使用)
 */
class CmStatePlugin(activity: Activity) : EventChannel.StreamHandler {
    val mActivity = activity
    var sink: EventChannel.EventSink? = null

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = EventChannel(registrar.messenger(), "im.weshine.plugin/cm/state")

            channel.setStreamHandler(CmStatePlugin(registrar.activity()))
        }
    }

    override fun onListen(reslut: Any?, channelSink: EventChannel.EventSink?) {
        sink = channelSink
        loadRewardAD()
    }

    override fun onCancel(result: Any?) {

    }

    /**
     * 加载激励视频
     */
    private fun loadRewardAD() {
        val adSlot = AdSlot.Builder()
                .setCodeId("901121430")
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
                if (rewoardAD != null) {
                    rewoardAD.setRewardAdInteractionListener(object : TTRewardVideoAd.RewardAdInteractionListener{
                        override fun onRewardVerify(p0: Boolean, p1: Int, p2: String?) {

                        }

                        override fun onSkippedVideo() {

                        }

                        override fun onAdShow() {

                        }

                        override fun onAdVideoBarClick() {

                        }

                        override fun onVideoComplete() {
                            sink?.success("CM_Plugin: video complete")
                        }

                        override fun onAdClose() {
                            sink?.success("CM_Plugin:  Close reward ad")
                        }

                        override fun onVideoError() {
                            sink?.success("CM_Plugin: video error")
                        }

                    })
                    rewoardAD.showRewardVideoAd(mActivity)
                    sink?.success("CM_Plugin: Show reward ad")
                } else {
                    sink?.success("CM_Plugin: Can not load ad from cm")
                }

            }

            override fun onRewardVideoCached() {

            }

            override fun onError(errId: Int, errMsg: String?) {
                sink?.success("CM_Plugin: Ad_error_errid:$errId,errMsg: $errMsg")
            }

        })
    }

}
