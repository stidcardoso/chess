package com.teda.chesstactics.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.google.gson.Gson
import com.teda.chesstactics.Constants
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.converter.DateConverter
import com.teda.chesstactics.data.dao.EloDao
import com.teda.chesstactics.data.dao.GroupDao
import com.teda.chesstactics.data.dao.PositionDao
import com.teda.chesstactics.data.entity.Elo
import com.teda.chesstactics.data.entity.Group
import com.teda.chesstactics.data.entity.Position
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

@Database(entities = [Elo::class, Position::class, Group::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class CDatabase : RoomDatabase() {

    abstract fun eloDao(): EloDao
    abstract fun positionDao(): PositionDao
    abstract fun groupDao(): GroupDao

    companion object {
        private var INSTANCE: CDatabase? = null
        fun getInstance(context: Context): CDatabase? {
            if (INSTANCE == null) {
                synchronized(CDatabase::class) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
//                    insertAll(INSTANCE!!, DataGenerator.generatePositions())
                }
            }
            return INSTANCE
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        CDatabase::class.java,
                        "c.db")
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                thread {
                                    insertAll(getInstance(context)!!, DataGenerator.generatePositions())
                                    insertElo(getInstance(context)!!)
                                    insertGroups(context)
                                }
                            }
                        }
                        )
                        .build()


        fun destroyInstance() {
            INSTANCE = null
        }

        fun insertAll(database: CDatabase, positions: ArrayList<Position>) {
//            database.runInTransaction {
            database.positionDao().insertAll(positions)
//            }
        }

        fun insertElo(database: CDatabase) {
            val elo = Elo()
            elo.elo = 1500.0
            val format = SimpleDateFormat("dd/MM/yyyy")
            val date = format.format(Date())
            elo.date = Date()
            elo.sDate = date
            database.eloDao().insert(elo)
        }

        fun insertGroups(context: Context) {
            val json = Utilities.loadJSONFromAsset(context, Constants.INITIAL_GROUPS)
            val gson = Gson()
            val group = gson.fromJson(json, Group::class.java)
            val id = group?.let { getInstance(context)?.groupDao()?.insertGroup(it) }?.toInt()
            group.positions?.let {
                for (position in it) {
                    position.groupId = id
                    getInstance(context)?.positionDao()?.insertAll(group.positions!!)
                }
            }
        }

    }
}