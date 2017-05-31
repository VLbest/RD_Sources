package com.thalmic.myo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VLI on 16/04/2016.
 */
public class Emg {

    private ArrayList<Integer> FirstData;
    private ArrayList<Integer> SecondData;

    public Emg(int[] array){
        this.FirstData = new ArrayList<Integer>();
        this.SecondData = new ArrayList<Integer>();
        this.dealWithIt(array);
    }

    // YES! Just deal with it! i am too tired...
    public void dealWithIt(int[] array){
        for(int i = 0; i < 16; i++){
            if(i > 7){
                SecondData.add(array[i]);
            }else{
                FirstData.add(array[i]);
            }
        }
    }

    public ArrayList<Integer> getAllEmgData(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.addAll(FirstData);
        list.addAll(SecondData);
        return list;
    }

    public ArrayList<Integer> getFirstEmgData(){
        return this.FirstData;
    }

    public ArrayList<Integer> getSecondData(){
        return this.SecondData;
    }
}
