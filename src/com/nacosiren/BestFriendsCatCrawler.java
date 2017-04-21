package com.nacosiren;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by naco_siren on 4/9/17.
 */
public class BestFriendsCatCrawler extends CatCrawler {

    /**
     * Constructor
     */
    public BestFriendsCatCrawler(CatGender catGender){
        super(catGender);

        //this._pageCount = pageCount;
        switch (_catGender) {
            case MALE:
                this._url = "http://bestfriends.org/adopt/adopt-our-sanctuary/cats?animalBreed=All&animalGeneralAge=All&animalGeneralSizePotential=All&animalColor=All&animalSex=1&animalName=&page=";
                _pageCount = 18;

                this._outDirName = "[Best Friends] Male";
                break;

            case FEMALE:
                this._url = "http://bestfriends.org/adopt/adopt-our-sanctuary/cats?animalBreed=All&animalGeneralAge=All&animalGeneralSizePotential=All&animalColor=All&animalSex=2&animalName=&page=";
                _pageCount = 18;

                this._outDirName = "[Best Friends] Female";
                break;

            default:
                break;
        }

    }

    /**
     * Entrance to crawling
     */
    @Override
    public void startCrawling(){
        //Paths.get(".").toAbsolutePath().normalize().toString();
        File directory = new File(_outDirName);
        if (directory.exists() == false || directory.isFile()) {
            if(directory.mkdir() == false) {
                System.err.println("> Failed to make directory! Crawling terminated!");
                return;
            }
        }

        this._startCatIndex = 0;
        for (int pageIndex = 1; pageIndex <= _pageCount; pageIndex++) {
            crawlPage(pageIndex, _startCatIndex);
        }
    }

    @Override
    public void crawlPage(int pageIndex, int startCatIndex) {
        /* Fetch and parse HTML */
        if (fetchPage(pageIndex) == -1) {
            System.out.println("> Failed to get page #" + pageIndex + ", continue next page!");
            return;
        }



        /* Locate the cats */
        Element eleBlockSystemMain = _document.body().getElementById("block-system-main");
        Element root = eleBlockSystemMain.getElementsByClass("view-content").first();

        ArrayList<Element> divCatItems = new ArrayList<>();
        for (Element catItem : root.children()) {
            if (catItem.className().equals("views-row animal-item-view")) {
                divCatItems.add(catItem);
            }
        }
        int catsCount = divCatItems.size();
        System.out.println("> " + catsCount + " cats are found!");


        /* Process cat items one by one */
        for (int i = 0; i < catsCount; i++) {
            /* Extract cat info */
            Element catItem = divCatItems.get(i);

            // Image
            String imageHref = null;
            {
                Element div = catItem.child(0);
                Element a = div.child(0);
                Element img = a.child(0);
                imageHref = img.attr("src");
            }

            // Name
            String name = null;
            String specHref = null;
            {
                Element div = catItem.child(1);
                Element a = div.child(0);
                name = a.text().trim();
                specHref = a.attr("href");
            }

            // Description
            String description = null;
            {
                Element h2 = catItem.child(2);
                description = h2.text().trim();
            }

            // Cat info instance
            CatInfo catInfo = new CatInfo(startCatIndex, name, _catGender, imageHref);
            catInfo._description = description;
            catInfo._specHref = specHref;



            /* Save cat info in a text file and download the image */
            if (crawlCat(catInfo) == -1) {
                System.out.println("> Failed to download #" + catInfo._index + ", continue next cat!");
                continue;
            }

            startCatIndex++;
        }

        /* Update next page's start index */
        _startCatIndex = startCatIndex;

    }


}
