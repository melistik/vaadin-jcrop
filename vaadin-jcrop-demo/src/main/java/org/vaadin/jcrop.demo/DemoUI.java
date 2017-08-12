package org.vaadin.jcrop.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.jcrop.Jcrop;
import org.vaadin.jcrop.selection.JcropSelection;
import org.vaadin.jcrop.selection.JcropSelectionChanged;


@SpringUI()
@Theme("valo")
public class DemoUI extends UI {

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
                DemoUI.this.jcrop.setSelection(10, 10, 100, 50);
            }
        }));
        buttonLayout.addComponent(new Button("clearSelection", new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent clickEvent) {
                DemoUI.this.jcrop.clearSelection();
            }
        }));
        buttonLayout.addComponent(new Button("switchImage", new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent clickEvent) {
                DemoUI.this.jcrop.setImageUrl(DemoUI.this.imageUrls[(++DemoUI.this.currentIndex) % 3]);
            }
        }));

        buttonLayout.addComponent(new Button("setResource", new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent clickEvent) {
                DemoUI.this.jcrop.setResource(new StreamResource(new SampleStreamResource(), "sample-image.png"));
            }
        }));
        this.toggleEnabled = new Button("toggle isEnabled", new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent clickEvent) {
                if (DemoUI.this.toggleEnabled.getCaption()
                        .contains("isEnabled")) {
                    DemoUI.this.jcrop.setEnabled(false);
                    DemoUI.this.toggleEnabled.setCaption("toggle isDisabled");
                } else {
                    DemoUI.this.jcrop.setEnabled(true);
                    DemoUI.this.toggleEnabled.setCaption("toggle isEnabled");
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
            DemoUI.this.jcrop.setMinCropSize(Integer.parseInt(DemoUI.this.minX.getValue()), Integer.parseInt(DemoUI.this.minY.getValue()));
            DemoUI.this.jcrop.setMaxCropSize(Integer.parseInt(DemoUI.this.maxX.getValue()), Integer.parseInt(DemoUI.this.maxY.getValue()));
            DemoUI.this.jcrop.setAspectRatio(Integer.parseInt(DemoUI.this.aspectRatio.getValue()));

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
                DemoUI.this.listenerLabel.setValue(selection.toString());
            }
        });

    }

}
