package br.com.fabriciohsilva.calculaflex.utils

import android.app.Activity
import br.com.fabriciohsilva.calculaflex.view.form.FormActivity
import br.com.fabriciohsilva.calculaflex.view.login.LoginActivity
import br.com.fabriciohsilva.calculaflex.view.result.ResultActivity
import br.com.fabriciohsilva.calculaflex.view.signup.SignUpActivity
import br.com.fabriciohsilva.calculaflex.view.splash.SplashActivity

class ScreenMap {

    companion object {
        val SCREEN_NOT_TRACKING = "SCREEN_NOT_TRACKING"

        private fun getScreenNameBy(className: String): String {
            val screensNames = getScreenNames()
            return if (screensNames[className] == null) SCREEN_NOT_TRACKING else screensNames[className]!!
        }//end private fun getScreenNameBy

        fun getScreenNameBy(activty: Activity): String {
            return getScreenNameBy(activty::class.java.simpleName)
        }//end fun getScreenNameBy

        fun getClassName(screenName: String): String {
            val screenNames = getScreenNames()
            for (o in screenNames.keys) {
                if (screenNames[o] == screenName) {
                    return o
                }
            }
            return ""
        }//end fun getClassName

        private fun getScreenNames(): Map<String, String> {
            return mapOf(
                //Login
                Pair(FormActivity::class.java.simpleName, "Cadastro_Formulario"),
                Pair(LoginActivity::class.java.simpleName, "Login_Usuario"),
                Pair(ResultActivity::class.java.simpleName, "Cálculo_Resultado"),
                Pair(SignUpActivity::class.java.simpleName, "Criacao_Usuario"),
                Pair(SplashActivity::class.java.simpleName, "Splash")
            )
        }//end private fun getScreenNames

    }//end companion object

}//end class ScreenMap