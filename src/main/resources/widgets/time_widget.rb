java_import "org.eclipse.swt.SWT"
java_import "org.eclipse.swt.widgets.Button"
java_import "org.eclipse.swt.layout.GridData"

class TimeWidget < Widget
  def initialize(shell_index)
    super shell_index

    @label = Button.new @composite, SWT::NONE
    grid_data = GridData.new
    grid_data.widthHint = 40
    @composite.layoutData = grid_data
  end

  def update
    super

    time = Time.now
    time_string = "#{time.hour}:#{time.min}"
    # puts time_string
    @label.setText time_string
  end
end

Barque.registerWidget TimeWidget
