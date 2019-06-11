package com.vanberlo.blake.newname_android;

import java.util.HashMap;
import java.util.Map;

/**
 * A class containing various constants in the app
 */
public class Constants {

    public static final int MAX_NAME_LENGTH = 20; // Maximum allowable name length
    public static final int MAX_NUM_TRIES = 50; // Maximum allowable tries to generate a name that isn't in the training set

    // A mapping of integers to characters
    public static final Map<Integer, Character> IX_TO_CHAR = new HashMap<Integer, Character>(){
        {
            put(0, '\n');
            put(1, 'a');
            put(2, 'b');
            put(3, 'c');
            put(4, 'd');
            put(5, 'e');
            put(6, 'f');
            put(7, 'g');
            put(8, 'h');
            put(9, 'i');
            put(10, 'j');
            put(11, 'k');
            put(12, 'l');
            put(13, 'm');
            put(14, 'n');
            put(15, 'o');
            put(16, 'p');
            put(17, 'q');
            put(18, 'r');
            put(19, 's');
            put(20, 't');
            put(21, 'u');
            put(22, 'v');
            put(23, 'w');
            put(24, 'x');
            put(25, 'y');
            put(26, 'z');
        }};

    // A mapping of characters to integers
    public static final Map<Character, Integer> CHAR_TO_IX = new HashMap<Character, Integer>(){
        {
            put('\n', 0);
            put('a', 1);
            put('b', 2);
            put('c', 3);
            put('d', 4);
            put('e', 5);
            put('f', 6);
            put('g', 7);
            put('h', 8);
            put('i', 9);
            put('j', 10);
            put('k', 11);
            put('l', 12);
            put('m', 13);
            put('n', 14);
            put('o', 15);
            put('p', 16);
            put('q', 17);
            put('r', 18);
            put('s', 19);
            put('t', 20);
            put('u', 21);
            put('v', 22);
            put('w', 23);
            put('x', 24);
            put('y', 25);
            put('z', 26);
        }};

}
