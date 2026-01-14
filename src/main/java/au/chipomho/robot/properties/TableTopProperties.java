package au.chipomho.robot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter()
@Setter()
@ConfigurationProperties(prefix = "app.command-context.table-top")
@SuppressWarnings({"PMD.AtLeastOneConstructor"})
public class TableTopProperties {
  private int width;
  private int height;
}
