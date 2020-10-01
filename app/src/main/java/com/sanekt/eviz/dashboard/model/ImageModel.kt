package com.sanekt.eviz.dashboard.model

import android.graphics.Matrix
import java.io.Serializable

class ImageModel :Serializable{
    var matrix1: String? = null
    var uriString: String? = null

    constructor(matrix1: String?, uriString: String?) {
        this.matrix1 = matrix1
        this.uriString = uriString
    }
}