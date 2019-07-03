package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamImplicit

@XStreamAlias("barque")
class XMLBarque(vararg monitors: XMLMonitor) {
    @XStreamImplicit(itemFieldName = "monitor")
    val monitorList: List<XMLMonitor>? = monitors.asList()

    override fun toString(): String {
        return "com.deflatedpickle.barque.Barque { monitors=${monitorList?.joinToString()} }"
    }
}