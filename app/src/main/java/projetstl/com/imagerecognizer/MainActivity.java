package projetstl.com.imagerecognizer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {


    public Gallery gal = new Gallery();
    private static int LOAD_IMAGE = 1;
    private String ImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button galleryButton = (Button)findViewById(R.id.Gallery_Button);

        galleryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Log.i("Gallery button","User clicked the gallery Button");
                //setContentView(R.layout.gallery_layout);

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                String test = gal.getImageString();



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

