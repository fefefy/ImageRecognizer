package projetstl.com.imagerecognizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class Photo extends Activity {

    private File imageFile;
    private String CurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_layout);
    }

    //Function to use Camera
    public void process(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "ImageRecognizer.jpg");
        Uri temp_uri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, temp_uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 0) {
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        if (imageFile.exists()) {
                            //Get absolute path from image
                            CurrentPhotoPath = imageFile.getAbsolutePath();
                            Toast.makeText(this, "The file was saved at " + CurrentPhotoPath, Toast.LENGTH_LONG).show();
                            //return to main activity layout
                            setContentView(R.layout.activity_main);
                            ImageView imgView = (ImageView) findViewById(R.id.Pictures_ImageView);
                            //Rotate image
                            imgView.setRotation(90);
                            imgView.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(imageFile)));
                            //Add image to gallery
                            galleryAddPic();
                        } else {
                            Toast.makeText(this, "There was an error saving the file", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Problème détecté", Toast.LENGTH_LONG).show();
        }
    }

    //Function to Add taken image to gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File imageFile = new File(CurrentPhotoPath);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}


