package com.example.smartbikepass.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smartbikepass.data.model.ApplicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications ORDER BY id DESC")
    fun getAllApplicationsFlow(): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications ORDER BY id DESC")
    suspend fun getAllApplications(): List<ApplicationEntity>

    @Query("SELECT * FROM applications WHERE UPPER(pass_id) = UPPER(:passId) LIMIT 1")
    suspend fun getByPassId(passId: String): ApplicationEntity?

    @Query("SELECT * FROM applications WHERE UPPER(pass_id) = UPPER(:passId) LIMIT 1")
    fun getByPassIdFlow(passId: String): Flow<ApplicationEntity?>

    @Query("SELECT * FROM applications WHERE LOWER(roll_no) = LOWER(:rollNo) ORDER BY id DESC")
    fun getApplicationsByRollNoFlow(rollNo: String): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE LOWER(roll_no) = LOWER(:rollNo) ORDER BY id DESC")
    suspend fun getApplicationsByRollNo(rollNo: String): List<ApplicationEntity>

    @Query("SELECT * FROM applications WHERE LOWER(status) IN (:statuses) ORDER BY id DESC")
    fun getApplicationsByStatusesFlow(statuses: List<String>): Flow<List<ApplicationEntity>>

    @Query("SELECT COUNT(*) FROM applications WHERE LOWER(status) = LOWER(:status)")
    fun getCountByStatusFlow(status: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM applications")
    fun getTotalCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: ApplicationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(applications: List<ApplicationEntity>)

    @Update
    suspend fun update(application: ApplicationEntity)

    @Query("UPDATE applications SET status = :status, transport_remarks = :remarks, transport_reviewed_at = :reviewedAt WHERE UPPER(pass_id) = UPPER(:passId)")
    suspend fun updateTransportReview(passId: String, status: String, remarks: String?, reviewedAt: String)

    @Query("UPDATE applications SET status = :status, principal_remarks = :remarks, principal_reviewed_at = :reviewedAt WHERE UPPER(pass_id) = UPPER(:passId)")
    suspend fun updatePrincipalReview(passId: String, status: String, remarks: String?, reviewedAt: String)

    @Query("DELETE FROM applications")
    suspend fun deleteAll()
}
