package org.yanncode.helloworld.openaiConnector;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static String conversationHistory = "";
    public static OpenAIApi createOpenAIApi(String apiKey) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    return chain.proceed(chain.request()
                            .newBuilder()
                            .header("Authorization", "Bearer " + apiKey)
                            .header("Content-Type", "application/json")
                            .build());
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OpenAIApi.class);
    }

    public void sendApiRequest(String prompt, ApiServiceCallback callback) {
        conversationHistory += "User: " + prompt + "\n";
        String apiKey = "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"; // Replace with your actual API key
        OpenAIApi openAIApi = this.createOpenAIApi(apiKey);

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", conversationHistory);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.addProperty("model", "gpt-3.5-turbo");
        requestBodyJson.add("messages", messages);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestBodyJson.toString());

        Call<ResponseBody> call = openAIApi.generateResponse(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.i("APIResponse", responseBody);
                        String content = extractInfoFromJsonString(responseBody);
                        callback.onSuccess(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure(e);
                    }
                } else {
                    Log.e("APIError", "Request failed: " + response.code());
                    callback.onFailure(new Exception("Request failed: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("APIError", "Request failed", t);
                callback.onFailure(t);
            }
        });
    }

    private String extractInfoFromJsonString(String str){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(str, JsonObject.class);

        JsonElement contentElement = jsonObject.getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .get("message")
                .getAsJsonObject()
                .get("content");
        String content = contentElement.getAsString();

        //System.out.println("Extracted content: " + content);
        return content;
    }
}