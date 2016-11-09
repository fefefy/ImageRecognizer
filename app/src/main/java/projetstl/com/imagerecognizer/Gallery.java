package projetstl.com.imagerecognizer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by serio on 28/10/2016.
 */

public class  Gallery extends Activity{

    private static int LOAD_IMAGE = 1;
    private String ImageString;
    private int READ_PERMISSION = 1;



    //Function to load images from Gallery
    public void loadImagefromGallery () {

        // Create intent to Open Image applications such as Gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        System.out.println("test");

        // Starting Intent


        //startActivityForResult(galleryIntent, LOAD_IMAGE);

    }

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String imageString) {
        ImageString = imageString;
    }

    @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            try {
                // When an Image is picked
                if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    ImageString = cursor.getString(columnIndex);
                    cursor.close();
                    ImageView imgView = (ImageView) findViewById(R.id.Pictures_ImageView);

                    //requests permission to read a files from user's device
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);

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
