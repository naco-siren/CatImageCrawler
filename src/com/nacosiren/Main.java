package com.nacosiren;

public class Main {

    public static void main(String[] args) {
        //BestFriendsCatCrawler bestFriendsMaleCatCrawler = new BestFriendsCatCrawler(CatGender.MALE);
        //bestFriendsMaleCatCrawler.startCrawling();

        //BestFriendsCatCrawler bestFriendsFemaleCatCrawler = new BestFriendsCatCrawler(CatGender.FEMALE);
        //bestFriendsFemaleCatCrawler.startCrawling();

        AdoptAPetCatCrawler adoptAPetMaleCatCrawler = new AdoptAPetCatCrawler(CatGender.MALE);
        adoptAPetMaleCatCrawler.startCrawling();

        AdoptAPetCatCrawler adoptAPetFemaleCatCrawler = new AdoptAPetCatCrawler(CatGender.FEMALE);
        adoptAPetFemaleCatCrawler.startCrawling();

    }
}
