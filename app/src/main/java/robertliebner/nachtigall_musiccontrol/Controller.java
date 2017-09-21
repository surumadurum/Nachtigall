package robertliebner.nachtigall_musiccontrol;

/**
 * Created by HP-Printer4 on 07.09.17.
 */


import tech.lity.rea.skatolo.*;
import tech.lity.rea.skatolo.events.*;
import tech.lity.rea.skatolo.extra.*;
import tech.lity.rea.skatolo.file.*;
import tech.lity.rea.skatolo.gui.*;
import tech.lity.rea.skatolo.gui.controllers.*;
import tech.lity.rea.skatolo.gui.group.*;
import tech.lity.rea.skatolo.gui.layout.*;
import tech.lity.rea.skatolo.gui.widgets.*;
import processing.core.*;

import android.graphics.PointF;
import android.view.MotionEvent;

import android.util.*;

public class Controller extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[]{"Controller_plain"});
    }

    Skatolo skatolo;

    Pointer pointer1, pointer2;

    public void settings()
    {
        size(displayWidth, displayHeight);
    }

    public void setup() {

        fill(255);

        rect(10,10,10,10);

        skatolo = new Skatolo(this);
//         disable outodraw because we want to draw our
//         custom cursor on to of skatolo
        skatolo.setAutoDraw(false);

        skatolo.addSlider("hello", 0, 100, 50, 40, 40, 400, 80);

        skatolo.addSlider("hello2", 0, 100, 50, 40, 140, 400, 80);

        skatolo.addButton("button")
                .setPosition(40,400)
                .setSize(200,200)
                ;


        // Disable the mouse
        skatolo.getMousePointer().disable();

        pointer1 = skatolo.addPointer(1);
        pointer2 = skatolo.addPointer(2);
        skatolo.addPointer(3);

//        noCursor();
    }


    public void draw() {

        background(skatolo.get("hello").getValue());

        text("Button" + ((Button)skatolo.get("button")).isPressed(),10,10);
        text("isAndroid" + skatolo.isAndroid ,10,30);

//        pointer1.updatePosition(mouseX, mouseY);


        for (Pointer p : skatolo.getPointerList()) {

            if (!p.isEnabled())
                continue;

            pushMatrix();
            translate(p.getX(), p.getY());

            stroke(255);
            if (p.isPressed()) {
                stroke(255, 0, 0);
            }
            if (p.isReleased()) {
                stroke(0, 255, 0);
            }
            if (p.isDragged()) {
                stroke(0, 0, 255);
            }


            line(-10, 0, 10, 0);
            line(0, -10, 0, 10);
            popMatrix();
        }

        // first draw skatolo
        skatolo.draw();


    }
//
//    public void mousePressed() {
//        skatolo.updatePointerPress(1, true);
//
//    }
//
//    public void mouseReleased() {
//
//        skatolo.updatePointerPress(1, false);
//
//
//    }
    //-----------------------------------------------------------------------------------------
    // Override Processing's surfaceTouchEvent, which will intercept all
    // screen touch events.  This code only runs when the screen is touched.

    public boolean surfaceTouchEvent(MotionEvent event) {


        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);
        pointerId++;

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

//                skatolo.addPointer(pointerId);
                skatolo.updatePointer(pointerId, (int) event.getX(pointerIndex), (int) event.getY(pointerIndex));

                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    skatolo.updatePointer(event.getPointerId(i)+1, (int) event.getX(i), (int) event.getY(i));
                    skatolo.updatePointerPress(event.getPointerId(i)+1, true);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                skatolo.updatePointerPress(pointerId, false);
//                delay(10);
//                skatolo.removePointer(pointerId);
                break;
            }
        }

        // If you want the variables for motionX/motionY, mouseX/mouseY etc.
        // to work properly, you'll need to call super.surfaceTouchEvent().
        return true;//super.surfaceTouchEvent(event);

//        // Number of places on the screen being touched:
//        int numPointers = me.getPointerCount();
//        for (int i=0; i < numPointers; i++) {
//            int pointerId = me.getPointerId(i);
//            float x = me.getX(i);
//            float y = me.getY(i);
//
//            skatolo.updatePointer(pointerId+1, (int)x,(int)y);
//
//            Log.d("ALL",me.actionToString(me.getAction()) + "  " + me.getAction() +  "   " + pointerId);
//
//            int action = me.getAction();
//
//            if(action == MotionEvent.ACTION_DOWN )
//            {
//                Log.d("CONC","ACTION DOWN " + pointerId);
//
//                skatolo.updatePointerPress(1, true);
//            }
//
//
//
//            if(action == MotionEvent.ACTION_UP )
//            {
//                Log.d("CONC","ACTION UP " + pointerId);
//                skatolo.updatePointerPress(1, false);
//
//            }
//
//            if( action == 261 && pointerId == 1)
//            {
//                Log.d("CONC","POINTER DOWN " + pointerId);
//
//                skatolo.updatePointerPress(2, true);
//            }
//            if( action == 262 && pointerId == 1)
//            {
//                Log.d("CONC","POINTER UP " + pointerId);
//                skatolo.updatePointerPress(2, false);
//
//            }
//
//        }
//        // If you want the variables for motionX/motionY, mouseX/mouseY etc.
//        // to work properly, you'll need to call super.surfaceTouchEvent().
//        return true;//super.surfaceTouchEvent(me);
    }
}