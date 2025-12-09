package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E> {
    private static final long serialVersionUID = 1L;
    
    private BSTreeNode<E> root;
    private int size;

    public BSTree() {
        this.root = null;
        this.size = 0;
    }

    public BSTree(E element) {
        this.root = new BSTreeNode<>(element);
        this.size = 1;
    }

    @Override
    public BSTreeNode<E> getRoot() {
        return root;
    }
    
    @Override
    public BSTreeNode<E> removeMin() {
        
        if (root == null) {
            return null;
        }

        BSTreeNode<E> minNode = null;

        
        if (root.getLeft() == null) {
            minNode = root;
            root = root.getRight();
            size--;
            return minNode; 
        }

        
        BSTreeNode<E> parent = null;
        BSTreeNode<E> current = root;

        
        while (current.getLeft() != null) {
            parent = current;
            current = current.getLeft();
        }

        
        minNode = current;
        
        
        parent.setLeft(current.getRight());
        
        size--;
        return minNode;
    }

    @Override
    public BSTreeNode<E> removeMax() {
        
        if (root == null) {
            return null;
        }

        BSTreeNode<E> maxNode = null;

        
        if (root.getRight() == null) {
            maxNode = root;
            root = root.getLeft();
            size--;
            return maxNode;
        }

        
        BSTreeNode<E> parent = null;
        BSTreeNode<E> current = root;

        
        while (current.getRight() != null) {
            parent = current;
            current = current.getRight();
        }

        
        maxNode = current;

        
        parent.setRight(current.getLeft());

        size--;
        return maxNode;
    }
    @Override
    public int getHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(BSTreeNode<E> node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.getLeft()), calculateHeight(node.getRight()));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) {
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) {
        return searchRecursive(root, entry);
    }

    private BSTreeNode<E> searchRecursive(BSTreeNode<E> current, E entry) {
        if (current == null) return null;
        int compareResult = entry.compareTo(current.getElement());
        if (compareResult == 0) return current;
        else if (compareResult < 0) return searchRecursive(current.getLeft(), entry);
        else return searchRecursive(current.getRight(), entry);
    }

    @Override
    public boolean add(E newEntry) {
        if (newEntry == null) throw new NullPointerException("Cannot add null entry");
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        }
        return addRecursive(root, newEntry);
    }

    private boolean addRecursive(BSTreeNode<E> current, E newEntry) {
        int compareResult = newEntry.compareTo(current.getElement());
        
        if (compareResult == 0) {
            
            return false; 
        } else if (compareResult < 0) {
            if (current.getLeft() == null) {
                current.setLeft(new BSTreeNode<>(newEntry));
                size++;
                return true;
            } else {
                return addRecursive(current.getLeft(), newEntry);
            }
        } else {
            if (current.getRight() == null) {
                current.setRight(new BSTreeNode<>(newEntry));
                size++;
                return true;
            } else {
                return addRecursive(current.getRight(), newEntry);
            }
        }
    }
    
    
    public E retrieve(E entry) {
        BSTreeNode<E> node = search(entry);
        return (node == null) ? null : node.getElement();
    }

    @Override
    public Iterator<E> inorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        inorderTraverse(root, list);
        return new TreeIterator(list);
    }

    private void inorderTraverse(BSTreeNode<E> node, ArrayList<E> list) {
        if (node == null) return;
        inorderTraverse(node.getLeft(), list);
        list.add(node.getElement());
        inorderTraverse(node.getRight(), list);
    }

    @Override
    public Iterator<E> preorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        preorderTraverse(root, list);
        return new TreeIterator(list);
    }

    private void preorderTraverse(BSTreeNode<E> node, ArrayList<E> list) {
        if (node == null) return;
        list.add(node.getElement());
        preorderTraverse(node.getLeft(), list);
        preorderTraverse(node.getRight(), list);
    }

    @Override
    public Iterator<E> postorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        postorderTraverse(root, list);
        return new TreeIterator(list);
    }

    private void postorderTraverse(BSTreeNode<E> node, ArrayList<E> list) {
        if (node == null) return;
        postorderTraverse(node.getLeft(), list);
        postorderTraverse(node.getRight(), list);
        list.add(node.getElement());
    }

    
    private class TreeIterator implements Iterator<E> {
        private ArrayList<E> list;
        private int currentIndex;

        public TreeIterator(ArrayList<E> list) {
            this.list = list;
            this.currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < list.size();
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException();
            return list.get(currentIndex++);
        }
        
        
    }
    
    
}