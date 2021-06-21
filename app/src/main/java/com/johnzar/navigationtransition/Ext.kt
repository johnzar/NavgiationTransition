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

data class CenterViewItem(
    val position:Int,
    val differ:Int
)

/**
 * 중간에서 가장 가까운 거리를 계산하는 ItemView.
 *
 * @param itemHeights
 * @return
 */
fun ArrayList<CenterViewItem>.getMinDifferItem(): CenterViewItem {
    var minItem = this[0] // 기본 첫 번째는 최소 나쁨 값
    for (i in this.indices) {
        // 최소 나쁨 값 가져오기
        if (this[i].differ <= minItem.differ) {
            minItem = this[i]
        }
    }
    return minItem
}
