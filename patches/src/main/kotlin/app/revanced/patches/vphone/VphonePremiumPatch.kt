package app.revanced.patches.titan.premium

import app.revanced.patcher.annotation.*
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.PatchContext
import app.revanced.patcher.patch.PatchOptions
import app.revanced.patcher.util.patchMethod
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.immutable.instruction.ImmutableInstructionReturnObject
import org.jf.dexlib2.immutable.instruction.ImmutableInstructionReturnVoid
import org.jf.dexlib2.immutable.reference.ImmutableStringReference
import org.jf.dexlib2.immutable.instruction.ImmutableInstructionReturnWide
import org.jf.dexlib2.Opcode

@Patch
@Name("TitanPremiumPatch")
@Description("Spoofs premium features for com.vphonegaga.titan (光速虚拟机), disables onDestroy, and injects fake values.")
@CompatiblePackages(["com.vphonegaga.titan"])
object TitanPremiumPatch {

    @JvmStatic
    fun execute(context: PatchContext): PatchResult {
        val opts = context.patchOptions

        val classes = context.getSmaliClasses()

        fun spoofBoolean(className: String, methodName: String, returnValue: Boolean) {
            context.patchMethod(className, methodName) { method ->
                replaceInstructions(
                    listOf(
                        ImmutableInstructionReturnObject(
                            if (returnValue) Opcode.RETURN_TRUE else Opcode.RETURN_FALSE, 0
                        )
                    )
                )
            }
        }

        fun spoofString(className: String, methodName: String, value: String) {
            context.patchMethod(className, methodName) { method ->
                replaceInstructions(
                    listOf(
                        constString(0, value),
                        ImmutableInstructionReturnObject(Opcode.RETURN_OBJECT, 0)
                    )
                )
            }
        }

        fun spoofInt(className: String, methodName: String, value: Int) {
            context.patchMethod(className, methodName) { method ->
                replaceInstructions(
                    listOf(
                        constInt(0, value),
                        ImmutableInstructionReturnObject(Opcode.RETURN, 0)
                    )
                )
            }
        }

        fun spoofLong(className: String, methodName: String, value: Long) {
            context.patchMethod(className, methodName) { method ->
                replaceInstructions(
                    listOf(
                        constWide(0, value),
                        ImmutableInstructionReturnWide(Opcode.RETURN_WIDE, 0)
                    )
                )
            }
        }

        fun disableMethod(className: String, methodName: String) {
            context.patchMethod(className, methodName) {
                replaceInstructions(listOf(ImmutableInstructionReturnVoid(Opcode.RETURN_VOID)))
            }
        }

        if (opts.boolean("forceVip", true)) {
            spoofBoolean("com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo$User", "isVip", true)
        }

        if (opts.boolean("forceAdvanceFeatures", true)) {
            spoofBoolean("com.vphonegaga.titan.personalcenter.userinfo.VPhoneGaGaUserInfo$User", "isEnableAccAdvanceFeatures", true)
        }

        if (opts.has("spoofToken")) {
            spoofString("com.vphonegaga.titan.user.User", "getToken", opts.string("spoofToken"))
        }

        if (opts.boolean("patchTokenExpire", true)) {
            spoofLong("com.vphonegaga.titan.user.User", "getTokenExpireTime", Long.MAX_VALUE)
        }

        if (opts.boolean("unlimitedCoins", true)) {
            spoofInt("com.vphonegaga.titan.user.User", "getCoinNum", opts.integer("unlimitedCoins"))
        }

        if (opts.boolean("disableOnDestroy", true)) {
            listOf(
                "com.common.activity.BaseActivity",
                "com.vphonegaga.titan.MyNativeActivity",
                "com.vphonegaga.titan.VPhoneInstance",
                "androidx.fragment.app.Fragment"
            ).forEach { cls ->
                disableMethod(cls, "onDestroy")
                disableMethod(cls, "performDestroy")
            }
        }

        return PatchResult.success()
    }
}
