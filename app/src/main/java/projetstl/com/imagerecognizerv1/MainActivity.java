package projetstl.com.imagerecognizerv1;
        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.AssetManager;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Environment;
        import android.os.Handler;
        import android.provider.MediaStore;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

        import org.bytedeco.javacpp.opencv_core;
        import org.bytedeco.javacpp.opencv_features2d;
        import org.bytedeco.javacpp.opencv_nonfree;

        import static org.bytedeco.javacpp.opencv_core.NORM_L2;
        import static org.bytedeco.javacpp.opencv_features2d.*;
        import static org.bytedeco.javacpp.opencv_highgui.*;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    // SIFT keypoint features
    private static final int N_FEATURES = 0;
    private static final int N_OCTAVE_LAYERS = 3;
    private static final double CONTRAST_THRESHOLD = 0.04;
    private static final double EDGE_THRESHOLD = 10;
    private static final double SIGMA = 1.6;

    public opencv_core.Mat img;
    private opencv_nonfree.SIFT SiftDesc;

    private String filePath;

    private ImageView imageView;
    private Bitmap inputImage;

    private File imageFile;
    private String CurrentPhotoPath;
    private static int LOAD_IMAGE = 1;
    private String ImageString;
    private String temp;
    private int READ_PERMISSION = 1;
    private Handler h = new Handler();
    private AssetManager assetManager;
    private ArrayList<String> listImages;


    public static File ToCache(Context context, String Path, String fileName) {
        InputStream input;
        FileOutputStream output;
        byte[] buffer;
        String filePath = context.getCacheDir() + "/" + fileName;
        File file = new File(filePath);
        AssetManager assetManager = context.getAssets();

        try {
            input = assetManager.open(Path);
            buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            output = new FileOutputStream(filePath);
            output.write(buffer);
            output.close();
            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String refFile = "Pepsi_13.jpg";
//        this.filePath = this.ToCache(this, "TestImage" + "/" + refFile, refFile).getPath();

        String test[] = new String[0];
        try {
            test = this.getAssets().list("TrainImage");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream input;
        Bitmap bm;
        String filePaths[] = new String[test.length];
        opencv_core.Mat images[] = new opencv_core.Mat[test.length];
        opencv_features2d.KeyPoint keypoints[] = new opencv_features2d.KeyPoint[test.length];
        for (int i = 0; i<test.length;i++){
            keypoints[i] = new KeyPoint();

        }

        BFMatcher matcher = new BFMatcher();
        DMatchVectorVector matches[] = new DMatchVectorVector[test.length];
        opencv_core.Mat[] descriptors = new opencv_core.Mat[test.length];
        DMatchVectorVector bestMatches[] = new DMatchVectorVector[test.length];


        opencv_core.Mat imgRef = imread(this.ToCache(this,"images/Pepsi_10.jpg", "Pepsi_10.jpg").getPath());
        opencv_features2d.KeyPoint keyPointRef = new opencv_features2d.KeyPoint();
        opencv_core.Mat descriptorsRef = new opencv_core.Mat();

        SiftDesc = new opencv_nonfree.SIFT(N_FEATURES, N_OCTAVE_LAYERS, CONTRAST_THRESHOLD, EDGE_THRESHOLD, SIGMA);


        SiftDesc.detect(imgRef, keyPointRef);
        SiftDesc.compute(imgRef,keyPointRef, descriptorsRef);

        for (int i = 0; i < test.length; i++) {
            matches[i] = new DMatchVectorVector();
            descriptors[i] = new opencv_core.Mat();
            String refFile = test[i];
            filePaths[i] = this.ToCache(this, "TrainImage" + "/" + refFile, refFile).getPath();
            images[i] = imread(filePaths[i]);
            SiftDesc.detect(images[i],keypoints[i]);
            SiftDesc.compute(images[i],keypoints[i],descriptors[i]);
            matcher.knnMatch(descriptorsRef,descriptors[i],matches[i],2);
            bestMatches[i] = refineMatches(matches[i]);
        }





        ImageView imageView = (ImageView) findViewById(R.id.Pictures_ImageView);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        //imageView.setImageBitmap(bitmap);

        Button keypointsButton = (Button) findViewById(R.id.Analyse_button);

        keypointsButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        img = imread(this.filePath);
        SiftDesc = new opencv_nonfree.SIFT(N_FEATURES, N_OCTAVE_LAYERS, CONTRAST_THRESHOLD, EDGE_THRESHOLD, SIGMA);

        //opencv_core.Mat descriptor = new opencv_core.Mat();
        opencv_features2d.KeyPoint keypoints = new opencv_features2d.KeyPoint();
        SiftDesc.detect(img, keypoints);

        Toast.makeText(this, "Nb of detected keypoints:" + keypoints.capacity(), Toast.LENGTH_LONG).show();


            opencv_core.Mat[] images = new opencv_core.Mat[]{

                    imread(this.filePath), imread(this.temp)
            };

        KeyPoint[] key = {new KeyPoint(), new KeyPoint()};
        opencv_core.Mat[] descriptors = new opencv_core.Mat[2];


        //   KeyPointVector[] keyPoints = {new KeyPointVector(), new KeyPointVector()};
        int nFeatures = 0;
        int nOctaveLayers = 3;
        double contrastThreshold = 0.03;
        int edgeThreshold = 10;
        double sigma = 1.6;

        opencv_nonfree.SIFT sift = new opencv_nonfree.SIFT(nFeatures, nOctaveLayers, contrastThreshold, edgeThreshold, sigma);

        // Detect SIFT features and compute descriptors for both images
        for(int i=0;i<=1;i++){

            // Create Surf Keypoint Detector
            sift.detect(images[i], key[i]);
            // Create Surf Extractor
            descriptors[i] = new opencv_core.Mat();
            sift.compute(images[i], key[i], descriptors[i]);
        }

        BFMatcher matcher = new BFMatcher();
        // opencv_features2d.BFMatcher matcher = new
        // opencv_features2d.BFMatcher();
        DMatchVectorVector matches = new DMatchVectorVector();

        long t = System.currentTimeMillis();

        //matcher.knnMatch(descriptors[0],descriptors[1],matches,2);

        DMatchVectorVector bestMatches = refineMatches(matches);

        opencv_core.Mat imageMatches = new opencv_core.Mat();
        byte[] mask = null;

        //drawMatches(images[0], key[0], images[1], key[1], bestMatches, imageMatches);

        Toast.makeText(this, "Nb of detected keypoints:" + keypoints.capacity(), Toast.LENGTH_LONG).show();
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

            } else if (requestCode != LOAD_IMAGE && resultCode == RESULT_OK) {
                Toast.makeText(this, "Voici votre photo", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Vous n'avez pas selectionné d'images !", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Problème détecté", Toast.LENGTH_LONG).show();
        }
    }

    private static DMatchVectorVector refineMatches(DMatchVectorVector oldMatches) {
        // Ratio of Distances
        double RoD = 0.6;
        DMatchVectorVector newMatches = new DMatchVectorVector();

        // Refine results 1: Accept only those matches, where best dist is < RoD
        // of 2nd best match.
        int sz = 0;
        newMatches.resize(oldMatches.size());

        double maxDist = 0.0, minDist = 1e100; // infinity

        for (int i = 0; i < oldMatches.size(); i++) {
            newMatches.resize(i, 1);
            if (oldMatches.get(i, 0).distance() < RoD
                    * oldMatches.get(i, 1).distance()) {
                newMatches.put(sz, 0, oldMatches.get(i, 0));
                sz++;
                double distance = oldMatches.get(i, 0).distance();
                if (distance < minDist)
                    minDist = distance;
                if (distance > maxDist)
                    maxDist = distance;
            }
        }
        newMatches.resize(sz);

        // Refine results 2: accept only those matches which distance is no more
        // than 3x greater than best match
        sz = 0;
        DMatchVectorVector brandNewMatches = new DMatchVectorVector();
        brandNewMatches.resize(newMatches.size());
        for (int i = 0; i < newMatches.size(); i++) {
            // Since minDist may be equal to 0.0, add some non-zero value
            if (newMatches.get(i, 0).distance() <= 3 * minDist) {
                brandNewMatches.resize(sz, 1);
                brandNewMatches.put(sz, 0, newMatches.get(i, 0));
                sz++;
            }
        }
        brandNewMatches.resize(sz);
        float Score = 0;

        //Calculation Score
        for (int i = 0; i < brandNewMatches.size(); i++){
            Score += brandNewMatches.get(i,0).distance();
        }
        Score /= brandNewMatches.size();
        // The Higher score is, the less image match
        System.out.println("Score : " + Score);

        return brandNewMatches;
    }
}