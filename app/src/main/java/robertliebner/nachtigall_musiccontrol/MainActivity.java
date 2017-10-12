package robertliebner.nachtigall_musiccontrol;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import processing.android.PFragment;
import processing.android.CompatUtils;
import processing.core.PApplet;



public class MainActivity extends AppCompatActivity implements Game.OnGameFragmentListener{
    private PApplet sketch;

    public static final String TAG = "Nachtigall";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(findViewById(R.id.fragment_container) != null)
        {
            if(savedInstanceState != null)
            {
                return;
            }

            Fragment frgChooseFunc = new ChooseFunctionFragment();

            getFragmentManager().beginTransaction().add(R.id.fragment_container,frgChooseFunc).commit();

        }

//        FrameLayout frame = new FrameLayout(this);
//        frame.setId(CompatUtils.getUniqueViewId());
//        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//
//        sketch = new Controller_plain();
//        // @@external@@
//        PFragment fragment = new PFragment(sketch);
//        fragment.setView(frame, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }


    public void ControlMusicians(View view){

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        sketch = new Controller_plain();
        // @@external@@
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);
    }

    public void MusiciansDisplay(View view){

//        ((ChooseFunctionFragment)getSupportFragmentManager().findFragmentById(R.id.fragment2)).ControlMusicians(null);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
//
        sketch = new TestDLIB();    //TODO: Anpassen
        // @@external@@
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment);
    }


    public void TakePictures(View view){

//        ((ChooseFunctionFragment)getSupportFragmentManager().findFragmentById(R.id.fragment2)).ControlMusicians(null);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        sketch = new TestDLIB();
        // @@external@@
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);
    }

    public void ControlMouths(View view){

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        sketch = new TestDLIB();
        // @@external@@
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);
    }

    public void Game(View view){

        Fragment frgGame = new Game();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,frgGame).commit();

    }

    @Override
    public void onStartCameraFragment() {

        Fragment frgCamera = new Camera2BasicFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,frgCamera).commit();

    }

}