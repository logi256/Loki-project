package com.example.smartbikepass.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "pass_id")
    val passId: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "roll_no")
    val rollNo: String,
    val email: String,
    val phone: String,
    val department: String,
    val year: String,
    @ColumnInfo(name = "vehicle_no")
    val vehicleNo: String,
    @ColumnInfo(name = "vehicle_type")
    val vehicleType: String,
    @ColumnInfo(name = "rc_book")
    val rcBook: String? = null,
    val license: String? = null,
    val insurance: String? = null,
    val status: String = "pending", // pending, transport_verified, transport_rejected, approved, principal_rejected
    @ColumnInfo(name = "transport_remarks")
    val transportRemarks: String? = null,
    @ColumnInfo(name = "principal_remarks")
    val principalRemarks: String? = null,
    @ColumnInfo(name = "transport_reviewed_at")
    val transportReviewedAt: String? = null,
    @ColumnInfo(name = "principal_reviewed_at")
    val principalReviewedAt: String? = null,
    @ColumnInfo(name = "submitted_at")
    val submittedAt: String
)
