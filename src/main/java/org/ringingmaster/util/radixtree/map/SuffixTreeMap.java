package org.ringingmaster.util.radixtree.map;

import org.ringingmaster.util.radixtree.ChildIteratorPool;
import org.ringingmaster.util.radixtree.MutableCharSequence;
import org.ringingmaster.util.radixtree.MutableSequence;
import org.ringingmaster.util.radixtree.RadixTree;
import org.ringingmaster.util.radixtree.SingleValueSupplier;
import org.ringingmaster.util.radixtree.TreeConfig;
import org.ringingmaster.util.radixtree.TreeVisitor;
import org.ringingmaster.util.radixtree.ValueFilter;
import org.ringingmaster.util.radixtree.ValueSupplier;
import org.ringingmaster.util.radixtree.sequence.CharSequenceWithAssignableTerminalChar;
import org.ringingmaster.util.radixtree.sequence.CharSequenceWithIntTerminatorAdapter;
import org.ringingmaster.util.radixtree.visitor.StringCompressionVisitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * User: nick
 * Date: 10/06/13
 * Time: 08:20
 * 
 * A Generalized suffix tree which uses a compact RadixTree as the 
 * underlying data structure.
 */
public class SuffixTreeMap<V> implements TrieMap<V> {
    
    private RadixTree<V> radixTree = new RadixTree<V>();
    
    //for terminators use the first unassigned unicode character
    //plane onwards
    private int lastTerminator = Character.MAX_VALUE + 1;

    private CharSequenceWithIntTerminatorAdapter intTerminatorAdapter = new CharSequenceWithIntTerminatorAdapter();
    private MutableSequence mutableSequence = new MutableSequence();

    private Map<CharSequence, CharSequenceWithAssignableTerminalChar> terminalNodesBySequence = new HashMap<>();
    
    private final TreeConfig<V> treeConfig;
    
    public SuffixTreeMap() {
        treeConfig = new TreeConfig<V>(new ChildIteratorPool<V>(), new SingleValueSupplier<V>());
    }
    
    public SuffixTreeMap(ValueSupplier<V> valueSupplier) {
        treeConfig = new TreeConfig<V>(new ChildIteratorPool<V>(), valueSupplier);
    }
    
    public void put(CharSequence s, V value) {
        int terminalChar = lastTerminator++;
        CharSequenceWithAssignableTerminalChar n = new CharSequenceWithAssignableTerminalChar(s, terminalChar);
        terminalNodesBySequence.put(s, n);
        mutableSequence.setSegment(n);
        addAllSuffixes(mutableSequence, n, value);
    }

    private void addAllSuffixes(MutableCharSequence s, CharSequenceWithAssignableTerminalChar n, V value) {
        for ( int c = 0; c < n.length() - 1; c++) {
            s.setStart(c);
            radixTree.add(s, value, treeConfig);
        }
    }

    public V remove(CharSequence s) {
        V result = null;
        CharSequenceWithAssignableTerminalChar t = terminalNodesBySequence.get(s);
        if ( t != null) {
            terminalNodesBySequence.remove(s);
            mutableSequence.setSegment(t);
            result = removeAllSuffixes(mutableSequence, t);
        }
        return result;
    }

    private V removeAllSuffixes(MutableCharSequence s, CharSequenceWithAssignableTerminalChar t) {
        V value = null;
        for ( int c = 0; c < t.length() - 1; c++) {
            s.setStart(c);
            value = (V)radixTree.remove(mutableSequence, null, treeConfig);
        }
        return value;
    }

    public V get(CharSequence s) {
        LinkedList<V> result = null;
        CharSequenceWithAssignableTerminalChar c = terminalNodesBySequence.get(s);
        if ( c != null) {
            mutableSequence.setSegment(c);
            result = new LinkedList<V>();
            radixTree.get(mutableSequence, result, treeConfig);
            assert(result.size() < 2);
        }
        return result.size() > 0 ? result.get(0) : null;
    }

    @Override
    public <E extends Collection<V>> E getStartingWith(CharSequence s, E collection) {
        return getStartingWith(s, collection, Integer.MAX_VALUE, (ValueFilter<V>) ValueFilter.NULL_VALUE_FILTER);
    }
    
    @Override
    public <E extends Collection<V>> E getStartingWith(CharSequence s, E collection, int maxItems) {
       return getStartingWith(s, collection, maxItems, (ValueFilter<V>) ValueFilter.NULL_VALUE_FILTER);
    }

    @Override    
    public <E extends Collection<V>> E getStartingWith(CharSequence s, E collection, int maxItems, ValueFilter<V> valueFilter) {
        intTerminatorAdapter.setCharSequence(s);
        mutableSequence.setSegment(intTerminatorAdapter); //do not add terminal node
        radixTree.get(mutableSequence, collection, maxItems, treeConfig, valueFilter);
        return collection;
    }

    public void accept(TreeVisitor<V> v) {
        radixTree.accept(v, treeConfig);
    }

    @Override
    public void compress() {
        StringCompressionVisitor v = new StringCompressionVisitor();
        radixTree.accept(v, treeConfig);    
    }

    public TreeConfig<V> getTreeConfig() {
        return treeConfig;
    }

}
