package com.homecontrol.andrew.homecontrollibrary;

import android.util.Log;

import java.util.HashSet;

/**
 * Created by andrew on 12/22/14.
 */
public class NetworkData {
    private static final String TAG = "NetworkData";
    private String networkAddress;
    private String networkName;
    private String passcode;
    private HashSet<String> networkList;
    private String lastNetworkUsed;

    public void setNetworkAddress(String addresss){
        networkAddress = addresss;
    }

    public String getNetworkAddress(){
        return networkAddress;
    }

    public void setNetworkName(String name){
        networkName = name;
    }

    public String getNetworkName(){
        return networkName;
    }

    public void setLastNetworkUsed(String network){
        lastNetworkUsed = network;
    }

    public String getLastNetworkUsed(){
        return lastNetworkUsed;
    }

    public void setPasscode(String code){
        passcode = code;
    }

    public String getPasscode(){
        return passcode;
    }

    public void loadNetworkList(HashSet<String> list){
        if(list == null){
            networkList = new HashSet<String>();    // if there were no prefereneces to read, make a new list
        }else {
            networkList = list;     // set network list to what was read
        }
    }

    public HashSet<String> getNetworkList(){
        return networkList;
    }

    public void addNetworkToList(String network){
        // just checking if its null
        if(networkList != null) {
            try {
                networkList.add(network);
                Log.d(TAG, network + " added to networkList");
            } catch (ClassCastException cce) {
                throw new ClassCastException(" String must be passed to addNetworkToList");
            }
        }
    }

    public void removeNetworkFromList(String network){
        // remove network with the given name
        networkList.remove(network);
    }

    public String[] getNetworkListArray(){
//        if(networkList == null)
//            Log.e(TAG, "networkList is null");
        // convert network
        String[] array = new String[networkList.size()];
        networkList.toArray(array);
        return array;
    }
}
