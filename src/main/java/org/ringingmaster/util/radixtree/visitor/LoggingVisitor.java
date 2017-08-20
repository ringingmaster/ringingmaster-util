package org.ringingmaster.util.radixtree.visitor;


import org.ringingmaster.util.radixtree.RadixTree;
import org.ringingmaster.util.radixtree.TreeConfig;
import org.ringingmaster.util.radixtree.TreeVisitor;
import org.ringingmaster.util.radixtree.ValueFilter;

import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * User: nick
 * Date: 22/05/13
 * Time: 17:33
 */
public class LoggingVisitor<V> implements TreeVisitor<V> {

    private int indentLevel = 0;
    private PrintWriter writer;
    private TreeConfig<V> treeConfig;
    private LinkedList<V> values = new LinkedList<V>();
    private ValueFilter<V> valueFilter = ValueFilter.NULL_VALUE_FILTER;

    public LoggingVisitor(PrintWriter writer, TreeConfig<V> treeConfig) {
        this.writer = writer;
        this.treeConfig = treeConfig;
    }
    
    @Override
    public boolean visit(RadixTree<V> radixTree) {
        indentLevel++;
        StringBuilder sb = new StringBuilder();
        addIndent(indentLevel, sb);
        sb.append(radixTree.getLabel());
        values.clear();
        if ( radixTree.getValues(values, treeConfig, valueFilter).size() > 0) {
            sb.append("\t vals: ");
            for (Object o : values) {
                sb.append(o.toString()).append(" ");
            }
        }
        sb.append("\n");
        writer.print(sb.toString());
        writer.flush();
        return true;
    }

    @Override
    public void visitComplete(RadixTree<V> radixTree) {
        indentLevel--;
    }

    private void addIndent(int level, StringBuilder sb) {
        for ( int loop=0; loop < level; loop++) {
            sb.append("  ");
        }
    }
}
