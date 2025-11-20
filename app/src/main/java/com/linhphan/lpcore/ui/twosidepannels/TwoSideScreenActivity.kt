package com.linhphan.lpcore.ui.twosidepannels

import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.linhphan.lpcore.R
import com.linhphan.lpcore.data.Cake
import com.linhphan.lpcore.databinding.ActivityTwoSideScreenBinding
import com.linhphan.lpcore.ui.base.activity.BaseActivity
import com.linhphan.lpcore.ui.twosidepannels.pannelone.PanelOneFragment
import com.linhphan.lpcore.ui.twosidepannels.panneltwo.PanelTwoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TwoSideScreenActivity : BaseActivity<ActivityTwoSideScreenBinding>(), PanelOneFragment.OnFragmentInteractionListener {

    private val viewModel: TwoSideScreenActivityViewModel by viewModels()
    private var isTwoPane: Boolean = false

    override fun getViewBinding(): ActivityTwoSideScreenBinding {
        return ActivityTwoSideScreenBinding.inflate(LayoutInflater.from(this))
    }

    override fun setupViews() {
        // Check if the second container exists to determine two-pane mode
        isTwoPane = binding.root.findViewById<View>(R.id.fragment_container_second) != null

        if (supportFragmentManager.findFragmentById(R.id.fragment_container_first) == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container_first, PanelOneFragment())
            }
        }
    }

    override fun setupObservers() {
        // Observe ViewModel state here
    }

    override fun onCakeClicked(cake: Cake) {
        val fragment = PanelTwoFragment.Companion.newInstance(cake)
        if (isTwoPane) {
            // In two-pane mode, the second fragment is already displayed
            supportFragmentManager.commit {
                replace(R.id.fragment_container_second, fragment)
            }
        } else {
            // In single-pane mode, swap fragments
            supportFragmentManager.commit {
                replace(R.id.fragment_container_first, fragment)
                addToBackStack(null)
            }
        }
    }
}