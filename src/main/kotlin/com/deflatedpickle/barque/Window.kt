package com.deflatedpickle.barque

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell

class Window(display: Display) : Shell(display, SWT.NO_TRIM) {
    val shellHandle = WinDef.HWND(Pointer(shell.handle))

    init {
        this.text = "Barque"
    }

    override fun checkSubclass() {
    }
}