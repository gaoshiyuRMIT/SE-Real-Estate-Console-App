package se;

import java.util.*;

import consts.*;

public class Notification {
    private String message;
    private NotifStatus status;
    private List<WithId> artifacts;
    private String artifactName;
    private String artifactId;

    public Notification(String message, WithId... artifacts) {
        this.artifacts = Arrays.asList(artifacts);
        this.message = message;
        this.status = NotifStatus.Active;
    }

    // change status
    public void dismiss() {
        this.status = NotifStatus.Archived;
    }

    public List<String> getObjectURIs() {
        List<String> res = new ArrayList<String>();
        for (WithId o : artifacts) {
            String s = String.format(
                "/%s/%s",
                o.getClass().getSimpleName(),
                o.getId()
            );
            res.add(s);
        }
        return res;
    }
}