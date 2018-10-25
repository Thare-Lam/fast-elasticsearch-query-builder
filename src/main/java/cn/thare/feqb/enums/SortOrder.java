package cn.thare.feqb.enums;

public enum SortOrder {

    ASC("asc"), DESC("desc");

    private String value;

    SortOrder(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
