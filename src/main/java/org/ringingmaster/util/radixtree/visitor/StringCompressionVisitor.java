package org.ringingmaster.util.radixtree.visitor;


import org.ringingmaster.util.radixtree.MutableCharSequence;
import org.ringingmaster.util.radixtree.MutableSequence;
import org.ringingmaster.util.radixtree.RadixTree;
import org.ringingmaster.util.radixtree.TreeVisitor;
import org.ringingmaster.util.radixtree.sequence.CharSequenceWithIntTerminatorAdapter;

import java.util.HashMap;

/**
 * User: nick
 * Date: 06/06/13
 * Time: 08:27
 * 
 * Reduce memory usage by reusing the same base sequence for each similar label
 * 
 * Generate shared base sequences which contain all known edge labels 
 * each up to Short.MAX_VALUE in length
 */
public class StringCompressionVisitor<V> implements TreeVisitor<V> {
    
    private HashMap<CharSequence, MutableCharSequence> instancePerLabel = new HashMap<CharSequence, MutableCharSequence>();
    
    StringBuilder sb = new StringBuilder();    
    CharSequenceWithIntTerminatorAdapter intTerminatorAdapter = new CharSequenceWithIntTerminatorAdapter(sb);
    
    @Override
    public boolean visit(RadixTree<V> radixTree) {
        String label = radixTree.getLabel();
        MutableCharSequence existingNodeWithSameLabel = instancePerLabel.get(label);
        if ( existingNodeWithSameLabel == null ) {
            
            //internal radix nodes use a short start/end
            if ( sb.length() + label.length() > Short.MAX_VALUE) {
                sb = new StringBuilder();
            }

            int start = sb.length();
            int end = sb.length() + label.length();
            MutableCharSequence s = new MutableSequence(intTerminatorAdapter, start, end);
            sb.append(label);
            instancePerLabel.put(label, s);
            
            radixTree.setLabel(intTerminatorAdapter, start, end);
        } else {
            radixTree.setLabel(
                existingNodeWithSameLabel.getImmutableBaseSequence(), 
                existingNodeWithSameLabel.getBaseSequenceStart(), 
                existingNodeWithSameLabel.getBaseSequenceEnd()
            );
        }
        return true;
    }

    @Override
    public void visitComplete(RadixTree<V> radixTree) {
    }
    
    public int getSize() {
        return instancePerLabel.size();
    }
    
}
