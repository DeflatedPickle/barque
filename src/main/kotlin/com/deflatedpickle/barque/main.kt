package com.deflatedpickle.barque

import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.util.GlobalValues
import com.deflatedpickle.barque.xml.Bar
import com.deflatedpickle.barque.xml.Barque
import com.deflatedpickle.barque.xml.Monitor
import com.thoughtworks.xstream.XStream
import cz.vutbr.web.css.CSSFactory
import org.eclipse.swt.widgets.Display

fun main(args: Array<String>) {
    val display = Display()

    val xStream = XStream()
    xStream.processAnnotations(Barque::class.java)
    xStream.processAnnotations(Monitor::class.java)
    xStream.processAnnotations(Bar::class.java)

    val layout = xStream.fromXML(ClassLoader.getSystemClassLoader().getResource("layout.xml")) as Barque
    println(layout)

    val rawStyle = CSSFactory.parseString(ClassLoader.getSystemClassLoader().getResource("style.css").readText(), null)
    val style = CSSUtil.parseCSS(rawStyle)
    println(style)

    val windowList = mutableListOf<Window>()

    for (monitor in layout.monitorList) {
        for (bar in monitor.barList) {
            when (monitor.type) {
                "primary" -> {
                    windowList.add(Window(display, display.primaryMonitor, bar, style[bar.clazz]!!))
                    GlobalValues.barCount++
                }
                "all" -> {
                    for (i in display.monitors) {
                        windowList.add(Window(display, i, bar, style[bar.clazz]!!))
                        GlobalValues.barCount++
                    }
                }
            }
        }
    }

    for (window in windowList) {
        window.layout()
        window.open()
    }

    while (!windowList[0].isDisposed) {
        if (!display.readAndDispatch()) {
            display.sleep()
        }
    }
}
