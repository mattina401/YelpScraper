/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Item {
    private SimpleStringProperty name;
    private SimpleIntegerProperty count;
    
    public Item (String nm, int cnt) {
        name = new SimpleStringProperty(nm);
        count = new SimpleIntegerProperty(cnt);
    }
    
    public String getName() {
        return name.get();
    }
    
    public int getCount() {
        return count.get();
    }
    
    public void setName(String nm) {
        name.set(nm);
    }

    public void setCount(int cnt) {
        count.set(cnt);
    }
}
