package com.villagemilk.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by redskull on 7/13/2016.
 */
public class DnResponse implements Serializable
{

    private static final long serialVersionUID = 1L;

    //private DnRespStatus status;

    private String message;

    private List<DnEntity> results;

    //private List<DnError> errors;

    private DnPagination pagination;

   /* @JsonIgnore
    private MediaType responseType;
*/
   /* @JsonIgnore
    private Map<String, String> headers = new HashMap<>();
*/
    /**
     * Add a result.
     */
    public void addResult(DnEntity result)
    {
        getResults().add(result);
    }

    /**
     * Add an error.
     */
   /* public void addError(DnError pkError) {
        getErrors().add(pkError);
    }
*/
    /**
     * Get the header.
     */
    /*public String getHeader(String name) {
        return getHeaders().get(name);
    }
*/
    /**
     * Set the header.
     */
    /*public void setHeader(String name, String header) {
        getHeaders().put(name, header);
    }*/

    /**
     * Get the status.
     */
  /*  public DnRespStatus getStatus() {
        return status;
    }*/

    /**
     * Set the status.
     */
  /*  public void setStatus(DnRespStatus status) {
        this.status = status;
    }*/

    /**
     * Get the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the results.
     */
    public List<DnEntity> getResults()
    {
        if (results == null)
        {
            results = new ArrayList<>();
        }
        return results;
    }

    /**
     * Set the results.
     */
    public void setResults(List<DnEntity> results) {
        this.results = results;
    }

    /**
     * Get the errors.
     */
  /*  public List<DnError> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }*/

    /**
     * Set the errors.
     */
   /* public void setErrors(List<DnError> errors) {
        this.errors = errors;
    }*/

    /**
     * Get the pagination.
     */
    public DnPagination getPagination() {
        return pagination;
    }

    /**
     * Set the pagination.
     */
    public void setPagination(DnPagination pagination) {
        this.pagination = pagination;
    }

    /**
     * Get the responseType.
     */
    /*public MediaType getResponseType() {
        return responseType;
    }*/

    /**
     * Set the responseType.
     */
   /* public void setResponseType(MediaType responseType) {
        this.responseType = responseType;
    }*/

    /**
     * Get the headers.
     */
   /* public Map<String, String> getHeaders() {
        return headers;
    }
*/
}
