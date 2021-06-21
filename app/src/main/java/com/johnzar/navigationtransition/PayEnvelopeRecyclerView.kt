package com.johnzar.navigationtransition

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.DecelerateInterpolator
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class PayEnvelopeRecyclerView(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    // 중간 위치에서 왼쪽까지의 거리.
    var centerToLeftDistance = 0
        private set

    // 최대 몇개의 아이템을 나타낼 수 있는지.
    var childViewHalfCount = 0
        private set

    private val decelerateInterpolator = DecelerateInterpolator()

    fun initialize(
        newAdapter: Adapter<EnvelopListAdapter.EnvelopeViewHolder>, // 아이템 어댑터.
        ItemWidth: Float,                                           // 아이템 너비.
        func: (Int, Int) -> Unit
    ) {
        LinearSnapHelper().attachToRecyclerView(this)
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                centerToLeftDistance = width / 2
                val childViewWidth: Int = context.dpTopx(ItemWidth)
                childViewHalfCount = (width / childViewWidth) / 2
                func(childViewHalfCount, centerToLeftDistance)
                initScrollListener()
            }
        })
        postDelayed({
            scrollToCenter(childViewHalfCount)
        },100L)
        adapter = newAdapter
    }

    private fun initScrollListener() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                post {
                    // 화면에 보이는 아이템 뷰의 갯수.
                    (0 until childCount).forEach { position ->
                        val child = getChildAt(position)
                        val childCenterX = (child.left + child.right) / 2
                        val scaleValue = getGaussianScale(childCenterX)
                        child.scaleX = scaleValue
                        child.scaleY = scaleValue
                    }
                }
            }
        })
    }

    private fun getGaussianScale(
        childCenterX: Int,              // 뷰 크기의 센터값.
        minScaleOffest: Float = 1f,     // 최소 스케일.
        scaleFactor: Float = 0.35f,     // 커지는 스케일 비율.
        spreadFactor: Double = 150.0    // 효과가 지속되는 비율 시간?!
    ): Float {
        val recyclerCenterX = (left + right) / 2
        return (Math.pow(
            Math.E,
            -Math.pow(childCenterX - recyclerCenterX.toDouble(), 2.toDouble()) / (2 * Math.pow(
                spreadFactor,
                2.toDouble()
            ))
        ) * scaleFactor + minScaleOffest).toFloat()
    }


    private fun scrollToCenter(position: Int) {
        var position = position
        position = if (position < childViewHalfCount) childViewHalfCount else position
        val linearLayoutManager = layoutManager as LinearLayoutManager
        val childView = linearLayoutManager.findViewByPosition(position) ?: return
        // 현재 View를 가운데 위치로 이동
        val childViewHalf = childView.width / 2     // 자식뷰의 절반.
        val childViewLeft = childView.left          // 자식뷰의 왼쪽.
        val viewCTop: Int = centerToLeftDistance    // 리싸이클러뷰 중앙에서 왼쪽거리
        val smoothDistance = childViewHalf + childViewLeft - viewCTop   //  스크롤 거리.
        smoothScrollBy(smoothDistance, 0, decelerateInterpolator)
    }

}