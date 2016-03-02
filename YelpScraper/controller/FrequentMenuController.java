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
import cs4440teamproject.Item;
import cs4440teamproject.SceneNavigator;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;


public class FrequentMenuController implements Initializable {
    @FXML private TreeTableView treeTable;
    @FXML private TreeTableColumn menuCol;
    @FXML private TreeTableColumn countCol;
    
    private void makeMatchCollection() {
        Mongo mongo = new MongoClient();
        DB db = mongo.getDB("test");
        DBCollection collection = db.getCollection("wordCollection");
        DBCollection menuColl = db.getCollection("menuCollection");
        DBCollection matchColl = db.getCollection("matchCollection");
        matchColl.drop();
        DBCursor cursor = collection.find().sort(new BasicDBObject("count", -1));
        int count = Integer.parseInt(cursor.next().get("count").toString()) / 20;
        DBObject query = new BasicDBObject("count", new BasicDBObject("$gt", (count > 1)? count : 1));
        cursor = collection.find(query);
        int pairSize = cursor.size();
        pairSize = pairSize * (pairSize + 1) / 2 - pairSize;
        int workCounter = 0;
        Queue<String> firstWords = new LinkedList();
        List<String> secondWords = new ArrayList();
        BasicDBObject dbObject;
        while(cursor.hasNext()) {
            dbObject = (BasicDBObject) cursor.next();
            firstWords.add(dbObject.getString("word"));
            secondWords.add(dbObject.getString("word"));
        }
        secondWords.remove(0);
        BasicDBObject menuObject;
        BasicDBList menuList;
        String fstWord;
        String mapKey;
        Map<String, Integer> menuMap = new HashMap();
        List<String> pairList = new ArrayList();
        List<BasicDBObject> qryList = new ArrayList();
        while(firstWords.size() > 1) {
            fstWord = firstWords.poll();
            for(String secWord : secondWords) {
                workCounter++;
                pairList.clear();
                pairList.add(fstWord);
                pairList.add(secWord);            
                qryList.clear();
                qryList.add(new BasicDBObject("words", fstWord));
                qryList.add(new BasicDBObject("words", secWord));
                query = new BasicDBObject("$and", qryList);
                cursor = menuColl.find(query);
                menuMap.clear();
                menuList = new BasicDBList();
                while(cursor.hasNext()) {
                    mapKey = cursor.next().get("name").toString();
                    if(menuMap.containsKey(mapKey)) {
                        count = menuMap.get(mapKey);
                        menuMap.put(mapKey, ++count);
                    } else {
                        menuMap.put(mapKey, 1);
                    }
                }
                if(!menuMap.isEmpty()) {
                    count = 0;
                    for(Entry entry : menuMap.entrySet()) {
                        menuObject = new BasicDBObject("name", entry.getKey());
                        menuObject.put("count", entry.getValue());
                        menuList.add(menuObject);
                        count += (int) entry.getValue();
                    }
                    dbObject = new BasicDBObject();
                    dbObject.put("words", pairList);
                    dbObject.put("count", count);
                    dbObject.put("menus", menuList);
                    matchColl.insert(dbObject);
                }
                System.out.println(workCounter + " / " + pairSize);
            }
            secondWords.remove(0);
        }
        System.out.println("Done, check your matchCollection");        
    }
    
    private void fillTreeTable() {
        Mongo mongo = new MongoClient();
        DB db = mongo.getDB("test");
        DBCollection collection = db.getCollection("matchCollection");
        
        DBObject query = new BasicDBObject("count", new BasicDBObject("$gt", 1));
        DBCursor cursor = collection.find(query);
        cursor.sort(new BasicDBObject("count", -1));
        
        TreeItem<Item> root = new TreeItem(new Item("Root", 0));
        TreeItem<Item> parent;
        
        String words;
        BasicDBObject dbObject;
        BasicDBObject menuObject;
        BasicDBList dbList;
        while(cursor.hasNext()) {
            dbObject = (BasicDBObject) cursor.next();
            words = dbObject.getString("words");
            parent = new TreeItem(new Item(words, dbObject.getInt("count")));
            root.getChildren().add(parent);
            dbList = (BasicDBList) dbObject.get("menus");
            for(Object obj : dbList) {
                menuObject = (BasicDBObject) obj;
                parent.getChildren().add(new TreeItem(new Item(menuObject.
                        getString("name"), menuObject.getInt("count"))));
            }
        }
        
        menuCol.setCellValueFactory(new TreeItemPropertyValueFactory<Item, String>("name"));
        countCol.setCellValueFactory(new TreeItemPropertyValueFactory<Item, Integer>("count"));
        treeTable.setRoot(root);
        treeTable.setShowRoot(false);
    }
    
    @FXML
    public void buttonClick() {
        SceneNavigator.switchScene(SceneNavigator.WELCOME);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeMatchCollection();
        fillTreeTable();
    }
}
