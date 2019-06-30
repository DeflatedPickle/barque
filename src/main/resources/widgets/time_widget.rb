class TimeWidget < Widget
  def draw(gc)
    time = Time.now
    time_string = "#{time.hour}:#{time.min}"
    text_size = gc.textExtent time_string
    gc.drawText time_string, @x - text_size.x / 2, @y - text_size.y / 2
  end
end

Barque.registerWidget TimeWidget
