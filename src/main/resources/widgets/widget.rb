java_import "com.deflatedpickle.barque.Barque"
java_import "org.eclipse.swt.SWT"
java_import "org.eclipse.swt.widgets.Composite"
java_import "org.eclipse.swt.layout.FillLayout"

class Widget
  def initialize(shell_index)
    @shell = Barque.INSTANCE.shellList.get(shell_index)
    @composite = Composite.new @shell, SWT::NONE
    @composite.setLayout FillLayout.new
  end

  def update
  end
end
