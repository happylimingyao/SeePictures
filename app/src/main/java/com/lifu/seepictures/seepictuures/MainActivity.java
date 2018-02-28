package com.lifu.seepictures.seepictuures;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lifu.seepictures.seepictuures.bean.Sister;
import com.lifu.seepictures.seepictuures.imageloader.PictureLoader;
import com.lifu.seepictures.seepictuures.network.SisterApi;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btnShow;
    private ImageView ivShow;
    private Button btnRefresh;
    private ArrayList<Sister> data;
    private int curPos = 0;
    private int page = 1;
    private PictureLoader loader;
    private SisterApi sisterApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sisterApi = new SisterApi();
        loader = new PictureLoader();
        initData();
        initUI();
    }

    private void initUI() {
        btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnShow = (Button) findViewById(R.id.btn_show);
        ivShow = (ImageView) findViewById(R.id.iv_show);
        btnShow.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
    }

    private void initData() {
        data = new ArrayList<>();
        new SisterTask(page).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
                if (data!=null&&!data.isEmpty()){
                    if (curPos>9){
                        curPos=0;
                    }
                }
                loader.load(ivShow, data.get(curPos).getUrl());
                curPos++;
                break;
            case R.id.btn_refresh:
                page++;
                new SisterTask(page).execute();
                curPos=0;
                break;
            default:
                break;
        }
    }

     private class SisterTask extends AsyncTask<Void,Void,ArrayList<Sister>>{
        private int page;
        public SisterTask(int page) {
            this.page=page;
        }

         @Override
         protected ArrayList<Sister> doInBackground(Void... voids) {
             return sisterApi.fetchSister(10,page);
         }

         @Override
        protected void onPostExecute(ArrayList<Sister> sisters) {
            super.onPostExecute(sisters);
            data.clear();
            data.addAll(sisters);
        }
    }
}
