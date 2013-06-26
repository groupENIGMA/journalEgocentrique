package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.FrameLayout.LayoutParams;

/**
 * 
 *
 */
public class ImageTapZoomView extends View implements OnGestureListener {

	private static final int SCALING_FACTOR = 50;
	private final int LANDSCAPE = 1;
	private GestureDetector gestureDetector;
	private Drawable image = null;
	private int scalefactor = 0;
	private int orientation;
	private int zoomCtr = 0;
	private long lastTouchTime = 0;
	private int winX, winY, imageX, imageY, scrollX = 0, scrollY = 0, left,
			top, bottom, right;

	public ImageTapZoomView(Context context, int orientation) {
		super(context);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		this.orientation = orientation;
		gestureDetector = new GestureDetector(this);
	}
	
	public ImageTapZoomView(Context context) {
		super(context);
	}

	public void setImage(Drawable bitmap, Activity activity) {
		image = bitmap;
		imageSetting(activity);
	}

	public void setImage(Bitmap bitmap, Activity activity) {
		image = new BitmapDrawable(bitmap).getCurrent();
		imageSetting(activity);
	}

	/**
	 * Works in both landscape and portrait mode.
	 */
	private void imageSetting(Activity activity) {
		scrollX = scrollY = 0;
		scalefactor = 0;
		imageX = winX = activity.getWindow().getWindowManager()
				.getDefaultDisplay().getWidth();
		imageY = winY = activity.getWindow().getWindowManager()
				.getDefaultDisplay().getHeight();
		if (orientation == LANDSCAPE) {
			imageX = 3 * imageY / 4;
		}
		calculatePos();
	}

	public void calculatePos() {
		int tempx, tempy;
		tempx = imageX + imageX * scalefactor / 100;
		tempy = imageY + imageY * scalefactor / 100;
		left = (winX - tempx) / 2;
		top = (winY - tempy) / 2;
		right = (winX + tempx) / 2;
		bottom = (winY + tempy) / 2;
		invalidate();
	}

	/**
	 * Redraws the bitmap when zoomed or scrolled.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (image == null)
			return;
		image.setBounds(left + scrollX, top + scrollY, right + scrollX, bottom
				+ scrollY);
		image.draw(canvas);
	}

	public void zoomIn() {
		scalefactor += SCALING_FACTOR;
		calculatePos();
	}

	public void zoomOut() {
		if (scalefactor == 0)
			return;
		scrollX = scrollY = 0;
		scalefactor -= SCALING_FACTOR;
		calculatePos();
	}

	public void scroll(int x, int y) {
		scrollX += x / 5;
		scrollY += y / 5;
		if (scrollX + left > 0) {
			scrollX = 0 - left;
		} else if (scrollX + right < winX) {
			scrollX = winX - right;
		}
		if (scrollY + top > 0) {
			scrollY = 0 - top;
		} else if (scrollY + bottom < winY) {
			scrollY = winY - bottom;
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		boolean onTouchEvent = gestureDetector.onTouchEvent(me);
		return onTouchEvent;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		long thisTime = arg0.getEventTime();
		if (thisTime - lastTouchTime < 250) {
			lastTouchTime = -1;
			onDoubleTap();
			return true;
		}
		lastTouchTime = thisTime;
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (zoomCtr == 0)
			return false;
		scroll((int) (e2.getX() - e1.getX()), (int) (e2.getY() - e1.getY()));
		return true;
	}

	private void onDoubleTap() {
		if (zoomCtr == 0) {
			zoomCtr++;
			zoomIn();
			return;
		}
		zoomCtr--;
		zoomOut();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}
}