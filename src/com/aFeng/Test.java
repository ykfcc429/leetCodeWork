package com.aFeng;

public class Test {

    public static void main(String[] args) {
//        String a = "123";
//        String b = "123";
//        Node<Integer> node = new Node<Integear>(123);
//        node.add(1231);
//        node.getNext().add(233);
        System.out.println(1>>1);
//        new InterfaceMain().hello();
    }



    static class Node<E>{
        E item;
        Node<E> next;
        Node(E e){
            item = e;
        }
        void add(E e){
            this.next = new Node<E>(e);
        }
        E get(){
            return item;
        }
        Node<E> getNext(){
            return next;
        }
    }
}
