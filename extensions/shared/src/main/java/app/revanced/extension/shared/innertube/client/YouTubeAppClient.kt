package app.revanced.extension.shared.innertube.client

import android.os.Build
import app.revanced.extension.shared.patches.PatchStatus
import app.revanced.extension.shared.settings.BaseSettings
import app.revanced.extension.shared.utils.PackageUtils
import org.apache.commons.lang3.ArrayUtils
import java.util.Locale

/**
 * Used to fetch streaming data.
 */
object YouTubeAppClient {
    // IOS
    /**
     * Video not playable: Paid / Movie / Private / Age-restricted
     * Note: Audio track available
     */
    private const val PACKAGE_NAME_IOS = "com.google.ios.youtube"

    /**
     * The hardcoded client version of the iOS app used for InnerTube requests with this client.
     *
     * It can be extracted by getting the latest release version of the app on
     * [the App Store page of the YouTube app](https://apps.apple.com/us/app/youtube-watch-listen-stream/id544007664/),
     * in the `What’s New` section.
     */
    private val CLIENT_VERSION_IOS = if (forceAVC())
        "17.40.5"
    else
        "20.20.7"

    private const val DEVICE_MAKE_IOS = "Apple"
    private const val OS_NAME_IOS = "iOS"

    /**
     * The device machine id for the iPhone 16 Pro Max (iPhone17,2),
     * used to get HDR with AV1 hardware decoding.
     * See [this GitHub Gist](https://gist.github.com/adamawolf/3048717) for more information.
     */
    private val DEVICE_MODEL_IOS = if (forceAVC())
        "iPhone12,5" // 11 Pro Max. (last device with iOS 13)
    else
        "iPhone17,2" // 16 Pro Max.
    private val OS_VERSION_IOS = if (forceAVC())
        "13.7.17H35" // Last release of iOS 13.
    else
        "18.5.22F76"
    private val USER_AGENT_VERSION_IOS = if (forceAVC())
        "13_7"
    else
        "18_5"
    private val USER_AGENT_IOS = iOSUserAgent(PACKAGE_NAME_IOS, CLIENT_VERSION_IOS)


    // IOS UNPLUGGED
    /**
     * Video not playable: Paid / Movie / Playlists / Music
     * Note: Audio track available
     */
    private const val PACKAGE_NAME_IOS_UNPLUGGED = "com.google.ios.youtubeunplugged"

    /**
     * The hardcoded client version of the iOS app used for InnerTube requests with this client.
     *
     * It can be extracted by getting the latest release version of the app on
     * [the App Store page of the YouTube TV app](https://apps.apple.com/us/app/youtube-tv/id1193350206/),
     * in the `What’s New` section.
     */
    private val CLIENT_VERSION_IOS_UNPLUGGED = if (forceAVC())
        "6.45"
    else
        "9.21"
    private val USER_AGENT_IOS_UNPLUGGED =
        iOSUserAgent(PACKAGE_NAME_IOS_UNPLUGGED, CLIENT_VERSION_IOS_UNPLUGGED)


    // ANDROID
    private const val PACKAGE_NAME_ANDROID = "com.google.android.youtube"
    private val CLIENT_VERSION_ANDROID = PackageUtils.getAppVersionName()
    private val USER_AGENT_ANDROID = androidUserAgent(
        packageName = PACKAGE_NAME_ANDROID,
        clientVersion = CLIENT_VERSION_ANDROID,
    )


    // ANDROID VR
    /**
     * Video not playable: Kids
     * Note: Audio track is not available
     *
     * Package name for YouTube VR (Google DayDream): com.google.android.apps.youtube.vr (Deprecated)
     * Package name for YouTube VR (Meta Quests): com.google.android.apps.youtube.vr.oculus
     * Package name for YouTube VR (ByteDance Pico): com.google.android.apps.youtube.vr.pico
     */
    private const val PACKAGE_NAME_ANDROID_VR = "com.google.android.apps.youtube.vr.oculus"

    /**
     * The hardcoded client version of the Android VR app used for InnerTube requests with this client.
     *
     * It can be extracted by getting the latest release version of the app on
     * [the App Store page of the YouTube app](https://www.meta.com/en-us/experiences/2002317119880945/),
     * in the `Additional details` section.
     */
    private val CLIENT_VERSION_ANDROID_VR = if (disableAV1())
        "1.43.32" // Last version of minSdkVersion 24.
    else
        "1.65.09"

    /**
     * The device machine id for the Meta Quest 3, used to get opus codec with the Android VR client.
     * See [this GitLab](https://dumps.tadiphone.dev/dumps/oculus/eureka) for more information.
     */
    private val DEVICE_MODEL_ANDROID_VR = if (disableAV1())
        "Quest"
    else
        "Quest 3"
    private const val DEVICE_MAKE_ANDROID_VR = "Oculus"
    private val OS_VERSION_ANDROID_VR = if (disableAV1())
        "7.1.1"
    else
        "14"
    private val ANDROID_SDK_VERSION_ANDROID_VR = if (disableAV1())
        "25"
    else
        "34"
    private val BUILD_ID_ANDROID_VR = if (disableAV1())
        "NGI77B"
    else
        "UP1A.231005.007.A1"

    private val USER_AGENT_ANDROID_VR = androidUserAgent(
        packageName = PACKAGE_NAME_ANDROID_VR,
        clientVersion = CLIENT_VERSION_ANDROID_VR,
        osVersion = OS_VERSION_ANDROID_VR,
        deviceModel = DEVICE_MODEL_ANDROID_VR,
        buildId = BUILD_ID_ANDROID_VR
    )


    // ANDROID UNPLUGGED
    /**
     * Video not playable: Playlists / Music
     * Note: Audio track is not available
     */
    private const val PACKAGE_NAME_ANDROID_UNPLUGGED = "com.google.android.apps.youtube.unplugged"
    private const val CLIENT_VERSION_ANDROID_UNPLUGGED = "9.25.2"

    /**
     * The device machine id for the Chromecast with Google TV 4K.
     * See [this GitLab](https://dumps.tadiphone.dev/dumps/google/kirkwood) for more information.
     */
    private const val DEVICE_MODEL_ANDROID_UNPLUGGED = "Google TV Streamer"
    private const val DEVICE_MAKE_ANDROID_UNPLUGGED = "Google"
    private const val OS_VERSION_ANDROID_UNPLUGGED = "14"
    private const val ANDROID_SDK_VERSION_ANDROID_UNPLUGGED = "34"
    private const val BUILD_ID_ANDROID_UNPLUGGED = "UTTK.241210.003"
    private const val GMS_CORE_VERSION_CODE_ANDROID_UNPLUGGED = "244738119"

    private val USER_AGENT_ANDROID_UNPLUGGED = androidUserAgent(
        packageName = PACKAGE_NAME_ANDROID_UNPLUGGED,
        clientVersion = CLIENT_VERSION_ANDROID_UNPLUGGED,
        osVersion = OS_VERSION_ANDROID_UNPLUGGED,
        deviceModel = DEVICE_MODEL_ANDROID_UNPLUGGED,
        buildId = BUILD_ID_ANDROID_UNPLUGGED
    )


    // ANDROID CREATOR
    /**
     * Video not playable: Livestream / HDR
     * Note: Audio track is not available
     */
    private const val PACKAGE_NAME_ANDROID_CREATOR = "com.google.android.apps.youtube.creator"
    private const val CLIENT_VERSION_ANDROID_CREATOR = "25.10.100"

    /**
     * The device machine id for the Google Pixel 9 Pro Fold.
     * See [this GitLab](https://dumps.tadiphone.dev/dumps/google/caiman) for more information.
     */
    private const val DEVICE_MODEL_ANDROID_CREATOR = "Pixel 9 Pro Fold"
    private const val DEVICE_MAKE_ANDROID_CREATOR = "Google"
    private const val OS_VERSION_ANDROID_CREATOR = "15"
    private const val ANDROID_SDK_VERSION_ANDROID_CREATOR = "35"
    private const val BUILD_ID_ANDROID_CREATOR = "AP3A.241005.015.A2"
    private const val GMS_CORE_VERSION_CODE_ANDROID_CREATOR = "250932035"

    private val USER_AGENT_ANDROID_CREATOR = androidUserAgent(
        packageName = PACKAGE_NAME_ANDROID_CREATOR,
        clientVersion = CLIENT_VERSION_ANDROID_CREATOR,
        osVersion = OS_VERSION_ANDROID_CREATOR,
        deviceModel = DEVICE_MODEL_ANDROID_CREATOR,
        buildId = BUILD_ID_ANDROID_CREATOR
    )


    // TVHTML5
    /**
     * Video not playable: Drm-protected
     * TODO: Find out why playback sometimes fails
     */
    private const val CLIENT_VERSION_TVHTML5 = "7.20250402.11.00"
    // Used by yt-dlp
    private const val USER_AGENT_TVHTML5 = "Mozilla/5.0 (ChromiumStylePlatform) Cobalt/Version"


    // TVHTML5 SIMPLY
    private const val CLIENT_VERSION_TVHTML5_SIMPLY = "1.0"


    // TVHTML5 EMBEDDED
    private const val CLIENT_VERSION_TVHTML5_EMBEDDED = "2.0"


    /**
     * Same format as Android YouTube User-Agent.
     * Example: 'com.google.android.youtube/19.46.40(Linux; U; Android 13; in_ID; 21061110AG Build/TP1A.220624.014) gzip'
     * Source: https://whatmyuseragent.com/apps/youtube.
     */
    private fun androidUserAgent(
        packageName: String,
        clientVersion: String,
        osVersion: String? = Build.VERSION.RELEASE,
        deviceModel: String? = Build.MODEL,
        buildId: String? = Build.ID,
    ): String =
        "$packageName/$clientVersion(Linux; U; Android $osVersion; ${Locale.getDefault()}; $deviceModel Build/$buildId) gzip"

    /**
     * Same format as iOS YouTube User-Agent.
     * Example: 'com.google.ios.youtube/16.38.2 (iPhone9,4; U; CPU iOS 14_7_1 like Mac OS X; en_AU)'
     * Source: https://github.com/mitmproxy/mitmproxy/issues/4836.
     */
    private fun iOSUserAgent(
        packageName: String,
        clientVersion: String
    ): String =
        "$packageName/$clientVersion ($DEVICE_MODEL_IOS; U; CPU iOS $USER_AGENT_VERSION_IOS like Mac OS X; ${Locale.getDefault()})"

    private fun disableAV1(): Boolean {
        return BaseSettings.SPOOF_STREAMING_DATA_VR_DISABLE_AV1.get()
    }

    private fun forceAVC(): Boolean {
        return BaseSettings.SPOOF_STREAMING_DATA_IOS_FORCE_AVC.get()
    }

    private fun useIOS(): Boolean {
        return PatchStatus.SpoofStreamingDataIOS() && BaseSettings.SPOOF_STREAMING_DATA_USE_IOS.get()
    }

    private fun useTV(): Boolean {
        return BaseSettings.SPOOF_STREAMING_DATA_USE_TV.get()
    }

    fun availableClientTypes(preferredClient: ClientType): Array<ClientType> {
        var availableClientTypes = ClientType.CLIENT_ORDER_TO_USE
        if (useIOS()) {
            availableClientTypes += ClientType.IOS_DEPRECATED
        }
        if (useTV()) {
            availableClientTypes += ClientType.CLIENT_ORDER_TO_USE_TV
        }

        if (ArrayUtils.contains(availableClientTypes, preferredClient)) {
            val clientToUse: Array<ClientType?> = arrayOfNulls(availableClientTypes.size)
            clientToUse[0] = preferredClient
            var i = 1
            for (c in availableClientTypes) {
                if (c != preferredClient) {
                    clientToUse[i++] = c
                }
            }
            return clientToUse.filterNotNull().toTypedArray()
        } else {
            return availableClientTypes
        }
    }

    @Suppress("DEPRECATION", "unused")
    enum class ClientType(
        /**
         * [YouTube client type](https://github.com/zerodytrash/YouTube-Internal-Clients?tab=readme-ov-file#clients)
         */
        val id: Int,
        /**
         * Device model, equivalent to [Build.MANUFACTURER] (System property: ro.product.vendor.manufacturer)
         */
        val deviceMake: String = Build.MANUFACTURER,
        /**
         * Device model, equivalent to [Build.MODEL] (System property: ro.product.model)
         */
        val deviceModel: String = Build.MODEL,
        /**
         * Device OS name.
         */
        val osName: String = "Android",
        /**
         * Device OS version, equivalent to [Build.VERSION.RELEASE] (System property: ro.system.build.version.release)
         */
        val osVersion: String = Build.VERSION.RELEASE,
        /**
         * Client user-agent.
         */
        val userAgent: String,
        /**
         * Android SDK version, equivalent to [Build.VERSION.SDK] (System property: ro.build.version.sdk)
         */
        val androidSdkVersion: String? = null,
        /**
         * App version.
         */
        val clientVersion: String,
        /**
         * Client screen enum.
         */
        val clientScreen: String? = null,
        /**
         * GmsCore versionCode.
         */
        val gmscoreVersionCode: String? = null,
        /**
         * If the client can access the API logged in.
         * If false, 'Authorization' or 'SessionId' must not be included.
         */
        val supportsCookies: Boolean = true,
        /**
         * If the client can only access the API logged in.
         * If true, 'Authorization' or 'SessionId' must be included.
         */
        val requireAuth: Boolean = false,
        /**
         * If the client requires params.
         */
        val requireParams: Boolean = false,
        /**
         * The streaming url has an obfuscated 'n' parameter.
         * If true, javascript must be fetched to decrypt the 'n' parameter.
         */
        val requireJS: Boolean = false,
        /**
         * Client name for innertube body.
         */
        val clientName: String,
        /**
         * Friendly name displayed in stats for nerds.
         */
        val friendlyName: String
    ) {
        ANDROID(
            id = 3,
            userAgent = USER_AGENT_ANDROID,
            androidSdkVersion = Build.VERSION.SDK,
            clientVersion = CLIENT_VERSION_ANDROID,
            clientName = "ANDROID",
            friendlyName = "Android"
        ),
        ANDROID_VR(
            id = 28,
            deviceMake = DEVICE_MAKE_ANDROID_VR,
            deviceModel = DEVICE_MODEL_ANDROID_VR,
            osVersion = OS_VERSION_ANDROID_VR,
            userAgent = USER_AGENT_ANDROID_VR,
            androidSdkVersion = ANDROID_SDK_VERSION_ANDROID_VR,
            clientVersion = CLIENT_VERSION_ANDROID_VR,
            clientName = "ANDROID_VR",
            friendlyName = if (disableAV1())
                "Android VR No AV1"
            else
                "Android VR"
        ),
        ANDROID_VR_NO_AUTH(
            id = 28,
            deviceMake = DEVICE_MAKE_ANDROID_VR,
            deviceModel = DEVICE_MODEL_ANDROID_VR,
            osVersion = OS_VERSION_ANDROID_VR,
            userAgent = USER_AGENT_ANDROID_VR,
            androidSdkVersion = ANDROID_SDK_VERSION_ANDROID_VR,
            clientVersion = CLIENT_VERSION_ANDROID_VR,
            supportsCookies = false,
            clientName = "ANDROID_VR",
            friendlyName = if (disableAV1())
                "Android VR No auth No AV1"
            else
                "Android VR No auth"
        ),
        ANDROID_UNPLUGGED(
            id = 29,
            deviceMake = DEVICE_MAKE_ANDROID_UNPLUGGED,
            deviceModel = DEVICE_MODEL_ANDROID_UNPLUGGED,
            osVersion = OS_VERSION_ANDROID_UNPLUGGED,
            userAgent = USER_AGENT_ANDROID_UNPLUGGED,
            androidSdkVersion = ANDROID_SDK_VERSION_ANDROID_UNPLUGGED,
            clientVersion = CLIENT_VERSION_ANDROID_UNPLUGGED,
            gmscoreVersionCode = GMS_CORE_VERSION_CODE_ANDROID_UNPLUGGED,
            requireAuth = true,
            clientName = "ANDROID_UNPLUGGED",
            friendlyName = "Android TV"
        ),
        ANDROID_CREATOR(
            id = 14,
            deviceMake = DEVICE_MAKE_ANDROID_CREATOR,
            deviceModel = DEVICE_MODEL_ANDROID_CREATOR,
            osVersion = OS_VERSION_ANDROID_CREATOR,
            userAgent = USER_AGENT_ANDROID_CREATOR,
            androidSdkVersion = ANDROID_SDK_VERSION_ANDROID_CREATOR,
            clientVersion = CLIENT_VERSION_ANDROID_CREATOR,
            gmscoreVersionCode = GMS_CORE_VERSION_CODE_ANDROID_CREATOR,
            requireAuth = true,
            clientName = "ANDROID_CREATOR",
            friendlyName = "Android Studio"
        ),
        IOS_UNPLUGGED(
            id = 33,
            deviceMake = DEVICE_MAKE_IOS,
            deviceModel = DEVICE_MODEL_IOS,
            osName = OS_NAME_IOS,
            osVersion = OS_VERSION_IOS,
            userAgent = USER_AGENT_IOS_UNPLUGGED,
            clientVersion = CLIENT_VERSION_IOS_UNPLUGGED,
            requireAuth = true,
            clientName = "IOS_UNPLUGGED",
            friendlyName = if (forceAVC())
                "iOS TV Force AVC"
            else
                "iOS TV"
        ),
        IOS_DEPRECATED(
            id = 5,
            deviceMake = DEVICE_MAKE_IOS,
            deviceModel = DEVICE_MODEL_IOS,
            osName = OS_NAME_IOS,
            osVersion = OS_VERSION_IOS,
            userAgent = USER_AGENT_IOS,
            clientVersion = CLIENT_VERSION_IOS,
            supportsCookies = false,
            clientName = "IOS",
            friendlyName = if (forceAVC())
                "iOS Force AVC"
            else
                "iOS"
        ),
        TV(
            id = 7,
            clientVersion = CLIENT_VERSION_TVHTML5,
            userAgent = USER_AGENT_TVHTML5,
            requireJS = true,
            requireParams = true,
            clientScreen = "WATCH",
            clientName = "TVHTML5",
            friendlyName = "TV"
        ),
        TV_SIMPLY(
            id = 75,
            clientVersion = CLIENT_VERSION_TVHTML5_SIMPLY,
            userAgent = USER_AGENT_TVHTML5,
            requireJS = true,
            clientScreen = "WATCH",
            clientName = "TVHTML5_SIMPLY",
            friendlyName = "TV Simply"
        ),
        TV_EMBEDDED(
            id = 85,
            clientVersion = CLIENT_VERSION_TVHTML5_EMBEDDED,
            userAgent = USER_AGENT_TVHTML5,
            requireJS = true,
            clientScreen = "EMBED",
            clientName = "TVHTML5_SIMPLY_EMBEDDED_PLAYER",
            friendlyName = "TV Embedded"
        );

        companion object {
            val CLIENT_ORDER_TO_USE: Array<ClientType> = arrayOf(
                IOS_UNPLUGGED,
                ANDROID_VR,
                ANDROID_CREATOR,
                ANDROID_UNPLUGGED,
                ANDROID_VR_NO_AUTH,
            )

            val CLIENT_ORDER_TO_USE_TV: Array<ClientType> = arrayOf(
                TV,
                TV_SIMPLY,
            )
        }
    }
}
