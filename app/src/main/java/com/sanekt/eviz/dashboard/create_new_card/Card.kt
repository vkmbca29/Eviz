package com.sanekt.eviz.dashboard.create_new_card

import android.graphics.Matrix
import com.sanekt.eviz.dashboard.model.ImageModel
import com.sanekt.eviz.dashboard.model.TextModel

open class Card {
    var imageData: ImageModel? = null
    var textData1: TextModel? = null
    var textData2: TextModel? = null
    var textData3: TextModel? = null

    constructor(
        imageData: ImageModel?,
        textData1: TextModel?,
        textData2: TextModel?,
        textData3: TextModel?
    ) {
        this.imageData = imageData
        this.textData1 = textData1
        this.textData2 = textData2
        this.textData3 = textData3
    }
}