import android.app.ProgressDialog
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomProgressDialog(context: Context, titulo: String, mensaje: String) :
    ProgressDialog(context) {
    init {
        setTitle(titulo)
        setMessage(mensaje)
        setCancelable(false) // Opcional, si deseas que no se pueda cancelar
    }

    override fun show() {
        CoroutineScope(Dispatchers.Main).launch {
            super.show()
        }

    }

    override fun dismiss() {
        CoroutineScope(Dispatchers.Main).launch {
            super.dismiss()
        }
    }


}
