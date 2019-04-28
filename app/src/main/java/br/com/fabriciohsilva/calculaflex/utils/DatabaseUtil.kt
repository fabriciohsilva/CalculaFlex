import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DatabaseUtil {
    companion object {
        private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        init {
            firebaseDatabase.setPersistenceEnabled(true)
        }//init

        fun getDatabase() : FirebaseDatabase {
            return firebaseDatabase
        }//end fun getDatabase


        fun saveToken(token: String?) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                FirebaseDatabase.getInstance().getReference("UsersTokens")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(token)
            }//end if (FirebaseAuth.getInstance().currentUser != null)
        }//end fun saveToken

    }//end companion object



}//end class DatabaseUtil