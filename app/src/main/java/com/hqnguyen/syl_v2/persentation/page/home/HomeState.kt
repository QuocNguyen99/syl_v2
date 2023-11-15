package com.hqnguyen.syl_v2.persentation.page.home

import com.hqnguyen.syl_v2.data.entity.RecordAndInfo
import com.hqnguyen.syl_v2.data.entity.RecordEntity

data class HomeState(
    var listRecord: List<RecordAndInfo> = arrayListOf()
)