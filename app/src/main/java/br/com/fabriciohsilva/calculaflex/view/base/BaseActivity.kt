package br.com.fabriciohsilva.calculaflex.view.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.fabriciohsilva.calculaflex.R
import br.com.fabriciohsilva.calculaflex.utils.CalculaFlexTracker
import br.com.fabriciohsilva.calculaflex.utils.ScreenMap

open class BaseActivity : AppCompatActivity() {

    open fun getScreenName(): String {
        return ScreenMap.getScreenNameBy(this)
    }

    override fun onStart() {
        super.onStart()
        CalculaFlexTracker.trackScreen(this, getScreenName())
    }
}
