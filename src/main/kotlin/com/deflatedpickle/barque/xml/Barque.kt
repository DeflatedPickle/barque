package com.deflatedpickle.barque.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamImplicit

@XStreamAlias("barque")
class Barque(vararg monitors: Monitor) {
    @XStreamImplicit(itemFieldName = "monitor")
    val monitorList: List<Monitor> = monitors.asList()

    override fun toString(): String {
        return "Barque { monitors=${monitorList.joinToString()} }"
    }
}