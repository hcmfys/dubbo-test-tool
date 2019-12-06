package org.springbus.gui;



import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.util.Map;

public class ServerModel implements ComboBoxModel {


    private String[] dataList;
    private Object anItem;

    public ServerModel(Map<String, List<String>> map) {
        String[] data =  map.keySet().toArray(new String[0]);
        parseData(data);
    }

    private void parseData(String[] data ){
        if (data != null) {
            dataList = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                String d = data[i];
                int index = d.lastIndexOf(".");
                if (index != -1) {
                    d = d.substring(index + 1);
                }
                dataList[i] = d;

            }
        }
    }
    public ServerModel( List<String> mapList) {
        String[] data = mapList.toArray(new String[0]);
        parseData(data);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.anItem = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return anItem;
    }

    @Override
    public int getSize() {
        return dataList.length;
    }

    @Override
    public Object getElementAt(int index) {
        return dataList[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
