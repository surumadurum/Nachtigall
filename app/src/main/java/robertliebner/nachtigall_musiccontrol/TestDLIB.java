package robertliebner.nachtigall_musiccontrol;


/**
 * Created by HP-Printer4 on 07.09.17.
 */
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import processing.core.*;

import ketai.camera.*;


public class TestDLIB extends PApplet {

    float currTime;

    FaceDet faceDet;

    KetaiCamera cam;

    Bitmap processed_image;     //will hold a rotated version of captured picture



    ArrayList<Point> landmarks;

    public static void main(String[] args) {
        PApplet.main(new String[]{"TestDLIB"});
    }

    public void settings()
    {
//
        fullScreen();
        size(width,height);
    }

    public void setup() {

//        ctx = getContext();
//        fullScreen();


        orientation(PORTRAIT);

       imageMode(CORNERS);
//        textAlign(CENTER, CENTER);
//        textSize(displayDensity * 25);
        cam = new KetaiCamera(this, 320, 240, 24);


        cam.setCameraID((cam.getCameraID() + 1));   //switch to front-side camera TODO: make this more flexible
        cam.start();


//        bitmap = BitmapFactory.decodeFile("/sdcard/pickel-im-gesicht-intro.jpg");


        faceDet = new FaceDet(Constants.getFaceShapeModelPath());
//        landmark_test();

    }

    public void draw() {
       // image(myImg,width/2, height/2, width, height);


        //background(0);

       // if( millis() - currTime >= 1000 )
        {
        //    currTime = millis();
            landmark_test();
        }

//        translate(160,120);
//        rotate(radians(270));
        PImage pimg = new PImage(processed_image);
       // pimg.setNative(processed_image);
        image(cam,width/2,height/2 -200);

        image(pimg,width/2,height/2 +200);






//        else
//        {
//            background(128);
//            text("Camera is currently off.", width/2, height/2);
//            cam.start();
//        }

        if(landmarks != null) {
            for (Point point : landmarks) {
                int pointX = point.x;

                int pointY = point.y;
                Log.d("LANDMARK", "Landmark found: " + pointX + " " + pointY);
                pushMatrix();
                translate(width/2,height/2 +200);
                ellipse(pointX, pointY, 10, 10);
                popMatrix();
            }
        }

    }

    public void onCameraPreviewEvent() {
        cam.read();
//        processed_image = BitmapFactory.decodeFile("/sdcard/pickel-im-gesicht-intro.jpg");
//        PImage _img = loadImage("/sdcard/pickel-im-gesicht-intro.jpg");


    }

    public void landmark_test() {

//        String path = Environment.getExternalStorageDirectory() + "/"+Environment.DIRECTORY_DOWNLOADS+ "/";
//

//        cam.setSaveDirectory("/sdcard");
//        Log.d("FOTO","FOTO: " + cam.savePhoto("test.jpg"));

//         myImg = new PImageBitmap(cam);

//        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.test);
//        BitmapFactory.

//        cam.get().

        Bitmap nat = (Bitmap)cam.getNative();
        processed_image = RotateAndMirrorBitmap(nat,270);

        Log.d("DETECTION","start");
        List<VisionDetRet> results = faceDet.detect(processed_image);
        Log.d("DETECTION","finished");

        for (final VisionDetRet ret : results) {
            Log.d("DETECTION","FACE FOUND");
            String label = ret.getLabel();
            int rectLeft = ret.getLeft();
            int rectTop = ret.getTop();
            int rectRight = ret.getRight();
            int rectBottom = ret.getBottom();

            noFill();
            stroke(255,0,0);

            pushMatrix();
            translate(width/2,height/2 +200);

            rect(rectLeft,rectTop,rectRight-rectLeft,rectBottom-rectTop);
            popMatrix();

            // Get 68 landmark points
            landmarks= ret.getFaceLandmarks();

        }

        Log.d("DETECTION","done with everything");
    }


    public static Bitmap RotateAndMirrorBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}

