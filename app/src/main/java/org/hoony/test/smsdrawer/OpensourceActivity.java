package org.hoony.test.smsdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class OpensourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensource);
        TextView tv = findViewById(R.id.open_text_view);
        tv.setMovementMethod(new ScrollingMovementMethod());
    }
}
