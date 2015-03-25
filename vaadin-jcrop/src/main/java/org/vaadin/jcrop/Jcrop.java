package org.vaadin.jcrop;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.vaadin.jcrop.client.JcropServerRpc;
import org.vaadin.jcrop.client.JcropState;
import org.vaadin.jcrop.selection.JcropSelection;
import org.vaadin.jcrop.selection.JcropSelectionChanged;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.Page;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * Vaadin wrapper for the jQuery Plugin Jcrop(https://github.com/tapmodo/Jcrop)<br>
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
@StyleSheet("vaadin://jcrop/jquery.Jcrop.min.css")
@JavaScript({ "vaadin://jcrop/jquery-1.11.2.min.js", "vaadin://jcrop/jquery.Jcrop.min.js", "Jcrop.js" })
public class Jcrop extends AbstractJavaScriptComponent {

	private final List<JcropSelectionChanged> listeners = new ArrayList<>();
	private JcropSelection selection = null;

	private ConnectorResource resource;
	private String requestHandlerUri = null;

	public Jcrop() {
		/**
		 * register the ServerRpc to the server-implementation in order to listen to it's calls
		 */
		registerRpc(new JcropServerRpc() {
			@Override
			public void cropSelectionChanged(final Integer x, final Integer y, final Integer width, final Integer height) {
				if (Jcrop.this.isEnabled()) {
					// update the state values
					setSelection(x, y, width, height, false);
					// trigger listeners
					for (JcropSelectionChanged listener : Jcrop.this.listeners) {
						// selection will get updated within setSelection
						listener.selectionChanged(Jcrop.this.selection);
					}
				}
			}
		});
		setPrimaryStyleName("image-cropper");
	}

	/**
	 * will release the selection
	 */
	public void clearSelection() {
		getState().x = 0;
		getState().y = 0;
		getState().cropWidth = 0;
		getState().cropHeight = 0;
		callFunction("clearSelection");
	}

	/**
	 * change the base url of image. this will reinitialize the Jcrop.
	 *
	 * @param imageUrl
	 */
	public void setImageUrl(final String imageUrl) {
		getState().imageUrl = imageUrl;
		triggerRepaint();
	}

	/**
	 * will initialize a RequestHandler and set the imageUrl to the handler
	 * 
	 * @param resource
	 */
	public void setResource(final ConnectorResource resource) {
		initRequestHandler();
		this.resource = resource;
		setImageUrl(getRequestUri());
	}

	/**
	 * initalize a unique RequestHandler
	 */
	private void initRequestHandler() {
		if (this.requestHandlerUri == null) {
			this.requestHandlerUri = UUID.randomUUID()
					.toString();
			VaadinSession.getCurrent()
					.addRequestHandler(new RequestHandler() {

						@Override
						public boolean handleRequest(final VaadinSession session, final VaadinRequest request, final VaadinResponse response)
								throws IOException {
							if (String.format("/%s", Jcrop.this.requestHandlerUri)
									.equals(request.getPathInfo())) {
								Jcrop.this.resource.getStream()
										.writeResponse(request, response);
								return true;
							} else {
								return false;
							}
						}
					});
		}
	}

	/**
	 * generate's the requestUri for the RequestHandler with a ts-parameter to avoid caching of browser on resource-changes
	 * 
	 * @return
	 */
	private String getRequestUri() {
		URI uri = Page.getCurrent()
				.getLocation();
		String url = null;
		if ((uri.getPort() == 80) || (uri.getPort() == 443) || (uri.getPort() <= 0)) {
			url = uri.getScheme() + "://" + uri.getHost() + uri.getPath();
		} else {
			url = uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
		}
		if (url != null && !url.endsWith("/")) {
			url += "/";
		}
		url += this.requestHandlerUri + "?ts=" + new Date().getTime();
		return url;
	}

	/**
	 * get the current selection state
	 *
	 * @return could be null
	 */
	public JcropSelection getSelection() {
		return this.selection;
	}

	/**
	 * set the selection of Jcrop programmatically<br>
	 * is only working when the component is already attached to the screen
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setSelection(final int x, final int y, final int width, final int height) {
		setSelection(x, y, width, height, true);
	}

	/**
	 * set the selection of Jcrop programmatically<br>
	 * is only working when the component is already attached to the screen
	 *
	 * @param selection
	 */
	public void setSelection(final JcropSelection selection) {
		setSelection(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight(), true);
	}

	private void setSelection(final int x, final int y, final int width, final int height, final boolean triggerSelection) {
		getState().x = x;
		getState().y = y;
		getState().cropWidth = width;
		getState().cropHeight = height;
		this.selection = new JcropSelection(x, y, width, height, width <= 0 && height <= 0);
		if (triggerSelection) {
			callFunction("setSelection", x, y, width, height);
		}
	}

	/**
	 * set the aspectRatio of the selection<br>
	 * Aspect ratio of w/h (e.g. 1 for square)<br>
	 * by default it's 0 and so disabled
	 *
	 * @param aspectRatio
	 */
	public void setAspectRatio(final float aspectRatio) {
		if (getState().aspectRatio != aspectRatio) {
			getState().aspectRatio = aspectRatio;
			triggerRepaint();
		}
	}

	/**
	 * set the minimum size of the crop selection<br>
	 * Minimum width/height, use 0 for unbounded dimension<br>
	 * by default it's unbounded
	 *
	 * @param x
	 * @param y
	 */
	public void setMinCropSize(final int x, final int y) {
		if (getState().minSizeX != x || getState().minSizeY != y) {
			getState().minSizeX = x;
			getState().minSizeY = y;
			triggerRepaint();
		}
	}

	/**
	 * set the maximum size of the crop selection<br>
	 * Minimum width/height, use 0 for unbounded dimension<br>
	 * by default it's unbounded
	 *
	 * @param x
	 * @param y
	 */
	public void setMaxCropSize(final int x, final int y) {
		if (getState().maxSizeX != x || getState().maxSizeY != y) {
			getState().maxSizeX = x;
			getState().maxSizeY = y;
			triggerRepaint();
		}
	}

	/**
	 * add a selection on the selectionChanged event
	 *
	 * @param listener
	 */
	public void addListener(final JcropSelectionChanged listener) {
		this.listeners.add(listener);
	}

	/**
	 * remove a selection of the selectionChanged event
	 *
	 * @param listener
	 */
	public void removeListener(final JcropSelectionChanged listener) {
		this.listeners.remove(listener);
	}

	/**
	 * explicitly map the custom state object to the server implementation
	 *
	 * @return
	 */
	@Override
	protected JcropState getState() {
		return (JcropState) super.getState();
	}

	/**
	 * used only to reinitialize the jCrop component when needed
	 */
	private void triggerRepaint() {
		this.getState().triggerRepaint = String.valueOf(new Date().getTime());
	}

}
