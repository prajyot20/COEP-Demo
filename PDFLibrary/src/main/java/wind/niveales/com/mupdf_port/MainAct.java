package wind.niveales.com.mupdf_port;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.artifex.mupdfdemo.R;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

    // Example of a call to a native method
    TextView tv = (TextView) findViewById(R.id.sample_text);
    tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'mupdf' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'mupdf' library on application startup.
    static {
        System.loadLibrary("mupdf");
    }
}
