package robertliebner.nachtigall_musiccontrol;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Random;
import android.os.Handler;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {Game.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Game#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game extends Fragment {

    private static final int CREATE_NEW_CHARACTER = 10;

    private static final int COUNTDOWN = 20;

    private int timeLeftInGame = 20;

    private ViewGroup thisView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Handler timeHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnGameFragmentListener mListener;

    private LinkedList<Character> characters = new LinkedList<Character>();

    public Game() {
        // Required empty public constructor
    }
//
//    ImageView imgKoenig;
//    ImageView imgNachtigall;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Game.
     */
    // TODO: Rename and change types and number of parameters
    public static Game newInstance(String param1, String param2) {
        Game fragment = new Game();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment =inflater.inflate(R.layout.fragment_game, null, false);

        //// TODO: 10.10.17 create a handler and a random object, then send a bunch of delayed messages that will create

        timeHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {

                Log.d("EVENT","message received");
                switch (msg.what){

                    case CREATE_NEW_CHARACTER:

                        Character newCharacter = new Character(new Random().nextBoolean()?R.mipmap.ic_koenig:R.mipmap.ic_nachtigall,1000)
                        {
                            @Override
                            void onClick(View view,Character character) {

                                if((int)view.getTag() == R.mipmap.ic_nachtigall)
                                {
                                    Log.d("SCORE","NACHTIGALL");

                                    //Yeah, score
                                    final TextView txtAnn = ((TextView)thisView.findViewById(R.id.txtAnnounce));

                                    txtAnn.setText("BINGO!");

                                    txtAnn.setTextColor(android.graphics.Color.rgb(0, 255,0));

                                    txtAnn.animate()
                                        .setDuration(500)
                                        .alpha((float)0.6)
                                        .scaleXBy(10)
                                        .scaleYBy(10)

                                            .withEndAction(new Runnable(){
                                                @Override
                                                public void run() {
                                                    txtAnn.setAlpha(0);
                                                    txtAnn.setScaleX(0);
                                                    txtAnn.setScaleY(0);
                                                }
                                            })
                                        .start();

                                    ;
                                }

                                if((int)view.getTag() == R.mipmap.ic_koenig)
                                {
                                    Log.d("SCORE","KOENIG");

                                    //Yeah, score
                                    final TextView txtAnn = ((TextView)thisView.findViewById(R.id.txtAnnounce));
                                    final TextView txtScore = ((TextView)thisView.findViewById(R.id.txtScore));

                                    txtAnn.setText("OH NEIN!");

                                    txtAnn.setTextColor(android.graphics.Color.rgb(255,0 ,0));

                                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",10f);
                                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",10f);
                                    PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha",1f);

                                    ObjectAnimator anim_annotation =  ObjectAnimator.ofPropertyValuesHolder(txtAnn,scaleX,scaleY,alpha);

                                    anim_annotation.setDuration(500);
                                    anim_annotation.setRepeatMode(ValueAnimator.REVERSE);
                                    anim_annotation.setRepeatCount(1);

                                    ObjectAnimator anim_scoreA = ObjectAnimator.ofFloat(txtScore,"rotation",-30f);
                                    anim_scoreA.setRepeatMode(ValueAnimator.REVERSE);
                                    anim_scoreA.setRepeatCount(3);
                                    ObjectAnimator anim_scoreB = ObjectAnimator.ofFloat(txtScore,"rotation",30f);
                                    anim_scoreB.setRepeatMode(ValueAnimator.REVERSE);
                                    anim_scoreB.setRepeatCount(3);


                                    AnimatorSet anim_score = new AnimatorSet();
                                    anim_score.setDuration(100);
                                    anim_score.playSequentially(anim_scoreA,anim_scoreB);


        //                            anim_score.setRepeatMode(ValueAnimator.REVERSE);
        //                            anim_score.setInterpolator(new OvershootInterpolator());


                                    AnimatorSet anim_set = new AnimatorSet();
                                    anim_set.playTogether(anim_annotation,anim_score);

                                    anim_set.start();




        //                            txtAnn.animate()
        //                                    .setDuration(500)
        //                                    .alpha((float)0.6)
        //                                    .scaleXBy(10)
        //                                    .scaleYBy(10)
        //                                    .withEndAction(new Runnable(){
        //                                        @Override
        //                                        public void run() {
        //                                            txtAnn.setAlpha(0);
        //                                            txtAnn.setScaleX(0);
        //                                            txtAnn.setScaleY(0);
        //                                        }
        //                                    })
        //
        //                                    .start();





                                }

                                characters.remove(character);
                            }
                        };

                        characters.add(newCharacter);
                        break;


                    case COUNTDOWN:
                        timeLeftInGame --;

                        ((TextView)thisView.findViewById(R.id.txtCountDown)).setText(String.format("%d:00",timeLeftInGame));

                        if (timeLeftInGame==0) {    //launch new fragment

                            mListener.onStartCameraFragment();

                        }

                }


                super.handleMessage(msg);
            }
        };

        Random random = new Random();

        for(int i=0;i<20;i++)
        {
            timeHandler.sendEmptyMessageDelayed(CREATE_NEW_CHARACTER,random.nextInt(20000));
        }


        for(int i=1;i<21;i++)
        {
            timeHandler.sendEmptyMessageDelayed(COUNTDOWN,i*1000);
        }

        thisView = container;

        return fragment;
    }



    void onStartAnimation()
    {
        final ValueAnimator  animator = ValueAnimator.ofFloat(0,1);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float)valueAnimator.getAnimatedValue();

              //  imgKoenig.setTranslationX((float)animator.getAnimatedValue());
//                imgKoenig.setScaleX(value);
//                imgKoenig.setScaleY(value);
            }
        });

        animator.setInterpolator(new OvershootInterpolator());

        animator.setDuration(1000);

//        animator.setRepeatCount(1);
//
//        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.start();


//        imgKoenig.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                imgKoenig.animate()
//                        .rotationBy(1000)
//                        .alpha(0)
//                        .setDuration(500)
////                        .start();
//            }
//        });


    }


//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGameFragmentListener) {
            mListener = (OnGameFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnGameFragmentListener {
        // TODO: Update argument type and name
        void onStartCameraFragment();
    }


    class Character
    {
        private int resource, lifespan;

        private ImageButton imageButton;

        public boolean isActive() {
            return active;
        }

        private boolean active = true;

        private final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                Log.d("HANDLER","received handler");

                deleteView();

                super.handleMessage(msg);
            }
        };

        @Override
        protected void finalize() throws Throwable {

            deleteView();

            super.finalize();
        }

        public Character(int _resource, int _lifespan) {
            resource = _resource;
            lifespan = _lifespan;

            createView(_resource);

            handler.sendEmptyMessageDelayed(100,_lifespan);

        }





        void createView(int _resource)
        {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            Random random = new Random();


            imageButton = new ImageButton(getContext());
            imageButton.setImageResource(_resource);

            imageButton.setTag(_resource);

            imageButton.setLayoutParams(new ViewGroup.LayoutParams(100,100));

            ((FrameLayout)thisView.findViewById(R.id.fragment_container)).addView(imageButton);

            imageButton.setTranslationY(random.nextInt(height-100));
            imageButton.setTranslationX(random.nextInt(width-100));

            imageButton.setScaleX(0);
            imageButton.setScaleY(0);

            imageButton.setAlpha((float)0);

            imageButton.animate()
                    .setInterpolator(new OvershootInterpolator())
                    .scaleX((float)1.5)
                    .scaleY((float)1.5)
                    .alpha(1)
                    .setDuration(500)
                    .start();

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Character.this.onClick(view);

                    view.animate().setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            Log.d("ANIM",this.toString());
                            deleteView();

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })

                    .rotationBy(1000)
                    .alpha(0)
                    .start();


                }
            });


        }

        void onClick(View view){
            onClick(view,this);
            Log.d("ANIM",this.toString());
        }


        void onClick(View view,Character character){
            Log.d("ANIM",this.toString());
        }

        void deleteView()
        {
            ((FrameLayout)thisView.findViewById(R.id.fragment_container)).removeView(imageButton);

            active = false;
        }

        public ImageButton getImageButton() {
            return imageButton;
        }
    }
}


