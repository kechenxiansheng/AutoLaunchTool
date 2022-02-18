package com.cm.launchtool.utils

import android.content.res.Resources
import com.cm.launchtool.MyApp

/**
 * 资源工具类
 * @author cm
 * Date 2020/7/16
 */
object ResourceUtils {
    val resources: Resources by lazy { MyApp.context.resources }

    fun getAnimId(resName: String?): Int {
        return resources.getIdentifier(resName, "anim", MyApp.context.packageName)
    }

    fun getAnimatorId(resName: String?): Int {
        return resources.getIdentifier(resName, "animator", MyApp.context.packageName)
    }

    fun getAttrId(resName: String?): Int {
        return resources.getIdentifier(resName, "attr", MyApp.context.packageName)
    }

    fun getBoolId(resName: String?): Int {
        return resources.getIdentifier(resName, "bool", MyApp.context.packageName)
    }

    /** 获取不到 */
    fun getColorId(resName: String?): Int {
        return resources.getIdentifier(resName, "color", MyApp.context.packageName)
    }

    fun getDimenId(resName: String?): Int {
        return resources.getIdentifier(resName, "dimen", MyApp.context.packageName)
    }

    fun getDrawableId(resName: String?): Int {
        return resources.getIdentifier(resName, "drawable", MyApp.context.packageName)
    }

    fun getId(resName: String?): Int {
        return resources.getIdentifier(resName, "id", MyApp.context.packageName)
    }

    fun getIntegerId(resName: String?): Int {
        return resources.getIdentifier(resName, "integer", MyApp.context.packageName)
    }

    fun getInterpolatorId(resName: String?): Int {
        return resources.getIdentifier(resName, "interpolator", MyApp.context.packageName)
    }

    fun getLayoutId(resName: String?): Int {
        return resources.getIdentifier(resName, "layout", MyApp.context.packageName)
    }

    fun getPluralsId(resName: String?): Int {
        return resources.getIdentifier(resName, "plurals", MyApp.context.packageName)
    }

    fun getStringId(resName: String?): Int {
        return resources.getIdentifier(resName, "string", MyApp.context.packageName)
    }

    fun getStyleId(resName: String?): Int {
        return resources.getIdentifier(resName, "style", MyApp.context.packageName)
    }

    fun getStyleableId(resName: String?): Int {
        return resources.getIdentifier(resName, "styleable", MyApp.context.packageName)
    }

    fun getXmlId(resName: String?): Int {
        return resources.getIdentifier(resName, "xml", MyApp.context.packageName)
    }

    fun getMipmapId(resName: String?): Int {
        return resources.getIdentifier(resName, "mipmap", MyApp.context.packageName)
    }

    fun getArrayId(resName: String?): Int {
        return resources.getIdentifier(resName, "array", MyApp.context.packageName)
    }

    /**
     * 通过反射来读取int[]类型资源Id
     * @param name
     * @return
     */
    fun getResourceDeclareStyleableIntArray(name: String): IntArray? {
        try {
            val fields2 = Class.forName(MyApp.context.packageName + ".R\$styleable").fields
            for (f in fields2) {
                if (f.name == name) {
                    return f[null] as IntArray
                }
            }
        } catch (t: Throwable) {
        }
        return null
    }
}