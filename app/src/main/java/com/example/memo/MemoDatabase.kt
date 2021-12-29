package com.example.memo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MemoEntity::class), version = 1)
//abstract : 추상 클래스
//companion object : 자바의 static method와 비슷하다
abstract class MemoDatabase : RoomDatabase(){
    abstract fun memoDAO() : MemoDAO

    //DB를 만드는 작업은 리소스를 많이 잡아먹어서 앱 전체 프로세스 안에서 객체를 한번만 생성한다 : 싱글턴 패턴
    companion object {
        var INSTANCE : MemoDatabase? = null

        fun getInstance(context: Context) : MemoDatabase? {
            if(INSTANCE == null){
                synchronized(MemoDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    MemoDatabase::class.java, "memo.DB")
                        .fallbackToDestructiveMigration()//전에 있던 정보를 삭제 하고 새로 구성하는것(?)
                        .build()
                }
            }

            return INSTANCE
        }
    }
}