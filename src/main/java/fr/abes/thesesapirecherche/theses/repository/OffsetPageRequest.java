package fr.abes.thesesapirecherche.theses.repository;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetPageRequest extends AbstractPageRequest {

    private final long offset;

    public OffsetPageRequest(long offset, int size) {
        super(0, size);
        this.offset = offset;
    }

    @Override public long getOffset() { return offset; }
    @Override public Sort getSort() { return Sort.unsorted(); }   // le tri passe par withSort
    @Override public Pageable next() { return new OffsetPageRequest(offset + getPageSize(), getPageSize()); }
    @Override public Pageable previous() { return new OffsetPageRequest(Math.max(0, offset - getPageSize()), getPageSize()); }
    @Override public Pageable first() { return new OffsetPageRequest(0, getPageSize()); }
    @Override public Pageable withPage(int page) { return new OffsetPageRequest((long) page * getPageSize(), getPageSize()); }
}