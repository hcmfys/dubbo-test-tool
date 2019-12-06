package org.springbus.gui;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Iterator;

public class TreeModelUtil {


    /**
     *
     * @param jObj
     * @param rootNode
     */
    public  static    void explorerTree(Object jObj, DefaultMutableTreeNode rootNode) {

        if(jObj instanceof JSONArray) {
            JSONArray array= (JSONArray) jObj;
            int size=array.size();
            for ( int i=0;i<size;i++) {
                Object o = array.get(i);
                DefaultMutableTreeNode curNode = new DefaultMutableTreeNode("[" + i + "]");
                if (o instanceof JSONObject) {
                    explorerTree(o, curNode);
                } else if (o instanceof JSONArray) {
                    curNode = new DefaultMutableTreeNode("[]");
                    explorerTree(o, curNode);
                } else {
                    curNode = new DefaultMutableTreeNode(o.toString());
                }
                rootNode.add(curNode);
            }

        }else  if(jObj instanceof  JSONObject) {
            JSONObject jsonObject = (JSONObject) jObj;
            Iterator<String> keys = jsonObject.keySet().iterator();
            while (keys.hasNext()) {
                String curKey = keys.next();
                Object o = jsonObject.get(curKey);
                DefaultMutableTreeNode curNode;
                if(o instanceof JSONObject) {
                    curNode = new DefaultMutableTreeNode(curKey);
                    rootNode.add(curNode);
                    explorerTree(o,curNode);
                } else if(o instanceof JSONArray) {
                    curNode = new DefaultMutableTreeNode("[]");
                    rootNode.add(curNode);
                    explorerTree(o,curNode);
                }else {
                    String strValue="null";
                    if(o!=null) {
                        strValue=o.toString();
                    }
                    curNode = new DefaultMutableTreeNode(curKey+" : "+strValue);
                    System.out.println(curKey+" : "+ strValue);
                    rootNode.add(curNode);
                }

            }

        }
    }
}
