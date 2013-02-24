package com.example.multimodal2;

import multimodal.FuzzyTime;
import multimodal.schedule.Room;

public class UserAction {
	enum CommandType{
	    DISPLAY, CANCEL, MOVE, BOOK, WHEN, WHERE, WHO
	};
	Room exactLocation;
	FuzzyTime time;
}
