package com.sanekt.eviz.dashboard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.sanekt.eviz.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_show_text.*

class ShowTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_text)
//        textId.text=intent.getStringExtra("text")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textId.text=Html.fromHtml(intent.getStringExtra("text"), Html.FROM_HTML_MODE_LEGACY);
        } else {
            textId.text=Html.fromHtml(intent.getStringExtra("text"));
        }
        linear.setOnClickListener {
            finish()
        }
    }
}
