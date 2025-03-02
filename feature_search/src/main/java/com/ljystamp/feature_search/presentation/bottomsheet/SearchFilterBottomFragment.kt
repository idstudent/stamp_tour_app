package com.ljystamp.feature_search.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ljystamp.core_ui.R
import com.ljystamp.feature_search.databinding.BottomFragmentSearchFilterBinding
import com.ljystamp.utils.setOnSingleClickListener

class SearchFilterBottomFragment : BottomSheetDialogFragment() {
    private var _binding: BottomFragmentSearchFilterBinding? = null
    private val binding get() = _binding!!
    private var filterClickListener: FilterClickListener? = null
    fun setFilterClickListener(listener: FilterClickListener) {
        filterClickListener = listener
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomFragmentSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            tvSearchTour.setOnSingleClickListener {
                filterClickListener?.onFilterSelected(12)
                dismiss()
            }

            tvSearchCulture.setOnSingleClickListener {
                filterClickListener?.onFilterSelected(14)
                dismiss()
            }

            tvSearchEvent.setOnSingleClickListener {
                filterClickListener?.onFilterSelected(15)
                dismiss()
            }

            tvSearchActivity.setOnSingleClickListener {
                filterClickListener?.onFilterSelected(28)
                dismiss()
            }

            tvSearchFood.setOnSingleClickListener {
                filterClickListener?.onFilterSelected(39)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}