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
public class AdoptAPetCatCrawler extends CatCrawler {
    public static final String URL_HEADER = "http://www.adoptapet.com/cat-adoption/search/250/miles/";
    public static final String[] LOCATIONS = new String[]{"NEW%20YORK%2C%20NY", "SEATTLE%2C%20WA", "TACOMA%2C%20WA", "BELLEVUE%2C%20WA", "SAN%20FRANCISCO%2C%20CA", "BERKELEY%2C%20CA", "PALO%20ALTO%2C%20CA", "MOUNTAIN%20VIEW%2C%20CA", "SUNNYVALE%2C%20CA", "SAN%20JOSE%2C%20CA", "CHICAGO%2C%20IL", "PHILADELPHIA%2C%20PA", "ORLANDO%2C%20FL", "INDIANAPOLIS%2C%20IN", "DETROIT%2C%20MI", "MILWAUKEE%2C%20WI", "DALLAS%2C%20TX", "ATLANTA%2C%20GA", ""};

    /**
     * Constructor
     */
    public AdoptAPetCatCrawler (CatGender catGender){
        super(catGender);

        this._pageCount = 13;

        switch (_catGender) {
            case MALE:
                this._outDirName = "[Adopt A Pet] Male";
                break;
            case FEMALE:
                this._outDirName = "[Adopt A Pet] Female";
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


        /* Crawl different places */
        this._startCatIndex = 0;

        for (String location : LOCATIONS) {
            System.out.println("=== Start crawling " + location + "! ===");

            /* Build URL given the location and cat gender */
            StringBuilder urlBuilder = new StringBuilder(URL_HEADER);
            urlBuilder.append(location);
            switch (_catGender) {
                case MALE:
                    urlBuilder.append("?sex=m&current_page=");
                    break;
                case FEMALE:
                    urlBuilder.append("?sex=f&current_page=");
                    break;
            }
            this._url = urlBuilder.toString();

            /* Crawl each page given the location */
            for (int pageIndex = 1; pageIndex <= _pageCount; pageIndex++) {
                crawlPage(pageIndex, _startCatIndex);
            }
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
        Element resultsWrapperEle = _document.body().getElementsByClass("results_wrapper").first();

        ArrayList<Element> divCatItems = new ArrayList<>();
        for (Element catItem : resultsWrapperEle.children()) {
            if (catItem.className().equals("pet_results rounded_corner")) {
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
                Element span = catItem.child(0);
                Element a = span.child(0);
                Element img = a.child(0);
                imageHref = img.attr("src");
            }

            // Name
            String name = null;
            String specHref = null;
            {
                Element p = catItem.child(1);
                Element a = p.child(0);
                name = a.text().trim();
                specHref = a.attr("href");
            }

            // Description
            String description = null;
            {
                Element a = catItem.child(3);
                description = a.text().trim();
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
