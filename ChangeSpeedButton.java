package com.example.myapplication;

import android.graphics.Rect;

public class ChangeSpeedButton extends CanvasButton {

	int value;

	ChangeSpeedButton(Rect rect, String msg, DrawThread drawThread, int value) {
		super(rect, msg, drawThread);
		this.value=value;
	}

	@Override
	void onTouch() {
		super.onTouch();
		super.getDrawThread().setSpeed(super.getDrawThread().getSpeed()+value);
	}
}
