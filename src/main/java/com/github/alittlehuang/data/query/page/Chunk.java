package com.github.alittlehuang.data.query.page;

import com.github.alittlehuang.data.util.Assert;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class Chunk<T> implements Serializable, Page<T> {

    private static final long serialVersionUID = 867755909294344406L;

    private final List<T> content = new ArrayList<>();
    private final @Getter
    Pageable pageable;

    /**
     * Creates a new {@link Chunk} with the given content and the given governing {@link Pageable}.
     *
     * @param content  must not be {@literal null}.
     * @param pageable must not be {@literal null}.
     */
    public Chunk(List<T> content, Pageable pageable) {

        Assert.notNull(content, "Content must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");

        this.content.addAll(content);
        this.pageable = pageable;
    }

    @Override
    public int getNumber() {
        return pageable.getPageNumber();
    }

    @Override
    public int getSize() {
        return pageable.getPageSize();
    }

    public int getNumberOfElements() {
        return content.size();
    }

    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    public boolean isFirst() {
        return !hasPrevious();
    }

    public boolean isLast() {
        return !hasNext();
    }

    protected abstract boolean hasNext();

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    public Iterator<T> iterator() {
        return content.iterator();
    }

    /**
     * Applies the given {@link Function} to the content of the {@link Chunk}.
     *
     * @param converter must not be {@literal null}.
     */
    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {

        Assert.notNull(converter, "Function must not be null!");

        return this.getContent().stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {

        if ( this == obj ) {
            return true;
        }

        if ( !( obj instanceof Chunk<?> ) ) {
            return false;
        }

        Chunk<?> that = (Chunk<?>) obj;

        boolean contentEqual = this.content.equals(that.content);
        boolean pageableEqual = this.pageable.equals(that.pageable);

        return contentEqual && pageableEqual;
    }

    @Override
    public int hashCode() {

        int result = 17;

        result += 31 * pageable.hashCode();
        result += 31 * content.hashCode();

        return result;
    }
}
