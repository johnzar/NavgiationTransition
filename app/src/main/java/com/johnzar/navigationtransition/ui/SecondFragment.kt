package com.johnzar.navigationtransition.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.johnzar.navigationtransition.*
import com.johnzar.navigationtransition.databinding.FragmentSecondBinding
import java.util.ArrayList

class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private val args by navArgs<SecondFragmentArgs>()

    private val envelopeAdapter by lazy(LazyThreadSafetyMode.NONE) {
        EnvelopListAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST", "onAttach()")
    }

    private val envelopeList = arrayListOf<Envelope>(
        Envelope(message = "사용안함", Color.BLUE),
        Envelope(message = "내마음", Color.CYAN),
        Envelope(message = "축하해요", Color.DKGRAY),
        Envelope(message = "사랑해요", Color.GRAY),
        Envelope(message = "힘내세요", Color.GREEN),
        Envelope(message = "뭐하세요", Color.LTGRAY),
        Envelope(message = "퇴근해요", Color.GREEN),
        Envelope(message = "출근해요", Color.RED),
        Envelope(message = "야근해요", Color.BLUE),
        Envelope(message = "집에가요", Color.YELLOW),
        Envelope(message = "술마셔요", Color.RED),
        Envelope(message = "배불러요", Color.MAGENTA)
    )

    private val centerViewItems: ArrayList<CenterViewItem> = ArrayList()
    private var isTouch = false // 리싸이클러뷰 아이템 터치여부.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 400
            scrimColor = Color.TRANSPARENT
            // interpolator = FastOutSlowInInterpolator()
            // setPathMotion(MaterialArcMotion())
            // fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            duration = 400
            scrimColor = Color.TRANSPARENT
            // interpolator = FastOutSlowInInterpolator()
            // setPathMotion(MaterialArcMotion())
            // fadeMode = MaterialContainerTransform.FADE_MODE_OUT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            tvMoney.text = args.money                               // 머니
            isContainEnvelope = args.isContainEnvelope              // 송금봉투 여부
            rootLytMoneyInfo.transitionName = args.tnsRootLayout    // 트랜지션 이름
            topEmptyLayout.setOnClickListener {
                findNavController().popBackStack()
            }
            initRecyclerView(rcvEnvelope)

            startPostponedEnterTransition()
        }
    }

    private fun initRecyclerView(rcvEnvelope: RecyclerView) = rcvEnvelope.run {
        adapter = envelopeAdapter
        LinearSnapHelper().attachToRecyclerView(this)
        val linearLayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false).apply {
                layoutManager = this
            }
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                val centerToLeftDistance = width / 2
                val childViewWidth: Int = context.dpTopx(133F)
                val childViewHalfCount = (width / childViewWidth) / 2

                // #1 송금봉투 아이템리스트 notify.
                initEnvelopeList(childViewHalfCount)
                envelopeAdapter.submitList(envelopeList)

                // #2 첫번째 아이템 중앙으로 스크롤.
                postDelayed({
                    scrollToCenter(
                        position = childViewHalfCount,
                        recyclerView = rcvEnvelope,
                        centerToLeftDistance = centerToLeftDistance
                    )
                }, 500)

                // #3 스크롤 리스너 추가.
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(
                        recyclerView: RecyclerView,
                        newState: Int
                    ) {
                        super.onScrollStateChanged(recyclerView, newState)
                        // #4 스크롤이 멈췄을 경우!
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            val firstVisibleItemPos =
                                linearLayoutManager.findFirstVisibleItemPosition()
                            val lastVisibleItemPos =
                                linearLayoutManager.findLastVisibleItemPosition()
                            val targetItemPos = (firstVisibleItemPos + lastVisibleItemPos) / 2
                            Log.i(
                                "TEST",
                                "targetItemPos:$targetItemPos, item:${envelopeList[targetItemPos]}"
                            )
                            rcvEnvelope.setBackgroundColor(envelopeList[targetItemPos].backgroundColor)
                        }
                    }

                    // #5 스크롤 중 일 경우.
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        post {
                            // 화면에 보이는 아이템 뷰의 갯수.
                            (0 until childCount).forEach { position ->
                                val child = getChildAt(position)
                                val childCenterX = (child.left + child.right) / 2
                                val scaleValue = getGaussianScale(recyclerView, childCenterX)
                                child.scaleX = scaleValue
                                child.scaleY = scaleValue
                            }
                        }
                    }

                })
            }
        })
    }

    private fun initEnvelopeList(childViewHalfCount: Int) {
        for (j in 0 until childViewHalfCount) {
            envelopeList.add(0, Envelope())
        }
        for (k in 0 until childViewHalfCount) {
            envelopeList.add(Envelope())
        }
    }

    private fun scrollToCenter(
        position: Int,
        recyclerView: RecyclerView,
        centerToLeftDistance: Int
    ) {
        val childView =
            (recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(position)
                ?: return
        // 현재 View를 가운데 위치로 이동
        val childViewHalf = childView.width / 2     // 자식뷰의 절반.
        val childViewLeft = childView.left          // 자식뷰의 왼쪽.
        val viewCTop: Int = centerToLeftDistance    // 리싸이클러뷰 중앙에서 왼쪽거리
        val smoothDistance = childViewHalf + childViewLeft - viewCTop   //  스크롤 거리.
        recyclerView.smoothScrollBy(smoothDistance, 0, DecelerateInterpolator())
    }


    private fun getGaussianScale(
        recyclerView: RecyclerView,
        childCenterX: Int,              // 뷰 크기의 센터값.
        minScaleOffest: Float = 1f,     // 최소 스케일.
        scaleFactor: Float = 0.35f,     // 커지는 스케일 비율.
        spreadFactor: Double = 150.0    // 효과가 지속되는 비율 시간?!
    ): Float {
        val recyclerCenterX = (recyclerView.left + recyclerView.right) / 2
        return (Math.pow(
            Math.E,
            -Math.pow(childCenterX - recyclerCenterX.toDouble(), 2.toDouble()) / (2 * Math.pow(
                spreadFactor,
                2.toDouble()
            ))
        ) * scaleFactor + minScaleOffest).toFloat()
    }


}

