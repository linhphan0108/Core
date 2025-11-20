package com.linhphan.lpcore

import androidx.appcompat.app.AppCompatActivity
import com.linhphan.lpcore.data.Cake
import com.linhphan.lpcore.ui.twosidepannels.pannelone.PanelOneFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity(), PanelOneFragment.OnFragmentInteractionListener {
    override fun onCakeClicked(cake: Cake) {
        // No-op for testing
    }
}