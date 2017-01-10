package projetstl.com.imagerecognizer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;

import static android.R.attr.bitmap;
import static android.R.attr.start;

public class MainActivity extends Activity {

     static {
        System.loadLibrary("opencv_java");
        System.loadLibrary("nonfree");
    }

    private ImageView imageView;
    private Bitmap inputImage;
    private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);

    private File imageFile;
    private String CurrentPhotoPath;
    private static int LOAD_IMAGE = 1;
    private String ImageString;
    private int READ_PERMISSION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    //Function to use Camera
    public void TakePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "LogoRecognizer.jpg");
        Uri temp_uri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, temp_uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);
        System.out.println("Appareil Photo ouvert");
    }

    //Function to Add taken image to gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File imageFile = new File(CurrentPhotoPath);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void LoadImageFromGallery(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, LOAD_IMAGE);
        System.out.println("Gallery ouverte");
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
                setContentView(R.layout.activity_main);
                ImageView imgView = (ImageView) findViewById(R.id.Pictures_ImageView);

                //requests permission to read a files from user's device
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);

                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory.decodeFile(ImageString));

            } else if (requestCode != LOAD_IMAGE && resultCode == RESULT_OK){
                Toast.makeText(this, "Voici votre photo", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Vous n'avez pas selectionné d'images !", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Problème détecté", Toast.LENGTH_LONG).show();
        }


    }

    public void Analyse(View view) {

        setContentView(R.layout.keypointmatching);
        imageView = (ImageView) this.findViewById(R.id.Analyse_ImageView);
        inputImage = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        KeypointAnalyse();
    }

    public void KeypointAnalyse() {
        Mat rgba = new Mat();
        Utils.bitmapToMat(inputImage, rgba);
        MatOfKeyPoint keyPoints = new MatOfKeyPoint();
        Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_RGBA2GRAY);
        detector.detect(rgba, keyPoints);
        Features2d.drawKeypoints(rgba, keyPoints, rgba);
        Utils.matToBitmap(rgba, inputImage);
        imageView.setImageBitmap(inputImage);
    }
}
