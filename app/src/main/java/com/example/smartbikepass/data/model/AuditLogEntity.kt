package com.example.smartbikepass.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_log")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "pass_id")
    val passId: String,
    val action: String,
    @ColumnInfo(name = "done_by")
    val doneBy: String,
    val remarks: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String
)
