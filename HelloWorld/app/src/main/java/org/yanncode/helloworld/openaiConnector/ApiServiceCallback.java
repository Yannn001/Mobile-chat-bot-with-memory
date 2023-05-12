package org.yanncode.helloworld.openaiConnector;

public interface ApiServiceCallback {
    void onSuccess(String content);
    void onFailure(Throwable t);
}