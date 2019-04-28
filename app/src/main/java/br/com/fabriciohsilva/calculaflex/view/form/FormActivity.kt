package br.com.fabriciohsilva.calculaflex.view.form

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.fabriciohsilva.calculaflex.R
import br.com.fabriciohsilva.calculaflex.model.CarData
import br.com.fabriciohsilva.calculaflex.utils.CalculaFlexTracker
import br.com.fabriciohsilva.calculaflex.utils.RemoteConfig
import br.com.fabriciohsilva.calculaflex.view.base.BaseActivity
import br.com.fabriciohsilva.calculaflex.view.login.LoginActivity
import br.com.fabriciohsilva.calculaflex.view.result.ResultActivity
import br.com.fabriciohsilva.calculaflex.watchers.DecimalTextWatcher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : BaseActivity() {

    private lateinit var userId: String
    private lateinit var mAuth: FirebaseAuth
    private val firebaseReferenceNode = "CarData"
    private val defaultClearValueText = "0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        FirebaseAuth.getInstance()
        userId = FirebaseAuth.getInstance().currentUser!!.uid

        listenerFirebaseRealtime()

        etGasPrice.addTextChangedListener(DecimalTextWatcher(etGasPrice))
        etEthanolPrice.addTextChangedListener(DecimalTextWatcher(etEthanolPrice))
        etGasAverage.addTextChangedListener(DecimalTextWatcher(etGasAverage, 1))
        etEthanolAverage.addTextChangedListener(DecimalTextWatcher(etEthanolAverage, 1))

        //loadBanner()


        btCalculate.setOnClickListener {
            saveCarData()
            val proximatela = Intent(this@FormActivity, ResultActivity::class.java)
            proximatela.putExtra("GAS_PRICE", etGasPrice.text.toString().toDouble())
            proximatela.putExtra("ETHANOL_PRICE", etEthanolPrice.text.toString().toDouble())
            proximatela.putExtra("GAS_AVERAGE", etGasAverage.text.toString().toDouble())
            proximatela.putExtra("ETHANOL_AVERAGE", etEthanolAverage.text.toString().toDouble())

            sendDataToAnalytics()

            startActivity(proximatela)

        }//end btCalculate.setOnClickListener


    }//end override fun onCreate


    private fun loadRemoteConfigs() {
        val gasprice: String = RemoteConfig.getFirebaseRemoteConfig().getString("GAS_PRICE")
        val ethanolprice: String = RemoteConfig.getFirebaseRemoteConfig().getString("ETHANOL_PRICE")

        if (etGasPrice.text == null || etGasPrice.text.toString().toDouble() == 0.00) {
            etGasPrice.setText(gasprice)
        }//if (etGasPrice.text == null)

        if (etEthanolPrice.text == null || etEthanolPrice.text.toString().toDouble() == 0.00) {
            etEthanolPrice.setText(ethanolprice)
        }//end if (etEthanolPrice.text == null)
    }//end private fun loadRemoteConfigs


    private fun saveCarData() {
        val carData = CarData(
            etGasPrice.text.toString().toDouble(),
            etEthanolPrice.text.toString().toDouble(),
            etGasAverage.text.toString().toDouble(),
            etEthanolAverage.text.toString().toDouble()
        )//end val carData = CarData

        FirebaseDatabase.getInstance().getReference(firebaseReferenceNode)
            .child(userId)
            .setValue(carData)
            //.addOnCompleteListener{}
    }//end private fun saveCarData

    private fun listenerFirebaseRealtime() {
        DatabaseUtil.getDatabase()
            .getReference(firebaseReferenceNode)
            .child(userId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val carData = dataSnapshot.getValue(CarData::class.java)
                    etGasPrice.setText(carData?.gasPrice.toString())
                    etEthanolPrice.setText(carData?.ethanolPrice.toString())
                    etGasAverage.setText(carData?.gasAverage.toString())
                    etEthanolAverage.setText(carData?.ethanolAverage.toString())
                }//end override fun onDataChange

                override fun onCancelled(p0: DatabaseError) {
                }//end override fun onCancelled(p0: DatabaseError)

            })//end .addValueEventListener

    }//end private fun listenerFirebaseRealtime


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.form_menu, menu)
        return true
    }//end override fun onCreateOptionsMenu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> {
                clearData()
                return true
            }//end R.id.action_clear
            R.id.action_logout -> {
                logout()
                return true
            }//end R.id.action_logout ->
            else -> return super.onOptionsItemSelected(item)
        }//end when (item.itemId)
    }//end override fun onOptionsItemSelected

    private fun clearData() {
        etGasPrice.setText(defaultClearValueText)
        etGasAverage.setText(defaultClearValueText)
        etEthanolPrice.setText(defaultClearValueText)
        etEthanolAverage.setText(defaultClearValueText)
    }//end private fun clearData()

    private  fun logout() {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }//end private  fun logout()


    private fun sendDataToAnalytics() {
        val bundle = Bundle()
        bundle.putString("EVENT_NAME", "CALCULATION")
        bundle.putDouble("GAS_PRICE", etGasPrice.text.toString().toDouble());
        bundle.putDouble("ETHANOL_PRICE", etEthanolPrice.text.toString().toDouble());
        bundle.putDouble("GAS_AVERAGE", etGasAverage.text.toString().toDouble());
        bundle.putDouble("ETHANOL_AVERAGE", etEthanolAverage.text.toString().toDouble());

        CalculaFlexTracker.trackEvent(this, bundle)
    }//end private fun sendDataToAnalytics

    private fun loadBanner() {
        val loginBanner = RemoteConfig.getFirebaseRemoteConfig()
            .getString("banner_image")

        Picasso.get().load(loginBanner).into(ivBanner)
    }


}//end class FormActivity