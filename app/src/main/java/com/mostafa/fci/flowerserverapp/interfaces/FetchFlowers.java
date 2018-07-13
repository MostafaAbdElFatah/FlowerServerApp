package com.mostafa.fci.flowerserverapp.interfaces;
import com.mostafa.fci.flowerserverapp.Classes.Flower;

import java.util.ArrayList;

public interface FetchFlowers  {
    void onCompleted(ArrayList<Flower> flowersList);
}
