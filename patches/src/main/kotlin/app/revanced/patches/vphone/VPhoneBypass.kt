package app.revanced.patches.vphone

import app.revanced.patcher.annotation.*
import app.revanced.patcher.patch.*
import app.revanced.patcher.extensions.*
import app.revanced.patcher.usage.*

@Patch
@Name("VPhoneBypass")
@Description("Bypass login, VIP, token verification, and force update for 光速虚拟机 (com.vphonegaga.titan)")
@SupportedPackages("com.vphonegaga.titan")
class VPhoneBypass : PatchBase() {
    override fun execute(context: PatchContext): PatchResult {
        val methodMap = mapOf(
            "com.vphonegaga.titan.beans.AppUpdateBean->isForceUpdate()Z" to false,
            "com.vphonegaga.titan.beans.AppUpdateBean->isLatest()Z" to true,
            "com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo\$User->isVip()Z" to true,
            "com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo\$User->isEnableAccAdvanceFeatures()Z" to true,
            "com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo\$User->getUid()Ljava/lang/String;" to "888888s",
            "com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo\$User->getNickname()Ljava/lang/String;" to "q122001100",
            "com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo\$User->getEncryptPhone()Ljava/lang/String;" to "15888888888s",
            "com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo\$User->getCoinNum()Ljava/lang/String;" to "99999",
            "com.vphonegaga.titan.network.configs.VPhoneGaGaNetworkFeedbackConfig->getBaseUrl()Ljava/lang/String;" to "https://dcdn.appmarket.api.88gsxn77j.cn",
            "com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo->isLogin()Z" to true,
            "com.common.utils.SystemUtil->exitApp()I" to 0,
            "com.vphonegaga.titan.roles.ConfigMgr->getNeedClearAllAdsFiles()Z" to true,
            "com.vphonegaga.titan.roles.ConfigMgr->getMagiskEnabled()Z" to true,
            "com.vphonegaga.titan.roles.ConfigMgr->getAndroid10Enabled()Z" to true,
            "com.vphonegaga.titan.personalcenter.beans.MaterialBean\$Material->getErrorCode()I" to 0,
            "com.vphonegaga.titan.personalcenter.beans.MaterialBean\$Material->isFeature()Z" to true,
            "com.vphonegaga.titan.roles.ConfigMgr->getVulkanEnabled()Z" to true
        )

        for ((sig, ret) in methodMap) {
            context.findMethod(sig)?.setReturnConstant(ret)
                ?: return PatchResult.Error("Failed to find $sig")
        }

        val skipMethods = listOf(
            "com.common.activity.BaseActivity->onDestroy()V",
            "com.vphonegaga.titan.MyNativeActivity->onDestroy()V",
            "com.vphonegaga.titan.VPhoneInstance->onDestroy()V",
            "androidx.fragment.app.Fragment->performDestroy()V"
        )

        for (sig in skipMethods) {
            context.findMethod(sig)?.apply {
                instructions?.clear()
                instructions?.addReturn()
            } ?: return PatchResult.Error("Failed to find $sig")
        }

        return PatchResult.Success()
    }
}
