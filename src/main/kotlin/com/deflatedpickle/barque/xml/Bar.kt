package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamImplicit

@XStreamAlias("bar")
class Bar(@XStreamAlias("clazz")
          @XStreamAsAttribute
          val clazz: String,
          vararg widgets: Widget) {
    @XStreamImplicit(itemFieldName = "widget")
    val widgetList: List<Widget>? = widgets.asList()

    override fun toString(): String {
        return "Bar { clazz=$clazz, widgets=$widgetList }"
    }
}