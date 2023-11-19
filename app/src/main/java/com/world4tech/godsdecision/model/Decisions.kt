package com.world4tech.godsdecision.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "decisions_table")
class Decisions(@PrimaryKey (autoGenerate = true) var id: Int = 0,
                @ColumnInfo (name = "dateTexy") var date:String,
                @ColumnInfo(name ="decisionText") var text:String)