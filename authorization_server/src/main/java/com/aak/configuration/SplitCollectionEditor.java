package com.aak.configuration;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Created by ahmed on 21.5.18.
 */
public class SplitCollectionEditor extends CustomCollectionEditor{

    private final Class<? extends Collection> collectionType;
    private final String splitRegex;

    public SplitCollectionEditor(Class<? extends Collection> collectionType, String splitRegex) {
        super(collectionType, true);
        this.collectionType = collectionType;
        this.splitRegex = splitRegex;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            super.setValue(super.createCollection(this.collectionType, 0));
        } else {
            super.setValue(text.split(splitRegex));
        }
    }
}
