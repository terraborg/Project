package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import androidx.annotation.RequiresApi;

class CanvasButton {

	private String msg;
	private boolean mode;
	private Paint paint;
	private Rect rect;
	private DrawThread drawThread;
	private int textSize;

	CanvasButton(Rect rect,String msg,DrawThread drawThread)
	{
		this.rect=rect;
		this.msg=msg;
		mode=false;
		this.paint=new Paint();
		this.drawThread=drawThread;
		this.textSize=10;
	}

	void setTextSize(int textSize)
	{
		this.textSize=textSize;
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	void draw(Canvas canvas)
	{
		paint.setTextSize(textSize);
		if(!mode)
		{
			paint.setColor(Color.GRAY);
			canvas.drawRoundRect(rect.left,rect.top,rect.right,rect.bottom,10,10,paint);
			paint.setColor(Color.BLACK);
		}
		else
		{
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rect.left,rect.top,rect.right,rect.bottom,10,10,paint);
			paint.setColor(Color.GRAY);
		}
		canvas.drawText(msg, (float) (rect.left+(rect.right-rect.left-textSize*msg.length()*0.55)/2),(float)(rect.top+(rect.bottom-rect.top)*0.7),paint);
	}

	DrawThread getDrawThread() {
		return drawThread;
	}

	void setMSG(String msg)
	{
		this.msg=msg;
	}

	void Switch()
	{
		mode=!mode;
	}

	void setMode(boolean mode)
	{
		this.mode=mode;
	}

	void checkForTouch(int x, int y)
	{
		if(x>=rect.left&&x<=rect.right&&y<=rect.bottom&&y>=rect.top)
		{
			onTouch();
		}
	}
	void onTouch()
	{

	}
}
