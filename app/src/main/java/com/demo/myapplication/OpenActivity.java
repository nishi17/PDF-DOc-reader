package com.demo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OpenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_open_pdf_from_assert, bt_open_pdf_from_storage, bt_open_doc_from_assert, open_online_pdf_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        init();

    }

    private void init() {
        bt_open_pdf_from_assert = (Button) findViewById(R.id.bt_open_pdf_from_assert);
        bt_open_pdf_from_assert.setOnClickListener(this);

        bt_open_pdf_from_storage = (Button) findViewById(R.id.bt_open_pdf_from_storage);
        bt_open_pdf_from_storage.setOnClickListener(this);

        bt_open_doc_from_assert = (Button) findViewById(R.id.bt_open_doc_from_assert);
        bt_open_doc_from_assert.setOnClickListener(this);

        open_online_pdf_webview = (Button) findViewById(R.id.open_online_pdf_webview);
        open_online_pdf_webview.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.bt_open_pdf_from_assert:

                Intent intent = new Intent(OpenActivity.this, OpenPDFfromAssert.class);
                startActivity(intent);

                break;

            case R.id.bt_open_pdf_from_storage:

                Intent intent1 = new Intent(OpenActivity.this, OpenPdfFromStorage.class);
                startActivity(intent1);

                break;

            case R.id.open_online_pdf_webview:

                Intent intent2 = new Intent(OpenActivity.this, OpenOnlinePdfWebview.class);
                startActivity(intent2);

                break;

            case R.id.bt_open_doc_from_assert:
                Intent intent3 = new Intent(OpenActivity.this, Open_doc_file_other_app.class);
                startActivity(intent3);

                break;


        }

    }
}
