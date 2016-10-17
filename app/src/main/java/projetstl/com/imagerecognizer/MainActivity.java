package projetstl.com.imagerecognizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button libraryButton = (Button)findViewById(R.id.Library_Button);


        libraryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Library button","User clicked the library Button");
                setContentView(R.layout.library_layout);
                //TODO ajout link vers classe Library.java
            }
        });

        Button photoButton = (Button)findViewById(R.id.Photo_button);
        photoButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Photo Button","User clicked the photo Button");
                setContentView(R.layout.photo_layout);

                //TODO ajout link vers classe Photo.java
            }
            });
    }

}

