@file:JvmName("ItemsAdapter")

package com.sanekt.eviz.presentation.items

import android.content.Context
import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.sanekt.eviz.DragDropTwoActivity
import com.sanekt.eviz.MainActivity
import com.sanekt.eviz.R
import com.sanekt.eviz.databinding.ItemViewBinding
import kotlinx.android.synthetic.main.item_view.view.*
import java.util.*

class ItemsAdapter(private var context: Context) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var items: List<String> = emptyList()

    private val loading = 0
    private val item = 1
    private var context1:Context?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context1=context
        return ItemViewHolder(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemViewHolder && items.size > position) {
            holder.bind(items[position])
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context1, DragDropTwoActivity::class.java)
            intent.putExtra("text",items[position])
            context1?.startActivity(intent)
        }
    }

    fun update(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(items: List<String>) {
            val adapter = adapter as ItemsAdapter
            adapter.update(items)
        }
    }

    override fun getItemViewType(position: Int) =
        if (items[position] == "loading") loading else item

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ItemViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view,
            parent,
            false
        )
    ) : ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.item = item
        }
    }
}