package org.vaadin.jcrop;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.jcrop.selection.JcropSelection;
import org.vaadin.jcrop.selection.JcropSelectionChanged;

import javax.servlet.annotation.WebServlet;


@Theme("valo")
public class JCropSampleUI extends UI {

    private Jcrop jcrop = new Jcrop();
    private Label listenerLabel = new Label("cropEvent: ");

    private String[] imageUrls = new String[]{"./VAADIN/images/pic-1.jpg", "./VAADIN/images/pic-2.jpg", "./VAADIN/images/pic-3.jpg"};
    private int currentIndex = 1;

    private Button toggleEnabled;
    private TextField minX = genNumberField("x", "0", new StringToIntegerConverter()), minY = genNumberField("y", "0", new StringToIntegerConverter());
    private TextField maxX = genNumberField("x", "0", new StringToIntegerConverter()), maxY = genNumberField("y", "0", new StringToIntegerConverter());
    private TextField aspectRatio = genNumberField("aspectRatio", "0", new StringToFloatConverter());

    private TextField genNumberField(String caption, String initValue, Converter converter) {
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
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        setContent(layout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        buttonLayout.addComponent(new Button("setSelectio", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                jcrop.setSelection(10, 10, 100, 50);
            }
        }));
        buttonLayout.addComponent(new Button("clearSelection", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                jcrop.clearSelection();
            }
        }));
        buttonLayout.addComponent(new Button("switchImage", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                jcrop.setImageUrl(imageUrls[(++currentIndex) % 3]);
            }
        }));
        toggleEnabled = new Button("toggle isEnabled", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                if (toggleEnabled.getCaption().contains("isEnabled")) {
                    jcrop.setEnabled(false);
                    toggleEnabled.setCaption("toggle isDisabled");
                } else {
                    jcrop.setEnabled(true);
                    toggleEnabled.setCaption("toggle isEnabled");
                }
            }
        });
        buttonLayout.addComponent(toggleEnabled);
        layout.addComponent(buttonLayout);

        HorizontalLayout maxMinSizeLayout = new HorizontalLayout();
        maxMinSizeLayout.setSpacing(true);

        maxMinSizeLayout.addComponent(new Label("minSize"));
        maxMinSizeLayout.addComponent(minX);
        maxMinSizeLayout.addComponent(minY);
        maxMinSizeLayout.addComponent(new Label("maxSize"));
        maxMinSizeLayout.addComponent(maxX);
        maxMinSizeLayout.addComponent(maxY);
        maxMinSizeLayout.addComponent(new Label("aspectRatio"));
        maxMinSizeLayout.addComponent(aspectRatio);
        Button setMinMaxSelectionBtn = new Button("changeSettings", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                jcrop.setMinCropSize((int) minX.getConvertedValue(), (int) minY.getConvertedValue());
                jcrop.setMaxCropSize((int) maxX.getConvertedValue(), (int) maxY.getConvertedValue());
                jcrop.setAspectRatio((float) aspectRatio.getConvertedValue());

            }
        });
        setMinMaxSelectionBtn.addStyleName(ValoTheme.BUTTON_TINY);
        maxMinSizeLayout.addComponent(setMinMaxSelectionBtn);
        layout.addComponent(maxMinSizeLayout);


        layout.addComponent(listenerLabel);

        jcrop.setWidth(750, Unit.PIXELS);
        jcrop.setHeight(500, Unit.PIXELS);
        jcrop.setImageUrl(imageUrls[currentIndex]);
        layout.addComponent(jcrop);
        layout.setExpandRatio(jcrop, 1);
        jcrop.addListener(new JcropSelectionChanged() {
            @Override
            public void selectionChanged(JcropSelection selection) {
                listenerLabel.setValue(selection.toString());
            }
        });
    }

    @WebServlet(urlPatterns = "/*", name = "JCropSampleUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = org.vaadin.jcrop.JCropSampleUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
