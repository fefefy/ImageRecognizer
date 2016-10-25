package projetstl.com.imagerecognizer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static int LOAD_IMAGE = 1;
    String ImageString;

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

    //Function to load images from Gallery
    public void loadImagefromGallery(View view) {

        // Create intent to Open Image applications such as Gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Starting Intent
        startActivityForResult(galleryIntent, LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                ImageString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.Library_ImageView);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory.decodeFile(ImageString));

            } else {
                Toast.makeText(this, "Vous n'avez pas selectionné d'images !", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Problème détecté", Toast.LENGTH_LONG).show();
        }

    }

}

