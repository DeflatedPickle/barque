package com.deflatedpickle.barque

import com.deflatedpickle.barque.xml.Bar
import com.deflatedpickle.barque.xml.Barque
import com.deflatedpickle.barque.xml.Monitor
import com.thoughtworks.xstream.XStream
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.widgets.Display

fun main(args: Array<String>) {
    val display = Display()

    val xStream = XStream()
    xStream.processAnnotations(Barque::class.java)
    xStream.processAnnotations(Monitor::class.java)
    xStream.processAnnotations(Bar::class.java)

    val layout = xStream.fromXML(ClassLoader.getSystemClassLoader().getResource("layout.xml")) as Barque
    println(layout)

    val windowList = mutableListOf<Window>()

    for (monitor in layout.monitorList) {
        when (monitor.type) {
            "primary" -> {
                windowList.add(Window(display).apply {
                    val monitorPosition = display.primaryMonitor.clientArea
                    this.location = Point(monitorPosition.x, monitorPosition.y)
                })
            }
            "all" -> {
                for (i in display.monitors) {
                    windowList.add(Window(display).apply {
                        val monitorPosition = i.clientArea
                        this.location = Point(monitorPosition.x, monitorPosition.y)
                    })
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
