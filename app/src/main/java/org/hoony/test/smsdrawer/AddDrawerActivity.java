package org.hoony.test.smsdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.hoony.test.smsdrawer.model.DrawerModel;

import static org.hoony.test.smsdrawer.adapter.SideAdapter.EXTRA_DRAWER_MODEL;

public class AddDrawerActivity extends AppCompatActivity {
    private EditText mEditDrawerName;
    private EditText mEditKeyword1;
    private EditText mEditKeyword2;
    private EditText mEditKeyword3;
    private EditText mEditNumber1;
    private EditText mEditNumber2;
    private EditText mEditNumber3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drawer);
        getSupportActionBar().setTitle("서랍 추가");

        mEditDrawerName = findViewById(R.id.editDrawerName);
        mEditKeyword1 = findViewById(R.id.editKeyword1);
        mEditKeyword2 = findViewById(R.id.editKeyword2);
        mEditKeyword3 = findViewById(R.id.editKeyword3);
        mEditNumber1 = findViewById(R.id.editNumber1);
        mEditNumber2 = findViewById(R.id.editNumber2);
        mEditNumber3 = findViewById(R.id.editNumber3);
    }

    public void addDrawer(View v) {
        if (mEditDrawerName.getText().toString().isEmpty()) {
            Toast.makeText(this, "서랍 이름을 지정해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEditKeyword1.getText().toString().isEmpty()
                && mEditKeyword2.getText().toString().isEmpty()
                && mEditKeyword3.getText().toString().isEmpty()
                && mEditNumber1.getText().toString().isEmpty()
                && mEditNumber2.getText().toString().isEmpty()
                && mEditNumber3.getText().toString().isEmpty()) {
            Toast.makeText(this, "하나 이상의 키워드나 번호를 지정해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        DrawerModel model = new DrawerModel(mEditDrawerName.getText().toString());
        if (!mEditKeyword1.getText().toString().isEmpty())
            model.addKeyword(mEditKeyword1.getText().toString());
        if (!mEditKeyword2.getText().toString().isEmpty())
            model.addKeyword(mEditKeyword2.getText().toString());
        if (!mEditKeyword3.getText().toString().isEmpty())
            model.addKeyword(mEditKeyword3.getText().toString());
        if (!mEditNumber1.getText().toString().isEmpty())
            model.addNumber(mEditNumber1.getText().toString());
        if (!mEditNumber2.getText().toString().isEmpty())
            model.addNumber(mEditNumber2.getText().toString());
        if (!mEditNumber3.getText().toString().isEmpty())
            model.addNumber(mEditNumber3.getText().toString());
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DRAWER_MODEL, model);
        Log.i("Model", model.getName());
        Log.i("Model", model.getKeywords().toString());
        Log.i("Model", model.getNumbers().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
