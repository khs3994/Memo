package com.example.memo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val context: Context, var list: List<MemoEntity>, var onDeleteListener: OnDeleteListener) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    //뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_memo,parent,false)

        return MyViewHolder(itemView)
    }

    //우리가 만든 아이템 리스트 틀과 내용을 합쳐주는것
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val memo = list[position]

        holder.memo.text = memo.memo
        holder.root.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                onDeleteListener.onDeleteListener(memo)
                return true
            }
        })

    }

    //크기 반환
    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val memo: TextView = itemView.findViewById(R.id.item_memo)
        val root = itemView.findViewById<ConstraintLayout>(R.id.root)
    }

}