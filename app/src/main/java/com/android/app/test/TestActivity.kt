package com.android.app.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.app.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
}