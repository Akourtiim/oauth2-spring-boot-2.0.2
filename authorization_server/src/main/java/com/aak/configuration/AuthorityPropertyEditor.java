package com.aak.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

/**
 * Created by ahmed on 21.5.18.
 */
public class AuthorityPropertyEditor implements PropertyEditor {

    private GrantedAuthority grantedAuthority;

    @Override
    public void setValue(Object value) {
        this.grantedAuthority = (GrantedAuthority) value;
    }

    @Override
    public Object getValue() {
        return grantedAuthority;
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {

    }

    @Override
    public String getJavaInitializationString() {
        return null;
    }

    @Override
    public String getAsText() {
        return grantedAuthority.getAuthority();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.isEmpty()) {
            this.grantedAuthority = new SimpleGrantedAuthority(text);
        }
    }

    @Override
    public String[] getTags() {
        return new String[0];
    }

    @Override
    public Component getCustomEditor() {
        return null;
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
