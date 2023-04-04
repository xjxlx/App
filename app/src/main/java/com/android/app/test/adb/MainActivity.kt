package com.android.app.test.adb

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.app.R
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private val TAG = "Android端"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_adb)
        ServerThread().start()
    }

    private inner class ServerThread : Thread() {

        override fun run() {
            var serverSocket: ServerSocket? = null
            try {
                serverSocket = ServerSocket(19000)
                while (true) {
                    val socket = serverSocket.accept()
                    Log.d(TAG, "接受连接")
                    ClientThread(socket).start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private inner class ClientThread(private val socket: Socket) : Thread() {
        init {
            Log.d(TAG, "当前Socket:$socket")
        }

        override fun run() {
            try {
                val dis = DataInputStream(socket.getInputStream())
                val dos = DataOutputStream(socket.getOutputStream())
                while (true) {
                    val data = dis.readUTF()
                    Log.d(TAG, "收到数据:$data")

                    //回写给客户端。
                    val s = "手机时间:" + System.currentTimeMillis()
                    dos.writeUTF(s)
                    dos.flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    socket.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}