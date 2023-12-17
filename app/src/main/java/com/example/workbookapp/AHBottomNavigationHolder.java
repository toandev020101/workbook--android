package com.example.workbookapp;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

public class AHBottomNavigationHolder {
    private static AHBottomNavigationHolder instance;
    private AHBottomNavigation bnvNavigate;

    public AHBottomNavigationHolder() {
    }

    public static synchronized AHBottomNavigationHolder getInstance() {
        if (instance == null) {
            instance = new AHBottomNavigationHolder();
        }
        return instance;
    }

    public AHBottomNavigation getBnvNavigate() {
        return bnvNavigate;
    }

    public void setBnvNavigate(AHBottomNavigation bnvNavigate) {
        this.bnvNavigate = bnvNavigate;
    }
}
