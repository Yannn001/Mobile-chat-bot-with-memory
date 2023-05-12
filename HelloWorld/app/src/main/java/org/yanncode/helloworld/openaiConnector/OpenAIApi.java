package org.yanncode.helloworld.openaiConnector;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenAIApi {
    @POST("v1/chat/completions")
    Call<ResponseBody> generateResponse(@Body RequestBody requestBody);
}