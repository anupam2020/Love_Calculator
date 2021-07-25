package com.example.lovecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextInputEditText name,pname;
    TextInputLayout name_layout,pname_layout;
    Button submit;
    TextView text,text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.textInputEditText1);
        pname=findViewById(R.id.textInputEditText2);

        name_layout=findViewById(R.id.textInputLayout1);
        pname_layout=findViewById(R.id.textInputLayout2);

        submit=findViewById(R.id.btn_submit);

        text=findViewById(R.id.textView);
        text2=findViewById(R.id.textView2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n=name.getText().toString();
                String p=pname.getText().toString();

                if(n.isEmpty() || p.isEmpty())
                {
                    DynamicToast.makeWarning(MainActivity.this,"Field cannot be empty!",3000).show();
                }
                else
                {
                    try {
                        calculateLove(n,p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void calculateLove(String n, String p) throws IOException {

//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://love-calculator.p.rapidapi.com/getPercentage?fname=John&sname=Alice")
//                .get()
//                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
//                .addHeader("x-rapidapi-host", "love-calculator.p.rapidapi.com")
//                .build();
//
//        Response response = client.newCall(request).execute();

        OkHttpClient client = new OkHttpClient();

        String url="https://love-calculator.p.rapidapi.com/getPercentage?fname="+n+"&sname="+p;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
                .addHeader("x-rapidapi-host", "love-calculator.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {
                    String resp=response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject=new JSONObject(resp);

                                String per=jsonObject.getString("percentage");
                                String res=jsonObject.getString("result");

                                text.setText(per);
                                text2.setText(res);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

            }
        });



    }
}