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
    private Random random = new Random();
    // private ArrayList<String> wordList= new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> letterToWord = new HashMap<>();
    private HashMap<Integer,ArrayList<String>> sizeToWord = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    private static final String TAG = "dictionary";

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        String sortedInDict;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            sortedInDict = sortLetters(word);
            int wL = word.length();

            if (letterToWord.containsKey(sortedInDict)) {
                letterToWord.get(sortedInDict).add(word);

            }
            else {
                letterToWord.put(sortedInDict, new ArrayList<String>());

                // add the word itself
                letterToWord.get(sortedInDict).add(word);
            }

            if (sizeToWord.containsKey(wL)) {
                sizeToWord.get(wL).add(word);

            } else {
                sizeToWord.put(wL, new ArrayList<String>());

                // add the word itself
                sizeToWord.get(wL).add(word);
            }

        }

//       for(String w: wordSet) {
//           if(letterToWord.get(sortLetters(w)).size()< MIN_NUM_ANAGRAMS){
//               letterToWord.remove(sortLetters(w));
//           }



//        }




      // Log.i("Log Activity", " " + sizeToWord.get(4).toString());
    }

    private String sortLetters(String word) {
        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return new String(wordArray);
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word)) {
            return false;
        }

        if (word.contains(base)) {
            return false;
        }

        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        if (letterToWord.containsKey(sortLetters(targetWord))) {
            return letterToWord.get((sortLetters(targetWord)));
        }
        return null;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for (char c = 'a'; c < 'z'; c++) {
            String extra = word + c;

            List<String> anagrams = getAnagrams(extra);

            // check if each anagram is a good word

            if (anagrams != null) {
                for (String s : anagrams) {
                    if (isGoodWord(s, word)) {
                        result.add(s);
                    }
                }
            }
        }
        return result;


    }

    public String pickGoodStarterWord() {
        ArrayList<String> minAnagramList = new ArrayList<>();//list of words that satisfy both of conditions
        //Log.i(TAG, "size to word contain" + sizeToWord.containsKey(wordLength));



        for (String key : sizeToWord.get(wordLength)) {
           // Log.i(TAG, "sizeToWord(" + wordLength+") = " + sizeToWord.get(wordLength));
            if (getAnagramsWithOneMoreLetter(key).size() >= MIN_NUM_ANAGRAMS) {
                minAnagramList.add(key);
               // Log.i(TAG, "added " + key);

                }
            }
         //   Log.i(TAG, "minAnagramList" + minAnagramList.size());
       // Log.i(TAG, "minAnagramList" + minAnagramList.toString());


      String randomWord = minAnagramList.get(random.nextInt(minAnagramList.size()));
       if (wordLength < MAX_WORD_LENGTH) {
           wordLength++;
        }
        return randomWord;
    }
    }


