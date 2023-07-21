package com.gotranspo.tramtransit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gotranspo.tramtransit.db.dao.AppDao
import com.gotranspo.tramtransit.db.entities.DestinationEnitity

@Database(entities = [DestinationEnitity::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun getDAO() : AppDao
    companion object
    {
        private var dbInstance : AppDatabase? = null
        fun getAPpDB(context : Context) : AppDatabase
        {
            if (dbInstance == null)
            {
                dbInstance = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,AppDatabase::class.java,"DestinationsDB"
                ).allowMainThreadQueries().build()
            }
            return  dbInstance!!
        }

    }


}