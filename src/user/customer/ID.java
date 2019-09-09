package user.customer;

import consts.*;

public class ID {
    private IDType idType;
    private String content;

    public ID(IDType typ, String ctt) {
        this.idType = typ;
        this.content = ctt.trim();
    }

    public boolean equals(Object o) {
        if (o instanceof ID) {
            ID id2 = (ID)o;
            return idType == id2.idType && content == id2.content;
        }
        return false;
    }
}