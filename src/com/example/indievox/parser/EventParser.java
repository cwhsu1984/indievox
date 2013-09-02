package com.example.indievox.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EventParser {
	/* Example of paring data.
	<div class="list-detail-block-wide">
	   <div class="list-detail-ico">
	      <div class="padding-h-m">
	         <a class="main-board-pjax" href="/riversidemusic/event-post/11641">
	            <img class="margin-bottom-m1" src="http://cdn2-data.indievox.com/indievox_user/180000/160850/event/event1164170.jpg?1375348652" border="0">
	         </a>
	      </div>
	   </div>
	   <div class="list-detail-info-event-wide">
	      <span class="font-bold">
	         2013-08-22(Thu)
	      </span>
	      <br>
	      <span class="list-detail-title">
	         <a class="main-board-pjax" href="/riversidemusic/event-post/11641">
	            �L���M / ������C         </a>
	      </span>
	      <br>
	      ���a�G            <a href="http://www.riverside.com.tw/index.php?option=com_cafe" target="_blank">
	         �e���d�� ��������@��      </a>
	            (�x�_)
	      <br>
	      �ɶ��G21:00~23:00   </div>
	   <div class="list-detail-rank-event-wide">
	      <span class="font-bold">
	         <a class="main-board-pjax " href="/elisa010">�L���M</a>,<a class="main-board-pjax " href="/thevoiceoflife">������C</a>
	      &nbsp;
	   </span></div>
	   <div class="list-detail-func-event-wide">
	      <div style="line-height:1.2em;">�Цܲ{���ʲ��A�⧹����C</div>   </div>
	   <br class="clearboth">
	 */

	public static void parse(ArrayList<Event> events) {
		try {
			Document doc = Jsoup.parse(new URL("http://www.indievox.com/event/ticket/"), 5000);
			Elements blocks = doc.select("div.list-detail-block-wide");
			for (Element block : blocks) {
				Element info = block.select("div.list-detail-info-event-wide").first();
				Element rank = block.select("div.list-detail-rank-event-wide").first();
				Element func = block.select("div.list-detail-func-event-wide").first();

				/* debug message
				System.out.println(block.select("div.list-detail-ico").select("[src]").attr("abs:src")); // poster: http://cdn-data.indievox.com/indievox_user/180000/160850/event/event1155170.jpg?1375348649
				System.out.println(info.select("span.font-bold").text()); // date: 2013-08-21(Wed)
				System.out.println(info.select("span.list-detail-title").text()); //event: Morphineside �ܰ����p / Dream Toy �ڦ��l
				System.out.println(info.select("[target]").text()); //place: �e���d�� ��������@�� �Цܲ{���ʲ��A�⧹����C or �⧹
				System.out.println(rank.text().substring(0, rank.text().length() - 1)); // band: �ܰ����p,Dream Toy �ڦ��l
				 */

				events.add(new Event(block.select("div.list-detail-ico").select("[src]").attr("abs:src"),
						info.select("span.font-bold").text(),
						info.select("span.list-detail-title").text(),
						rank.text().substring(0, rank.text().length() - 1),
						info.select("[target]").text(),
						func.text())
						);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
