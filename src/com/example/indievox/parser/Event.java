package com.example.indievox.parser;

public class Event {
	public String poster;
	public String date;
	public String event;
	public String band;
	public String place;
	public String preorder;

	public Event(String p, String d, String e, String b, String pl, String pre) {
		poster = p;
		date = d;
		event = e;
		band = b;
		place = pl;
		preorder = pre;
	}

	public String toString() {
		return "poster: " + poster + "\n" + "date: " + date + "\n" + "event: "
				+ event + "\n" + "band: " + band + "\n" + "place: " + place
				+ "\n" + "preorder: " + preorder + "\n";
	}
}
