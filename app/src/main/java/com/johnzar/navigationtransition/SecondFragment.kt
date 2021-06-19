package com.johnzar.navigationtransition

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.johnzar.navigationtransition.databinding.FragmentSecondBinding
import net.cachapa.expandablelayout.util.FastOutSlowInInterpolator

class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private val args by navArgs<SecondFragmentArgs>()

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
        }
        startPostponedEnterTransition()
    }
}

