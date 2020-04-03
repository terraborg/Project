package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {

	private SurfaceHolder surfaceHolder;

	public Game game;

	private int x1,x2,y1,y2;

	private boolean running = true,pause=true;

	private int speed;

	private int width, height;

	public Rect rect,pauseRect;

	public Canvas gameCanvas,canvas;

	int choosedCell;

	Cell choosed;

	public DrawThread(Context context, SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
		canvas = this.surfaceHolder.lockCanvas();
		width = canvas.getWidth();
		height = canvas.getHeight();
		choosedCell=-1;
		speed=500;
		x1 = (int) (width*0.05)/10*10;
		x2 = (int) (width*0.65)/10*10;
		y1 = (int) (height*0.05/10*10);
		y2 = (int) (height*0.75)/10*10;
		pauseRect = new Rect((int)(x2+width*0.03),(int)(y1+height*0.45),(int)(x2+width*0.15),(int)(y1+height*0.55));
		rect = new Rect(x1,y1,x2,y2);
		game = new Game(y2-y1, x2-x1);
		this.surfaceHolder.unlockCanvasAndPost(canvas);
	}

	void pleaseStop()
	{
		running = false;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public void run() {

		int k=0;
		while(running)
		{
			Paint paint = new Paint();

			canvas = surfaceHolder.lockCanvas();
			try
			{
				DrawBack(canvas);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			surfaceHolder.unlockCanvasAndPost(canvas);
			try {
				Thread.sleep(1000/speed);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameCanvas = surfaceHolder.lockCanvas(rect);
			rect.left=x1;
			rect.top=y1;
			rect.right=x2;
			rect.bottom=y2;
			if(pause)
				try
				{
					game.tick();
					if(game.Cells.size()!=0)
						k++;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			try
			{
				paint.setARGB(255,0,0,0);
				gameCanvas.drawARGB(255,230,230,255);
				for(int i=0; i<game.CellsSize;i++)
				{
					paint.setARGB(255, game.Cells.get(i).r, game.Cells.get(i).g, game.Cells.get(i).b);
					gameCanvas.drawRect(game.Cells.get(i).x*10+rect.left, game.Cells.get(i).y*10+rect.top, game.Cells.get(i).x*10+10+rect.left, game.Cells.get(i).y*10+10+rect.top,paint);
				}
				paint.setColor(Color.BLACK);
				gameCanvas.drawText(String.valueOf(k),rect.right-40,rect.bottom-40,paint);
			}
			finally {
				surfaceHolder.unlockCanvasAndPost(gameCanvas);
			}
		}
	}
	boolean OnPause()
	{
		return !pause;
	}
	void pleaseStart()
	{
		pause = true;
	}
	void pleasePause()
	{
		pause = false;
	}
	void DrawBack(Canvas canvas)
	{
		Paint paint =new Paint();
		paint.setARGB(255,0,127,65);
		canvas.drawRect(0,0,width,y1,paint);
		canvas.drawRect(0,y1,x1,y2,paint);
		canvas.drawRect(x2,y1,width,y2,paint);
		canvas.drawRect(0,y2,width,height,paint);
		paint.setColor(Color.GRAY);
		canvas.drawRect((float)(x2+width*0.03),(float)(y1),(float)(x2+width*0.23),(float)(y1+height*0.40),paint);
		if(choosedCell>=game.Cells.size())
			choosedCell=-1;
		if(choosedCell!=-1)
		{
			choosed=game.Cells.get(choosedCell).clone(game.Cells.get(choosedCell));
			choosedCell=-1;
		}
		if(choosed!=null)
			drawStats(choosed,canvas);
		canvas.drawRect(pauseRect,paint);
		paint.setTextSize((float)(40));
		paint.setColor(Color.BLACK);
		canvas.drawText("PAUSE",(float)(x2+width*0.05),(float)(y1+height*0.51),paint);
	}

	public void drawStats(Cell c,Canvas canvas) {
		Paint paint =new Paint();
		paint.setARGB(255,255,200,0);
		canvas.drawText("E:", (float) (x2+width*0.05),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.Energy), (float) (x2+width*0.06),(float)(y1+height*0.03),paint);
		paint.setARGB(255,0,0,255);
		canvas.drawText("M:",(float) (x2+width*0.08),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.Mineral), (float) (x2+width*0.09),(float)(y1+height*0.03),paint);
		paint.setARGB(255,255,0,0);
		canvas.drawText("K:",(float) (x2+width*0.11),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.kills), (float) (x2+width*0.12),(float)(y1+height*0.03),paint);
		paint.setARGB(255,255,0,255);
		canvas.drawText("Age:",(float) (x2+width*0.13),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.age), (float) (x2+width*0.15),(float)(y1+height*0.03),paint);
		paint.setARGB(255,0,255,127);
		canvas.drawText("Muts:",(float) (x2+width*0.17),(float)(y1+height*0.03),paint);
		canvas.drawText(String.valueOf(c.muts), (float) (x2+width*0.19),(float)(y1+height*0.03),paint);
		paint.setARGB(255,255,0,0);
		canvas.drawText("R:",(float) (x2+width*0.05),(float)(y1+height*0.03+235),paint);
		canvas.drawText(String.valueOf(c.r), (float) (x2+width*0.06),(float)(y1+height*0.03+235),paint);
		paint.setARGB(255,0,255,0);
		canvas.drawText("G:",(float) (x2+width*0.08),(float)(y1+height*0.03+235),paint);
		canvas.drawText(String.valueOf(c.g), (float) (x2+width*0.09),(float)(y1+height*0.03+235),paint);
		paint.setARGB(255,0,0,255);
		canvas.drawText("B:",(float) (x2+width*0.11),(float)(y1+height*0.03+235),paint);
		canvas.drawText(String.valueOf(c.b), (float) (x2+width*0.12),(float)(y1+height*0.03+235),paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				c.setCurrent(i*8+j);
				paint.setColor(Color.BLACK);
				canvas.drawRect((float)(x2+width*0.05+i*25),(float)(y1+height*0.05+j*25),(float)(x2+width*0.05+i*25+25),(float)(y1+height*0.05+j*25+25),paint);
				if(c.getCurMind()==16||c.getCurMind()==17||c.getCurMind()==34)
					paint.setARGB(255,255,200,0);
				if(c.getCurMind()>=18&&c.getCurMind()<26)
					paint.setColor(Color.GREEN);
				if(c.getCurMind()>=43&&c.getCurMind()<51)
					paint.setColor(Color.BLUE);
				if(c.getCurMind()==51)
					paint.setARGB(255,255,0,255);
				if(c.getCurMind()>=35&&c.getCurMind()<43)
					paint.setColor(Color.RED);
				if(c.getCurMind()>=8&&c.getCurMind()<16)
					paint.setColor(Color.WHITE);
				canvas.drawText(String.valueOf(c.getCurMind()),(float)(x2+width*0.05+i*25+5),(float)(y1+height*0.05+j*25+15),paint);
			}
	}
}
