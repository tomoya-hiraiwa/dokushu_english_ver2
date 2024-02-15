package edu.wsc2022.a01.dokushu_english_ver2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.wsc2022.a01.dokushu_english_ver2.databinding.MultipleItemBinding

class AnswerListAdapter(private val dataList: MutableList<String>): RecyclerView.Adapter<AnswerListAdapter.AnswerViewHolder>() {
    private var answerListClicker: AnswerListClicker? = null
    interface AnswerListClicker{
        fun onClick(data: String)
    }

    fun setOnAnswerListClicker(lis: AnswerListClicker){
        answerListClicker = lis
    }
    inner class AnswerViewHolder(private val b: MultipleItemBinding): RecyclerView.ViewHolder(b.root){
        fun bindData(position: Int){
            val data = dataList[position]
            b.listText.text = data
            b.root.setOnClickListener {
                answerListClicker?.onClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(MultipleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bindData(position)
    }
}