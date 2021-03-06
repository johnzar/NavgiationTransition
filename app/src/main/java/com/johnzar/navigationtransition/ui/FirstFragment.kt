package com.johnzar.navigationtransition.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.johnzar.navigationtransition.R
import com.johnzar.navigationtransition.databinding.FragmentFirstBinding


/*
* The transition will only work if there is an equal amount of views in the layouts we are transitioning to/from.
* Adding empty views inside the CardView in Fragment A
* */
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            tvMoney.text = "246,000원"
//            tvName.text = "예금주: 금영호"
            btnSend.setOnClickListener {
                findNavController().navigate(
                    FirstFragmentDirections.actionFirstFragmentToSecondFragment(
                        tnsRootLayout = rootLyt.transitionName,
                        money = tvMoney.text.toString().trim(),
                        isContainEnvelope = checkBongtu.isChecked
                    ), FragmentNavigatorExtras(
                        rootLyt to rootLyt.transitionName,
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TEST","onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST","onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TEST","onDetach()")
    }
}