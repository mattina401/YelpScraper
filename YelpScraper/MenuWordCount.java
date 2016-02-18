/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Keun Lee
 */
public class MenuWordCount {
    private SimpleStringProperty word;
    private SimpleStringProperty count;
    
    public MenuWordCount (String wd, int cnt) {
        word = new SimpleStringProperty(wd);
        count = new SimpleStringProperty(String.valueOf(cnt));
    }
    
    public String getWord() {
        return word.get();
    }
    
    public int getCount() {
        return Integer.parseInt(count.get());
    }
    
    public void setWord(String wd) {
        word.set(wd);
    }

    public void setCount(int cnt) {
        count.set(String.valueOf(cnt));
    }
}
