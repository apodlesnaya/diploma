package diploma;

import javafx.scene.Node;

public class WordEntity {

    boolean isUsed;
    Node node;

    public WordEntity(boolean isUsed, Node node) {
        this.isUsed = isUsed;
        this.node = node;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public Node getNode() {
        return node;
    }
}
