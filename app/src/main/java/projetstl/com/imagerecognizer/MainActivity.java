package projetstl.com.imagerecognizer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static int LOAD_IMAGE = 1;
    private String ImageString;
    private int READ_PERMISSION = 1;

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

