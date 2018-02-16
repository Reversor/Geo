package util;

import java.util.*;
import java.util.function.Consumer;

public class MyMap<K, V> implements Map<K, V> {
    private Map.Entry<K, V>[] entries;
    private Entry<K, V> root;
    private Entry<K, V> last;


    public MyMap() {
        entries = new Map.Entry[]{};
    }

    public MyMap(int initialCapacity) {
        entries = new Map.Entry[initialCapacity];
    }

    @Override
    public int size() {
        return entries.length;
    }

    @Override
    public boolean isEmpty() {
        return entries.length == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Entry<K, V> entry = root;
        do {
            if (entry.getValue().equals(value)) {
                return true;
            }
            entry = entry.right;
        } while (entry != null);
        return false;
    }

    @Override
    public int hashCode() {
        // TODO
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Map && entrySet().equals(((Map) obj).entrySet());
    }

    @Override
    public String toString() {
        // TODO
        return Arrays.toString(entries);
    }

    @Override
    public V get(Object key) throws ClassCastException, NullPointerException {
        if (isEmpty()) {
            return null;
        }
        for (Map.Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private V replaceValue(Map.Entry<K, V> entry, V value) {
        V oldValue = entry.getValue();
        entry.setValue(value);
        return oldValue;
    }

    private void expand() {
        entries = Arrays.copyOf(entries, entries.length + 1);
    }

    @Override
    public V put(K key, V value) throws NullPointerException {
        if (!isEmpty()) {
            for (Map.Entry<K, V> entry : entries) {
                if (entry == null) {
                    entry = new Entry<K, V>(key, value);
                    return null;
                }
                if (entry.getKey().equals(key)) {
                    return replaceValue(entry, value);
                }
            }
        }
        Map.Entry<K, V> newEntry = new Entry<K, V>(key, value);
        expand();
        entries[size() - 1] = newEntry;
        return null;
    }

    @Override
    public V remove(Object key) throws ClassCastException {
        if (isEmpty()) return null;
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].getKey().equals(key)) {
                /*Entry<K, V>[] newEntries = new Entry[]{};
                if (i > 0) {
                    System.out.println(i);
                    System.arraycopy(entries, 0, newEntries, 0, i - 1);
                }
                if (i < entries.length - 1) {
                    System.arraycopy(entries, i + 1, newEntries, i, entries.length - 1);
                }
                entries = newEntries;*/
                //FIXME
                System.arraycopy(entries, i + 1, entries, i, entries.length - i + 2);
                return entries[i].getValue();
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) throws ClassCastException, NullPointerException {
        if (this != m) m.forEach(this::put);
    }

    @Override
    public void clear() {
        entries = new Map.Entry[]{};
        root = null;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        return new Values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    int hashCompare(Object first, Object second) {
        return first.hashCode() - second.hashCode();
    }

    static final class Entry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> parent, left, right;
        int level;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public int hashCode() {
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            return keyHash ^ valueHash;
        }

        @Override
        public String toString() {
            return "{ " + key + ": " + value + " }";
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof Entry
                    && key.equals(((Map.Entry) o).getKey())
                    && value.equals(((Map.Entry) o).getValue());
        }
    }

    abstract class MySpliterator<T> implements Spliterator<T> {
        Entry<K, V> entry;
        int size;
        long position;

        MySpliterator(Entry<K, V> entry) {
            this.entry = entry;
            this.position = 0;
            this.size = MyMap.this.size();
            entries = new Map.Entry[size];
        }

        MySpliterator(Entry<K, V> entry, long position, int origin) {
            this.entry = entry;
            this.position = position;
            this.size = origin;
        }

        boolean isSplitable() {
            return size - position > 1 && entry.right != null;
        }

        @Override
        public long estimateSize() {
            return size - position;
        }

        @Override
        public int characteristics() {
            return SIZED | SUBSIZED;
        }
    }

    class EntrySpliterator extends MySpliterator<Map.Entry<K, V>> {
        EntrySpliterator(Entry<K, V> entry) {
            super(entry);
        }

        EntrySpliterator(Entry<K, V> entry, long position, int origin) {
            super(entry, position, origin);
        }

        @Override
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action) {
            if (entry != null && position <= size) {
                action.accept(entry);
                position++;
                entry = entry.right;
                return true;
            }
            return false;
        }

        // FIXME: something wrong here
        @Override
        public Spliterator<Map.Entry<K, V>> trySplit() {
            if (isSplitable()) {
                return null;
            }
            return null;
        }
    }

    class KeySpliterator extends MySpliterator<K> {
        KeySpliterator(Entry<K, V> entry) {
            super(entry);
        }

        KeySpliterator(Entry<K, V> entry, long position, int origin) {
            super(entry, position, origin);
        }

        @Override
        public boolean tryAdvance(Consumer<? super K> action) {
            if (entry != null) {
                action.accept(entry.key);
                entry = entry.right;
                position++;
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<K> trySplit() {
            if (super.isSplitable()) {
                return new KeySpliterator(entry, position, size / 2);
            }
            return null;
        }

        @Override
        public int characteristics() {
            return super.characteristics() | DISTINCT;
        }
    }

    class ValueSpliterator extends MySpliterator<V> {

        ValueSpliterator(Entry<K, V> entry) {
            super(entry);
        }

        ValueSpliterator(Entry<K, V> entry, long position, int origin) {
            super(entry, position, origin);
        }

        @Override
        public boolean tryAdvance(Consumer<? super V> action) {
            if (entry != null) {
                action.accept(entry.value);
                entry = entry.right;
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<V> trySplit() {
            /*if (super.isSplitable()) {
                return new ValueSpliterator(entry, position, size / 2);
            }*/
            return null;
        }
    }

    abstract class MyIterator<T> implements Iterator<T> {
        Entry<K, V> current;
        Entry<K, V> next;

        MyIterator(Entry<K, V> first) {
            next = first;
        }

        public boolean hasNext() {
            return next != null;
        }

        Map.Entry<K, V> nextEntry() {
            if (hasNext()) {
                current = next;
                next = next.right;
                return current;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            MyMap.this.remove(current);
        }
    }

    class EntryIterator extends MyIterator<Map.Entry<K, V>> {
        EntryIterator() {
            super(root);
        }

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    class KeyIterator extends MyIterator<K> {
        KeyIterator() {
            super(root);
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    class ValueIterator extends MyIterator<V> {
        ValueIterator() {
            super(root);
        }

        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        // TODO
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return MyMap.this.size();
        }

        @Override
        public boolean remove(Object o) {
            return MyMap.this.remove(o) != null;
        }

        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(root);
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public boolean removeAll(Collection<?> c) {
            // TODO
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return MyMap.this.remove(o) != null;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            // TODO
            c.forEach(MyMap.this::remove);
            return super.retainAll(c);
        }

        @Override
        public void clear() {
            super.clear();
        }

        @Override
        public Spliterator<K> spliterator() {
            return new KeySpliterator(root);
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return MyMap.this.size();
        }

    }

    final class Values extends AbstractCollection<V> {
        @Override
        public final int size() {
            return MyMap.this.size();
        }

        @Override
        public final void clear() {
            MyMap.this.clear();
        }

        @Override
        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public final boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public final Spliterator<V> spliterator() {
            return new ValueSpliterator(root);
        }
    }

}
