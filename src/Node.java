class Node {
    private PolynomItem value;
    private Node next;
    private Node prev;

    Node(PolynomItem value) {
        this.value = value;
    }
    Node() {}

    public synchronized PolynomItem getValue() {
        return value;
    }

    public synchronized Node getPrev() {
        return prev;
    }

    public synchronized void setPrev(Node prev) {
        this.prev = prev;
    }

    public synchronized Node getNext() {
        return next;
    }

    public synchronized void setNext(Node next) {
        this.next = next;
    }

    public synchronized boolean setNextForNode(Node node) {
        if(prev == null && next == null) {
            // first add
            next = node;
            node.setPrev(this);
            return true;
        }
        else if(value == null){
            return false;
        }

        if(node.getValue().getRank() == value.getRank())
        {
            // sum the coefficient
            value.sumCoefficient(node.getValue().getCoefficient());
        }
        else if(node.getValue().getRank() < value.getRank())
        {
            //add on the middle
            node.setNext(this);
            prev.setNext(node);
            node.setPrev(prev);
            prev = node;
        }
        else if(next == null) {
            //should add on the end
            next = node;
            node.setPrev(this);
        }
        else {
            return false;
        }

        return true;
    }
}