package com.example.memo

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memo.databinding.ActivityMainBinding
@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() , OnDeleteListener{

    private lateinit var binding: ActivityMainBinding
    lateinit var db : MemoDatabase
    var memoList = listOf<MemoEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getInstance를 이용해서 싱글턴 객체를 가져옴
       db = MemoDatabase.getInstance(this)!!

        binding.btnAdd.setOnClickListener {
            val memo = MemoEntity(null,binding.edittextMemo.text.toString())
            binding.edittextMemo.setText("")
            insertMemo(memo)
        }

        //선형으로 보이게끔 리사이클러뷰 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        getAllMemos()
    }

    //1. Insert Data
    //2. Get Data
    //3. Delete Data

    //4. Set RecyclerView

    fun insertMemo(memo : MemoEntity){
        //1.MainThread vs WorkerThread(Background Thread)
        // 모든 UI관련된 일은 MainThread에서 진행한다
        // 모든 데이터 관련 통신은 WorkerThread에서 진행한다

        val insertTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                //데이터 관련 작업이기 때문에 워커스레드에 정의한다
                db.memoDAO().insert(memo)
            }

            //위 작업 후에 무슨 동작을 할것인지 지정한다
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                //위에서 insert를 했기 때문에 DB를 다시 불러와본다
                getAllMemos()
            }
        }
        insertTask.execute()
    }

    fun getAllMemos(){
        val getTask = (object : AsyncTask<Unit,Unit,Unit>(){ //Async는 비동기 적인 활동이나 백그라운드에서 하는 활동을 도와준다
            override fun doInBackground(vararg params: Unit?) {
                memoList = db.memoDAO().getAll()//메모리스트에 내용 저장
                //비어있는 메모리스트에 새로운 리스트를 추가
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)//저징한 내용을 리사이클러뷰에 넣는다
            }
        }).execute()
    }

    fun deleteMemo(memo: MemoEntity){
        val deleteTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                db.memoDAO().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }
        deleteTask.execute()
    }

    fun setRecyclerView(memoList : List<MemoEntity>){
        binding.recyclerView.adapter = MyAdapter(this,memoList,this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }
}