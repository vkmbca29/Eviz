package com.sanekt.eviz.dashboard.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.MyCardListAdapter
import com.sanekt.eviz.dashboard.model.DashboardModel
import java.io.File

class CardFragment : Fragment() , MyCardListAdapter.ItemclickListener {

    var ctx: Context? = null

    var dataString =
        "{\"imageData\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":100.0,\"matrixX\":70.06935,\"matrixY\":125.96808,\"uriString\":\"file:///storage/emulated/0/DCIM/Camera/IMG_20200906_094422741.jpg\"},\"textData1\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":400.0,\"matrixX\":261.84467,\"matrixY\":50.95072,\"textColor\":-16777216,\"textName\":\"Radha\",\"typeface\":0},\"textData2\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":400.0,\"matrixX\":256.85162,\"matrixY\":146.9535,\"textColor\":-16777216,\"textName\":\"Lead Manager\",\"typeface\":0},\"textData3\":{\"matrixAngle\":-0.0,\"matrixHeight\":100.0,\"matrixScaleX\":1.0,\"matrixWidth\":400.0,\"matrixX\":218.9043,\"matrixY\":253.94867,\"textColor\":-16777216,\"textName\":\"radha@gmail.com\",\"typeface\":0}}"
    var dashBoardModelList: ArrayList<DashboardModel>? = null

    var SharedCardButton: Button? = null
    var MyCardButton: Button? = null
    var MyCardList: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_card, container, false)

        ctx = activity;

        SharedCardButton = view.findViewById(R.id.sharedCardButton)
        MyCardButton = view.findViewById(R.id.myCardButton)
        MyCardList = view.findViewById(R.id.myCardList)

        SharedCardButton?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.colorPrimaryDark))
        MyCardButton?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.colorPrimaryDark))
        setonClickListner()

        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity)
        MyCardList?.layoutManager = mLayoutManager
        MyCardList?.itemAnimator = DefaultItemAnimator()
        MyCardList?.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
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

        checkStoragePermission1()

        return view
    }


    private fun checkStoragePermission1() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        var gpath: String = context?.getExternalFilesDir(null)!!.absolutePath
                        var fullpath = File(gpath)
                        val fileList = imageReaderNew(fullpath)
                        val adapter = MyCardListAdapter(
                            dashBoardModelList!!,
                            this@CardFragment!!, fileList
                        )
                        MyCardList?.adapter = adapter
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

    fun imageReaderNew(root: File): ArrayList<File> {
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


    private fun setonClickListner() {
        MyCardButton?.setOnClickListener {
            Toast.makeText(activity, "card button clicked", Toast.LENGTH_SHORT).show()
            SharedCardButton?.setBackgroundColor(
                ContextCompat.getColor(
                    ctx!!,
                    R.color.colorPrimaryDark
                )
            )
            MyCardButton?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.blue))
        }
        SharedCardButton?.setOnClickListener {
            Toast.makeText(activity, "shared button clicked", Toast.LENGTH_SHORT).show()
            MyCardButton?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.colorPrimaryDark))
            SharedCardButton?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.blue))
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