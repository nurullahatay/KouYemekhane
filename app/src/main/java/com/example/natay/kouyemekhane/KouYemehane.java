package com.example.natay.kouyemekhane;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class KouYemehane extends AppCompatActivity {
    ListView list ;
    private ArrayList<String> listItems=new ArrayList<String>();
    private ProgressDialog progressDialog ;
    private ArrayAdapter<String> dataAdapter;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kou_yemehane);
        new FetchTitle().execute();
        list = (ListView)findViewById(R.id.listView1);

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,listItems);

    }

    private class FetchTitle extends AsyncTask<Void, Void, Void> {
        String title;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(KouYemehane.this);
            progressDialog.setTitle("BAŞLIK");
            progressDialog.setMessage("Başlık Çekiliyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Document doc  = Jsoup.connect("http://sksdb.kocaeli.edu.tr/SKSDB/yemeklistesi").get();    // web siteye bağlantıyı gerçeleştirme
                title = doc.title();  // ilgili sayfanın başlığını almak için
                Element h2=doc.select("h2").first();
                listItems.add(h2.text());
                Element table = doc.select("table").first();
                Elements tr = table.select("tr");
                Elements td= tr.select("td");
                Elements th =tr.select("th");
                ArrayList<String> listItems1=new ArrayList<String>();
                for (int i = 0; i < td.size(); i++) {
                    listItems1.add(td.get(i).text());
                }
                for (int i = 0; i < th.size(); i++) {
                    listItems.add(th.get(i).text() +"\n"+ listItems1.get(i));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            list.setAdapter(dataAdapter);
            progressDialog.dismiss();
        }
    }
}
