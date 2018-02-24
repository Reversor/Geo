package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Spliterator.DISTINCT;

public class MyMap<K, V> extends AbstractMap<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger("mymap");

    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 10;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final Function<Object, Integer> DEFAULT_HASH_FUNCTION = key -> key.hashCode() * 31;

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
        return n >= MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : n + 1;
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
                            || (value != null && entry.value.equals(value)))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> entry = table[index(hash(key))];
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
        int i = index(hash);
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
        if (++count > threshold) resize();
        return null;
    }

    private void resize() {
//        Arrays.copyOf(table, )
    }

    private int hash(Object key) {
        return key == null
                ? 0
                : hashFunction.apply(key);
    }

    // аналогично hash % capacity, когда capacity степень двойки
    private int index(int hash) {
        return hash & (capacity - 1);
    }

    @Override
    public V remove(Object key) {
        int i = index(hash(key));
        Entry<K, V> entry = table[i];
        if (entry == null) return null;
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

    @SuppressWarnings("unchecked")
    @Override
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

    static final class Entry<K, V> implements Map.Entry<K, V> {
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

        private final int fence;
        private final int characteristic;
        private final Function<Map.Entry<K, V>, T> getter;
        private int index;

        MySpliterator(Function<Map.Entry<K, V>, T> getter) {
            this(getter, 0);
        }

        MySpliterator(Function<Map.Entry<K, V>, T> getter, int characteristic) {
            this(0, MyMap.this.size(), getter, characteristic);
        }

        MySpliterator(int index, int fence, Function<Map.Entry<K, V>, T> getter, int characteristic) {
            this.index = index;
            this.fence = fence;
            this.characteristic = DEFAULT_CHARACTERISTIC | characteristic;
            this.getter = getter;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (action == null)
                throw new NullPointerException();
            if (index >= 0 && index < fence) {
                action.accept(getter.apply(table[index++]));
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            int mid = (index + fence) >>> 1;
            return (index >= mid)
                    ? null
                    : new MySpliterator<>(index, index = mid, getter, characteristic);
        }

        @Override
        public long estimateSize() {
            return fence - index;
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
        Function<Entry<K, V>, T> getter;

        MyIterator(Function<Entry<K, V>, T> getter) {
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
            if (current == null){
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
            return new MyIterator<>(e -> e.key);
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
            return new MyIterator<>(e -> e.value);
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
