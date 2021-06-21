package com.johnzar.navigationtransition

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.ColorParser
import com.google.android.material.transition.MaterialContainerTransform
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
        Log.d("TEST","onAttach()")
    }

    private val envelopeList = arrayListOf<Envelope?>(
        Envelope(message = "사용안함",Color.BLUE),
        Envelope(message = "내마음",Color.CYAN),
        Envelope(message = "축하해요",Color.DKGRAY),
        Envelope(message = "사랑해요",Color.GRAY),
        Envelope(message = "힘내세요",Color.GREEN),
        Envelope(message = "뭐하세요",Color.LTGRAY),
        Envelope(message = "퇴근해요",Color.MAGENTA),
        Envelope(message = "출근해요",Color.RED),
        Envelope(message = "야근해요",Color.WHITE),
        Envelope(message = "집에가요",Color.YELLOW),
        Envelope(message = "술마셔요",Color.MAGENTA),
        Envelope(message = "배불러요",Color.MAGENTA)
    )

    private val centerViewItems: ArrayList<CenterViewItem> = ArrayList()
    private var isTouch = false // 리싸이클러뷰 아이템 터치여부.
    private val decelerateInterpolator = DecelerateInterpolator()


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
            rcvEnvelope.run {
                initialize(
                    newAdapter = envelopeAdapter,
                    ItemWidth = 132F
                ) { childViewHalfCount, centerToLeftDistance ->
                    // 여기서 아이템 리스트에 앞뒤로 추가를 해줘야 함.
                    initEnvelopeList(childViewHalfCount,centerToLeftDistance)
                    // 그리고 어댑터 notify.
                    envelopeAdapter.submitList(envelopeList)
                    envelopeList
                }
            }
            startPostponedEnterTransition()
        }
    }

    private fun initEnvelopeList(childViewHalfCount: Int, centerToLeftDistance: Int) {
        for (j in 0 until childViewHalfCount) { //头部的空布局
            envelopeList.add(0, Envelope("",null))
        }
        for (k in 0 until childViewHalfCount) {  //尾部的空布局
            envelopeList.add(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST","onDestroy()")
    }
}

