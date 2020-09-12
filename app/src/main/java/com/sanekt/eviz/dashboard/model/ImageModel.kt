package com.sanekt.eviz.dashboard.model

import android.graphics.Matrix

class ImageModel {
    var matrix1: Matrix? = null
    var uriString: String? = null

    constructor(matrix1: Matrix?, uriString: String?) {
        this.matrix1 = matrix1
        this.uriString = uriString
    }
}