package com.example.ubertaxi;

import java.util.List;

public class Result {
    List<Order> results;



    public Result(List<Order> orders) {
        this.results = orders;
    }

    public List<Order> getOrders() {
        return results;
    }

    public void setOrders(List<Order> orders) {
        this.results = orders;
    }
}
