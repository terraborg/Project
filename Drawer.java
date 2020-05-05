package com.example.myapplication;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Drawer extends SurfaceView implements SurfaceHolder.Callback {
	public Drawer(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	private DrawThread drawThread;

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
			drawThread = new DrawThread(getContext(),getHolder());
			drawThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		drawThread.pleaseStop();
		boolean retry = true;
		while (retry)
		{
			try
			{
				drawThread.join();
				retry = false;
			}
			catch (InterruptedException e)
			{

			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int x,y;
		x = (int)event.getX();
		y = (int)event.getY();
		drawThread.onTouch(x,y);
		if(checkForRect(x,y,drawThread.rect))
			if(drawThread.getCell((x-drawThread.rect.left)/10,(y-drawThread.rect.top)/10)==-1)
				drawThread.putCell((x-drawThread.rect.left)/10,(y-drawThread.rect.top)/10);
			else
				try
				{
					drawThread.choosedCell=drawThread.getCell((x-drawThread.rect.left)/10,(y-drawThread.rect.top)/10);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		return false;
	}
	private boolean checkForRect(int x, int y, Rect rect)
	{
		return x>=rect.left&&x<=rect.right&&y<=rect.bottom&&y>=rect.top;
	}
}
