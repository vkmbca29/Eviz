package com.sanekt.eviz.dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sanekt.eviz.dashboard.create_new_card.MainActivity
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.model.DashboardModel
import kotlinx.android.synthetic.main.content_dash_board.*
import java.io.File


class DashBoardActivity : AppCompatActivity(),MyCardListAdapter.ItemclickListener{
    var dataString = "{\"imageData\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":100.0,\"matrixX\":70.06935,\"matrixY\":125.96808,\"uriString\":\"file:///storage/emulated/0/DCIM/Camera/IMG_20200906_094422741.jpg\"},\"textData1\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":400.0,\"matrixX\":261.84467,\"matrixY\":50.95072,\"textColor\":-16777216,\"textName\":\"Radha\",\"typeface\":0},\"textData2\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":400.0,\"matrixX\":256.85162,\"matrixY\":146.9535,\"textColor\":-16777216,\"textName\":\"Lead Manager\",\"typeface\":0},\"textData3\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":400.0,\"matrixX\":218.9043,\"matrixY\":253.94867,\"textColor\":-16777216,\"textName\":\"radha@gmail.com\",\"typeface\":0}}"
    var dashBoardModelList:ArrayList<DashboardModel>?=null
    var context:Context?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.mipmap.logo_xl_big)
        context=this
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(applicationContext)
        MyCardList.layoutManager = mLayoutManager
        MyCardList.itemAnimator = DefaultItemAnimator()
        MyCardList.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
        dashBoardModelList = ArrayList<DashboardModel>()
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        dashBoardModelList!!.add(DashboardModel("titel", "sybtitle", "url", "icon", "daterecieved"))
        checkStoragePermission1(this)
    }
    private fun checkStoragePermission1(dashBoardActivity: DashBoardActivity) {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        var gpath: String =context?.getExternalFilesDir(null)!!.absolutePath
                        var fullpath = File(gpath)
                        val fileList=imageReaderNew(fullpath)
                        val adapter = MyCardListAdapter(dashBoardModelList!!,dashBoardActivity,fileList)
                        MyCardList.adapter = adapter
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                }
            })
            .onSameThread()
            .check()
    }

    fun imageReaderNew(root: File):ArrayList<File> {
        val fileList: ArrayList<File> = ArrayList()
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".jpg")) {
                    // File absolute path
                    Log.e("downloadFilePath", currentFile.getAbsolutePath())
                    // File Name
                    Log.e("downloadFileName", currentFile.getName())
                    fileList.add(currentFile.absoluteFile)
                }
            }
            Log.w("fileList", "" + fileList.size)
        }
        return fileList
    }

    override fun onStart() {
        super.onStart()
        SharedCardButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
        MyCardButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
        setonClickListner()
    }

    private fun setonClickListner() {
        MyCardButton.setOnClickListener {
            Toast.makeText(this,"card button clicked",Toast.LENGTH_SHORT).show()
            SharedCardButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
            MyCardButton.setBackgroundColor(ContextCompat.getColor(this,R.color.blue))
        }
        SharedCardButton.setOnClickListener {
            Toast.makeText(this,"shared button clicked",Toast.LENGTH_SHORT).show()
            MyCardButton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
            SharedCardButton.setBackgroundColor(ContextCompat.getColor(this,R.color.blue))
        }
        create_new.setOnClickListener {
//            Toast.makeText(this,"create new cards",Toast.LENGTH_SHORT).show()
            checkStoragePermission()
        }

    }


    private fun checkStoragePermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val sendIntent = Intent(this@DashBoardActivity, MainActivity::class.java)
                        startActivityForResult(sendIntent,100)
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                }
            })
            .onSameThread()
            .check()
    }

    override fun onShareButtonCLick(text: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.type = "text/plain"
        Intent.createChooser(sendIntent, "Share via")
        startActivity(sendIntent)
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data!=null) {
            dataString= data?.getStringExtra("result").toString()
            val adapter = MyCardListAdapter(dashBoardModelList!!,this,dataString)
            MyCardList.adapter = adapter
            adapter.notifyDataSetChanged()
        }else{
            val adapter = MyCardListAdapter(dashBoardModelList!!,this,dataString)
            MyCardList.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }*/

}