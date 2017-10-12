package robertliebner.nachtigall_musiccontrol;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.support.v7.app.AppCompatActivity;

/**
 * Created by HP-Printer4 on 02.10.17.
 */

public class ChooseFunctionFragment extends Fragment {
    public interface OnButtonsPressed{
        void onMusicControlButton();
        void onTakePicturesButton();

    }

    OnButtonsPressed mCallback; //will later be connected to our main activity by onAttach interface implementation


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        try{
//            mCallback = (OnButtonsPressed) activity;
//        }
//        catch(ClassCastException ex)
//        {
//            throw new ClassCastException(activity.toString() + "must implement callback interface");
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("EVENT","Inflating choose_func_view");
        return inflater.inflate(R.layout.choose_func_view,container,false);
    }



    public void ControlMusicians(View view){
        mCallback.onMusicControlButton();
    }
}
