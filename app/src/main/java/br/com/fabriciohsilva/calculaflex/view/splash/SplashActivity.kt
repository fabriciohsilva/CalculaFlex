package br.com.fabriciohsilva.calculaflex.view.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import br.com.fabriciohsilva.calculaflex.BuildConfig
import br.com.fabriciohsilva.calculaflex.R
import br.com.fabriciohsilva.calculaflex.utils.RemoteConfig
import br.com.fabriciohsilva.calculaflex.view.base.BaseActivity
import br.com.fabriciohsilva.calculaflex.view.form.FormActivity
import br.com.fabriciohsilva.calculaflex.view.login.LoginActivity
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    private val TEMPO_AGUARDO_SPLASHSCREEN = 3500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Crashlytics.getInstance().crash()

        val preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val isFirstOpen = preferences.getBoolean("open_first", true)

        RemoteConfig.remoteConfigFetch()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    RemoteConfig.getFirebaseRemoteConfig().activateFetched()
                    val minVersionApp = RemoteConfig.getFirebaseRemoteConfig()
                        .getLong("min_version_app")
                        .toInt()
                    if (minVersionApp <= BuildConfig.VERSION_CODE)
                        if (isFirstOpen) {
                            showLogin()
                        } else {
                            markAppAlreadyOpen(preferences)
                            showSplash()
                        }//end if (isFirstOpen)
                    else
                        showAlertMinVersion()
                } else
                    if (isFirstOpen) {
                        showLogin()
                    } else {
                        markAppAlreadyOpen(preferences)
                        showSplash()
                    }//end if (isFirstOpen)
            }

    }//end override fun onCreate

    private fun markAppAlreadyOpen(preferences: SharedPreferences) {
        val editor = preferences.edit()
        editor.putBoolean("open_first", false)
        editor.apply()
    }//end private fun markAppAlreadyOpen

    private fun showLogin() {
        val nextScreen = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(nextScreen)
        finish()
    }//end private fun showLogin()

    private fun showSplash() {
        //Carrega a animacao
        val anim = AnimationUtils.loadAnimation(this, R.anim.animacao_splash)
        anim.reset()
        ivLogo.clearAnimation()
        //Roda a animacao
        ivLogo.startAnimation(anim)
        //Chama a próxima tela após 3,5 segundos definido na SPLASH_DISPLAY_LENGTH
        Handler().postDelayed({
            val proximaTela = Intent(this@SplashActivity, FormActivity::class.java)
            startActivity(proximaTela)
            finish()
        }, TEMPO_AGUARDO_SPLASHSCREEN)
    }//end private fun carregar


    private fun showAlertMinVersion() {
        AlertDialog.Builder(this)
            .setTitle("Ops")
            .setMessage("Você esta utilizando uma versão muito antiga do aplicativo. Deseja atualizar?")

            .setPositiveButton("Sim") { dialog, which ->
                var intent: Intent
                try {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                    startActivity(intent)
                } catch (e: android.content.ActivityNotFoundException) {
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                    startActivity(intent)
                }
            }

            .setNegativeButton("Não") { dialog, which ->
                finish()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    private fun continueApp() {
        val preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val isFirstOpen = preferences.getBoolean("open_first", true)

        if (isFirstOpen) {
            showLogin()
        } else {
            markAppAlreadyOpen(preferences)
            showSplash()
        }
    }



}//end class SplashActivity