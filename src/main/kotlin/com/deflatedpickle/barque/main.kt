package com.deflatedpickle.barque

import com.deflatedpickle.barque.gui.BarWindow
import com.deflatedpickle.barque.util.CSSUtil
import com.deflatedpickle.barque.xml.Bar
import com.deflatedpickle.barque.xml.Barque
import com.deflatedpickle.barque.xml.Monitor
import com.thoughtworks.xstream.XStream
import cz.vutbr.web.css.CSSFactory
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Widget

fun main(args: Array<String>) {
    Thread(RubyThread()).start()

    val display = Display()

    val xStream = XStream()
    xStream.processAnnotations(Barque::class.java)
    xStream.processAnnotations(Monitor::class.java)
    xStream.processAnnotations(Bar::class.java)
    xStream.processAnnotations(Widget::class.java)

    val layout = xStream.fromXML(ClassLoader.getSystemClassLoader().getResource("layout.xml")) as Barque
    println("Layout: $layout")

    val rawStyle = CSSFactory.parseString(ClassLoader.getSystemClassLoader().getResource("style.css").readText(), null)
    val style = CSSUtil.parseCSS(rawStyle)
    println("Style: $style")

    val windowList = mutableListOf<BarWindow>()

    for (monitor in layout.monitorList!!) {
        for (bar in monitor.barList!!) {
            when (monitor.type) {
                "primary" -> {
                    windowList.add(BarWindow(display, display.primaryMonitor, bar, style))
                    BarWindow.barCount++
                }
                "all" -> {
                    for (i in display.monitors) {
                        windowList.add(BarWindow(display, i, bar, style))
                        BarWindow.barCount++
                    }
                }
            }
        }
    }

    println("Widgets: ${RubyThread.scriptClasses}")
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
