/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



/**
 *
 * @author Keun Lee
 */
public class YelpScraper {
    public static void scrape(String menu, String location, int limit) {
        Mongo m = new MongoClient();
        DB db = m.getDB("test");
        DBCollection coll = db.getCollection("storeCollection");
        coll.drop();
        BasicDBObject restaurant_doc = new BasicDBObject();
        String url, menuurl, trim, pagecounturl;
        int index;
        pagecounturl = "http://www.yelp.com/search?find_desc=" + menu + "&find_loc=" + location + "&ns=1&start=" + 1;
        Document count = null;
        try {
            count = Jsoup.connect(pagecounturl).timeout(100000).get();
        } catch(IOException e) {
            System.out.println("JSoup Connect Failed!");
            e.printStackTrace();
        }
        String countpage = count.select(".search-pagination").text();
        System.out.println(countpage);
        String countpagesplit [] = countpage.split(" ");
        System.out.println(countpagesplit);
        index = Integer.parseInt(countpagesplit[3]);
        index = (index > limit)? limit : index;
        for(int i = 0; i < index; i++) {
            url = "http://www.yelp.com/search?find_desc=" + menu + "&find_loc=" + location + "&ns=1&start=" + i * 10;
            System.out.println("\nFetching to " + url + "...");
            Document doc = null;
            try {
                    doc = Jsoup.connect(url).timeout(100000).get();
            } catch (IOException e2) {
                System.out.println("JSoup Connect Failed!!");
                e2.printStackTrace();
            }
            Elements media_stories = doc.select(".biz-listing-large");
            System.out.println(media_stories.toString());
            for (int t = 0; t < media_stories.size(); t++) {
                if (media_stories.get(t).select(".indexed-biz-name a").attr("href").length() > 1) {
                    restaurant_doc.put("restaurant", media_stories.get(t).select(".indexed-biz-name a").text());
                    restaurant_doc.put("address",media_stories.get(t).select(".secondary-attributes address").text());
                    restaurant_doc.put("rating", media_stories.get(t).select(".biz-rating i").attr("title").substring(0, 3));
                    restaurant_doc.put("contact", media_stories.get(t).select(".biz-phone").text());
                    if ((media_stories.get(t).select(".category-str-list a").text()).contains(" ")) {
                            restaurant_doc.put("category", (media_stories.get(t).select(".category-str-list a").text()).split(" ")[0]);
                    } else {
                            restaurant_doc.put("category", media_stories.get(t).select(".category-str-list a").text());
                    }
                    restaurant_doc.put("price range", media_stories.get(t).select(".business-attribute").text());


                    trim = media_stories.get(t).select(".indexed-biz-name a").attr("href").substring(5,media_stories.get(t).select(".indexed-biz-name a").attr("href").length());
                    menuurl = "http://www.yelp.com/menu/" + trim;

                    try {
                        Document menudoc = Jsoup.connect(menuurl).get();
                        Elements menu_media_stories = menudoc.select(".media-story");
                        StringBuffer menubuffer = new StringBuffer();
                        
                        BasicDBList menuList = new BasicDBList();
                        BasicDBObject menuObj;
                        String name;
                        String price;
                        for (int s = 0; s < menu_media_stories.size(); s++) {
                            menuObj = new BasicDBObject();
                            name = menu_media_stories.get(s).select(".menu-item-details h3").text();
                            price = menu_media_stories.get(s).select(".menu-item-price-amount").text();
                            menuObj.put("name", name);
                            menuObj.put("price", price);
//                            System.out.println("MENU: " + name + ", PRICE: " + price);
                            menuList.add(menuObj);
                        }
                        restaurant_doc.put("menu", menuList);
                    } catch (IOException e2) {
                            //restaurant_doc.put("menu", "NoMenu");
                            System.out.println("No menu url exists!");
                    }
                    coll.insert(restaurant_doc);
                    restaurant_doc.clear();
                }
            }
        }
        System.out.println("Scraping finished.");
    }
}