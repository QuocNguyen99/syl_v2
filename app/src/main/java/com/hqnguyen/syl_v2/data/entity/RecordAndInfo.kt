package com.hqnguyen.syl_v2.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RecordAndInfo(
    @Embedded val record: RecordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "idRecord"
    )
    val infoRecord: InfoRecordEntity
)