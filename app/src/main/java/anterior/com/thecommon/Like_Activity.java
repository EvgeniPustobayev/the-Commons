package anterior.com.thecommon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import anterior.com.thecommon.adapters.LikeAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Like_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_);

        LikeAdapter likeAdapter = new LikeAdapter(this);
        ListView list_like = (ListView)findViewById(R.id.list_like);
        list_like.setAdapter(likeAdapter);

        final ImageButton btn_back =(ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
