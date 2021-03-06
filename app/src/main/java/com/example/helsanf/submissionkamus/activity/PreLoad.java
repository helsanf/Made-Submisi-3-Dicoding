package com.example.helsanf.submissionkamus.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.helsanf.submissionkamus.R;
import com.example.helsanf.submissionkamus.db.KamusHelper;
import com.example.helsanf.submissionkamus.model.KamusModel;
import com.example.helsanf.submissionkamus.res.SharedManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PreLoad extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_load);
        progressBar =  findViewById(R.id.progressbar);
        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void,Integer,Void> {
        final String TAG = LoadData.class.getSimpleName();
        KamusHelper kamusHelper;
        SharedManager sharedManager;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper = new KamusHelper(PreLoad.this);
            sharedManager = new SharedManager(PreLoad.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = sharedManager.getFirstRun();

            if (firstRun) {
                ArrayList<KamusModel> kamusModelsEngToInd = preLoadRaw("Eng");
                ArrayList<KamusModel> kamusModelsIndToEng = preLoadRaw("Ind");

                kamusHelper.open();
                Double progressMaxInsert = 100.0;
                int total_size = kamusModelsEngToInd.size() + kamusModelsIndToEng.size();
                Double progressDiff = (progressMaxInsert - progress) / total_size;

                kamusHelper.beginTransaction();
                try {
                    for (KamusModel model : kamusModelsEngToInd) {
                        kamusHelper.insertTransaction(model, "Eng");
                    }
                    kamusHelper.setTransactionSuccess();
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground: Exception");
                }
                kamusHelper.endTransaction();
                progress += progressDiff;
                publishProgress((int) progress);


                kamusHelper.beginTransaction();
                try {
                    for (KamusModel model : kamusModelsIndToEng) {
                        kamusHelper.insertTransaction(model, "Ind");
                    }
                    kamusHelper.setTransactionSuccess();
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground: Exception");
                }
                kamusHelper.endTransaction();
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();

                sharedManager.setFirstRun(false);
                publishProgress((int) maxprogress);

            } else {
                try {
                    synchronized (this) {
                        this.wait(2000);

                        publishProgress(50);

                        this.wait(2000);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(PreLoad.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }


    public ArrayList<KamusModel> preLoadRaw(String selection) {
        int raw_data;
        if(selection == "Eng"){
            raw_data = R.raw.english_indonesia;
        }else{
            raw_data = R.raw.indonesia_english;
        }
        ArrayList<KamusModel> kamusModels = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(raw_data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                KamusModel kamusModel;

                kamusModel = new KamusModel(splitstr[0], splitstr[1]);
                kamusModels.add(kamusModel);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kamusModels;
    }
}
