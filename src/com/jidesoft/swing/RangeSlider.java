/*
 * @(#)RangeSlider.java 11/22/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.swing;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.lang.reflect.Method;

/**
 * <tt>RangeSlider</tt> is a slider that can be used to select a range. A regular slider has only one thumb. So it can
 * only be used to select one value. <tt>RangeSlider</tt> has two thumbs. Each one can be moved independently or both
 * are moved together.
 * <p/>
 * {@link #getLowValue()} will return the value of low range and {@link #getHighValue()} is the high range.
 */
public class RangeSlider extends JSlider {

    private static final String uiClassID = "SliderUI";

    private boolean _rangeDraggable = true;
    public static final String CLIENT_PROPERTY_MOUSE_POSITION = "RangeSlider.mousePosition";

    /**
     * Creates a horizontal range slider with the range 0 to 100 and initial low and high values both at 50.
     */
    public RangeSlider() {
    }

    /**
     * Creates a range slider using the specified orientation with the range 0 to 100 and initial low and high values
     * both at 50.
     *
     * @param orientation the orientation of the <code>RangeSlider</code>.
     */
    public RangeSlider(int orientation) {
        super(orientation);
    }

    /**
     * Creates a horizontal slider using the specified min and max with an initial value equal to the average of the min
     * plus max. and initial low and high values both at 50.
     *
     * @param min the minimum value of the slider.
     * @param max the maximum value of the slider.
     */
    public RangeSlider(int min, int max) {
        super(min, max);
    }

    /**
     * Creates a horizontal slider using the specified min, max, low and high value.
     *
     * @param min  the minimum value of the slider.
     * @param max  the maximum value of the slider.
     * @param low  the low value of the slider since it is a range.
     * @param high the high value of the slider since it is a range.
     */
    public RangeSlider(int min, int max, int low, int high) {
        super(new DefaultBoundedRangeModel(low, high - low,
                min, max));
    }

    /**
     * Resets the UI property to a value from the current look and feel.
     *
     * @see javax.swing.JComponent#updateUI
     */
    @Override
    public void updateUI() {
        if (UIDefaultsLookup.get("RangeSliderUI") == null) {
            LookAndFeelFactory.installJideExtension();
        }
        try {
            Class<?> uiClass = Class.forName(UIManager.getString("RangeSliderUI"));
            Class acClass = javax.swing.JComponent.class;
            Method m = uiClass.getMethod("createUI", new Class[]{acClass});
            if (m != null) {
                Object uiObject = m.invoke(null, new Object[]{this});
                setUI((ComponentUI) uiObject);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns a string that specifies the name of the L&F class that renders this component.
     *
     * @return the string "RangeSliderUI"
     * @see javax.swing.JComponent#getUIClassID
     * @see javax.swing.UIDefaults#getUI
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Returns the range slider's low value.
     *
     * @return the range slider's low value.
     */
    public int getLowValue() {
        return getModel().getValue();
    }

    /**
     * Returns the range slider's high value.
     *
     * @return the range slider's high value.
     */
    public int getHighValue() {
        return getModel().getValue() + getModel().getExtent();
    }

    /**
     * Returns true if the specified value is within the range slider's range.
     *
     * @param value value
     * @return true if the specified value is within the range slider's range.
     */
    public boolean contains(int value) {
        return (value >= getLowValue() && value <= getHighValue());
    }

    @Override
    public void setValue(int value) {
        Object clientProperty = getClientProperty(CLIENT_PROPERTY_MOUSE_POSITION);
        if(clientProperty != null) {
            if(Boolean.TRUE.equals(clientProperty)) {
                setLowValue(value);
            }
            else{
                setHighValue(value);
            }
        }
        else {
            setLowValue(value);
        }
    }

    /**
     * Sets the range slider's low value.  This method just forwards the value to the model.
     *
     * @param lowValue the new low value
     */
    public void setLowValue(int lowValue) {
        int high;
        if ((lowValue + getModel().getExtent()) > getMaximum()) {
            high = getMaximum();
        }
        else {
            high = getHighValue();
        }
        int extent = high - lowValue;

        getModel().setRangeProperties(lowValue, extent,
                getMinimum(), getMaximum(), true);
    }

    /**
     * Sets the range slider's high value.  This method just forwards the value to the model.
     *
     * @param highValue the new high value
     */
    public void setHighValue(int highValue) {
        getModel().setExtent(highValue - getLowValue());
    }

    /**
     * Checks if the range is draggable. If true, user can drag the area between the two thumbs to drag the range.
     *
     * @return true or false.
     */
    public boolean isRangeDraggable() {
        return _rangeDraggable;
    }

    /**
     * Sets the flag if the range is draggable. If true, user can drag the area between the two thumbs to drag the range.
     *
     * @param rangeDraggable true or false.
     */
    public void setRangeDraggable(boolean rangeDraggable) {
        _rangeDraggable = rangeDraggable;
    }
}