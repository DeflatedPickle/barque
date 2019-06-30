package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute

@XStreamAlias("widget")
class Widget(@XStreamAlias("clazz")
             @XStreamAsAttribute
             val clazz: String,
             @XStreamAlias("script")
             @XStreamAsAttribute
             val script: String) {
    override fun toString(): String {
        return "Widget { clazz=$clazz, script=$script }"
    }
}