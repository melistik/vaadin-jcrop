package org.vaadin.jcrop.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * is been called from JavaScript connector
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public interface JcropServerRpc extends ServerRpc {

    void cropSelectionChanged(Integer x, Integer y, Integer width, Integer height);
}
