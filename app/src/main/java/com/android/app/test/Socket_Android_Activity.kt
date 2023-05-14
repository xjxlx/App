package com.android.app.test

import android.os.Bundle
import android.util.Log
import com.android.app.R
import com.android.apphelper2.utils.LogUtil
import com.android.helper.base.AppBaseActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

class Socket_Android_Activity : AppBaseActivity() {

    companion object {
        private val TAG = "Android端"
    }

    override fun initData(savedInstanceState: Bundle?) {
        ServerThread().start()
    }

    override fun getBaseLayout(): Int {
        return R.layout.activity_socket_android
    }

    class ServerThread : Thread() {
        override fun run() {
            super.run()

            var serverSocket: ServerSocket? = null
            try {
                serverSocket = ServerSocket(19000)
                while (true) {
                    val socket = serverSocket.accept()
                    LogUtil.e(TAG, "接受连接")
                    ClientThread(socket!!).start()
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

    private class ClientThread(val socket: Socket) : Thread() {
        init {
            LogUtil.e(TAG, "当前Socket:$socket")
        }

        override fun run() {
            super.run()

            try {
                val dis = DataInputStream(socket.getInputStream())
                val dos = DataOutputStream(socket.getOutputStream())
                while (true) {
                    val data = dis.readUTF();
                    Log.d(TAG, "收到数据:$data");

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