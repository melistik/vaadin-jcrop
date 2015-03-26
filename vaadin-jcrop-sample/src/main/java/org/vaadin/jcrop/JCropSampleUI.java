package org.vaadin.jcrop;

import javax.servlet.annotation.WebServlet;

import org.vaadin.jcrop.selection.JcropSelection;
import org.vaadin.jcrop.selection.JcropSelectionChanged;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
public class JCropSampleUI extends UI {

	private final Jcrop jcrop = new Jcrop();
	private final Label listenerLabel = new Label("cropEvent: ");

	private final String[] imageUrls = new String[] { "./VAADIN/images/pic-1.jpg", "./VAADIN/images/pic-2.jpg", "./VAADIN/images/pic-3.jpg" };
	private int currentIndex = 1;

	private Button toggleEnabled;
	private final TextField minX = genNumberField("x", "0", new StringToIntegerConverter()), minY = genNumberField("y", "0", new StringToIntegerConverter());
	private final TextField maxX = genNumberField("x", "0", new StringToIntegerConverter()), maxY = genNumberField("y", "0", new StringToIntegerConverter());
	private final TextField aspectRatio = genNumberField("aspectRatio", "0", new StringToFloatConverter());

	private TextField genNumberField(final String caption, final String initValue, final Converter converter) {
		TextField textField = new TextField();
		textField.setValue(initValue);
		textField.setConverter(converter);
		textField.setInputPrompt(caption);
		textField.setImmediate(true);
		textField.setInvalidAllowed(false);
		textField.setValidationVisible(true);
		textField.setWidth(60, Unit.PIXELS);
		textField.addStyleName(ValoTheme.TEXTFIELD_TINY);
		return textField;
	}

	@Override
	protected void init(final VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		setContent(layout);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		buttonLayout.addComponent(new Button("setSelection", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent clickEvent) {
				JCropSampleUI.this.jcrop.setSelection(10, 10, 100, 50);
			}
		}));
		buttonLayout.addComponent(new Button("clearSelection", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent clickEvent) {
				JCropSampleUI.this.jcrop.clearSelection();
			}
		}));
		buttonLayout.addComponent(new Button("switchImage", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent clickEvent) {
				JCropSampleUI.this.jcrop.setImageUrl(JCropSampleUI.this.imageUrls[(++JCropSampleUI.this.currentIndex) % 3]);
			}
		}));

		buttonLayout.addComponent(new Button("setResource", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent clickEvent) {
				JCropSampleUI.this.jcrop.setResource(new StreamResource(new SampleStreamResource(), "sample-image.png"));
			}
		}));
		this.toggleEnabled = new Button("toggle isEnabled", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent clickEvent) {
				if (JCropSampleUI.this.toggleEnabled.getCaption()
						.contains("isEnabled")) {
					JCropSampleUI.this.jcrop.setEnabled(false);
					JCropSampleUI.this.toggleEnabled.setCaption("toggle isDisabled");
				} else {
					JCropSampleUI.this.jcrop.setEnabled(true);
					JCropSampleUI.this.toggleEnabled.setCaption("toggle isEnabled");
				}
			}
		});
		buttonLayout.addComponent(this.toggleEnabled);
		layout.addComponent(buttonLayout);

		HorizontalLayout maxMinSizeLayout = new HorizontalLayout();
		maxMinSizeLayout.setSpacing(true);

		maxMinSizeLayout.addComponent(new Label("minSize"));
		maxMinSizeLayout.addComponent(this.minX);
		maxMinSizeLayout.addComponent(this.minY);
		maxMinSizeLayout.addComponent(new Label("maxSize"));
		maxMinSizeLayout.addComponent(this.maxX);
		maxMinSizeLayout.addComponent(this.maxY);
		maxMinSizeLayout.addComponent(new Label("aspectRatio"));
		maxMinSizeLayout.addComponent(this.aspectRatio);
		Button setMinMaxSelectionBtn = new Button("changeSettings", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent clickEvent) {
				JCropSampleUI.this.jcrop.setMinCropSize((int) JCropSampleUI.this.minX.getConvertedValue(), (int) JCropSampleUI.this.minY.getConvertedValue());
				JCropSampleUI.this.jcrop.setMaxCropSize((int) JCropSampleUI.this.maxX.getConvertedValue(), (int) JCropSampleUI.this.maxY.getConvertedValue());
				JCropSampleUI.this.jcrop.setAspectRatio((float) JCropSampleUI.this.aspectRatio.getConvertedValue());

			}
		});
		setMinMaxSelectionBtn.addStyleName(ValoTheme.BUTTON_TINY);
		maxMinSizeLayout.addComponent(setMinMaxSelectionBtn);
		layout.addComponent(maxMinSizeLayout);

		layout.addComponent(this.listenerLabel);

		this.jcrop.setWidth(750, Unit.PIXELS);
		this.jcrop.setHeight(500, Unit.PIXELS);
		this.jcrop.setImageUrl(this.imageUrls[this.currentIndex]);
		layout.addComponent(this.jcrop);
		layout.setExpandRatio(this.jcrop, 1);
		this.jcrop.addListener(new JcropSelectionChanged() {
			@Override
			public void selectionChanged(final JcropSelection selection) {
				JCropSampleUI.this.listenerLabel.setValue(selection.toString());
			}
		});

	}

	@WebServlet(
			urlPatterns = "/*",
			name = "JCropSampleUIServlet",
			asyncSupported = true)
	@VaadinServletConfiguration(
			ui = org.vaadin.jcrop.JCropSampleUI.class,
			productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
