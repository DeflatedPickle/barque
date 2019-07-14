package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamImplicit

@XStreamAlias("monitor")
class XMLMonitor(@XStreamAlias("type")
              @XStreamAsAttribute
              val type: String,
                 vararg bars: XMLBar) {
    @XStreamImplicit(itemFieldName = "bar")
    val barList: List<XMLBar>? = bars.asList()

    override fun toString(): String {
        return "XMLMonitor { type=$type, bars=${barList?.joinToString()} }"
    }
}