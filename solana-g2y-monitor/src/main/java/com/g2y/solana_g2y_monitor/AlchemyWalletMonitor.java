package com.g2y.solana_g2y_monitor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlchemyWalletMonitor {

    private static final String ALCHEMY_API_URL = "https://solana-mainnet.g.alchemy.com/v2/en0zmw_HLWmQmprSKyY0oenuk4sjwzgn";
    private static final Gson gson = new Gson();
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
    	AlchemyWalletMonitor tracker = new AlchemyWalletMonitor();
        tracker.trackTransactions();
    }

    public void trackTransactions() {
        retryRequest(1);
    }

    private void retryRequest(int attempt) {
        if (attempt > MAX_RETRY_ATTEMPTS) {
            System.out.println("Maximum retry attempts reached, giving up.");
            return;
        }

        OkHttpClient httpClient = new OkHttpClient();

        JsonObject payload = new JsonObject();
        payload.addProperty("jsonrpc", "2.0");
        payload.addProperty("id", 1);
        payload.addProperty("method", "getSignaturesForAddress");

        JsonArray params = new JsonArray();
        params.add("G2YxRa6wt1qePMwfJzdXZG62ej4qaTC7YURzuh2Lwd3t");
        payload.add("params", params);

        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(ALCHEMY_API_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                scheduler.schedule(() -> retryRequest(attempt + 1), 5, TimeUnit.SECONDS);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Unexpected code " + response);
                    scheduler.schedule(() -> retryRequest(attempt + 1), 5, TimeUnit.SECONDS);
                    return;
                }

                String responseData = response.body().string();
                handleTransactionsResponse(responseData);
            }
        });
    }

    private void handleTransactionsResponse(String responseData) {
        JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);
        JsonArray transactions = jsonResponse.getAsJsonArray("result");

        for (JsonElement transactionElem : transactions) {
            JsonObject transaction = transactionElem.getAsJsonObject();

            String recipientAddress = transaction.get("signature").getAsString();
            System.out.println("Transaction signature: " + recipientAddress);

        }
    }
}


