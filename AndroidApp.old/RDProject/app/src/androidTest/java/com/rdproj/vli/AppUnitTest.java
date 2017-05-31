package com.rdproj.vli;

import android.os.Handler;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.rdproj.vli.rdproject.comm.GestureUnit;
import com.rdproj.vli.rdproject.comm.MyoData;
import com.rdproj.vli.rdproject.comm.RestClient;

import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AppUnitTest {



    @Test
    public void sendMyoData() throws InterruptedException {
        /* CRREATE LEFT MYO DATA */
        MyoData leftMyo = new MyoData();
        leftMyo.accX = -10 + (int)(Math.random() * 10);
        leftMyo.accY = -10 + (int)(Math.random() * 10);
        leftMyo.accZ = -10 + (int)(Math.random() * 10);
        leftMyo.oriW = -10 + (int)(Math.random() * 10);
        leftMyo.oriX = -10 + (int)(Math.random() * 10);
        leftMyo.oriY = -10 + (int)(Math.random() * 10);
        leftMyo.oriZ = -10 + (int)(Math.random() * 10);
        leftMyo.gyroX = -10 + (int)(Math.random() * 10);
        leftMyo.gyroY = -10 + (int)(Math.random() * 10);
        leftMyo.gyroZ = -10 + (int)(Math.random() * 10);
        leftMyo.emg1 = -10 + (int)(Math.random() * 10);
        leftMyo.emg2 = -10 + (int)(Math.random() * 10);
        leftMyo.emg3 = -10 + (int)(Math.random() * 10);
        leftMyo.emg4 = -10 + (int)(Math.random() * 10);
        leftMyo.emg5 = -10 + (int)(Math.random() * 10);
        leftMyo.emg6 = -10 + (int)(Math.random() * 10);
        leftMyo.emg7 = -10 + (int)(Math.random() * 10);
        leftMyo.emg8 = -10 + (int)(Math.random() * 10);

        /* CRREATE RIGHT MYO DATA */
        MyoData rightMyo = new MyoData();
        rightMyo.accX = -10 + (int)(Math.random() * 10);
        rightMyo.accY = -10 + (int)(Math.random() * 10);
        rightMyo.accZ = -10 + (int)(Math.random() * 10);
        rightMyo.oriW = -10 + (int)(Math.random() * 10);
        rightMyo.oriX = -10 + (int)(Math.random() * 10);
        rightMyo.oriY = -10 + (int)(Math.random() * 10);
        rightMyo.oriZ = -10 + (int)(Math.random() * 10);
        rightMyo.gyroX = -10 + (int)(Math.random() * 10);
        rightMyo.gyroY = -10 + (int)(Math.random() * 10);
        rightMyo.gyroZ = -10 + (int)(Math.random() * 10);
        rightMyo.emg1 = -10 + (int)(Math.random() * 10);
        rightMyo.emg2 = -10 + (int)(Math.random() * 10);
        rightMyo.emg3 = -10 + (int)(Math.random() * 10);
        rightMyo.emg4 = -10 + (int)(Math.random() * 10);
        rightMyo.emg5 = -10 + (int)(Math.random() * 10);
        rightMyo.emg6 = -10 + (int)(Math.random() * 10);
        rightMyo.emg7 = -10 + (int)(Math.random() * 10);
        rightMyo.emg8 = -10 + (int)(Math.random() * 10);

        /* CREATE GESTURE UNIT */
        GestureUnit gestureUnit = new GestureUnit();
        gestureUnit.timestamp = System.currentTimeMillis()/1000;
        gestureUnit.leftMyo = leftMyo;
        gestureUnit.rightMyo = rightMyo;
        gestureUnit.gestureName = "Dummy";

        /* SEND DATA */
        RestClient restClient = new RestClient();
        restClient.setGestureUnitData(gestureUnit);
        /*
        for(int i = 0; i < 100; i++){
            Thread.sleep(50);
        }
        */
    }

    public void getApiStatus(){
        RestClient restClient = new RestClient();
        final Handler handler = new Handler();
        restClient.getApiStatus(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                final boolean status = response.body();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(".", ".");
                    }
                });
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

}
