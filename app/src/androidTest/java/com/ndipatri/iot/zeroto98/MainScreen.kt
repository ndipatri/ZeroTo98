package com.ndipatri.iot.zeroto98

import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.kaspersky.kaspresso.screens.KScreen

object MainScreen : KScreen<MainScreen>() {

    override val layoutId: Int = R.layout.activity_main
    override val viewClass: Class<*> = MainActivity::class.java

    val textView = KTextView { withId(R.id.textview_first) }
    val button = KButton { withId(R.id.button_first) }
}