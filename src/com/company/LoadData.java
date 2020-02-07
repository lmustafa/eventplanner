package com.company;

import java.util.*;

public class LoadData {

    LinkedList<LinkedList<String>> list = new LinkedList<>();

    /*
    *0 - name of event
    *1 - genre
    *2 - description
    *3 - local date
    *4 - local time
    *5 - postal code
    *6 - city name
    *7 - province
    *8 - country
    *9 - address
    *10 - venue
    */

    public void makeList() {
        for(int i = 0; i < 11; i ++) {
            list.add(new LinkedList<String>());
        }

        for(int j = 0; j < 11; j++) {
            for(int m = 0; m < 11; m++) {
                list.get(j).add(new String("Hello " + m + "!"));
            }
            //list.get(j).add(new String("Hello " + j + "!"));
        }

        for(LinkedList<String> sublist : list) {
            for(String o : sublist) {
                System.out.println(o);
            }
        }

//        for(int k = 0; k < 11; k++) {
//            list.get(k).add(new String("Hello " + j + "!"))
//        }
    }

}
