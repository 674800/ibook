package com.book.guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GuideActivity extends Activity {
	/** Called when the activity is first created. */
	private PageWidget mPageWidget;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	ImagePageFactory imgPageFactory;
    boolean mNoBook;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mPageWidget = new PageWidget(this);
		setContentView(mPageWidget);

		mCurPageBitmap = Bitmap.createBitmap(PageWidget.WIDTH, PageWidget.HEIGHT, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap
				.createBitmap(PageWidget.WIDTH, PageWidget.HEIGHT, Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		imgPageFactory = new ImagePageFactory();

		try {
	        imgPageFactory.openbook("/system/media/guidebook");
			imgPageFactory.draw(mCurPageCanvas);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(this, "no guidebook",
					Toast.LENGTH_SHORT).show();
            mNoBook = true;
		}

		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				
				boolean ret=false;
				if (v == mPageWidget && !mNoBook) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());
						try {
							imgPageFactory.draw(mCurPageCanvas);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						if (mPageWidget.DragToRight()) {
							try {
								imgPageFactory.prePage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}						
							if(imgPageFactory.isfirstPage())return false;
							try {
								imgPageFactory.draw(mNextPageCanvas);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							try {
								imgPageFactory.nextPage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if(imgPageFactory.islastPage())return false;
							try {
								imgPageFactory.draw(mNextPageCanvas);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}
                 
					 ret = mPageWidget.doTouchEvent(e);
					return ret;
				}
				return false;
			}

		});
	}
}
