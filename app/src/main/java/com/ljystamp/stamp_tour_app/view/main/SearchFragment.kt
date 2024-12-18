package com.ljystamp.stamp_tour_app.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ljystamp.stamp_tour_app.FilterClickListener
import com.ljystamp.stamp_tour_app.databinding.FragmentSearchBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.ljystamp.stamp_tour_app.view.search.SearchListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment: BaseFragment<FragmentSearchBinding>() {
    private var contentTypeId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.run {
            ivFilter.setOnSingleClickListener {
                showFilterBottomSheet()
            }

            tvFilter.setOnSingleClickListener {
                showFilterBottomSheet()
            }

            ivSearch.setOnSingleClickListener {
                if(contentTypeId == -1) {
                    Toast.makeText(requireActivity(), "검색 조건을 선택하세요", Toast.LENGTH_SHORT).show()
                    return@setOnSingleClickListener
                }

                val inputText = etSearch.text.toString()

                if(inputText == "") {
                    Toast.makeText(requireActivity(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                    return@setOnSingleClickListener
                }else {
                    val intent = Intent(requireActivity(), SearchListActivity::class.java)
                    intent.putExtra("contentTypeId", contentTypeId)
                    intent.putExtra("keyword", inputText)
                    startActivity(intent)
                }


            }
        }
    }

    private fun showFilterBottomSheet() {
        val bottomSheet = SearchFilterBottomFragment()
        bottomSheet.setFilterClickListener(object: FilterClickListener {
            override fun onFilterSelected(filterType: Int) {
                contentTypeId = filterType

                binding.run {
                    when(contentTypeId) {
                        12 -> {
                            tvFilter.text = "여행지"
                        }
                        14 -> {
                            tvFilter.text = "문화"
                        }
                        15 -> {
                            tvFilter.text = "축제"
                        }
                        28 -> {
                            tvFilter.text = "액티비티"
                        }
                        39 -> {
                            tvFilter.text = "음식"
                        }
                    }
                }
            }
        })
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater)
    }
}