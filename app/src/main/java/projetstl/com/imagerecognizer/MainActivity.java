package projetstl.com.imagerecognizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button galleryButton = (Button)findViewById(R.id.Gallery_Button);
        galleryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Gallery button","User clicked the gallery Button");
                Intent intent = new Intent(MainActivity.this,Gallery.class);
                startActivity(intent);
            }
        });

        Button photoButton = (Button)findViewById(R.id.Photo_button);
        photoButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Photo Button","User clicked the photo Button");
                Intent intent = new Intent(MainActivity.this, Photo.class);
                startActivity(intent);
            }
            });
    }
}

