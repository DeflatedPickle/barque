java_import "com.deflatedpickle.barque.Barque"
java_import "javax.swing.JPanel"

class Widget
  def initialize(shell_index)
    @shell = Barque.INSTANCE.shellList.get(shell_index)
    @composite = JPanel.new
    @shell.add @composite
  end

  def update
  end
end
