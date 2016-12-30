package projetstl.com.imagerecognizer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.DescriptorExtractor;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_nonfree.SIFT;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;
import static org.bytedeco.javacpp.opencv_highgui.imread;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;


public class  Gallery extends Activity {

    private static int LOAD_IMAGE = 1;
    private String ImageString;
    private int READ_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);

    }

    public void loadImageFromGallery(View view) {

        ImageView imgView = (ImageView) findViewById(R.id.Pictures_ImageView);

 /*       Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, LOAD_IMAGE);*/

        System.out.println("Couille");

      /*  Mat mImage = imread("/storage/emulated/0/DCIM/Camera/Starbucks_Logo.jpg", 1);
*/


        Mat mImage = imread("data/church01.jpg", 1);

        int nFeatures = 0;
        int nOctaveLayers = 3;
        double contrastThreshold = 0.03;
        int edgeThreshold = 10;
        double sigma = 1.6;

        // Create Surf Keypoint Detector
        SIFT siftFeatureDetector = new SIFT(nFeatures, nOctaveLayers, contrastThreshold, edgeThreshold, sigma);

        // Create Surf Extractor
        DescriptorExtractor siftDescriptorExtractor = DescriptorExtractor.create("SIFT");

        KeyPoint keypoints = new KeyPoint();

        Mat descriptors = new Mat();

        // Read the image


        // opencv_imgproc.cvtColor(mImage, mImage, opencv_imgproc.COLOR_BGR2GRAY);

        // Process it
        siftFeatureDetector.detect(mImage, keypoints);

        siftDescriptorExtractor.compute(mImage, keypoints, descriptors);

        Mat outImage = new opencv_core.Mat();
        drawKeypoints(mImage, keypoints, outImage, new opencv_core.Scalar(0, 255, 255, 0), DrawMatchesFlags.DRAW_RICH_KEYPOINTS);

        //show(outImage, "out");



        waitKey(0);

    }








    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

/*
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

*/




               // Mat mImage = imread("/storage/emulated/0/DCIM/Camera/Starbucks_Logo.jpg", 1);




// mImage = matrice
// outImage = matrice

                // Set the Image in ImageView after decoding the String


                //imgView.setImageBitmap(BitmapFactory.decodeFile(ImageString));


            } else {
                Toast.makeText(this, "Vous n'avez pas selectionné d'images !", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Problème détecté", Toast.LENGTH_LONG).show();
        }
    }


}
