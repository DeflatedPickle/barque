package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamImplicit

@XStreamAlias("monitor")
class Monitor(@XStreamAlias("type")
              @XStreamAsAttribute
              val type: String,
              vararg bars: Bar) {
    @XStreamImplicit(itemFieldName = "bar")
    val barList: List<Bar>? = bars.asList()

    override fun toString(): String {
        return "Monitor { type=$type, bars=${barList?.joinToString()} }"
    }
}