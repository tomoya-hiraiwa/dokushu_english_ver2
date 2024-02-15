package edu.wsc2022.a01.dokushu_english_ver2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.wsc2022.a01.dokushu_english_ver2.databinding.MultipleItemBinding

class ChoiceListAdapter(private val dataList: List<ChoiceSet>): RecyclerView.Adapter<ChoiceListAdapter.ChoiceViewHolder>() {
    private var choiceListClicker: ChoiceListClicker? = null
    interface ChoiceListClicker{
        fun onClick(data: String)
    }

    fun setOnChoiceListClicker(lis: ChoiceListClicker){
        choiceListClicker = lis
    }
    var selectedList = mutableListOf<String>()
    inner class ChoiceViewHolder(private val b: MultipleItemBinding): RecyclerView.ViewHolder(b.root){
        fun bindData(position: Int){
            val data = dataList[position]
            if (selectedList.contains(data.value)){
                b.root.visibility = View.INVISIBLE
            }
            else b.root.visibility = View.VISIBLE
            b.listText.text = data.value
            b.root.setOnClickListener {
                selectedList.add(data.value)
                choiceListClicker?.onClick(data.value)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceViewHolder {
        return ChoiceViewHolder(MultipleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ChoiceViewHolder, position: Int) {
        holder.bindData(position)
    }
}