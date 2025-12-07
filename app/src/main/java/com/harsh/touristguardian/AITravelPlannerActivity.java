package com.harsh.touristguardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AITravelPlannerActivity extends AppCompatActivity {

    private TextInputEditText destinationEditText;
    private TextInputEditText daysEditText;
    private MaterialButton generateButton;
    private MaterialButton shareButton;
    private ProgressBar progressBar;
    private CardView resultCardView;
    private TextView resultTextView;
    
    // Groq API Configuration
    private static final String GROQ_API_KEY = "";
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    // Updated to use current available models
    // Options: llama-3.3-70b-versatile, llama-3.1-8b-instant, mixtral-8x7b-32768, gemma2-9b-it
    private static final String GROQ_MODEL = "llama-3.3-70b-versatile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_planner);

        // Initialize views
        destinationEditText = findViewById(R.id.destinationEditText);
        daysEditText = findViewById(R.id.daysEditText);
        generateButton = findViewById(R.id.generateButton);
        shareButton = findViewById(R.id.shareButton);
        progressBar = findViewById(R.id.progressBar);
        resultCardView = findViewById(R.id.resultCardView);
        resultTextView = findViewById(R.id.resultTextView);

        // Set up back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("AI Travel Planner");
        }

        // Generate button click
        generateButton.setOnClickListener(v -> generateTravelPlan());

        // Share button click
        shareButton.setOnClickListener(v -> shareTravelPlan());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Generate travel plan using AI
     */
    private void generateTravelPlan() {
        String destination = destinationEditText.getText().toString().trim();
        String days = daysEditText.getText().toString().trim();

        // Validation
        if (destination.isEmpty()) {
            destinationEditText.setError("Please enter a destination");
            destinationEditText.requestFocus();
            return;
        }

        if (days.isEmpty()) {
            daysEditText.setError("Please enter number of days");
            daysEditText.requestFocus();
            return;
        }

        int numDays;
        try {
            numDays = Integer.parseInt(days);
            if (numDays <= 0 || numDays > 30) {
                daysEditText.setError("Please enter days between 1-30");
                daysEditText.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            daysEditText.setError("Please enter a valid number");
            daysEditText.requestFocus();
            return;
        }

        // Show progress, hide result
        progressBar.setVisibility(View.VISIBLE);
        generateButton.setEnabled(false);
        resultCardView.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);

        // Create prompt for AI
        String prompt = createPrompt(destination, numDays);

        // Call Groq API
        callGroqAPI(prompt);
    }

    /**
     * Create prompt for AI
     */
    private String createPrompt(String destination, int days) {
        return "Create a detailed " + days + "-day travel itinerary for " + destination + ", India. " +
                "Include:\n" +
                "1. Day-by-day schedule with specific places to visit\n" +
                "2. Best time to visit each place\n" +
                "3. Estimated travel time between locations\n" +
                "4. Safety tips specific to " + destination + "\n" +
                "5. Local cuisine recommendations\n" +
                "6. Budget estimates (budget, mid-range, luxury options)\n" +
                "7. Important cultural notes and customs\n" +
                "8. Emergency contacts for " + destination + "\n\n" +
                "Format the response in a clear, organized manner with sections for each day. " +
                "Make it practical and tourist-friendly. Use bullet points and clear headings.";
    }

    /**
     * Call Groq API
     */
    private void callGroqAPI(String prompt) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            // Create JSON body for Groq API (OpenAI-compatible format)
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", GROQ_MODEL);
            
            // Create messages array
            JSONArray messagesArray = new JSONArray();
            JSONObject messageObject = new JSONObject();
            messageObject.put("role", "user");
            messageObject.put("content", prompt);
            messagesArray.put(messageObject);
            
            jsonBody.put("messages", messagesArray);
            jsonBody.put("max_tokens", 2048);
            jsonBody.put("temperature", 0.7);
            jsonBody.put("stream", false);

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(GROQ_API_URL)
                    .header("Authorization", "Bearer " + GROQ_API_KEY)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        generateButton.setEnabled(true);
                        Toast.makeText(AITravelPlannerActivity.this, 
                                "Network Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            
                            // Parse Groq API response (OpenAI-compatible format)
                            JSONArray choices = jsonResponse.getJSONArray("choices");
                            if (choices.length() > 0) {
                                JSONObject choice = choices.getJSONObject(0);
                                JSONObject message = choice.getJSONObject("message");
                                String content = message.getString("content");
                                
                                runOnUiThread(() -> {
                                    displayResult(content);
                                });
                            } else {
                                throw new Exception("No choices in response");
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                generateButton.setEnabled(true);
                                Toast.makeText(AITravelPlannerActivity.this, 
                                        "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            generateButton.setEnabled(true);
                            String errorMsg = "API Error: " + response.code();
                            try {
                                JSONObject errorJson = new JSONObject(responseBody);
                                if (errorJson.has("error")) {
                                    JSONObject error = errorJson.getJSONObject("error");
                                    errorMsg = error.optString("message", errorMsg);
                                    if (error.has("type")) {
                                        errorMsg = error.optString("type") + ": " + errorMsg;
                                    }
                                }
                            } catch (Exception e) {
                                // Show first 200 chars of response for debugging
                                errorMsg += "\nResponse: " + responseBody.substring(0, Math.min(200, responseBody.length()));
                            }
                            Toast.makeText(AITravelPlannerActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        });
                    }
                }
            });

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            generateButton.setEnabled(true);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Display the generated travel plan
     */
    private void displayResult(String plan) {
        progressBar.setVisibility(View.GONE);
        generateButton.setEnabled(true);
        resultCardView.setVisibility(View.VISIBLE);
        shareButton.setVisibility(View.VISIBLE);
        
        // Format the text nicely - replace markdown formatting if needed
        String formattedPlan = plan
                .replace("**", "")  // Remove markdown bold
                .replace("*", "•")  // Replace asterisks with bullets
                .replace("\n\n", "\n")  // Clean up extra newlines
                .trim();
        
        resultTextView.setText(formattedPlan);
    }

    /**
     * Share travel plan
     */
    private void shareTravelPlan() {
        String plan = resultTextView.getText().toString();
        if (plan.isEmpty()) {
            Toast.makeText(this, "No plan to share", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Travel Plan - " + destinationEditText.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, plan);
        startActivity(Intent.createChooser(shareIntent, "Share Travel Plan"));
    }
}
