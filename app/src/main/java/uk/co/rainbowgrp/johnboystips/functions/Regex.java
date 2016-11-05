package uk.co.rainbowgrp.johnboystips.functions;

/**
 * Created by Martin on 9/8/2016.
 */
public class Regex {

    public static boolean isEmail(String email) {

        String regex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]+[a-z0-9-]*[a-z0-9]+";
        return email.matches(regex);

    }

}
