package com.sanekt.eviz.dashboard.model

import android.graphics.Matrix
import android.graphics.Typeface

class TextModel {
    var matrix1: String? = null
    var textName: String? = null
    var textColor: Int? = null
    var typeface: Int? = null

    constructor(matrix1: String?, textName: String?, textColor: Int?, typeface: Int?) {
        this.matrix1 = matrix1
        this.textName = textName
        this.textColor = textColor
        this.typeface = typeface
    }
}