package user.customer;

import consts.*;

public class ID {
    private IDType idType;
    private String content;

    public ID(IDType typ, String ctt) {
        this.idType = typ;
        this.content = ctt.trim();
    }

    public ID(String typ, String ctt) {
        this(IDType.valueOf(typ), ctt);
    }

    public boolean equals(Object o) {
        if (o instanceof ID) {
            ID id2 = (ID)o;
            return idType == id2.idType && content == id2.content;
        }
        return false;
    }

    public boolean equals(ID id) {
        return idType == id.idType && content == id.content;
    }

    public String toString() {
        return String.format("%s(%s)", idType.name(), content);
    }
}