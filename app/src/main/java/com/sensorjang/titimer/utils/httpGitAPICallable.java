package com.sensorjang.titimer.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.Callable;

public class httpGitAPICallable implements Callable<String> {
    String url;
    public void setUrl(String url){
        this.url = url;
    }
    public String call() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();//一个全局的执行者，所有请求动作交由它执行。
        Request.Builder builder=new Request.Builder();//通过Builder来构造request。
        Request request=builder.get().url(url).build();//builder到build返回request
        Call call = okHttpClient.newCall(request);//将request传入
        Response response=call.execute();
        return response.body().string();
    }
}
