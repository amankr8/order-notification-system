package com.flykraft.model.store;

public enum OrderStatus {
    PLACED(1, "Order Placed"),
    SHIPPED(2, "Order Shipped"),
    DELIVERED(3, "Order Delivered");

    private Integer id;
    private java.lang.String name;

    OrderStatus(Integer id, java.lang.String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }
}
