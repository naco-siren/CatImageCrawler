package com.nacosiren;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Created by naco_siren on 4/9/17.
 */
public class CatCrawler {
    /* Params: */
    protected static final int TIMEOUT_LIMIT = 8000;
    protected static final int GET_HTML_RETRY_TIMES = 3;
    protected static final int DOWNLOAD_RETRY_TIMES = 3;

    /* Input */
    protected CatGender _catGender;

    protected String _url;
    protected int _pageCount;

    protected String _outDirName;

    /* Data: */
    protected int _startCatIndex;
    protected Document _document;


    protected CatCrawler(CatGender catGender){
        this._catGender = catGender;
    }

    /**
     * Entrance to crawling
     */
    protected void startCrawling(){
        throw new NotImplementedException();
    }

    /**
     * Crawl all the cats on the given page
     * @param pageIndex
     * @param startCatIndex
     */
    protected void crawlPage(int pageIndex, int startCatIndex) {
        throw new NotImplementedException();
    }

    /**
     * Fetch and parse the HTML of given URL
     * @param pageIndex
     * @return 0 on success
     */
    protected int fetchPage(int pageIndex){
        _document = null;

        boolean hasGotHTML = false;
        int getHTMLRetryTimes = 0;
        while (hasGotHTML == false && getHTMLRetryTimes < GET_HTML_RETRY_TIMES) {
            //isSuccess = true;
            System.out.println("> Getting page #" + pageIndex + ", trial #" + getHTMLRetryTimes);

            try {

                /* Fetch and parse the web page's HTML document */
                _document = Jsoup.connect(_url + pageIndex)
                        .timeout(TIMEOUT_LIMIT)
                        .get();

                hasGotHTML = true;
                return 0;

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());

                getHTMLRetryTimes++;
                hasGotHTML = false;
            }
        }

        return -1;
    }


    /**
     * Save cat's info and download its image
     * @param catInfo
     * @return 0 on success
     */
    protected int crawlCat(CatInfo catInfo){
        /* Save cat info in a text file */
        String infoOutputFileName = _outDirName + File.separator + catInfo._index + ".txt";
        try {
            PrintWriter writer = new PrintWriter(new File(infoOutputFileName));

            writer.println(catInfo._index);
            writer.println(catInfo._name);
            writer.println(catInfo._description);
            writer.println(catInfo._specHref);

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());

            return -1;
        }


        /* Download the cat image */
        String imageOutputFileName = _outDirName + File.separator + catInfo._index + ".jpg";

        boolean hasDownloadedImage = false;
        int downloadRetryTimes = 0;
        while (hasDownloadedImage == false && downloadRetryTimes < DOWNLOAD_RETRY_TIMES) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new URL(catInfo._imageHref));
                File file = new File(imageOutputFileName);
                ImageIO.write(bufferedImage, "jpg", file);

                hasDownloadedImage = true;
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());

                downloadRetryTimes++;
                hasDownloadedImage = false;
            }
        }

        return -1;
    }
}
