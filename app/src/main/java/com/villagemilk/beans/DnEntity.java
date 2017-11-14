package com.villagemilk.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by redskull on 7/13/2016.
 */
public class DnEntity
{
    private static final long serialVersionUID = 1L;

    private String id;

    private LinkedHashMap<String, Object> properties;

    /**
     * Construct a new empty base entity.
     */
    public DnEntity() {

    }

    /**
     * Construct a new base entity with the given id.
     */
    public DnEntity(String id) {
        this.id = id;
    }

    /**
     * Get the property.
     */
    public Object getProperty(String name)
    {
        return getProperties().get(name);
    }

    /**
     * Get the string representation of the property.
     */
    public String getPropertyAsString(String name) {
        Object value = getProperty(name);
        if (value == null)
        {
            return null;
        }
        else
        {
            return value.toString();
        }
    }

    /**
     * Get the value of the property as a list of strings.
     */
    @SuppressWarnings("rawtypes")
    public List<String> getPropertyAsList(String name) {
        Object values = getProperty(name);
        if (values == null) {
            return null;
        } else {
            List<String> propertyValues = new ArrayList<>();
            if (values instanceof Collection) {
                Collection valuesList = (Collection) values;
                for (Object value : valuesList) {
                    if (value != null) {
                        propertyValues.add(value.toString());
                    }
                }
            } else {
                propertyValues.add(values.toString());
            }
            return propertyValues;
        }
    }

    /**
     * Get the value of the property as a set of strings.
     */
    @SuppressWarnings("rawtypes")
    public Set<String> getPropertyAsSet(String name) {
        Object values = getProperty(name);
        if (values == null) {
            return null;
        } else {
            Set<String> propertyValues = new LinkedHashSet<>();
            if (values instanceof Collection) {
                Collection valuesList = (Collection) values;
                for (Object value : valuesList) {
                    if (value != null) {
                        propertyValues.add(value.toString());
                    }
                }
            } else {
                propertyValues.add(values.toString());
            }
            return propertyValues;
        }
    }

    /**
     * Get the value of the property as a list of maps.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<LinkedHashMap<String, Object>> getPropertyAsMapList(String name) {
        Object values = getProperty(name);
        if (values == null) {
            return null;
        }
        else
        {
            List<LinkedHashMap<String, Object>> propertyValues = new ArrayList<>();
            if (values instanceof Collection)
            {
                Collection valuesList = (Collection) values;
                for (Object value : valuesList)
                {
                    if (value != null && value instanceof Map)
                    {
                        propertyValues.add((LinkedHashMap<String, Object>) value);
                    }
                }
            }
            else
            {
                if (values != null && values instanceof Map)
                {
                    propertyValues.add((LinkedHashMap<String, Object>) values);
                }
            }
            return propertyValues;
        }
    }

    /**
     * Set the property.
     */
    public void setProperty(String name, Object param)
    {
        getProperties().put(name, param);
    }

    /**
     * Get the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set the id.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Get the properties.
     */
    public LinkedHashMap<String, Object> getProperties()
    {
        if (properties == null)
        {
            properties = new LinkedHashMap<>();
        }
        return properties;
    }

    /**
     * Set the properties.
     */
    public void setProperties(LinkedHashMap<String, Object> properties)
    {
        this.properties = properties;
    }
}
