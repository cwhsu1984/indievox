package com.example.indievox.parser;

public class Album {
	public String cover;
	public String band;
	public String record;
	public String album;
	public String type;
	public String date;
	
	public Album(String c, String b, String r, String a, String t, String d) {
		cover = c;
		band = b;
		record = r;
		album = a;
		type = t;
		date = d;
	}
	
	public String toString() {
		return "cover: " + cover + "\n" +
				"band: " + band + "\n" +
				"record: " + record + "\n" +
				"album: " + album + "\n" +
				"type: " + type + "\n" +
				"date: " + date + "\n";
	}
}
