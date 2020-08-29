package com.sanekt.eviz.dashboard

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanekt.eviz.MainActivity
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.model.DashboardModel
import kotlinx.android.synthetic.main.content_dash_board.*


class DashBoardActivity : AppCompatActivity(), MyCardListAdapter.ItemclickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.mipmap.logo_xl_big)

        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(applicationContext)
        MyCardList.layoutManager = mLayoutManager
        MyCardList.itemAnimator = DefaultItemAnimator()
        MyCardList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val dashBoardModelList = ArrayList<DashboardModel>()
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        val adapter = MyCardListAdapter(dashBoardModelList, this)
        MyCardList.adapter = adapter


    }

    override fun onStart() {
        super.onStart()
        SharedCardButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        MyCardButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        MyCardButton.setTextColor(ContextCompat.getColor(this, R.color.black))
        SharedCardButton.setTextColor(ContextCompat.getColor(this, R.color.black))

        setonClickListner()
    }

    private fun setonClickListner() {
        MyCardButton.setOnClickListener {
            Toast.makeText(this, "card button clicked", Toast.LENGTH_SHORT).show()
            SharedCardButton.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            SharedCardButton.setTextColor(ContextCompat.getColor(this, R.color.black))
            MyCardButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            MyCardButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        SharedCardButton.setOnClickListener {
            Toast.makeText(this, "shared button clicked", Toast.LENGTH_SHORT).show()
            MyCardButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            SharedCardButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            SharedCardButton.setTextColor(ContextCompat.getColor(this, R.color.white))
            MyCardButton.setTextColor(ContextCompat.getColor(this, R.color.black))

        }
        create_new.setOnClickListener {
//            Toast.makeText(this,"create new cards",Toast.LENGTH_SHORT).show()
            val sendIntent = Intent(this, MainActivity::class.java)
            startActivity(sendIntent)
        }

    }

    override fun onShareButtonCLick(text: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.type = "text/plain"
        Intent.createChooser(sendIntent, "Share via")
        startActivity(sendIntent)
    }

}