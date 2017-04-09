package com.tdd.katas.microservices.partservice.model;

public class PartData {
    private String partId;
    private String description;

    public PartData(String partId, String description) {
        this.partId = partId;
        this.description = description;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return
                obj != null
                && obj instanceof PartData
                && this.partId!= null
                && this.partId.equals(((PartData) obj).getPartId());
    }

}
