package com.hqnguyen.syl_v2.ui.page.home

import com.hqnguyen.syl_v2.data.entity.RecordEntity

data class HomeState(
    var listRecord: List<RecordEntity> = arrayListOf()
)