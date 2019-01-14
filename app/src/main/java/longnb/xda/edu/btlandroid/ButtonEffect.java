package longnb.xda.edu.btlandroid;

import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;


public class ButtonEffect {
    View button;
    public ButtonEffect(View button) {
        this.button = button;
    }
    public void setEffect(){
        button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch (View v, MotionEvent event){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}