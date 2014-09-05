App.SoundcloudPlayer = Em.Component.extend({
    tagName: "iframe",
    /*
    var iframe = document.querySelector('#iframe');
          iframe.src = location.protocol + "//" + host2widgetBaseUrl[location.hostname] + "?url=" + widgetUrl;
          var widget = SC.Widget(iframe);

    */
    //classNames: ["iframe"],
    widget: null,

    didInsertElement: function() {
        this.element.width = "100%";
        this.element.height = "150px";
        this.element.scrolling = "no";
        this.element.frameborder = "no";
        this.element.src = "w.soundcloud.com/player/?url="+this.soundcloudUrl;
        this.set("widget", SC.Widget(this.element));
    },

});
