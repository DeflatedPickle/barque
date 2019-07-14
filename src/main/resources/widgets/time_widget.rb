java_import "javax.swing.JButton"

class TimeWidget < Widget
  def initialize(shell_index)
    super shell_index

    @label = JButton.new
    @composite.add @label
  end

  def update
    super

    time = Time.now
    # TODO: Format to always be two digits
    time_string = "#{time.hour}:#{time.min}:#{time.sec}"
    # puts time_string
    @label.setText time_string
  end
end

Barque.registerWidget TimeWidget
