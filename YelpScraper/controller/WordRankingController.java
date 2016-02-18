/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject.controller;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import cs4440teamproject.MenuWordCount;
import cs4440teamproject.SceneNavigator;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Keun Lee
 */
public class WordRankingController implements Initializable {
    private static final String[] CHECKWORDS = {"la", "de", "and", "or", "my", "of", "in", "di", "e", "-", "a", "b", "c", "s", "d", "r", "u"};
    
    @FXML private TableView tableView;
    @FXML private TableColumn wordCol;
    @FXML private TableColumn countCol;
    
    private void makeCollectionsFillTable() {
        Mongo mongo = new MongoClient();
        DB db = mongo.getDB("test");
        DBCollection collection = db.getCollection("storeCollection");
        DBCollection menuColl = db.getCollection("menuCollection");
        menuColl.drop();
        DBObject query = new BasicDBObject("menu", new BasicDBObject("$exists", true));
        DBCursor cursor = collection.find(query);
        BasicDBList list;
        BasicDBObject dbObject;
        BasicDBObject menuObject;
        List<String> wordList = new ArrayList();
        //BasicDBList wordList;
        String[] words;
        Map<String, Integer> wordCount = new HashMap();
        int count;
        String tempStr;
        while(cursor.hasNext()) {
            dbObject = (BasicDBObject) cursor.next(); //object=>each store
            list = (BasicDBList) dbObject.get("menu"); //list=>menu list of each store
            if(!list.isEmpty()) {
                for(int i = 0; i < list.size(); i++) {
                    dbObject = (BasicDBObject) list.get(i); //{name:$$,price:$$} each menu entry of a store
                    words = dbObject.get("name").toString().split(" ");
                    menuObject = new BasicDBObject();
                    wordList.clear();
                    //wordList = new BasicDBList();
                    for(String str : words) {
                        str = str.replaceAll("[0-9:.,'()#&%$/*\"]","");
                        str = str.toLowerCase();
                        if(Arrays.asList(CHECKWORDS).contains(str)) {
                            continue;
                        }
                        if(str.compareTo("") != 0) {
                            if(wordCount.containsKey(str)) {
                                count = wordCount.get(str);
                                wordCount.put(str, ++count);
                            } else {
                                wordCount.put(str, 1);
                            }
                            wordList.add(str);
                            //wordList.add(new BasicDBObject("word", str));
                        }
                    }
                    tempStr = dbObject.get("name").toString();
                    if(tempStr.compareTo("") != 0) {
                        menuObject.put("name", tempStr);
                        tempStr = dbObject.get("price").toString();
                        if(tempStr.compareTo("") != 0 && !tempStr.contains(" ")) {
                            menuObject.put("price", tempStr);
                        }
                        menuObject.put("words", wordList);
                        menuColl.insert(menuObject);
                    }
                }
            }
        }
        DBCollection wordCollection = db.getCollection("wordCollection");
        wordCollection.drop();
        wordCol.setCellValueFactory(new PropertyValueFactory<MenuWordCount, String>("word"));
        countCol.setCellValueFactory(new PropertyValueFactory<MenuWordCount, Integer>("count"));
        ObservableList<MenuWordCount> observeList = tableView.getItems();
        for(Entry<String, Integer> entry : wordCount.entrySet()) {
            dbObject = new BasicDBObject();
            dbObject.put("word", entry.getKey());
            dbObject.put("count", entry.getValue());
            observeList.add(new MenuWordCount(entry.getKey(), entry.getValue()));
            wordCollection.insert(dbObject);
        }
    }
    
    private void fillMenuCollection() {
        Mongo mongo = new MongoClient();
        DB db = mongo.getDB("test");
        DBCollection collection = db.getCollection("storeCollection");
        DBObject query = new BasicDBObject("menu", new BasicDBObject("$exists", true));
        DBCursor cursor = collection.find(query);
    }
    
    @FXML
    public void buttonOneClick() {
        SceneNavigator.switchScene(SceneNavigator.FREQUENT);
    }
    
    @FXML
    public void buttonTwoClick() {
        SceneNavigator.switchScene(SceneNavigator.WELCOME);
    }
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeCollectionsFillTable();
    }
}
