package br.com.fabriciohsilva.calculaflex.view.result

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.fabriciohsilva.calculaflex.R
import br.com.fabriciohsilva.calculaflex.extensions.format
import br.com.fabriciohsilva.calculaflex.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : BaseActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_result)

            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);

            if (intent.extras == null) {
                Toast.makeText(this, "Não foi possível realizar a operação", Toast.LENGTH_SHORT).show()
            } else {
                calculate()
            }

        }

    private fun calculate() {
        val gasPrice = intent.extras.getDouble("GAS_PRICE", 0.0)
        val ethanolPrice = intent.extras.getDouble("ETHANOL_PRICE", 0.0)
        val gasAverage = intent.extras.getDouble("GAS_AVERAGE", 0.0)
        val ethanolAverage = intent.extras.getDouble("ETHANOL_AVERAGE", 0.0)
        val performanceOfMyCar = ethanolAverage / gasAverage
        val priceOfFuelIndice = ethanolPrice / gasPrice

        if (priceOfFuelIndice <= performanceOfMyCar) {
            tvSuggestion.text = getString(R.string.ethanol)
        } else {
            tvSuggestion.text = getString(R.string.gasoline)
        }

        tvEthanolAverageResult.text = (ethanolPrice / ethanolAverage).format(2)
        tvGasAverageResult.text = (gasPrice / gasAverage).format(2)
        tvFuelRatio.text = getString(R.string.label_fuel_ratio,performanceOfMyCar.format(2))

    }//end private fun calculate()

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }//end override fun onSupportNavigateUp()


}//end class ResultActiviity