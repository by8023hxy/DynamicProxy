package com.baiyu.designpattern.dynamicproxydemo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.baiyu.designpattern.dynamicproxydemo.proxy.ApiManager
import com.baiyu.designpattern.dynamicproxydemo.proxy.CallBack

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.proxy_test).setOnClickListener {
            ApiManager.getInstance(this)
                .test("动态代理", "1010", "baiyu")
                .execute(object : CallBack<String>() {
                    override fun onSuccess(result: String) {
                        Log.d("DynamicProxy", "result==$result")
                    }

                    override fun onError(errorMsg: String?, errorCode: String?) {
                        Log.d("DynamicProxy", "errorMsg==$errorMsg  errorCode==$errorCode")
                    }
                })
        }
    }
}