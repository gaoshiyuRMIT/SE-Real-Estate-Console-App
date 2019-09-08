package user.customer;

import consts.*;

public class ID {
    private IDType idType;
    private String content;

    public ID(IDType typ, String ctt) {
        this.idType = typ;
        this.content = ctt.trim();
    }

    public boolean equals(ID id2) {
        return idType == id2.idType && content == id2.content;
    }
}