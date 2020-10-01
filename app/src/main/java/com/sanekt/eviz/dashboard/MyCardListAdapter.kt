package com.sanekt.eviz.dashboard

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.fragments.CardFragment
import com.sanekt.eviz.dashboard.fragments.HomeFragment
import com.sanekt.eviz.dashboard.model.DashboardModel
import java.io.File


class MyCardListAdapter(
    private val dashBoardModel: ArrayList<DashboardModel>,
    context: CardFragment,
    fileList: ArrayList<File>
) :
    Adapter<MyCardListAdapter.MyViewHolder>() {

    var listener:ItemclickListener? = null
    var context1:Context? = null
    var fileList:ArrayList<File>? = null
    init {
        listener = context
        context1 = context.activity
        this.fileList = fileList
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       var cardView:ImageView? = null
        var title:TextView? = null
        var subTitle:TextView?= null
        var dateReceived:TextView?= null
        var sharedCard:ImageView?= null


        init {
           cardView = itemView.findViewById(R.id.cardview)
            title = itemView.findViewById(R.id.card_title)
            subTitle = itemView.findViewById(R.id.received_date_text_view)
            sharedCard = itemView.findViewById(R.id.share_card)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val itemView = inflator.inflate(R.layout.dashboard_list_item_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() =
        fileList!!.size


    override fun onBindViewHolder(holder: MyCardListAdapter.MyViewHolder, position: Int) {
        holder.sharedCard?.setOnClickListener {
          listener?.onShareButtonCLick("this is demo card link")
        }
        holder.cardView!!.setImageURI(Uri.parse(fileList?.get(position).toString()))
    }
    interface ItemclickListener{
        fun onShareButtonCLick(text:String)
    }
}