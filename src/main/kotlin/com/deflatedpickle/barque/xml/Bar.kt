package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute

@XStreamAlias("bar")
class Bar(@XStreamAlias("clazz")
          @XStreamAsAttribute
          val clazz: String) {
    override fun toString(): String {
        return "Bar { clazz=$clazz }"
    }
}