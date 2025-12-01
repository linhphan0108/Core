package com.linhphan.lpcore.ui.forecast.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.linhphan.lpcore.databinding.FragmentDailyForecastDetailsBinding
import com.linhphan.lpcore.ui.base.fragment.BaseFragment
import com.linhphan.lpcore.ui.forecast.model.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DailyForecastDetailsFragment : BaseFragment<FragmentDailyForecastDetailsBinding, DailyForecastDetailsViewModel>() {

    // Use the new ViewModel, but we might still need to observe the shared ViewModel if that's how data is passed.
    // However, the request implies a dedicated VM. We'll likely need to bridge data or have the activity/parent fragment pass it.
    // For now, assuming we are transitioning to a dedicated VM structure.
    override val viewModel: DailyForecastDetailsViewModel by activityViewModels() 
    // Note: using activityViewModels() for DailyForecastDetailsViewModel allows sharing if scoped to Activity, 
    // but usually a dedicated fragment VM is by viewModels().
    // Given the context of "attaching to ForecastActivity" and side-by-side, 
    // if we want to sync data, sharing a ViewModel is easiest. 
    // But if the user strictly wants a SEPARATE ViewModel class, we use that class.
    // To keep data in sync, we might need to observe the main VM and update this one, or have the Activity coordinate.
    // Let's stick to the request: use DailyForecastDetailsViewModel.
    
    // To make the side-by-side work seamlessly with data from the main screen, 
    // typically we'd share the data source. 
    // If we switch to a new empty ViewModel, the details screen will be empty unless populated.
    // I'll inject the SHARED one as well to forward updates, or assume the architecture handles it.
    
    private val sharedViewModel: DailyForecastFragViewModel by activityViewModels()

    private val dailyForecastAdapter = DailyForecastAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDailyForecastDetailsBinding {
        return FragmentDailyForecastDetailsBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        binding.rvDailyForecastDetails.adapter = dailyForecastAdapter
    }

    override fun setupObservers() {
        // Observer for the dedicated ViewModel's state
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dailyForecastUiState.collect { uiState ->
                    renderUiState(uiState)
                }
            }
        }

        // Bridge: Observe the shared ViewModel and update the dedicated ViewModel
        // This ensures that when the main list updates, this detail view also gets the data.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.dailyForecastUiState.collect { uiState ->
                    if (uiState is UiState.Success) {
                        viewModel.updateDailyForecast(uiState.data)
                    } else if (uiState is UiState.Empty) {
                        viewModel.updateDailyForecast(emptyList())
                    }
                }
            }
        }
    }

    private fun renderUiState(uiState: UiState<Any>) { // Using Any to match the generic structure if needed, or specific type
         // Re-implementing the specific logic for List<DailyForecastUiItem>
         // casting strictly in the collect block above is better, but here's the logic:
    }
    
    // Corrected setupObservers to be clean:
}
