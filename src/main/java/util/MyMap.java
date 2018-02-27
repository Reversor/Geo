package util;

import static java.util.Spliterator.DISTINCT;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyMap<K, V> extends AbstractMap<K, V> {

    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final Function<Object, Integer> DEFAULT_HASH_FUNCTION = k -> k.hashCode() * 31;

    @SuppressWarnings("unchecked")
    private final Entry[] EMPTY_ENTRIES = new Entry[DEFAULT_INITIAL_CAPACITY];

    private Entry<K, V>[] table;

    private Function<Object, Integer> hashFunction;

    private float loadFactor;
    private int capacity;
    private int threshold;
    private int count;

    private EntrySet entrySet;
    private KeySet keySet;
    private Values values;

    @SuppressWarnings("unchecked")
    public MyMap(int initialCapacity, float loadFactor, Function<Object, Integer> hashFunction) {
        if (isPowerOfTwo(initialCapacity)) {
            this.capacity = initialCapacity;
        } else {
            this.capacity = roundingToPowerOfTwo(initialCapacity);
        }
        this.table = new Entry[capacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
        this.hashFunction = hashFunction == null ? DEFAULT_HASH_FUNCTION : hashFunction;
        this.count = 0;
    }

    @SuppressWarnings("unchecked")
    public MyMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_HASH_FUNCTION);
    }

    @SuppressWarnings("unchecked")
    public MyMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_HASH_FUNCTION);
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] resize() {
        Entry<K, V>[] newTable;
        int oldCapacity = capacity;
        int oldThreshold = threshold;
        if (oldCapacity >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return table;
        }
        int newCapacity;
        newCapacity = capacity << 1; // x2
        threshold = (int) (newCapacity * loadFactor);
        newTable = new Entry[newCapacity];
        Entry<K, V> entry = null;
        int index = 0;
        int transfers = 0;
        while (entry != null || (index < table.length) && (transfers < count)) {
            if (entry == null) {
                entry = table[index++];
            } else {
                newTable[index(entry.hash, newCapacity)] = entry;
                entry = entry.next;
            }
        }
        capacity = newCapacity;
        return newTable;
    }

    private boolean isPowerOfTwo(int num) {
        return num != 0 && 0 == (num & (num - 1));
    }

    private int roundingToPowerOfTwo(int capacity) {
        int n = capacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        if (n < MAXIMUM_CAPACITY) {
            return n + 1;
        }
        return MAXIMUM_CAPACITY;
    }

    private int hash(Object key) {
        return key == null
                ? 0
                : hashFunction.apply(key);
    }

    private int index(int hash, int capacity) {
        return hash & (capacity - 1); // hash % capacity
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!isEmpty()) {
            for (Entry<K, V> root : table) {
                for (Entry<K, V> entry = root; entry != null; entry = entry.next) {
                    if (entry.value == value
                            || (value != null && entry.value.equals(value))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> entry = table[index(hash(key), capacity)];
        while (Objects.nonNull(entry)) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public V put(K key, V value) throws NullPointerException {
        Objects.requireNonNull(value, "Use a non-null value");
        int hash = hash(key);
        int i = index(hash, capacity);
        Entry<K, V> entry = table[i];
        while (entry != null) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
            entry = entry.next;
        }
        table[i] = new Entry<>(hash, key, value, table[i]);
        if (++count > threshold) {
            table = resize();
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int i = index(hash(key), capacity);
        Entry<K, V> entry = table[i];
        if (entry == null) {
            return null;
        }
        if (entry.key.equals(key)) {
            table[i] = entry.next;
            count--;
            return entry.value;
        }
        while (entry.next != null) {
            if (entry.next.key.equals(key)) {
                Entry<K, V> removedEntry = entry.next;
                entry.next = removedEntry.next;
                count--;
                return removedEntry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = EMPTY_ENTRIES;
        count = 0;
    }

    @Override
    public Set<K> keySet() {
        return keySet == null
                ? keySet = new KeySet()
                : keySet;
    }

    @Override
    public Collection<V> values() {
        return keySet == null
                ? values = new Values()
                : values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet == null
                ? entrySet = new EntrySet()
                : entrySet;
    }

    private static final class Entry<K, V> implements Map.Entry<K, V> {

        final int hash;
        final K key;
        V value;
        Entry<K, V> next;

        Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
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
            return 37 * keyHash ^ 17 * valueHash;
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

    private final class MySpliterator<T> implements Spliterator<T> {

        private final int DEFAULT_CHARACTERISTIC = SIZED | SUBSIZED;

        private final Function<Map.Entry<K, V>, T> getter;
        private final int fence;
        private final int characteristic;
        Entry<K, V> current = null;
        private int tableIndex;
        private int mapIndex;

        MySpliterator(Function<Map.Entry<K, V>, T> getter) {
            this(getter, 0);
        }

        MySpliterator(Function<Map.Entry<K, V>, T> getter, int characteristic) {
            this(0, 0, table.length, getter, characteristic);
        }

        MySpliterator(int tableIndex, int mapIndex,
                int fence,
                Function<Map.Entry<K, V>, T> getter,
                int characteristic) {
            this.current = null;
            this.tableIndex = tableIndex;
            this.mapIndex = mapIndex;
            this.fence = fence;
            this.characteristic = DEFAULT_CHARACTERISTIC | characteristic;
            this.getter = getter;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            if (tableIndex >= 0 || tableIndex < fence && mapIndex < count) {
                while (current != null || tableIndex < fence && mapIndex < count) {
                    if (current == null) {
                        current = table[tableIndex++];
                    } else {
                        action.accept(getter.apply(current));
                        current = current.next;
//                        mapIndex++;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            int mid = (tableIndex + fence) >>> 1;
            if (tableIndex >= mid || mapIndex + 1 >= count) {
                return null;
            }
            return new MySpliterator<>(tableIndex, mapIndex, tableIndex = mid, getter,
                    characteristic);
        }

        @Override
        public long estimateSize() {
            return fence - tableIndex;
        }

        @Override
        public int characteristics() {
            return characteristic;
        }

        @Override
        public Comparator<? super T> getComparator() {
            return null;
        }
    }

    private final class MyIterator<T> implements Iterator<T> {

        int index;
        Entry<K, V> current;
        Entry<K, V> next;
        Function<Map.Entry<K, V>, T> getter;

        MyIterator(Function<Map.Entry<K, V>, T> getter) {
            this.getter = getter;
            index = 0;
            current = next = null;
            if (!isEmpty()) {
                setNearestEntryAsNext();
            }
        }

        private void setNearestEntryAsNext() {
            while (next == null && index < table.length) {
                next = table[index++];
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                Entry<K, V> entry = next;
                if (entry != null) {
                    current = entry;
                    next = entry.next;
                    if (next == null) {
                        setNearestEntryAsNext();
                    }
                }
                return getter.apply(entry);
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            MyMap.this.remove(current.key);
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new MyIterator<>(e -> e);
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
            return new MySpliterator<>((e) -> e, Spliterator.SIZED | Spliterator.SUBSIZED);
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public boolean removeAll(Collection<?> c) {
            return super.removeAll(c);
        }

        @Override
        public boolean remove(Object o) {
            return MyMap.this.remove(o) != null;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return super.retainAll(c);
        }

        @Override
        public void clear() {
            super.clear();
        }

        @Override
        public Spliterator<K> spliterator() {
            return new MySpliterator<>(Map.Entry::getKey, DISTINCT);
        }

        @Override
        public Iterator<K> iterator() {
            return new MyIterator<>(Map.Entry::getKey);
        }

        @Override
        public int size() {
            return MyMap.this.size();
        }

    }

    private class Values extends AbstractCollection<V> {

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
            return new MyIterator<>(Map.Entry::getValue);
        }

        @Override
        public final boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public final boolean remove(Object o) {
            return super.remove(o);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return super.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return super.retainAll(c);
        }

        @Override
        public final Spliterator<V> spliterator() {
            return new MySpliterator<>(Map.Entry::getValue);
        }
    }

}
