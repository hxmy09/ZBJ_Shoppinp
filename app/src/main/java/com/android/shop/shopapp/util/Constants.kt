package shopping.hxmy.com.shopping.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.IOException


/**
 * Created by myron on 3/29/18.
 */

const val G_BAIHUO = "百货"
const val G_NVZHUANG = "女装"
const val G_NANZHUANG = "男装"
const val G_XIDI = "洗涤"
const val G_XIANGBAO = "箱包"
const val G_MEIZHUANG = "饰品"
const val G_NEIYI = "内衣"
const val G_WANJU = "玩具"
const val G_WENJU = "文具"
const val G_WUJIN = "童装"
const val G_XIEZI = "鞋子"
const val G_FANGZHIPIN = "纺织品"
const val G_TEJIA = "特价"

const val PAGE_NUMBER = 20

//EXTRA

const val GROUP = "group"

const val DEFAULT_ITEM_SIZE = 8

const val MSG_CODE_REFRESH = 0
const val MSG_CODE_LOADMORE = 1

fun compressImg(imgPath: String, context: Context): File? {
    var compressedImageFile: File? = null
    try {
        compressedImageFile = Compressor(context)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).absolutePath)
                .compressToFile(File(imgPath))
    } catch (e: IOException) {
    }

    return compressedImageFile
}


const val WEI_FU_KUAN = 100
const val DAI_FA_HUO = 200
const val DAI_SHOU_HUO = 300
const val SHOU_HOU = 400


const val USER_STATE_ADMIN = 1
const val USER_STATE_MANAGER = 2
const val USER_STATE_USER = 3
const val USER_STATE_REGISTER = 0

