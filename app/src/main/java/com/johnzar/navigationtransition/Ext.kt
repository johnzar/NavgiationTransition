package com.johnzar.navigationtransition

import android.content.Context

/**
 * 핸드폰 해상도에 따라 dp 단위에서 px(픽셀)로 변환
 */
fun Context.dpTopx(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 핸드폰 해상도에 따라 px(픽셀) 단위에서 dp로 변환
 */
fun Context.pxTodp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}