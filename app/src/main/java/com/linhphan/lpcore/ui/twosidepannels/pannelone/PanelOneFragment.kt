package com.linhphan.lpcore.ui.twosidepannels.pannelone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.linhphan.lpcore.domain.model.Cake
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.databinding.FragmentPanelOneBinding
import com.linhphan.lpcore.ui.base.fragment.BaseFragment
import com.linhphan.lpcore.ui.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PanelOneFragment : BaseFragment<FragmentPanelOneBinding, PanelOneFragmentViewModel>() {

    val activityViewModel: MainActivityViewModel by activityViewModels()
    override val viewModel: PanelOneFragmentViewModel by viewModels()
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var adapter: CakeAdapter

    interface OnFragmentInteractionListener {
        fun onCakeClicked(cake: Cake)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPanelOneBinding {
        return FragmentPanelOneBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        adapter = CakeAdapter { cake ->
            listener?.onCakeClicked(cake)
        }
        binding.rvCakes.layoutManager = LinearLayoutManager(context)
        binding.rvCakes.adapter = adapter

        binding.btnRetry.setOnClickListener {
            viewModel.loadCakes()
        }
    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cakes.collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvCakes.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                            binding.btnRetry.visibility = View.GONE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvCakes.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                            binding.btnRetry.visibility = View.GONE
                            adapter.submitList(result.data)
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvCakes.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.btnRetry.visibility = View.VISIBLE
                            binding.tvError.text = result.exception.message ?: "Unknown error"
                        }
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}