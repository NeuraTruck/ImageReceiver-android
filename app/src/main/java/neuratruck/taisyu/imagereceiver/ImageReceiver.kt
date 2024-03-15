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
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        val client = OkHttpClient()
        val request = Request.Builder().url("ws://192.168.11.6:9090").build()
        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Message received") // 受信したメッセージに関するログ出力
                try {
                    val imageBytes = Base64.decode(text, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    runOnUiThread {
                        imageView.setImageBitmap(bitmap)
                        Log.d("WebSocket", "Image updated") // 画像更新のログ出力
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error decoding image", e) // エラーログ出力
                }
            }

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Log.d("WebSocket", "Connection opened") // WebSocket接続開始のログ出力
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                Log.e("WebSocket", "Error on WebSocket", t) // WebSocketエラーのログ出力
            }
        }

        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }
}
