/*
 * Copyright 2010 Sony Ericsson Mobile Communications AB
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.sonyericsson.tutorial.zoom4;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sonyericsson.zoom.DynamicZoomControl;
import com.sonyericsson.zoom.ImageZoomView;
import com.sonyericsson.zoom.LongPressZoomListener;
import com.sonyericsson.zoom.PinchZoomListener;

/**
 * Activity for zoom tutorial 1
 */
public class TutorialZoomActivity4 extends Activity implements OnLongClickListener {

    /** Constant used as menu item id for resetting zoom state */
    private static final int MENU_ID_RESET = 0;

    /** Image zoom view */
    private ImageZoomView mZoomView;

    /** Zoom control */
    private DynamicZoomControl mZoomControl;

    /** Decoded bitmap image */
    private Bitmap mBitmap;

    /** On touch listener for zoom view */
    private LongPressZoomListener mZoomListener;
    
    private PinchZoomListener mPinchZoomListener;
    
    private boolean longpressZoom = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mZoomControl = new DynamicZoomControl();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tintin);

        mZoomListener = new LongPressZoomListener(getApplicationContext());
        mZoomListener.setZoomControl(mZoomControl);
        mPinchZoomListener = new PinchZoomListener(getApplicationContext());
        mPinchZoomListener.setZoomControl(mZoomControl);
        
        mZoomView = (ImageZoomView)findViewById(R.id.zoomview);
        mZoomView.setZoomState(mZoomControl.getZoomState());
        mZoomView.setImage(mBitmap);

        mZoomControl.setAspectQuotient(mZoomView.getAspectQuotient());

        resetZoomState();
        
        mZoomView.setOnTouchListener(mPinchZoomListener);
        mZoomView.setOnLongClickListener(this);
        
        final Button b = (Button)this.findViewById(R.id.zoomtype); 
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(longpressZoom){
                    mZoomView.setOnTouchListener(mPinchZoomListener);
                    longpressZoom = false;
                    b.setText("LongPressZoom");
                }else{
                    mZoomView.setOnTouchListener(mZoomListener);
                    longpressZoom = true;
                    b.setText("PinchZoom");
                }
            }
        });
        
        CharSequence text = "Pinch zoom ftw!\n(Press button top left to switch between zoom modes)";
        
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    
    @Override
    public void onPause(){
        super.onPause();
        
        // Kill when pressing home key.
        this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBitmap.recycle();
        mZoomView.setOnTouchListener(null);
        mZoomControl.getZoomState().deleteObservers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_RESET, 2, R.string.menu_reset);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_RESET:
                resetZoomState();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Reset zoom state and notify observers
     */
    private void resetZoomState() {
        mZoomControl.getZoomState().setPanX(0.5f);
        mZoomControl.getZoomState().setPanY(0.5f);
        mZoomControl.getZoomState().setZoom(1f);
        mZoomControl.getZoomState().notifyObservers();
    }

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "On Long Click", Toast.LENGTH_SHORT).show();
		return true;
	}

}
