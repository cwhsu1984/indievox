package com.example.indievox.parser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AlbumParser {
	/* Example of parsing data.
	<div class="disc-block">
	      <div class="disc-art">
	         <span class="quick-play-button invisible" style=""><a data-id="6036" data-type="disc"><img src="/iv-asset/image/ui-icon/icon_play_bt.png" alt="播放" border="0"></a></span>         <!--<span class="quick-play-button invisible">
	            <a data-id="6036" data-type="disc">
	               <img src="/iv-asset/image/ui-icon/icon_play_bt.png" alt="播放" border="0" />
	            </a>
	         </span>-->
	         <a class="main-board-pjax" href="/disc/6036">
	            <img src="http://cdn2-music.indievox.com/indievox_music/mp3/30000/22773/603670X70.jpg?1377118691" border="0" width="70" height="70">
	         </a>
	      </div>
	      <div class="disc-block-details">
	         <a class="main-board-pjax link-black font-bolder" href="/disc/6036">
	            Sacrifice Mountain Hills         </a>
	         <br>
	         <a class="main-board-pjax link-black" href="/skipskipbenben">
	            skip skip ben ben         </a>
	         <div class="font-size-11">
	            形式：            <span class="song-details-value">
	               專輯            </span>
	            <br>
	            <span class="song-details-value">
	               另類            </span>
	            <br>
	            發行日期：            <span class="song-details-value">
	               2012-11-23            </span>
	            <br>
	                        廠牌：            <span class="song-details-value">
	                              <a class="main-board-pjax" href="/22records">
	               22RECORDS               </a>
	                           </span>
	                     </div>
	      </div>
	   </div>
	 */

	public static void parse(ArrayList<Album> albums) {
		try {
			Document doc = Jsoup.parse(new URL("http://www.indievox.com/music/new-release/disc"), 5000);
			Elements blocks = doc.select("div.disc-block");
			for (Element block : blocks) {
				Element disc = block.select("div.disc-block-details").first();
				Element band = disc.select("a[href]").first();
				Element record = disc.select("a[href]").get(1);
				Elements detail = disc.select("span.song-details-value");

				/* debug message
				System.out.println(block.select("a.main-board-pjax").select("[src]").attr("abs:src")); // record: http://cdn2-music.indievox.com/indievox_music/mp3/30000/22773/603670X70.jpg?1377118691
				System.out.println(band.text());  // band: Sacrifice Mountain Hills
				System.out.println(record.text());  // record: skip skip ben ben
				System.out.println(detail.get(0).text()); // album: 專輯
				System.out.println(detail.get(1).text()); // type: 另類
				System.out.println(detail.get(2).text()); // date: 2012-11-23
				 */

				albums.add(new Album(
						block.select("a.main-board-pjax").select("[src]").attr("abs:src"), // cover
						band.text(), // band
						record.text(), // record
						detail.get(0).text(), // album
						detail.get(1).text(), // type
						detail.get(2).text() // date
						)
						);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
