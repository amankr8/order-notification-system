package com.flykraft.model.store;

public class Order {
    private Integer orderId;
    private Integer customerId;
    private Integer vendorId;
    private Integer partnerId;
    private Integer statusId;

    public Order(Integer customerId, Integer vendorId) {
        this.customerId = customerId;
        this.vendorId = vendorId;
        this.statusId = OrderStatus.PLACED.getId();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}
