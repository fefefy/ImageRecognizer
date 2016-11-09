package projetstl.com.imagerecognizer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;

public class MainActivity extends Activity {

    private static int LOAD_IMAGE = 1;
    private File imageFile;
    String ImageString;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Fonction pour utiliser l'appareil photo

    public void process(View view){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile=new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "test.jpg");
        Uri tempuri=Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0)
        {
            switch (resultCode) {

                case Activity.RESULT_OK:
                    if(imageFile.exists())
                    {
                        Toast.makeText(this,"The file was saved at "+imageFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(this,"There was an error saving the file",Toast.LENGTH_LONG).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }



}

