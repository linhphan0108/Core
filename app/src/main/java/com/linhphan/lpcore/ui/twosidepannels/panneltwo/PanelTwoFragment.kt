package com.linhphan.lpcore.ui.twosidepannels.panneltwo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.linhphan.lpcore.data.Cake
import com.linhphan.lpcore.databinding.FragmentPanelTwoBinding
import com.linhphan.lpcore.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PanelTwoFragment : BaseFragment<FragmentPanelTwoBinding, PanelTwoFragmentViewModel>() {

    override val viewModel: PanelTwoFragmentViewModel by viewModels()

    private var cake: Cake? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPanelTwoBinding {
        return FragmentPanelTwoBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        arguments?.let {
            val name = it.getString(ARG_CAKE_NAME)
            val desc = it.getString(ARG_CAKE_DESC)
            binding.tvCakeName.text = name
            binding.tvCakeDescription.text = desc
        }
    }

    override fun setupObservers() {
        // Observe data if needed
    }

    companion object {
        private const val ARG_CAKE_NAME = "cake_name"
        private const val ARG_CAKE_DESC = "cake_desc"

        fun newInstance(cake: Cake): PanelTwoFragment {
            val fragment = PanelTwoFragment()
            val args = Bundle()
            args.putString(ARG_CAKE_NAME, cake.name)
            args.putString(ARG_CAKE_DESC, cake.description)
            fragment.arguments = args
            return fragment
        }
    }
}