window.org_vaadin_jcrop_Jcrop = function () {

    var elem = this.getElement(), self = this;
    var jcrop_api = null, oldTriggerRepaintValue = "0", enabled = true;

    this.clearSelection = function () {
        jcrop_api.release();
    }

    this.setSelection = function (x, y, width, height) {
        jcrop_api.setSelect([x, y, x + width, y + height]);
    }

    this.onStateChange = function (e) {
        var state = this.getState();

        // a little hack to only reinitialized the component when needed
        if (oldTriggerRepaintValue != state.triggerRepaint) {
            oldTriggerRepaintValue = state.triggerRepaint;
            // destroy and create image
            $(elem).empty();
            imageUrl = state.imageUrl;

            // create dom
            var imageElem = $("<img>").attr({"src": imageUrl, "class": "image-cropper"});
            $(elem).append(imageElem);

            var JcropOptions = {
                bgFade: true,
                bgOpacity: .5,
                boxWidth: $(elem).width(),
                boxHeight: $(elem).height(),
                minSize: [state.minSizeX, state.minSizeY],
                maxSize: [state.maxSizeX, state.maxSizeY],
                onSelect: function (data) {
                    self.getRpcProxy().cropSelectionChanged(data.x, data.y, data.w, data.h);
                },
                onRelease: function () {
                    self.getRpcProxy().cropSelectionChanged(0, 0, 0, 0);
                }
            };
            if (state.aspectRatio > 0) {
                JcropOptions.aspectRatio = state.aspectRatio;
            }

            // init Jcrop
            imageElem.Jcrop(JcropOptions, function () {
                jcrop_api = this;
            });

            // set selection when state contains selection
            if (state.cropWidth > 0 || state.cropHeight > 0) {
                this.setSelection(state.x, state.y, state.cropWidth, state.cropHeight);
            }
            if (!state.enabled) {
                jcrop_api.disable();
            }
        }

        if (enabled != state.enabled) {
            enabled = state.enabled;
            if (enabled) {
                jcrop_api.enable();
            } else {
                jcrop_api.disable();
            }
        }
    }

}