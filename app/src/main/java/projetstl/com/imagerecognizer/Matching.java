package projetstl.com.imagerecognizer;

import android.app.Activity;
import android.os.Bundle;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_features2d.DescriptorExtractor;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_nonfree.SIFT;

import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;
import static org.bytedeco.javacpp.opencv_highgui.imread;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;


public class Matching extends Activity{

    private static int LOAD_IMAGE = 1;
    private String ImageString;
    private int READ_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(Matching_view);

    }


    protected void processing() {

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
        drawKeypoints(mImage, keypoints, outImage, new opencv_core.Scalar(0, 255, 255, 0), opencv_features2d.DrawMatchesFlags.DRAW_RICH_KEYPOINTS);

        //show(outImage, "out");


        waitKey(0);

    }

}
