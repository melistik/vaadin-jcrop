package org.vaadin.jcrop;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.vaadin.jcrop.selection.JcropSelection;
import org.vaadin.jcrop.selection.JcropSelectionChanged;


@Theme("valo")
public class JCropSampleUI extends UI {

    private Jcrop jcrop = new Jcrop();
    private Label listenerLabel = new Label("cropEvent: ");

    private String[] imageUrls = new String[]{"http://www.softwareag.com/blog/reality_check/wp-content/uploads/2013/11/1317251262950612915clue_simple_clouds.png", "http://www.fotos.sc/img2/u/heliocentric/n/Kfer_Gras.jpg"};
    private int currentIndex = 1;

    private Button toggleEnabled;
    private TextField minX = genNumberField("x", 0), minY = genNumberField("y", 0), maxX = genNumberField("x", 0), maxY = genNumberField("y", 0);

    private TextField genNumberField(String caption, int initValue) {
        TextField textField = new TextField();
        textField.setValue(String.valueOf(initValue));
        textField.setConverter(new StringToIntegerConverter());
        textField.setInputPrompt(caption);
        textField.setImmediate(true);
        textField.setInvalidAllowed(false);
        textField.setValidationVisible(true);
        textField.setWidth(40, Unit.PIXELS);
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
                jcrop.setImageUrl(imageUrls[(++currentIndex) % 2]);
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
        maxMinSizeLayout.addComponent(new Label("minSize"));
        maxMinSizeLayout.addComponent(minX);
        maxMinSizeLayout.addComponent(minY);
        maxMinSizeLayout.addComponent(new Label("minSize"));
        maxMinSizeLayout.addComponent(maxX);
        maxMinSizeLayout.addComponent(maxY);
        maxMinSizeLayout.addComponent(new Button("switchImage", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                System.out.print("minx: " + minX.getConvertedValue() + " minY:" + minY.getConvertedValue());
                jcrop.setMinCropSize((int) minX.getConvertedValue(), (int) minY.getConvertedValue());
                jcrop.setMinCropSize((int) maxX.getConvertedValue(), (int) maxY.getConvertedValue());

            }
        }));
        layout.addComponent(maxMinSizeLayout);

        layout.addComponent(listenerLabel);

        jcrop.setWidth(600, Unit.PIXELS);
        jcrop.setHeight(400, Unit.PIXELS);
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
