package org.vaadin.jcrop.client;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * Transfer states to JavaScript connector
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public class JcropState extends JavaScriptComponentState {

    public String imageUrl;
    /**
     * height and width is used by other parts thats why cropWidth and -height
     */
    public int x = 0, y = 0, cropWidth = 0, cropHeight = 0;

    public float aspectRatio = 0;

    public int minSizeX = 0, minSizeY = 0, maxSizeX = 0, maxSizeY = 0;

    public String triggerRepaint = "1";
}
