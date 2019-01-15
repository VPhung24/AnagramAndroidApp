/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static ArrayList<String> wordList = new ArrayList<String>();
    private static HashSet<String> wordSet = new HashSet<String>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private Random random = new Random();
    private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private ArrayList<String> addOneLetter;
    private HashMap<Integer,  ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);
            if(lettersToWord.containsKey(sortLetters(word))) {
                //
                // if has key, add word to the key
                //

                lettersToWord.get(sortLetters(word)).add(word);


                // when in error: Log.d(tag: "shawn", anagrams.toString());
                // press logcat on bottom of screen
                // search ie. "shawn" and find problems
                // another way, press the button two away from play after adding red dots at left side of screen

                // if key does not exist
            } else {
                // if key does not exist yet, do not add word to key
                ArrayList<String> values = new ArrayList<String>();
                values.add(word);
                lettersToWord.put(sortLetters(word), values);
            };
            if(sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> wordsOfSize = new ArrayList<String>();
                wordsOfSize.add(word);
                sizeToWords.put(word.length(), wordsOfSize);
            };
        };
    };

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word) && !base.contains(word)) {
            return true;
        } else {
            return false;
        }
    }

    public static String sortLetters(String input){

        char tempArray[] = input.toCharArray();

        Arrays.sort(tempArray);

        return new String(tempArray);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        result = lettersToWord.get(sortLetters(targetWord));
        Log.d("Result", result.toString());
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        HashSet<String> wordSetForAddLetter = new HashSet<String>();
        for (char letter : alphabet) {
            String sortedLetters = sortLetters(word + letter);
            if (lettersToWord.containsKey(sortedLetters)) {
                wordSetForAddLetter.addAll(lettersToWord.get(sortedLetters));
            }
        }
        ArrayList<String> result = new ArrayList<String>(wordSetForAddLetter);
        Log.d("Result for One More Letter", result.toString());
        return result;
    }

    public String pickGoodStarterWord() {
        Random rand = new Random();
        int rand_int = rand.nextInt(wordList.size());
        String word = wordList.get(rand_int);
        if (lettersToWord.get(sortLetters(word)).size() >= MIN_NUM_ANAGRAMS) {
            return word;
        } else {
            return pickGoodStarterWord();
        }
    }
}
