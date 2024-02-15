package edu.wsc2022.a01.dokushu_english_ver2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.wsc2022.a01.dokushu_english_ver2.databinding.SingleItemBinding

class SingleListAdapter(private val dataList: List<ChoiceSet>,private val context: Context): RecyclerView.Adapter<SingleListAdapter.SingleListViewHolder>() {
    private var singleClicker: SingleClicker? = null
    interface SingleClicker{
        fun onClick(data: String)
    }
    fun setOnSingleClicker(lis: SingleClicker){
        singleClicker = lis
    }
    inner class SingleListViewHolder(private val b: SingleItemBinding): RecyclerView.ViewHolder(b.root){
        fun bindData(position: Int){
            val data = dataList[position]
            b.listText.text = data.value
            b.root.setOnClickListener {
                singleClicker?.onClick(data.value)
                b.root.setBackgroundColor(context.resources.getColor(R.color.select,context.theme))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleListViewHolder {
        return SingleListViewHolder(SingleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: SingleListViewHolder, position: Int) {
        holder.bindData(position)
    }
}