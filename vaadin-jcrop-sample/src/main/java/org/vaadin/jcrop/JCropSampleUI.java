package org.vaadin.jcrop;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.jcrop.selection.JcropSelection;
import org.vaadin.jcrop.selection.JcropSelectionChanged;

import javax.servlet.annotation.WebServlet;

@Theme("valo")
public class JCropSampleUI extends UI {

    private final Jcrop jcrop = new Jcrop();
    private final Label listenerLabel = new Label("cropEvent: ");

    private final String[] imageUrls = new String[]{"./VAADIN/images/pic-1.jpg", "./VAADIN/images/pic-2.jpg", "./VAADIN/images/pic-3.jpg"};
    private final TextField minX = genNumberField("x", "0"), minY = genNumberField("y", "0");
    private final TextField maxX = genNumberField("x", "0"), maxY = genNumberField("y", "0");
    private final TextField aspectRatio = genNumberField("aspectRatio", "0");
    private int currentIndex = 1;
    private Button toggleEnabled;

    private TextField genNumberField(final String caption, final String initValue) {
        TextField textField = new TextField();
        textField.setValue(initValue);
        textField.setPlaceholder(caption);
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
        Button setMinMaxSelectionBtn = new Button("changeSettings", (ClickListener) clickEvent -> {
            JCropSampleUI.this.jcrop.setMinCropSize(Integer.parseInt(JCropSampleUI.this.minX.getValue()), Integer.parseInt(JCropSampleUI.this.minY.getValue()));
            JCropSampleUI.this.jcrop.setMaxCropSize(Integer.parseInt(JCropSampleUI.this.maxX.getValue()), Integer.parseInt(JCropSampleUI.this.maxY.getValue()));
            JCropSampleUI.this.jcrop.setAspectRatio(Integer.parseInt(JCropSampleUI.this.aspectRatio.getValue()));

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
