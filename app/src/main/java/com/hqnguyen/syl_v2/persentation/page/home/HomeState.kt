package com.hqnguyen.syl_v2.persentation.page.home

import com.hqnguyen.syl_v2.data.entity.RecordAndInfo

data class HomeState(
    val listRecord: List<RecordAndInfo> = arrayListOf()
)