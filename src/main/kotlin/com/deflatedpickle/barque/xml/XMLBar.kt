package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamImplicit

@XStreamAlias("bar")
class XMLBar(@XStreamAlias("clazz")
          @XStreamAsAttribute
          val clazz: String,
             vararg widgets: XMLWidget) {
    @XStreamImplicit(itemFieldName = "widget")
    val widgetList: List<XMLWidget>? = widgets.asList()

    override fun toString(): String {
        return "XMLBar { clazz=$clazz, widgets=$widgetList }"
    }
}