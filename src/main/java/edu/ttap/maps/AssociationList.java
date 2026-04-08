package edu.ttap.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * An association list is an implementation of a map via a list of key-value pairs.
 */
public class AssociationList<K, V> implements Map<K, V> {
    private static class Node<K, V> implements Map.Entry <K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey(){
            return key;
        }

        public V getValue(){
            return value;
        }

        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    ArrayList<Node<K, V>> data = new ArrayList<>();

    /**
     * Clears the association list, removing all key-value pairs.
     */
    @Override
    public void clear() {
        data.clear();
    }

    /**
     * @param key the key to check
     * @return true iff this map contains a mapping for the specified key
     */
    @Override
    public boolean containsKey(Object key) {
        for (Node<K, V> node : data) {
            if (node.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param value the value to check
     * @return true iff this map maps one or more keys to the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        for (Node<K, V> node : data) {
            if (node.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> ntreeSet = new TreeSet<>();
        for (Node <K, V> element : data){
            ntreeSet.add(element);
        }
        return ntreeSet;
    }

    /**
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key
     */
    @Override
    public V get(Object key) {
        for (Node<K, V> node : data) {
            if (node.getKey().equals(key)) {
                return node.getValue();
            }
        }
        return null;
    }

    /**
     * @return true iff this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return data.size() == 0;
    }

    /**
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new TreeSet<>();
        for(Node <K, V> i : data) {
            keys.add(i.getKey());
        }
        return keys;
    }

    /**
     * If there is no entry for key in the map, updates the entry to associate key
     * with value. Otherwise, it updates the entry for key accordingly.
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    @Override
    public V put(K key, V value) {
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i).getKey().equals(key)){
                V old = data.get(i).getValue();
                data.get(i).setValue(value);
                return old;
            }
        }
        data.add(new Node <K, V> (key, value));
        return null;
    }


    /**
     * Copies all of the mappings from the specified map to this map. The effect of this
     * operation is equivalent to applying the put(K, V) operation to each entry in the
     * specified map.
     * @param m the map whose mappings are to be copied to this map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> i : m.entrySet()) {
            this.put(i.getKey(), i.getValue());
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * @param key the key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no mapping for
     *         key.
     */
    @Override
    public V remove(Object key) {
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i).getKey().equals(key)){
                V old = data.get(i).getValue();
                data.remove(i);
                return old;
            }
        }
        return null;
    }

    /**
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return data.size();
    }

    /**
     * @return a collection vof the values contained in this map, e.g., a list
     */
    @Override
    public Collection<V> values() {
        Collection <V> list = new ArrayList <> ();
        for (Node <K, V> i : data){
            list.add(i.getValue());
        }
        return list;
        }
}
