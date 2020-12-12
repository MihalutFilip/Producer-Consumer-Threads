class CustomLinkedList {
    private Node head;

    CustomLinkedList() {
        head = new Node();
    }

    public Node getHead() {
        return head;
    }

    public void add(PolynomItem item) {
        Node first = head;
        Node node = new Node(item);
        while(first != null && !first.setNextForNode(node))
        {
            first = first.getNext();
        }
    }
}