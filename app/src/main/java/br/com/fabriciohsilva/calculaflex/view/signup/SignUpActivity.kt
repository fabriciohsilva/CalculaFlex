package br.com.fabriciohsilva.calculaflex.view.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.fabriciohsilva.calculaflex.R
import br.com.fabriciohsilva.calculaflex.model.User
import br.com.fabriciohsilva.calculaflex.view.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        btCreate.setOnClickListener {
            mAuth.createUserWithEmailAndPassword(
                inputEmail.text.toString(),
                inputPassword.text.toString()
            )//end mAuth.createUserWithEmailAndPassword
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    saveInRealTimeDataBase()
                } else {
                    Toast.makeText(this@SignUpActivity, it.exception?.message, Toast.LENGTH_SHORT).show()
                }//end if (it.isSucessfull)
            }//end .addOnCompleteListener

        }//end btCreate.setOnClickListener

    }//end override fun onCreate


    private fun saveInRealTimeDataBase(){
        val user = User(inputName.text.toString(), inputEmail.text.toString(), inputPhone.text.toString())

        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(this, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("email", inputEmail.text.toString())
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao criar o usuário", Toast.LENGTH_SHORT).show()
                }//end if (it.isSuccessful)
            }//end .addOnCompleteListener

    }//end private fun saveInRealTimeDataBase

}//end class SignUpActivity