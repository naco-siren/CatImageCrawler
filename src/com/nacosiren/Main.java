package com.nacosiren;

public class Main {

    public static void main(String[] args) {
        /* Top quality, small quantity */
        BestFriendsCatCrawler bestFriendsMaleCatCrawler = new BestFriendsCatCrawler(CatGender.MALE);
        bestFriendsMaleCatCrawler.startCrawling();

        BestFriendsCatCrawler bestFriendsFemaleCatCrawler = new BestFriendsCatCrawler(CatGender.FEMALE);
        bestFriendsFemaleCatCrawler.startCrawling();


        /* Average quality, unlimited quantity */
        AdoptAPetCatCrawler adoptAPetMaleCatCrawler = new AdoptAPetCatCrawler(CatGender.MALE);
        adoptAPetMaleCatCrawler.startCrawling();

        AdoptAPetCatCrawler adoptAPetFemaleCatCrawler = new AdoptAPetCatCrawler(CatGender.FEMALE);
        adoptAPetFemaleCatCrawler.startCrawling();

    }
}
