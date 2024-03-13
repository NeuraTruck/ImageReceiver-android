package neuratruck.taisyu.imagereceiver

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        val client = OkHttpClient()
        val request = Request.Builder().url("ws://YOUR_JETSON_NANO_IP:PORT").build()
        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val imageBytes = Base64.decode(text, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }

        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }
}
