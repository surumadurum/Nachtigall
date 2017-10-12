package robertliebner.nachtigall_musiccontrol;


/**
 * Created by HP-Printer4 on 07.09.17.
 */
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import processing.core.*;

import ketai.camera.*;

import oscP5.*;

import controlP5.*;


public class TestDLIB extends PApplet {

    float currTime;

    FaceDet faceDet;

    KetaiCamera cam;

    Bitmap processed_image;     //will hold a rotated version of captured picture

    OscP5 osc;
    ControlP5 ctlp5;


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



        osc = new OscP5(this,4000);

        ctlp5 = new ControlP5(this);

        ctlp5.addBang("BUTTON_CAPTURE")
                .setPosition(width/2, height/2)
                .setSize(40,40)
                .setColorBackground(50);    //TODO make round and find correct color (red)

        //        ctx = getContext();
//        fullScreen();


        orientation(PORTRAIT);

      // imageMode(CORNERS);
//        textAlign(CENTER, CENTER);
//        textSize(displayDensity * 25);



        cam = new KetaiCamera(this, 640, 480, 50);


        cam.setCameraID((cam.getCameraID() + 1));   //switch to front-side camera TODO: make this more flexible

        cam.autoSettings();


        cam.start();



//        bitmap = BitmapFactory.decodeFile("/sdcard/pickel-im-gesicht-intro.jpg");


     //   faceDet = new FaceDet(Constants.getFaceShapeModelPath());
//        findLandmarks();

    }

    public void draw() {
        //background(0);

       // if( millis() - currTime >= 1000 )
        {
        //    currTime = millis();
//            findLandmarks();
        }


    //    PImage pimg = new PImage(processed_image);
       // pimg.setNative(processed_image);
//        image(cam,0,0,width,height);      //TODO why is it that if I dont print this picture I will nor get a displayed a processed_image as well?


        pushMatrix();
        translate(width/2,height/2);
        scale(-1,1);
        rotate(radians(270));

        image(cam, 0, 0, height, width);
        popMatrix();


    }

    public void onCameraPreviewEvent() {
        //read the input into KetaiCamera
        cam.read();

        //get the plain image and rotate it
//        Bitmap nat = (Bitmap)cam.getNative();
//        processed_image = RotateAndMirrorBitmap(nat,270);

//        processed_image = BitmapFactory.decodeFile("/sdcard/pickel-im-gesicht-intro.jpg");
//        PImage _img = loadImage("/sdcard/pickel-im-gesicht-intro.jpg");


    }


    public void BUTTON_CAPTURE()
    {
        Log.d("BUTTON","CAPTURE");

        //take a picture
        cam.enableFlash();
        cam.savePhoto();
        cam.pause();

        findLandmarks();

    }

    public void findLandmarks() {

//        String path = Environment.getExternalStorageDirectory() + "/"+Environment.DIRECTORY_DOWNLOADS+ "/";
//

//        cam.setSaveDirectory("/sdcard");
//        Log.d("FOTO","FOTO: " + cam.savePhoto("test.jpg"));

//         myImg = new PImageBitmap(cam);

//        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.test);
//        BitmapFactory.

//        cam.get().



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

        if(landmarks != null) {
            for (Point point : landmarks) {
                int pointX = point.x;

                int pointY = point.y;
                Log.d("LANDMARK", "Landmark found: " + pointX + " " + pointY);
//                pushMatrix();
//                translate(width/2,height/2 );
                ellipse(pointX, pointY, 10, 10);
//                popMatrix();
            }
        }


        Log.d("DETECTION","done with everything");
    }


    public static Bitmap RotateAndMirrorBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        matrix.postScale(-1, 1);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}

