package com.example.smartbikepass.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.data.model.AuditLogEntity
import com.example.smartbikepass.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ApplicationEntity::class, UserEntity::class, AuditLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun applicationDao(): ApplicationDao
    abstract fun userDao(): UserDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "smartbike.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    scope.launch(Dispatchers.IO) {
                        try {
                            if (instance.userDao().getUserCount() == 0) {
                                populateInitialData(instance)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                instance
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val userDao = database.userDao()
            val appDao = database.applicationDao()
            val auditDao = database.auditLogDao()

            // Seed default users with standard credentials
            userDao.insertAll(
                listOf(
                    UserEntity(username = "transport", password = "transport123", role = "transport"),
                    UserEntity(username = "principal", password = "principal123", role = "principal"),
                    UserEntity(username = "admin", password = "admin123", role = "admin"),
                    UserEntity(username = "admin", password = "admin", role = "admin"),
                    UserEntity(username = "transport", password = "transport", role = "transport"),
                    UserEntity(username = "principal", password = "principal", role = "principal")
                )
            )

            // Seed initial applications from imported project
            val initialApps = listOf(
                ApplicationEntity(
                    passId = "SBPS-D5C279BA",
                    fullName = "logeshwar.r",
                    rollNo = "24aid31",
                    email = "logeshwarr24aid@vetias.ac.in",
                    phone = "8122318908",
                    department = "Computer Science",
                    year = "2nd Year",
                    vehicleNo = "TN86 A8311",
                    vehicleType = "Two Wheeler (Petrol)",
                    rcBook = "rc_e19a10c2.png",
                    license = "dl_55e7ac22.png",
                    insurance = "ins_876e9a29.png",
                    status = "approved",
                    transportRemarks = "Documents verified",
                    principalRemarks = "approved the pass",
                    transportReviewedAt = "2026-02-26 11:39:55",
                    principalReviewedAt = "2026-02-26 11:41:34",
                    submittedAt = "2026-02-26 11:38:22"
                ),
                ApplicationEntity(
                    passId = "SBPS-4EC7C9EF",
                    fullName = "logeshwar.r",
                    rollNo = "24aid31",
                    email = "logeshwarr24aid@vetias.ac.in",
                    phone = "8122318908",
                    department = "Computer Science",
                    year = "2nd Year",
                    vehicleNo = "TN86 A8311",
                    vehicleType = "Two Wheeler (Petrol)",
                    rcBook = "rc_d5c53869.jpg",
                    license = "dl_d4b9bfa0.png",
                    insurance = "ins_423b1861.png",
                    status = "transport_rejected",
                    transportRemarks = "not correct file to upload image",
                    principalRemarks = null,
                    transportReviewedAt = "2026-02-27 19:59:05",
                    principalReviewedAt = null,
                    submittedAt = "2026-02-27 19:56:58"
                ),
                ApplicationEntity(
                    passId = "SBPS-3B652BDD",
                    fullName = "logeshwar.r",
                    rollNo = "24aid31",
                    email = "logeshwarr24aid@vetias.ac.in",
                    phone = "8122318908",
                    department = "Computer Science",
                    year = "2nd Year",
                    vehicleNo = "TN86 A8311",
                    vehicleType = "Two Wheeler (Petrol)",
                    rcBook = "rc_19d554ae.png",
                    license = "dl_20d988f6.png",
                    insurance = "ins_355ec10f.jpg",
                    status = "approved",
                    transportRemarks = "Verified",
                    principalRemarks = "Bike Pass approved",
                    transportReviewedAt = "2026-02-27 20:03:20",
                    principalReviewedAt = "2026-02-27 20:04:39",
                    submittedAt = "2026-02-27 20:02:31"
                ),
                ApplicationEntity(
                    passId = "SBPS-9E21A4B7",
                    fullName = "Priya Sharma",
                    rollNo = "CS22B045",
                    email = "priya.s@college.edu",
                    phone = "9876543210",
                    department = "Computer Science",
                    year = "3rd Year",
                    vehicleNo = "TN09 BF4589",
                    vehicleType = "Two Wheeler (Electric)",
                    rcBook = "rc_sample.png",
                    license = "dl_sample.png",
                    insurance = "ins_sample.png",
                    status = "pending",
                    transportRemarks = null,
                    principalRemarks = null,
                    transportReviewedAt = null,
                    principalReviewedAt = null,
                    submittedAt = "2026-09-15 10:15:00"
                ),
                ApplicationEntity(
                    passId = "SBPS-7C88D2E1",
                    fullName = "Karthik Raj",
                    rollNo = "ME21B018",
                    email = "karthik.r@college.edu",
                    phone = "9845123456",
                    department = "Mechanical",
                    year = "4th Year",
                    vehicleNo = "TN38 CZ9012",
                    vehicleType = "Two Wheeler (Petrol)",
                    rcBook = "rc_sample.png",
                    license = "dl_sample.png",
                    insurance = "ins_sample.png",
                    status = "transport_verified",
                    transportRemarks = "Documents verified with original RC and license.",
                    principalRemarks = null,
                    transportReviewedAt = "2026-09-15 11:30:00",
                    principalReviewedAt = null,
                    submittedAt = "2026-09-15 09:00:00"
                )
            )
            appDao.insertAll(initialApps)

            // Seed initial audit log
            val initialLogs = listOf(
                AuditLogEntity(passId = "SBPS-D5C279BA", action = "Application Submitted", doneBy = "logeshwar.r", remarks = null, createdAt = "2026-02-26 11:38:22"),
                AuditLogEntity(passId = "SBPS-D5C279BA", action = "Transport: verify", doneBy = "transport", remarks = "", createdAt = "2026-02-26 11:39:55"),
                AuditLogEntity(passId = "SBPS-D5C279BA", action = "Principal: approve", doneBy = "principal", remarks = "approved the pass", createdAt = "2026-02-26 11:41:34"),
                AuditLogEntity(passId = "SBPS-4EC7C9EF", action = "Application Submitted", doneBy = "logeshwar.r", remarks = null, createdAt = "2026-02-27 19:56:58"),
                AuditLogEntity(passId = "SBPS-4EC7C9EF", action = "Transport: reject", doneBy = "transport", remarks = "not correct file to upload image", createdAt = "2026-02-27 19:59:05"),
                AuditLogEntity(passId = "SBPS-3B652BDD", action = "Application Submitted", doneBy = "logeshwar.r", remarks = null, createdAt = "2026-02-27 20:02:31"),
                AuditLogEntity(passId = "SBPS-3B652BDD", action = "Transport: verify", doneBy = "transport", remarks = "", createdAt = "2026-02-27 20:03:20"),
                AuditLogEntity(passId = "SBPS-3B652BDD", action = "Principal: approve", doneBy = "principal", remarks = "Bike Pass approved", createdAt = "2026-02-27 20:04:39"),
                AuditLogEntity(passId = "SBPS-9E21A4B7", action = "Application Submitted", doneBy = "Priya Sharma", remarks = null, createdAt = "2026-09-15 10:15:00"),
                AuditLogEntity(passId = "SBPS-7C88D2E1", action = "Application Submitted", doneBy = "Karthik Raj", remarks = null, createdAt = "2026-09-15 09:00:00"),
                AuditLogEntity(passId = "SBPS-7C88D2E1", action = "Transport: verify", doneBy = "transport", remarks = "Documents verified with original RC and license.", createdAt = "2026-09-15 11:30:00")
            )
            auditDao.insertAll(initialLogs)
        }
    }
}
