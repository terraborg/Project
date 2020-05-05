package com.example.myapplication;

import android.graphics.Rect;

public class PauseButton extends CanvasButton {
	PauseButton(Rect rect, String msg, DrawThread drawThread) {
		super(rect, msg, drawThread);
	}
	@Override
	void onTouch() {
		super.onTouch();
		if(super.getDrawThread().OnPause())
		{
			super.getDrawThread().pleaseStart();
		}
		else
			super.getDrawThread().pleasePause();
		super.Switch();
	}

}
