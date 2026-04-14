package edu.ttap.intmap;

public class OurHashArray {
    private class mapList {
        private class node {
            private char key;
            private int count;
            private node next;
            public node(char key, node next) {
                this.key = key;
                this.count = 0;
                this.next = next;
            }

            public node(char key){
                this.key = key;
                count = 0;
                next = null;
            }

            public void increment() {count++;}
            public void setNext(node newNode) {next = newNode;}
            public char getKey() {return key;}
            public boolean hasKey(char ch) {return key == ch;}
            public int getCount() {return count;}
            public node getNext() {return next;}
        }
        
        node data;
        public mapList(){
            data = null;
        }

        public 
    }
    
    private node[] hashArray;
    private int size;

    public OurHashArray() {
        hashArray = new node[10];
        size = 0;
    }

    public void put
}
