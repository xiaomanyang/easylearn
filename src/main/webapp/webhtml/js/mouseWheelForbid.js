/**
 * Created by sgs on 2017/9/20.
 */
var EventUtil = {
    getEvent: function (event) {
        return event ? event : window.event;
    },
    addHandler: function (element, type, handler) {
        if (element.addEventListener) {
            element.addEventListener(type, handler, false);
        } else if (element.attachEvent) {
            element.attachEvent("on" + type, handler);
        } else {
            element["on" + type] = handler;
        }
    },
    getWheelDelta: function (e) {
        if(e.shiftKey){
            e.preventDefault ? e.preventDefault(): e.returnValue= false;
        }
    }
};
function handleMouseWheel(event) {
    event = EventUtil.getEvent(event);
    var delta = EventUtil.getWheelDelta(event);
};
EventUtil.addHandler(document, "mousewheel", handleMouseWheel);
EventUtil.addHandler(document, "DOMMouseScroll", handleMouseWheel);
if (document.getElementById('iframe')) {
        EventUtil.addHandler(document.getElementById('iframe').contentWindow.document, "mousewheel", handleMouseWheel);
        EventUtil.addHandler(document.getElementById('iframe').contentWindow.document, "DOMMouseScroll", handleMouseWheel);
};
