package com.villagemilk.beans;

import java.io.Serializable;

/**
 * Created by redskull on 7/13/2016.
 */
public class DnPagination implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String first;

    private String previous;

    private String next;

    private String last;

    /**
     * Get the first.
     */
    public String getFirst() {
        return first;
    }

    /**
     * Set the first.
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * Get the previous.
     */
    public String getPrevious() {
        return previous;
    }

    /**
     * Set the previous.
     */
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    /**
     * Get the next.
     */
    public String getNext() {
        return next;
    }

    /**
     * Set the next.
     */
    public void setNext(String next) {
        this.next = next;
    }

    /**
     * Get the last.
     */
    public String getLast() {
        return last;
    }

    /**
     * Set the last.
     */
    public void setLast(String last) {
        this.last = last;
    }
}
