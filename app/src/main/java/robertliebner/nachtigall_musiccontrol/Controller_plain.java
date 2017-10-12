package robertliebner.nachtigall_musiccontrol;

/**
 * Created by HP-Printer4 on 07.09.17.
 */



import processing.core.*;

import android.view.MotionEvent;
import android.graphics.PointF;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import oscP5.*;
import netP5.*;


public class Controller_plain extends PApplet {

    final static boolean DEBUG = true;

    public static final int remotePort = 13000, localPort = 12000;


    private OscP5 oscP5;
    private NetAddress myRemoteLocation;

    List<RectButton> buttons;

    VScrollbar sl_expression;
    HScrollbar sl_tempo;

    private SparseArray<PointF> mActivePointers;

    public static void main(String[] args) {
        PApplet.main(new String[]{"Controller_plain"});
    }

    public void settings()
    {
        fullScreen();
        size(width,height);
    }

    public void setup() {

        orientation(LANDSCAPE);

        textMode(CENTER);
        fill(255);

        oscP5 = new OscP5(this,localPort);

        myRemoteLocation = new NetAddress("255.255.255.255",remotePort);


        sl_expression = new VScrollbar(0, 20, 80, height - 20, 3 * 5 + 1)
        {
            @Override
            public void OnValueChanged() {
//                Log.d("EVENT","Event listener received value changed");
                Log.d("EVENT",String.format("%f",abs(oldpos-getPos())));
            }
        };
//        sl_expression = new VScrollbar(0, 20, 80, height - 20, 3 * 5 + 1);


        sl_tempo = new HScrollbar(80, height - 20, width - 80, 80, 10);

        buttons = new ArrayList<RectButton>();

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++)
                buttons.add(new RectButton(200 + 210 * x, 80 + 210 * y, 200, new color(10, 0, 100, 255), new color(100, 70, 255, 255))
                {
                    @Override
                    public void onPress(){
                        Log.d("EVENT","Listener recvd button press");
                    }
                });
        }

        mActivePointers = new SparseArray<PointF>();
    }


    public void draw() {

        background(255);

        sl_expression.draw();

        sl_tempo.draw();


        //check for PRESS & RELEASE events on buttons (TODO should be inside class)
        for (RectButton button_ : buttons) {
            button_.draw();
        }


        //check for changed values of sliders
        //// TODO: 06.10.17



        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
            PointF point = mActivePointers.valueAt(i);
            if (point != null) {

                pushMatrix();
                translate(point.x, point.y);

                stroke(255, 0, 0);

                line(-10, 0, 10, 0);
                line(0, -10, 0, 10);
                popMatrix();
            }


        }


        if(DEBUG)
        {
            text("Button: " + buttons.get(0).pressed(), 10, 10);

        }
//        text("isAndroid" + skatolo.isAndroid ,10,30);


    }


    //-----------------------------------------------------------------------------------------
    // Override Processing's surfaceTouchEvent, which will intercept all
    // screen touch events.  This code only runs when the screen is touched.

    public boolean surfaceTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                break;
            }
        }

        // If you want the variables for motionX/motionY, mouseX/mouseY etc.
        // to work properly, you'll need to call super.surfaceTouchEvent().
        return super.surfaceTouchEvent(event);
    }


    abstract class Scrollbar {

        int swidth, sheight;    // width and height of bar
        int xpos, ypos;         // x and y position of bar
        float spos, newspos,oldpos;    // x position of slider
        int sposMin, sposMax;   // max and min values of slider
        int loose;              // how loose/heavy
        boolean over;           // is the mouse over the slider?
        boolean locked;
        float ratio;

        int _touchX, _touchY; //holds the position of the pointer that was found to be over scrollbar

        boolean enabled = true;
        boolean showLabel = true;

        boolean isEnabled() {
            return enabled;
        }

        void enable() {
            enabled = true;

        }

        void disable() {
            enabled = false;

        }

        void showLabel() {
            showLabel = true;

        }

        void hideLabel() {
            showLabel = false;

        }

        public void OnValueChanged(){};

        abstract void update();

        public void draw()
        {

        }
    }


    class HScrollbar extends Scrollbar{

        HScrollbar(int xp, int yp, int sw, int sh, int l) {
            swidth = sw;
            sheight = sh;
            int widthtoheight = sw - sh;
            ratio = (float) sw / (float) widthtoheight;
            xpos = xp;
            ypos = yp - sheight / 2;
            spos = xpos + swidth / 2 - sheight / 2;
            newspos = spos;
            sposMin = xpos;
            sposMax = xpos + swidth - sheight;
            loose = l;
            oldpos = spos;
        }

        void update() {



            if (/*mousePressed() && */over() && enabled) {    //on a touch, we don't need to check whether it's pressed
                locked = true;
            }
            else ///*!mousePressed()*/!over)
            {      //same here
                locked = false;
            }

            if (locked) {
                newspos = constrain(_touchX - sheight / 2, sposMin, sposMax);
            }
            if (abs(newspos - spos) > 1) {
                spos = spos + (newspos - spos) / loose;
            }

            if(abs(oldpos-getPos()) > 0.0001)
            {
                this.OnValueChanged();
                oldpos = getPos();
            }
        }

        int constrain(int val, int minv, int maxv) {
            return min(max(val, minv), maxv);
        }

        boolean over() {

            for (int size = mActivePointers.size(), i = 0; i < size; i++) {
                PointF point = mActivePointers.valueAt(i);
                if (point != null) {
                    if (point.x > xpos && point.x < xpos + swidth &&
                            point.y > ypos && point.y < ypos + sheight) {
                        _touchX = (int) point.x;
                        _touchY = (int) point.y;
                        return true;
                    }

                }
            }

            return false;
        }

        public void draw() {

            update();


            fill(255);
            rect(xpos, ypos, swidth, sheight);
            if (over || locked) {
                fill(153, 102, 0);
            } else {
                fill(102, 102, 102);
            }
            rect(spos, ypos, sheight, sheight);
            if(showLabel)
                text(getPos(),xpos+swidth/2,ypos+sheight/2);



        }

        float getPos() {
            // Convert spos to be values between
            // 0 and the total width of the scrollbar
            return spos * ratio;
        }
    }


     class VScrollbar extends Scrollbar{

        VScrollbar(int xp, int yp, int sw, int sh, int l) {
            swidth = sw;
            sheight = sh;
            int widthtoheight = sw - sh;
            ratio = (float) sw / (float) widthtoheight;
            xpos = xp;
            ypos = yp/*-sheight/2*/;
            spos = ypos - swidth / 2 + sheight / 2;
            newspos = spos;
            sposMin = ypos;
            sposMax = ypos - swidth + sheight;
            loose = l;
            oldpos = spos;

        }

        void update() {

            if (/*mousePressed() && */over() && enabled) {    //on a touch, we don't need to check whether it's pressed
                locked = true;
            }
            else  //if (/*!mousePressed()*/!over) {      //same here
            {
                locked = false;
            }
            if (locked) {
                newspos = constrain(_touchY - swidth / 2, sposMin, sposMax);
            }
            if (abs(newspos - spos) > 1) {
                spos = spos + (newspos - spos) / loose;
            }


            text(abs(oldpos-getPos()),200,10);
            if(abs(oldpos-getPos()) > 0.0001)
            {

                OnValueChanged();
                oldpos = getPos();
            }


        }

        int constrain(int val, int minv, int maxv) {
            return min(max(val, minv), maxv);
        }

        boolean over() {

            for (int size = mActivePointers.size(), i = 0; i < size; i++) {
                PointF point = mActivePointers.valueAt(i);
                if (point != null) {
                    if (point.x > xpos && point.x < xpos + swidth &&
                            point.y > ypos && point.y < ypos + sheight) {
                        _touchX = (int) point.x;
                        _touchY = (int) point.y;
                        return true;
                    }

                }
            }

            return false;
        }

        public void draw() {

            update();

            fill(255);
            rect(xpos, ypos, swidth, sheight);
            if (over || locked) {
                fill(153, 102, 0);
            } else {
                fill(102, 102, 102);
            }
            rect(xpos, spos, swidth, sheight);

            if(showLabel)
                text(getPos(),xpos+swidth/2,ypos+sheight/2);

        }

        float getPos() {
            // Convert spos to be values between
            // 0 and the total width of the scrollbar
            return spos * ratio;
        }


    }


    abstract class Button

    {

        int x, y;
        int size;
        color basecolor, highlightcolor;
        color currentcolor;
        boolean over = false;

        boolean pressed = false;

        boolean enabled = true;

        int _touchX, _touchY;

        private boolean last_state() {
            return pressed;
        }

        boolean pressed() {
            if (over && enabled) {
                pressed = true;
                return true;
            } else {
                pressed = false;
                return false;
            }
        }

        boolean isEnabled() {
            return enabled;
        }

        void enable() {
            enabled = true;

        }

        void disable() {
            enabled = false;

        }

        abstract boolean over();


        public void draw()
        {
            if (over() && enabled) {
                currentcolor = highlightcolor;
            } else {
                currentcolor = basecolor;
            }

            //comapare state to last state
            boolean last_state = this.last_state();
            boolean cur_state = this.pressed();

            if (last_state != cur_state) //something changed
            {
                if (cur_state == true) {

                    //PRESS EVENT
                    Log.d("EVENT", "PRESS");

                    this.onPress();

                    OscMessage myMessage = new OscMessage("/test");

                    myMessage.add(123); /* add an int to the osc message */

                  /* send the message */
                    oscP5.send(myMessage, myRemoteLocation);

                    //as we only want one to be clickable at a time, disable the other buttons
                    for (RectButton but_ : buttons)
                        if (but_ != this)
                            but_.disable();
                } else {
                    //RELEASE EVENT
                    Log.d("EVENT", "RELEASE");

                    this.onRelease();

                    for (RectButton but_ : buttons)
                        but_.enable();
                }
            }
        }

        //class that can be overwritten
        public void onPress(){};
        public void onRelease(){};

    }

    class CircleButton extends Button {
        CircleButton(int ix, int iy, int isize, color icolor, color ihighlight) {
            x = ix;
            y = iy;
            size = isize;
            basecolor = icolor;
            highlightcolor = ihighlight;
            currentcolor = basecolor;
        }

        @Override
        boolean over() {
            if (overCircle(x, y, size)) {
                over = true;
                return true;
            } else {
                over = false;
                return false;
            }
        }

        private boolean overCircle(int x, int y, int diameter) {
            float disX = x - mouseX;
            float disY = y - mouseY;
            if (sqrt(sq(disX) + sq(disY)) < diameter / 2) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void draw() {
            super.draw();
            stroke(255);
            fill(currentcolor.r, currentcolor.b, currentcolor.g, currentcolor.alpha);
            ellipse(x, y, size, size);
        }
    }

    class RectButton extends Button {
        RectButton(int ix, int iy, int isize, color icolor, color ihighlight) {
            x = ix;
            y = iy;
            size = isize;
            basecolor = icolor;
            highlightcolor = ihighlight;
            currentcolor = basecolor;
        }

        @Override
        boolean over() {
            if (overRect(x, y, size, size)) {
                over = true;
                return true;
            } else {
                over = false;
                return false;
            }
        }

        private boolean overRect(int x, int y, int width, int height) {
            for (int size = mActivePointers.size(), i = 0; i < size; i++) {
                PointF point = mActivePointers.valueAt(i);
                if (point != null) {
                    if ((int) point.x >= x && (int) point.x <= x + width &&
                            (int) point.y >= y && (int) point.y <= y + height) {

                        _touchX = (int) point.x;
                        _touchY = (int) point.y;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void draw() {
            super.draw();
            stroke(255);
            fill(currentcolor.r, currentcolor.b, currentcolor.g, currentcolor.alpha);
            rect(x, y, size, size);
        }
    }


}


class color {
    int r, g, b, alpha;

    color(int r, int g, int b, int alpha) {
        this.r = r;
        this.b = b;
        this.g = g;
        this.alpha = alpha;
    }
}