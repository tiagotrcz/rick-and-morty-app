package dev.pimentel.rickandmorty.presentation.filter

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.FilterDialogBinding
import dev.pimentel.rickandmorty.databinding.FilterItemBinding
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterIntent
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.extensions.lifecycleBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FilterDialog : DialogFragment(R.layout.filter_dialog) {

    private val binding by lifecycleBinding(FilterDialogBinding::bind)
    private val viewModel: FilterContract.ViewModel by viewModels<FilterViewModel>()

    private lateinit var searchFieldListener: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindOutputs()
        bindInputs()
    }


    private fun bindOutputs() {
        lifecycleScope.launch {
            viewModel.state().collect { state ->
                binding.apply {
                    state.titleRes?.let(title::setText)

                    state.clearText.takeIf { true }.also {
                        searchField.removeTextChangedListener(searchFieldListener)
                        searchField.text = null
                        searchField.addTextChangedListener(searchFieldListener)
                    }

                    state.clearSelection.takeIf { true }.also { oldFilters.clearCheck() }

                    state.list.mapIndexed { index, filter ->
                        val binding = FilterItemBinding.inflate(layoutInflater, oldFilters, false)
                        binding.item.text = filter
                        binding.item.id = index
                        binding.root
                    }.forEach(oldFilters::addView)

                    ok.isEnabled = state.canApply

                    state.result?.let { result ->
                        parentFragmentManager.setFragmentResult(
                            FILTER_RESULT_LISTENER_KEY,
                            bundleOf(FILTER_RESULT_KEY to result)
                        )
                    }
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun bindInputs() {
        binding.apply {
            cancel.setOnClickListener { viewModel.intentChannel.offer(FilterIntent.Close) }

            ok.setOnClickListener { viewModel.intentChannel.offer(FilterIntent.GetFilter) }

            searchFieldListener = searchField.doAfterTextChanged { text ->
                viewModel.intentChannel.offer(FilterIntent.SetFilterFromText(text.toString()))
            }

            oldFilters.setOnCheckedChangeListener { group, index ->
                try {
                    if ((group[index] as RadioButton).isChecked) {
                        viewModel.intentChannel.offer(FilterIntent.SetFilterFromSelection(index))
                    }
                } catch (exception: IndexOutOfBoundsException) {
                    Timber.d(exception)
                }
            }
        }

        viewModel.intentChannel.offer(
            FilterIntent.Initialize(
                requireArguments()[FILTER_TYPE_ARGUMENT_KEY] as FilterType
            )
        )
    }

    companion object {
        const val FILTER_TYPE_ARGUMENT_KEY = "FILTER_TYPE_ARGUMENT"

        const val FILTER_RESULT_LISTENER_KEY = "FILTER_RESULT_LISTENER"
        const val FILTER_RESULT_KEY = "FILTER_RESULT"
    }
}
