package org.yanncode.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.JsonArray;

import org.yanncode.helloworld.openaiConnector.ApiService;
import org.yanncode.helloworld.openaiConnector.ApiServiceCallback;
import org.yanncode.helloworld.openaiConnector.OpenAIApi;

public class MainActivity extends AppCompatActivity {
    ApiService apiService = new ApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textHello = findViewById(R.id.textMessage);
        textHello.setText("Hi, this is ChatGPT");
    }

    public void onBtnClick(View view) {
        TextView textHello = findViewById(R.id.textMessage);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        apiService.sendApiRequest(editText.getText().toString(), new ApiServiceCallback() {
            @Override
            public void onSuccess(String content) {
                System.out.println(content);
                TextView textHello = findViewById(R.id.textMessage);
                textHello.setText(content);
            }

            @Override
            public void onFailure(Throwable t) {
                System.err.println("Failed to get content: " + t.getMessage());
                TextView textHello = findViewById(R.id.textMessage);
                textHello.setText("Failed to get content: " + t.getMessage());
            }
        });
    }
}