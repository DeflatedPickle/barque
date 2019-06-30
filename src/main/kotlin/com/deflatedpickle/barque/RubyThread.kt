package com.deflatedpickle.barque

import org.jruby.Ruby
import org.jruby.RubyClass
import org.jruby.RubyObject
import org.jruby.embed.IsolatedScriptingContainer
import org.jruby.embed.LocalVariableBehavior
import org.jruby.runtime.builtin.IRubyObject
import java.io.File

class RubyThread : Runnable {
    companion object {
        @Volatile
        var queue = listOf<String>()

        val rubyContainer = IsolatedScriptingContainer(LocalVariableBehavior.PERSISTENT)
        val ruby: Ruby

        @Volatile
        var scriptClasses = mapOf<String, RubyClass>()

        init {
            ruby = rubyContainer.provider.runtime
        }
    }

    var run = true

    override fun run() {
        val scriptsFolder = File(ClassLoader.getSystemResource("widgets").path)
        val scripts = mutableListOf<String>()
        // Makes sure the core class is always loaded first
        scripts.add(File(scriptsFolder, "widget.rb").readText())
        for (i in scriptsFolder.listFiles()) {
            val text = i.readText()
            if (i.name != "widget.rb") {
                scripts.add(text)
            }
        }
        queue = scripts

        while (run) {
            for (i in queue) {
                ruby.evalScriptlet(i)
            }
            queue = listOf()
        }
    }
}