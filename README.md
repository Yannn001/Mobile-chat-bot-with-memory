# Mobile-chat-bot-with-memory

This Android application uses the OpenAI GPT-3.5-turbo model to generate responses to user input. It uses OkHttp and Retrofit libraries for networking and Gson for JSON manipulation.

## Installation

1. Clone the repository.
2. Open in Android Studio.
3. Make sure to replace `apiKey` with your actual OpenAI API key.

## Package: org.yanncode.helloworld.openaiConnector

### ApiService Class

The `ApiService` class is where the main logic of the application resides. Here is a brief overview of the methods in this class.

#### createOpenAIApi(String apiKey)

This method creates an instance of the OpenAIApi interface using Retrofit. It adds necessary headers to each request using an OkHttp interceptor.

#### sendApiRequest(String prompt, ApiServiceCallback callback)

This method sends a request to the OpenAI API. The request includes the user's input as a message. It also updates the `conversationHistory` string which keeps track of the whole conversation.

In case of a successful response, it extracts the generated message from the response and passes it to the `onSuccess` method of the provided callback. If the request fails, it calls the `onFailure` method of the callback.

#### extractInfoFromJsonString(String str)

This method extracts the generated message from the OpenAI API response. It uses Gson to parse the JSON response.

## How to Use

The `sendApiRequest` method of the `ApiService` class is what you'll mainly interact with. You need to provide the user's input and a callback as arguments. The callback will be used to handle the response from the API.
